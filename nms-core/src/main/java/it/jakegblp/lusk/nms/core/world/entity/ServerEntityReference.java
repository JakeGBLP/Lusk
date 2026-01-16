package it.jakegblp.lusk.nms.core.world.entity;

import it.jakegblp.lusk.common.Either;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.UUID;

@NullMarked
public final class ServerEntityReference<T extends Entity> implements PureNMSObject<Objects> {

    private Either<UUID, T> entity;

    private ServerEntityReference(T entity) {
        this.entity = Either.right(entity);
    }

    private ServerEntityReference(UUID uuid) {
        this.entity = Either.left(uuid);
    }

    @Nullable
    public static <T extends Entity> ServerEntityReference<T> of(@Nullable T entity) {
        return entity != null ? new ServerEntityReference<>(entity) : null;
    }

    public static <T extends Entity> ServerEntityReference<T> of(UUID uuid) {
        return new ServerEntityReference<>(uuid);
    }

    public UUID getUUID() {
        return entity.mapEither(uuid -> uuid, Entity::getUniqueId);
    }

    @Nullable
    public T getEntity(World world, Class<T> type) {
        if (entity.isRight()) {
            T cached = entity.rightOrThrow();
            if (cached.isValid())
                return cached;
            entity = Either.left(cached.getUniqueId());
        }

        if (entity.isLeft()) {
            UUID uuid = entity.leftOrThrow();
            Entity found = world.getEntity(uuid);

            if (type.isInstance(found) && found.isValid()) {
                T casted = type.cast(found);
                entity = Either.right(casted);
                return casted;
            }
        }

        return null;
    }

    @Nullable
    public T getEntity(Class<T> type) {
        for (World world : Bukkit.getWorlds()) {
            T entity = getEntity(world, type);
            if (entity != null)
                return entity;
        }
        return null;
    }

    public boolean matches(Entity entity) {
        return getUUID().equals(entity.getUniqueId());
    }

    @Nullable
    public static <T extends Entity> T get(
            @Nullable ServerEntityReference<T> ref,
            World world,
            Class<T> type
    ) {
        return ref != null ? ref.getEntity(world, type) : null;
    }

    @Nullable
    public static Entity getEntity(
            @Nullable ServerEntityReference<Entity> ref,
            World world
    ) {
        return get(ref, world, Entity.class);
    }

    @Nullable
    public static LivingEntity getLivingEntity(
            @Nullable ServerEntityReference<LivingEntity> ref,
            World world
    ) {
        return get(ref, world, LivingEntity.class);
    }

    @Nullable
    public static Player getPlayer(
            @Nullable ServerEntityReference<Player> ref,
            World world
    ) {
        return get(ref, world, Player.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (!(o instanceof ServerEntityReference<?> other)) return false;
        else return getUUID().equals(other.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID());
    }
}
