package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExprEntityMetadataEntry extends PropertyExpression<Object, Object> {

    static {
        register(ExprEntityMetadataEntry.class, Object.class, "(metadata %entitymetadatakeys%|%entitymetadatakeys% metadata)", "entitymetadatas/entitymetadatapackets");
    }

    private Expression<MetadataKey<?,?>> keyExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        keyExpression = (Expression<MetadataKey<?,?>>) expressions[matchedPattern];
        int metadataIndex = Math.abs(matchedPattern-1);
        if (!LiteralUtils.hasUnparsedLiteral(expressions[metadataIndex])) {
            setExpr(expressions[metadataIndex]);
        } else {
            setExpr(LiteralUtils.defendExpression(expressions[metadataIndex]));
            return LiteralUtils.canInitSafely(getExpr());
        }
        return true;
    }

    @Override
    protected Object[] get(Event event, Object[] source) {
        List<Object> values = new ArrayList<>();
        MetadataKey<?, ?>[] keys = keyExpression.getAll(event);
        if (keys == null || keys.length == 0) return new Object[0];
        for (Object object : source) {
            EntityMetadata entityMetadata;
            if (object instanceof EntityMetadata metadata) {
                entityMetadata = metadata;
            } else if (object instanceof EntityMetadataPacket packet) {
                entityMetadata = packet.getEntityMetadata();
            } else continue;
            for (MetadataKey<?, ?> key : keys) {
                if (entityMetadata.has(key)) {
                    values.add(entityMetadata.get(key));
                }
            }
        }
        return values.toArray();
    }


    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        switch (mode) {
            case RESET, DELETE -> {
                return new Class[0];
            }
            case REMOVE_ALL -> {
                return null;
            }
        }
        Set<Class<?>> types = Utils.getLiterals(keyExpression).stream()
                .map(lit -> lit.getSingle().valueClass())
                .collect(Collectors.toSet());

        if (types.size() != 1) {
            Skript.error("You cannot set multiple metadata entries of different values at once.");
            return null;
        }
        Class<?> literalType = types.iterator().next();
        if (mode == Changer.ChangeMode.SET || Number.class.isAssignableFrom(literalType)) {
            return new Class[]{(mode != Changer.ChangeMode.SET && literalType == Integer.class) ? Integer.class : literalType};
        }
        Skript.error("You cannot add or remove from non-number metadata entries.");
        return null;
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.REMOVE_ALL) return;
        var keys = keyExpression.getAll(event);
        if (keys == null || keys.length == 0) return;
        switch (mode) {
            case RESET, DELETE -> {
                for (Object object : getExpr().getAll(event)) {
                    EntityMetadata entityMetadata;
                    if (object instanceof EntityMetadata metadata) {
                        entityMetadata = metadata;
                    } else if (object instanceof EntityMetadataPacket packet) {
                        entityMetadata = packet.getEntityMetadata();
                    } else continue;
                    for (var key : keys) {
                        entityMetadata.remove(key);
                    }
                }
                return;
            }
        }
        if (delta == null || delta.length == 0) return;
        Object value = delta[0];
        if (value == null) return;
        if (mode == Changer.ChangeMode.SET) {
            for (Object object : getExpr().getAll(event)) {
                EntityMetadata entityMetadata;
                if (object instanceof EntityMetadata metadata) {
                    entityMetadata = metadata;
                } else if (object instanceof EntityMetadataPacket packet) {
                    entityMetadata = packet.getEntityMetadata();
                } else continue;
                for (var key : keys) {
                    entityMetadata.setUnsafe(key, value);
                }
            }
            return;
        }
        Operator operator;
        switch (mode) {
            case ADD -> operator = Operator.ADDITION;
            case REMOVE -> operator = Operator.SUBTRACTION;
            default -> {
                return;
            }
        }
        Number number = (Number) value;
        for (Object object : getExpr().getAll(event)) {
            EntityMetadata entityMetadata;
            if (object instanceof EntityMetadata metadata) {
                entityMetadata = metadata;
            } else if (object instanceof EntityMetadataPacket packet) {
                entityMetadata = packet.getEntityMetadata();
            } else continue;
            for (var key : keys) {
                if (entityMetadata.has(key)) {
                    Object finalValue = entityMetadata.get(key).value();
                    if (finalValue == null) finalValue = 0;
                    entityMetadata.setUnsafe(key, Arithmetics.calculateUnsafe(operator, finalValue, number));
                }
            }
        }

    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }
}