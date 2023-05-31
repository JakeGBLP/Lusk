package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Affected Entities")
@Description("Returns entities affected by a beacon.")
@Examples({"broadcast the entities affected by {_beacon}"})
@Since("1.0.3")
public class ExprBeaconAffectedEntities extends SimpleExpression<LivingEntity> {
    static {
        Skript.registerExpression(ExprBeaconAffectedEntities.class, LivingEntity.class, ExpressionType.COMBINED,
                "[the] entities affected by %block%",
                "[the] affected entities of %block%",
                "%block%'[s] affected entities");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected LivingEntity @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null) {
            if (block.getState() instanceof Beacon beacon) {
                return beacon.getEntitiesInRange().toArray(new LivingEntity[0]);
            }
        }
        return new LivingEntity[0];
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends LivingEntity> getReturnType() {
        return LivingEntity.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the affected entities of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}
