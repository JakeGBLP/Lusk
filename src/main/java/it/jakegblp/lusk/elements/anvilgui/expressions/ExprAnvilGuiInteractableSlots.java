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
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Interactable Slots")
@Description("Gets the interactable slots of an Anvil GUI.\nAllowed slots: 0, 1 and 2\nNo slots are interactable by default, resetting or deleting this will make all slots not interactable.")
@Examples({"set anvil gui right item of {_anvil} to barrier named \"<red>Click To Close!\""})
@Since("1.3")
public class ExprAnvilGuiInteractableSlots extends PropertyExpression<AnvilGuiWrapper, Integer> {

    static {
        register(ExprAnvilGuiInteractableSlots.class, Integer.class, "["+ANVIL_GUI_PREFIX + "] interactable slots", "anvilguiinventories");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends AnvilGuiWrapper>) expressions[0]);
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event event, AnvilGuiWrapper @NotNull [] source) {
        return Arrays.stream(source)
                .map(AnvilGuiWrapper::getInteractableSlots).filter(Objects::nonNull)
                .flatMapToInt(Arrays::stream)
                .boxed()
                .toArray(Integer[]::new);
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET, REMOVE_ALL -> new Class[]{Integer[].class};
            case DELETE, RESET -> new Class[0];
        };
    }

    @Override
    public void change(@NotNull Event event, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Stream<? extends AnvilGuiWrapper> anvilGuiStream = getExpr().stream(event);
        int[] slots;
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
            slots = null;
        } else {
            slots = Arrays.stream((Integer[])delta).mapToInt(Integer::intValue).toArray();
        }
        switch (mode) {
            case SET -> anvilGuiStream.forEach(anvilGuiWrapper -> anvilGuiWrapper.setInteractableSlots(slots));
            case ADD -> anvilGuiStream.forEach(anvilGuiWrapper -> anvilGuiWrapper.addInteractableSlots(slots));
            case REMOVE, REMOVE_ALL -> anvilGuiStream.forEach(anvilGuiWrapper -> anvilGuiWrapper.removeInteractableSlots(slots));
            case DELETE, RESET -> anvilGuiStream.forEach(AnvilGuiWrapper::resetInteractableSlots);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "lusk anvil gui interactable slots of " + getExpr().toString(event, debug);
    }
}