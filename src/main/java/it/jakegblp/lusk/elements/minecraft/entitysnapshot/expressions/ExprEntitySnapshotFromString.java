package it.jakegblp.lusk.elements.minecraft.entitysnapshot.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT_GET_AS_STRING;

@Name("Entity Snapshot - From String")
@Description("Attempts to create an entity snapshots from a previously created string nbt of one.")
@Examples("send entity snapshot of string nbt {_stringNBT}")
@Since("1.3.3")
@RequiredPlugins("1.20.5+")
@SuppressWarnings("UnstableApiUsage")
public class ExprEntitySnapshotFromString extends SimplePropertyExpression<String, EntitySnapshot> {

    static {
        if (HAS_ENTITY_SNAPSHOT_GET_AS_STRING)
            Skript.registerExpression(ExprEntitySnapshotFromString.class, EntitySnapshot.class, ExpressionType.PROPERTY, "entity[ |-]snapshot[s] (of|from|using) (nbt string|string nbt) %strings%");
    }

    @Override
    public @Nullable EntitySnapshot convert(String s) {
        try {
            return Bukkit.getEntityFactory().createEntitySnapshot(s);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    @Override
    protected String getPropertyName() {
        return null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "entity snapshots of string nbt "+getExpr().toString(event, debug);
    }

    @Override
    public Class<? extends EntitySnapshot> getReturnType() {
        return EntitySnapshot.class;
    }
}
