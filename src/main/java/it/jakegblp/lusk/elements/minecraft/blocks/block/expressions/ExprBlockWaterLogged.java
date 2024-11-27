package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

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
import it.jakegblp.lusk.api.wrappers.BlockDataWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static it.jakegblp.lusk.utils.Constants.STATE_OR_PROPERTY;

@Name("Entity - Waterlogged Property")
@Description("Returns whether or not a block is waterlogged.\nCan be set.")
@Examples({"broadcast waterlogged property of block"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBlockWaterLogged extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprBlockWaterLogged.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] [is] water[ |-]log[ged] "+STATE_OR_PROPERTY+" of %blocks/blockstates%",
                "%blocks/blockstates%'[s] [is] water[ |-]log[ged] "+STATE_OR_PROPERTY,
                "whether %blocks/blockstates% is water[ |-]logged [or not]",
                "whether [or not] %blocks/blockstates% is water[ |-]logged");
    }

    private Expression<Object> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        blockExpression = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        return blockExpression.stream(e)
                .map(BlockDataWrapper::create)
                .filter(Objects::nonNull)
                .map(BlockDataWrapper::isWaterLogged)
                .toArray(Boolean[]::new);
    }
    //todo: reset? delete? not too relevant
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            blockExpression.stream(e)
                    .map(BlockDataWrapper::create)
                    .filter(Objects::nonNull)
                    .forEach(blockDataWrapper -> blockDataWrapper.setWaterLogged(aBoolean));
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
        return "the is waterlogged state of " + blockExpression.toString(e, debug);
    }
}