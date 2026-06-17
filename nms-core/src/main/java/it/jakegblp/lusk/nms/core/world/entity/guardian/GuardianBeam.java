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
import it.jakegblp.lusk.nms.core.world.YawPitch;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@EqualsAndHashCode(callSuper = false)
@NullMarked
public class GuardianBeam extends Displayable implements Copyable<GuardianBeam>, LazyProducer {

    protected Vector start, end;
    protected int id, targetId;
    protected UUID uuid, targetUuid;
    @SuppressWarnings("NotNullFieldNotInitialized")
    protected ClientboundPacket[] displayPackets;
    @SuppressWarnings("NotNullFieldNotInitialized")
    protected ClientboundPacket[] removePackets;
    protected boolean dirty = true;

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

    @Contract("-> new")
    public Vector getStart() {
        return start.clone();
    }

    @Contract("-> new")
    public Vector getEnd() {
        return end.clone();
    }

    public void setStart(Vector start) {
        this.start = start.clone();
        markDirty();
    }

    public void setEnd(Vector end) {
        this.end = end.clone();
        markDirty();
    }

    public void setId(int id) {
        this.id = id;
        markDirty();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        markDirty();
    }

    public void setTargetUuid(UUID targetUuid) {
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
                new EntityMetadataPacket(id, EntityMetadata.of(NMS.getMetadataKeyRegistry().ENTITY.INVISIBLE, true, NMS.getMetadataKeyRegistry().GUARDIAN.IS_RETRACTING_SPIKES, true)),
                new EntityMetadataPacket(targetId, EntityMetadata.of(NMS.getMetadataKeyRegistry().ENTITY.INVISIBLE, true))
        };
        removePackets = new ClientboundPacket[] {new RemoveEntitiesPacket(id, targetId)};
    }

    @Override
    @Contract("-> new")
    public GuardianBeam copy() {
        return new GuardianBeam(start.clone(), end.clone(), id, targetId, uuid, targetUuid, isPersistent());
    }
}
