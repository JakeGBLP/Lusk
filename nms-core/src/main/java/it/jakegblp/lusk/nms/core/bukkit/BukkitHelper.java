package it.jakegblp.lusk.nms.core.bukkit;

import it.jakegblp.lusk.common.Either;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BukkitHelper {

    public static void playEffect(Entity entity, EntityEffect entityEffect) {
        entity.playEffect(entityEffect);
    }

    public static void playEffect(Entity entity, InternalEntityEffect internalEntityEffect) {
        NMS.playInternalEntityEffect(entity, internalEntityEffect);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static @Nullable Either<EntityEffect, InternalEntityEffect> getEntityEffectById(byte id) {
        for (InternalEntityEffect value : InternalEntityEffect.values()) {
            if (value.getData() == id) {
                return Either.right(value);
            }
        }
        for (EntityEffect value : EntityEffect.values()) {
            if (value.getData() == id && !SimpleClass.of(EntityEffect.class).getField(value.name()).isDeprecated()) {
                return Either.left(value);
            }
        }
        return null;
    }

    // todo: copper golem state
}
