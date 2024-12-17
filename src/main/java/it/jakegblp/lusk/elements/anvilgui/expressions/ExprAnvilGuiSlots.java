package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.events.AnvilGuiSnapshotEvent;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - First/Second/Result Items")
@Description("Gets one of the 3 items in an anvil gui.\n*NOTES*:\n- These can be set.\n- To apply the changes you need to reopen the anvil gui to the player.")
@Examples({"set anvil gui right item of {_anvil} to barrier named \"<red>Click To Close!\""})
@Since("1.3")
public class ExprAnvilGuiSlots extends PropertyExpression<AnvilGuiWrapper, ItemStack> {

    static {
        register(ExprAnvilGuiSlots.class, ItemStack.class, ANVIL_GUI_PREFIX + " (left:(left|first)|right:(right|second)|output|result|third) (item|slot)", "anvilguiinventories");
    }

    private int state;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends AnvilGuiWrapper>) expressions[0]);
        state = parseResult.hasTag("left") ? -1 : parseResult.hasTag("right") ? 0 : 1;
        return true;
    }

    @Override
    protected ItemStack @NotNull [] get(@NotNull Event event, AnvilGuiWrapper @NotNull [] source) {
        return get(source, anvilGuiWrapper -> {
            if (event instanceof AnvilGuiSnapshotEvent snapshotEvent)
                return switch (state) {
                    case -1 -> snapshotEvent.getLeftItem();
                    case 0 -> snapshotEvent.getRightItem();
                    default -> snapshotEvent.getOutputItem();
                };
            return switch (state) {
                case -1 -> anvilGuiWrapper.getLeft();
                case 0 -> anvilGuiWrapper.getRight();
                default -> anvilGuiWrapper.getOutput();
            };
        });
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{ItemStack.class};
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta[0] instanceof ItemStack itemStack) {
            getExpr().stream(event).forEach(anvilGuiWrapper -> {
                switch (state) {
                    case -1 -> anvilGuiWrapper.setLeft(itemStack);
                    case 0 -> anvilGuiWrapper.setRight(itemStack);
                    default -> anvilGuiWrapper.setOutput(itemStack);
                }
            });
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "lusk anvil gui " + (state == -1 ? "left" : state == 0 ? "right" : "output") + " slot of " + getExpr().toString(event, debug);
    }
}