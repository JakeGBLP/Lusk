package it.jakegblp.lusk.elements.expressions.aliases;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Constants;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Axeables")
@Description("Returns all the blocks that will change upon being right clicked holding an axe.")
@Examples({"broadcast all axeables"})
@Since("1.0.1, 1.1 (strippable)")
public class ExprAxeables extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprAxeables.class, ItemType.class, ExpressionType.SIMPLE,
                "[all [[of] the]|the] (axe|stripp)able[ block]s");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        return Utils.toItemTypes(Constants.axeables);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all of the axeable blocks";
    }
}
