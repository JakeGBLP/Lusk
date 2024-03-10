package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
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
public class ExprBeaconAffectedEntities extends PropertyExpression<Block, LivingEntity> {
    static {
        Skript.registerExpression(ExprBeaconAffectedEntities.class, LivingEntity.class, ExpressionType.PROPERTY,
                "[all [[of] the]|the] (entities|players) affected by %block%",
                "[all [[of] the]|the] beacon affected (entities|players) of %block%",
                "%block%'[s] beacon affected (entities|players)");
    }


    private Expression<Block> blockExpression;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) expressions[0];
        return true;
    }
    @Override
    protected LivingEntity @NotNull [] get(@NotNull Event event, Block @NotNull [] source) {
        Block block = blockExpression.getSingle(event);
        if (block != null && block.getState() instanceof Beacon beacon) {
            return beacon.getEntitiesInRange().toArray(new LivingEntity[0]);
        }
        return new LivingEntity[0];
    }

    @Override
    public @NotNull Class<? extends LivingEntity> getReturnType() {
        return LivingEntity.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "all of the beacon affected entities of "+(event != null ? blockExpression.toString(event,debug) : "");
    }


}