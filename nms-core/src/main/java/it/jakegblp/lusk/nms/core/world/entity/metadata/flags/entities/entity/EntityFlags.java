package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.entity;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

/**
 * Wrapper for the entity metadata bitmask at index 0.<br>
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@NullMarked
public class EntityFlags extends FlagByte<Entity> implements Copyable<EntityFlags> {
    public static final BooleanFlag<Entity> BURNING = Flag.booleanFlag("BURNING", entity -> entity.getVisualFire().equals(TriState.TRUE), (entity, aBoolean) -> entity.setVisualFire(TriState.byBoolean(aBoolean)));
    public static final BooleanFlag<Entity> SNEAKING = Flag.booleanFlag("SNEAKING", Entity::isSneaking, Entity::setSneaking);
    public static final BooleanFlag<Entity> UNUSED = Flag.unusedBooleanFlag("UNUSED"); // Previously riding
    public static final BooleanFlag<Entity> SPRINTING = Flag.booleanFlag("SPRINTING", EntityUtils::isSprinting, EntityUtils::setSprinting);
    public static final BooleanFlag<Entity> SWIMMING = Flag.booleanFlag("SWIMMING", EntityUtils::isSwimming, EntityUtils::setSwimming);
    public static final BooleanFlag<Entity> INVISIBLE = Flag.booleanFlag("INVISIBLE", Entity::isInvisible, Entity::setInvisible);
    public static final BooleanFlag<Entity> GLOWING = Flag.booleanFlag("GLOWING", Entity::isGlowing, Entity::setGlowing);
    public static final BooleanFlag<Entity> GLIDING = Flag.booleanFlag("GLIDING", EntityUtils::isGliding, EntityUtils::setGliding);

    @SuppressWarnings("unchecked")
    private static final Flag<Entity, ?, ?>[] FLAGS = new Flag[] {BURNING, SNEAKING, UNUSED, SPRINTING, SWIMMING, INVISIBLE, GLOWING, GLIDING};

    public static Flag<Entity, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    public EntityFlags(Entity entity) {
        super(entity);
    }

    public EntityFlags() {
        super();
    }

    public EntityFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Entity> getFlagHolderClass() {
        return Entity.class;
    }

    @Override
    public Flag<Entity, ?, ?>[] getConstants() {
        return getFlags();
    }

    public boolean isOnFire() {
        return get(BURNING);
    }

    public void setOnFire(boolean value) {
        set(BURNING, value);
    }

    public boolean isSneaking() {
        return get(SNEAKING);
    }

    public void setSneaking(boolean value) {
        set(SNEAKING, value);
    }

    public boolean isSprinting() {
        return get(SPRINTING);
    }

    public void setSprinting(boolean value) {
        set(SPRINTING, value);
    }

    public boolean isSwimming() {
        return get(SWIMMING);
    }

    public void setSwimming(boolean value) {
        set(SWIMMING, value);
    }

    public boolean isInvisible() {
        return get(INVISIBLE);
    }

    public void setInvisible(boolean value) {
        set(INVISIBLE, value);
    }

    public boolean isGlowing() {
        return get(GLOWING);
    }

    public void setGlowing(boolean value) {
        set(GLOWING, value);
    }

    public boolean isFlyingElytra() {
        return get(GLIDING);
    }

    public void setFlyingElytra(boolean value) {
        set(GLIDING, value);
    }

    @Override
    public EntityFlags copy() {
        return new EntityFlags(this.flags);
    }
}
