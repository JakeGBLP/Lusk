package it.jakegblp.lusk.elements.minecraft.entitysnapshot.expressions;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.doc.*;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.EntitySnapshot;

@Name("Entity Snapshot - Entity Type")
@Description("Gets the entity type of the provided entity snapshots.")
@Examples("send entity snapshot type of {_entitySnapshot}")
@Since("1.3")
@RequiredPlugins("1.20.2")
public class ExprEntitySnapshotType extends SimplePropertyExpression<EntitySnapshot, EntityType> {

    static {
        register(ExprEntitySnapshotType.class, EntityType.class, "entity[ |-]snapshot type", "entitysnapshots");
    }

    @Override
    public EntityType convert(EntitySnapshot from) {
        return new EntityType(EntityUtils.toSkriptEntityData(from.getEntityType()), 1);
    }

    @Override
    protected String getPropertyName() {
        return "entity snapshot type";
    }

    @Override
    public Class<? extends EntityType> getReturnType() {
        return EntityType.class;
    }
}
