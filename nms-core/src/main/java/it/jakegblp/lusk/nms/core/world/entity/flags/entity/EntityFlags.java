package it.jakegblp.lusk.nms.core.world.entity.flags.entity;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Entity;

/**
 * Wrapper for the entity metadata bitmask at index 0.<br>
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
public class EntityFlags extends FlagByte<EntityFlag, Entity, Boolean> implements Cloneable {

    public EntityFlags() {
        super();
    }

    public EntityFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<EntityFlag> getBitFlagClass() {
        return EntityFlag.class;
    }

    @Override
    public Class<Entity> getFlagHolderClass() {
        return Entity.class;
    }

    public boolean isOnFire() {
        return get(EntityFlag.BURNING);
    }

    public void setOnFire(boolean value) {
        set(EntityFlag.BURNING, value);
    }

    public boolean isSneaking() {
        return get(EntityFlag.SNEAKING);
    }

    public void setSneaking(boolean value) {
        set(EntityFlag.SNEAKING, value);
    }

    public boolean isUnused() {
        return get(EntityFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(EntityFlag.UNUSED, value);
    }

    public boolean isSprinting() {
        return get(EntityFlag.SPRINTING);
    }

    public void setSprinting(boolean value) {
        set(EntityFlag.SPRINTING, value);
    }

    public boolean isSwimming() {
        return get(EntityFlag.SWIMMING);
    }

    public void setSwimming(boolean value) {
        set(EntityFlag.SWIMMING, value);
    }

    public boolean isInvisible() {
        return get(EntityFlag.INVISIBLE);
    }

    public void setInvisible(boolean value) {
        set(EntityFlag.INVISIBLE, value);
    }

    public boolean isGlowing() {
        return get(EntityFlag.GLOWING);
    }

    public void setGlowing(boolean value) {
        set(EntityFlag.GLOWING, value);
    }

    public boolean isFlyingElytra() {
        return get(EntityFlag.GLIDING);
    }

    public void setFlyingElytra(boolean value) {
        set(EntityFlag.GLIDING, value);
    }

    @Override
    public EntityFlags clone() {
        return new EntityFlags(this.flags);
    }
}
