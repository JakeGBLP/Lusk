package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Sign - Editable State")
@Description("Returns whether or not a sign is editable.\nCan be set.")
@Examples({"broadcast editable state of target"})
@Since("1.0.3")
public class ExprSignEditableState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprSignEditableState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] sign [is] (editable|:waxed) state of %block%",
                "%block%'[s] sign [is] (editable|:waxed) state",
                "whether [the] sign %block% is (editable|:waxed) [or not]",
                "whether [or not] [the] sign %block% is (editable|:waxed)");
    }

    private Expression<Block> blockExpression;
    private boolean waxed;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        waxed = parseResult.hasTag("waxed");
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null && block.getState() instanceof Sign sign) {
            return new Boolean[]{waxed == sign.isWaxed()};
        }
        return new Boolean[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            Block block = blockExpression.getSingle(e);
            if (block != null && block.getState() instanceof Sign sign) {
                sign.setWaxed(waxed == aBoolean);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the sign is "+ (waxed ? "waxed" : "editable")+" state of " + (e == null ? "" : blockExpression.toString(e,debug));
    }
}