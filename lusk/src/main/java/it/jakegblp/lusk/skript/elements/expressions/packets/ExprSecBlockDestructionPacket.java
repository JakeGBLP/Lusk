package it.jakegblp.lusk.skript.elements.expressions.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.BlockDestructionPacket;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExprSecBlockDestructionPacket extends SectionExpression<BlockDestructionPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("id", null, false, Integer.class))
                .addEntryData(new ExpressionEntryData("location", null, false, Location.class, Vector.class))
                .addEntryData(new ExpressionEntryData<>("stage", null, false, Integer.class))
                .build();
        Skript.registerExpression(ExprSecBlockDestructionPacket.class, BlockDestructionPacket.class, ExpressionType.COMBINED,
                "[a] new block ((dig[ging]|break[ing]|min(e|ing)) stage|destruction [stage]) packet");
    }

    private Expression<?> locationExpression;
    private Expression<Integer> idExpression, stageExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        idExpression = (Expression<Integer>) container.getOptional("id", false);
        if (idExpression == null) return false;
        locationExpression = (Expression<?>) container.getOptional("location", false);
        if (locationExpression == null) return false;
        stageExpression = (Expression<Integer>) container.getOptional("stage", false);
        if (stageExpression instanceof Literal<Integer> literal) {
            Integer stage = literal.getSingle();
            if (stage == null) return false;
            if (stage > 10 || stage < 0) {
                Skript.warning("The block destruction stage '"+stage+"' is not within 0 (removal) and 10 (maximum destruction).");
            }
            return true;
        } else return stageExpression != null;
    }

    @Override
    protected BlockDestructionPacket @Nullable [] get(Event event) {
        Vector processedLocation;
        Object unprocessedLocation = locationExpression.getSingle(event);
        if (unprocessedLocation instanceof Location location)
            processedLocation = location.toVector();
        else if (unprocessedLocation instanceof Vector vector)
            processedLocation = vector;
        else return new BlockDestructionPacket[0];
        Integer stage = stageExpression.getSingle(event);
        if (stage == null) return new BlockDestructionPacket[0];
        Integer id = idExpression.getSingle(event);
        if (id == null) return new BlockDestructionPacket[0];
        return new BlockDestructionPacket[] {new BlockDestructionPacket(id, processedLocation.toBlockVector(), Math.clamp(stage,0,10) - 1)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BlockDestructionPacket> getReturnType() {
        return BlockDestructionPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new block destruction packet";
    }
}
