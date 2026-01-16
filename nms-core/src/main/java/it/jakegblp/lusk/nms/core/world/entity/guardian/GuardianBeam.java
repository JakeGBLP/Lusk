package it.jakegblp.lusk.nms.core.world.entity.guardian;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
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
@EqualsAndHashCode(callSuper = false)
public class GuardianBeam extends Displayable implements Copyable<GuardianBeam>, LazyProducer {

    private static final Prototype<EntityMetadata, Integer, EntityMetadata> BASE_GUARDIAN_DATA = Prototype.unary(
            EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.INVISIBLE, true, MetadataKeys.GuardianKeys.IS_RETRACTING_SPIKES, true)),
            entityMetadata -> entityMetadata,// todo: clone
            (entityMetadata, id) -> entityMetadata.with(MetadataKeys.GuardianKeys.TARGET_ENTITY_ID, id));

    // todo: this one shouldn't use unary
    private static final Prototype<EntityMetadata, Integer, EntityMetadataPacket> BASE_BAT_PACKET = Prototype.of(
            EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.INVISIBLE, true)),
            entityMetadataPacket -> entityMetadataPacket,// todo: clone
            ((entityMetadata, integer) -> new EntityMetadataPacket(integer, entityMetadata)));

    protected @NotNull Vector start, end;
    protected int id, targetId;
    protected @NotNull UUID uuid, targetUuid;
    protected @NotNull ClientboundPacket[] displayPackets, removePackets;
    protected boolean dirty = true;

    @NullMarked
    public GuardianBeam(Vector start, Vector end, int id, int targetId, UUID uuid, UUID targetUuid, boolean persistent) {
        super(persistent);
        this.start = start.clone();
        this.end = end.clone().add(this.start.clone().subtract(end).normalize());
        this.id = id;
        this.targetId = targetId;
        this.uuid = uuid;
        this.targetUuid = targetUuid;
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
    protected void displayImplementation(Player player) {
        NMSApi.sendPackets(player, getDisplayPackets(), ExecutionMode.ASYNCHRONOUS);
    }

    @Override
    protected void removeImplementation(Player player) {
        NMSApi.sendPackets(player, getRemovePackets(), ExecutionMode.ASYNCHRONOUS);
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

    public void setTargetId(int targetId) {
        this.targetId = targetId;
        markDirty();
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
                new AddEntityPacket(id, uuid, start, yawPitch.getYaw(), yawPitch.getPitch(), (float) yawPitch.yaw(), new Vector(), EntityType.GUARDIAN, 0),
                new AddEntityPacket(targetId, targetUuid, end, 0, new Vector(), EntityType.BAT, 0),
                new EntityMetadataPacket(id, BASE_GUARDIAN_DATA.apply(targetId)),
                BASE_BAT_PACKET.apply(targetId)
        };
        removePackets = new ClientboundPacket[] {new RemoveEntitiesPacket(id, targetId)};
    }

    @Override
    public GuardianBeam copy() {
        return new GuardianBeam(start.clone(), end.clone(), id, targetId, uuid, targetUuid, isPersistent());
    }
}
