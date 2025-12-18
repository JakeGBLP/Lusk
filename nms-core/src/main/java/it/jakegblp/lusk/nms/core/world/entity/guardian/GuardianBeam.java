package it.jakegblp.lusk.nms.core.world.entity.guardian;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.RemoveEntitiesPacket;
import it.jakegblp.lusk.nms.core.util.Displayable;
import it.jakegblp.lusk.nms.core.util.LazyProducer;
import it.jakegblp.lusk.nms.core.util.Prototype;
import it.jakegblp.lusk.nms.core.world.YawPitch;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class GuardianBeam implements Cloneable, LazyProducer, Displayable {

    private static final Prototype<EntityMetadata, Integer> BASE_GUARDIAN_DATA = Prototype.of(
            new EntityMetadata(Map.of(MetadataKeys.EntityKeys.INVISIBLE, true, MetadataKeys.GuardianKeys.IS_RETRACTING_SPIKES, true)),
            entityMetadata -> entityMetadata,// todo: clone
            (entityMetadata, id) -> entityMetadata.set(MetadataKeys.GuardianKeys.TARGET_ENTITY_ID, id));

    // todo: this one shouldn't use unary
    private static final Prototype<EntityMetadataPacket, Integer> BASE_BAT_PACKET = Prototype.of(
            new EntityMetadataPacket(-1, Map.of(MetadataKeys.EntityKeys.INVISIBLE, true)),
            entityMetadataPacket -> entityMetadataPacket,// todo: clone
            EntityMetadataPacket::setId);

    protected @NotNull Vector start, end;
    protected int id, targetId;
    protected @NotNull UUID uuid, targetUuid;
    protected boolean persistent;
    protected @NotNull ClientboundPacket[] displayPackets, removePackets;
    protected boolean dirty = true;

    @NullMarked
    public GuardianBeam(Vector start, Vector end, int id, int targetId, UUID uuid, UUID targetUuid, boolean persistent) {
        this.start = start.clone();
        this.end = end.clone().add(this.start.clone().subtract(end).normalize());
        this.id = id;
        this.targetId = targetId;
        this.uuid = uuid;
        this.targetUuid = targetUuid;
        this.persistent = persistent;
    }

    @NullMarked
    public GuardianBeam(Vector start, Vector end, int id, int targetId, boolean persistent) {
        this(start, end, id, targetId, UUID.randomUUID(), UUID.randomUUID(), persistent);
    }
    @NullMarked
    public GuardianBeam(Vector start, Vector end, boolean persistent) {
        this(start, end, CommonUtils.randomId(), CommonUtils.randomId(), persistent);
    }

    @Override
    public void display(Player... players) {
        NMSApi.sendPackets(players, getDisplayPackets());
    }

    @Override
    public void remove(Player... players) {
        NMSApi.sendPackets(players, getRemovePackets());
    }

    /**
     * @return a clone
     */
    public @NotNull Vector getStart() {
        return start.clone();
    }

    /**
     * @return a clone
     */
    public @NotNull Vector getEnd() {
        return end.clone();
    }

    public void setStart(@NotNull Vector start) {
        this.start = start.clone();
        markDirty();
    }

    public void setEnd(@NotNull Vector end) {
        this.end = end.clone();
        markDirty();
    }

    public void setId(int id) {
        this.id = id;
        markDirty();
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
        markDirty();
    }

    public void setTargetUuid(@NotNull UUID targetUuid) {
        this.targetUuid = targetUuid;
        markDirty();
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
        markDirty();
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
        markDirty();
    }

    @Override
    public GuardianBeam clone() {
        try {
            return (GuardianBeam) super.clone(); // todo: finish
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public ClientboundPacket[] getDisplayPackets() {
        if (isDirty())
            produce();
        return displayPackets;
    }

    public ClientboundPacket[] getRemovePackets() {
        if (isDirty())
            produce();
        return removePackets;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void produce() {
        dirty = false;
        YawPitch yawPitch = YawPitch.between(start, end);
        displayPackets = new ClientboundPacket[] {
                new AddEntityPacket(id, uuid, start, yawPitch.getYaw(), yawPitch.getPitch(), EntityType.GUARDIAN, 0, new Vector(0, 0, 0), yawPitch.yaw()),
                new AddEntityPacket(targetId, targetUuid, end, 0, 0, EntityType.BAT, 0, new Vector(), 0),
                new EntityMetadataPacket(id, BASE_GUARDIAN_DATA.apply(targetId)),
                BASE_BAT_PACKET.apply(targetId)
        };
        removePackets = new ClientboundPacket[] {new RemoveEntitiesPacket(id, targetId)};
    }
}
