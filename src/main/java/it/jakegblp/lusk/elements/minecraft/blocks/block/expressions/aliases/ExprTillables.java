package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions.aliases;

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

@Name("Tillables")
@Description("Returns all the tillable blocks.")
@Examples({"broadcast all tillables"})
@Since("1.0.0")
public class ExprTillables extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprTillables.class, ItemType.class, ExpressionType.SIMPLE,
                "[all [[of] the]|the] tillable[ block]s");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        return Utils.toItemTypes(Constants.tillables);
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
        return "all of the tillable blocks";
    }
}
