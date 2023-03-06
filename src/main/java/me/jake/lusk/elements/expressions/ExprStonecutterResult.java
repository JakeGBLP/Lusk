package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("StoneCutter Recipe")
@Description("Gets the result in a Stonecutter Recipe Select event")
@Examples("""
        on stonecutter recipe select:
          broadcast result
        """)
@Since("1.0.0")
public class ExprStonecutterResult extends SimpleExpression<ItemStack> {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent")) {
            Skript.registerExpression(ExprStonecutterResult.class, ItemStack.class, ExpressionType.SIMPLE,
                    "[the] [stonecutt(er|ing)] result");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(PlayerStonecutterRecipeSelectEvent.class)) {
            Skript.error("This expression can only be used in the Stonecutter Recipe Select Event!");
            return false;
        }
        return true;
    }
    @Override
    protected ItemStack @NotNull [] get(@NotNull Event e) {
        return new ItemStack[]{((PlayerStonecutterRecipeSelectEvent) e).getStonecuttingRecipe().getResult()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the Stonecutter Result";
    }
}
