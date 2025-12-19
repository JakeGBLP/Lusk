package it.jakegblp.lusk.nms.api;

import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ClientSideEntity {
    protected final Set<Player> viewers = new HashSet<>();
    protected @NotNull EntityType type;
    protected final @NotNull UUID uuid;
    protected final int id;
    protected @NotNull Location location;
    protected @NotNull Vector velocity;
    protected @NotNull Double headYaw;
    protected int data;
    protected @Nullable EntityMetadata entityMetadata;

    @NullMarked
    public ClientSideEntity(
            EntityType type,
            @Nullable UUID uuid,
            @Nullable Integer id,
            Location location,
            @Nullable Vector velocity,
            @Nullable Double headYaw,
            @Nullable EntityMetadata entityMetadata,
            @Nullable Integer data
            ) {
        this.type = type;
        this.uuid = uuid != null ? uuid : UUID.randomUUID();
        this.id = id != null ? id : NMSApi.generateRandomEntityId();
        this.location = location;
        this.velocity = velocity != null ? velocity : new Vector();
        this.headYaw = headYaw != null ? headYaw : 0;
        this.entityMetadata = entityMetadata;
        this.data = data != null ? data : 0;
    }

    //@NullMarked
    //public static ClientSideEntity spawn(
    //        EntityType entityType,
    //        Location location,
    //        Vector velocity,
    //        double headYaw) {}
//
    //public AddEntityPacket getSpawnPacket(@NotNull Location location, @NotNull Vector velocity, double headYaw) {
    //    return new AddEntityPacket(id, uuid, location, type, data, velocity, headYaw);
    //}

}
