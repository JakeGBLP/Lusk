package it.jakegblp.lusk.elements.minecraft.entitysnapshot.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.EntitySnapshot;

import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT_GET_AS_STRING;

@Name("Entity Snapshot - As String")
@Description("Gets the String NBT of the provided entity snapshots.")
@Examples("send entity snapshot string nbt of {_entitySnapshot}")
@Since("1.3.3")
@RequiredPlugins("1.20.5+")
@SuppressWarnings("UnstableApiUsage")
public class ExprEntitySnapshotAsString extends SimplePropertyExpression<EntitySnapshot, String> {

    static {
        if (HAS_ENTITY_SNAPSHOT_GET_AS_STRING)
           register(ExprEntitySnapshotAsString.class, String.class, "entity[ |-]snapshot (nbt string|string nbt)", "entitysnapshots");
    }

    @Override
    public String convert(EntitySnapshot entitySnapshot) {
        return entitySnapshot.getAsString();
    }

    @Override
    protected String getPropertyName() {
        return "entity snapshot nbt string";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
