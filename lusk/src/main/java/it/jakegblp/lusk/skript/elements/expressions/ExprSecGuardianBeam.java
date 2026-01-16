package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.guardian.GuardianBeam;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.UUID;

public class ExprSecGuardianBeam extends SectionExpression<GuardianBeam> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("id", null, false, Integer.class))
                .addEntryData(new ExpressionEntryData<>("target id", null, false, Integer.class))
                .addEntryData(new ExpressionEntryData<>("uuid", null, false, Integer.class))
                .addEntryData(new ExpressionEntryData<>("target uuid", null, false, Integer.class))
                .addEntryData(new ExpressionEntryData<>("start", null, false, Vector.class, Location.class))
                .addEntryData(new ExpressionEntryData<>("end", null, false, Vector.class, Location.class))
                .build();
        Skript.registerExpression(ExprSecGuardianBeam.class, GuardianBeam.class, ExpressionType.COMBINED,
                "[a] new [:persistent] [virtual] guardian beam");
    }

    private Expression<Integer> idExpression, targetIdExpression;
    private Expression<UUID> uuidExpression, targetUuidExpression;
    private Expression<Object> startExpression, endExpression;
    private boolean persistent;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        persistent = result.hasTag("persistent");
        idExpression = (Expression<Integer>) container.getOptional("id", false);
        targetIdExpression = (Expression<Integer>) container.getOptional("target id", false);
        uuidExpression = (Expression<UUID>) container.getOptional("target id", false);
        targetUuidExpression = (Expression<UUID>) container.getOptional("target uuid", false);
        startExpression = (Expression<Object>) container.getOptional("start", false);
        endExpression = (Expression<Object>) container.getOptional("end", false);
        return true;
    }

    @Override
    protected GuardianBeam @Nullable [] get(Event event) {
        Integer id = idExpression.getSingle(event);
        if (id == null) return new GuardianBeam[0];
        Integer targetId = targetIdExpression.getSingle(event);
        if (targetId == null) return new GuardianBeam[0];
        UUID uuid = uuidExpression.getSingle(event);
        if (uuid == null) return new GuardianBeam[0];
        UUID targetUuid = targetUuidExpression.getSingle(event);
        if (targetUuid == null) return new GuardianBeam[0];
        Vector start = AddonUtils.getVectorFromExpression(startExpression, event);
        if (start == null) return new GuardianBeam[0];
        Vector end = AddonUtils.getVectorFromExpression(endExpression, event);
        if (end == null) return new GuardianBeam[0];
        return new GuardianBeam[]{new GuardianBeam(start, end, id, targetId, uuid, targetUuid, persistent)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends GuardianBeam> getReturnType() {
        return GuardianBeam.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + (persistent ? "persistent " : "") + "guardian beam";
    }
}
