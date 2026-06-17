package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.EntityMovePacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericMovementPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.GenericRotationPacket;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MoveEntityPacket implements ClientboundPacketWithEntityId<EntityMovePacketEvent> {

    protected int entityId;
    protected boolean onGround;

    public abstract boolean hasMovement();
    public abstract boolean hasRotation();

    protected MoveEntityPacket(SimpleByteBuf buf) {
        read(buf);
    }

    @Override
    public EntityMovePacketEvent createEvent(Player player, boolean async) {
        return new EntityMovePacketEvent(this, player, async);
    }

    

    @Override
    public MoveEntityPacket copy() {
        throw new RuntimeException("Missing copying implementation.");
    }

    @Getter
    @Setter
    @NullMarked
    public static class Position extends MoveEntityPacket implements GenericMovementPacket {

        protected double movementX, movementY, movementZ;

        public Position(int entityId, Vector movement, boolean onGround) {
            this(entityId, movement.getX(), movement.getY(), movement.getZ(), onGround);
        }

        public Position(int entityId, double movementX, double movementY, double movementZ, boolean onGround) {
            super(entityId, onGround);
            this.movementX = movementX;
            this.movementY = movementY;
            this.movementZ = movementZ;
        }

        public Position(SimpleByteBuf buf) {
            super(buf);
        }

        @Contract("-> new")
        @Override
        public Vector getMovement() {
            return new Vector(movementX, movementY, movementZ);
        }

        @Override
        public void setMovement(Vector vector) {
            movementX = vector.getX();
            movementY = vector.getY();
            movementZ = vector.getZ();
        }

        @Override
        public void write(SimpleByteBuf buf) {
            buf.writeVarInt(entityId);
            buf.writeShort((short) (movementX * 4096.0));
            buf.writeShort((short) (movementY * 4096.0));
            buf.writeShort((short) (movementZ * 4096.0));
            buf.writeBoolean(onGround);
        }

        @Override
        public void read(SimpleByteBuf buf) {
            this.entityId = buf.readVarInt();
            this.movementX = buf.readShort() / 4096.0;
            this.movementY = buf.readShort() / 4096.0;
            this.movementZ = buf.readShort() / 4096.0;
            this.onGround = buf.readBoolean();
        }

        @Override
        public Position copy() {
            return new Position(entityId, movementX, movementY, movementZ, onGround);
        }

        @Override
        public boolean hasMovement() {
            return true;
        }

        @Override
        public boolean hasRotation() {
            return false;
        }
    }

    @Getter
    @Setter
    @NullMarked
    public static class Rotation extends MoveEntityPacket implements GenericRotationPacket {

        protected float yaw, pitch;

        public Rotation(int entityId, float yaw, float pitch, boolean onGround) {
            super(entityId, onGround);
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Rotation(SimpleByteBuf buf) {
            super(buf);
        }

        @Override
        public void write(SimpleByteBuf buf) {
            buf.writeVarInt(entityId);
            buf.writeByte((byte) (yaw * 256.0f / 360.0f));
            buf.writeByte((byte) (pitch * 256.0f / 360.0f));
            buf.writeBoolean(onGround);
        }

        @Override
        public void read(SimpleByteBuf buf) {
            this.entityId = buf.readVarInt();
            this.yaw = (buf.readByte() & 255) * 360.0f / 256.0f;
            this.pitch = (buf.readByte() & 255) * 360.0f / 256.0f;
            this.onGround = buf.readBoolean();
        }

        @Override
        public Rotation copy() {
            return new Rotation(entityId, yaw, pitch, onGround);
        }

        @Override
        public boolean hasMovement() {
            return false;
        }

        @Override
        public boolean hasRotation() {
            return true;
        }
    }

    @Getter
    @Setter
    @NullMarked
    public static class PositionRotation extends MoveEntityPacket implements GenericRotationPacket, GenericMovementPacket {

        protected double movementX, movementY, movementZ;

        protected float yaw;
        protected float pitch;

        public PositionRotation(int entityId, Vector movement, float yaw, float pitch, boolean onGround) {
            this(entityId, movement.getX(), movement.getY(), movement.getZ(), yaw, pitch, onGround);
        }

        public PositionRotation(int entityId, double movementX, double movementY, double movementZ, float yaw, float pitch, boolean onGround) {
            super(entityId, onGround);
            this.movementX = movementX;
            this.movementY = movementY;
            this.movementZ = movementZ;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public PositionRotation(SimpleByteBuf buf) {
            super(buf);
        }

        @Contract("-> new")
        public Vector getMovement() {
            return new Vector(movementX, movementY, movementZ);
        }

        public void setMovement(Vector vector) {
            this.movementX = vector.getX();
            this.movementY = vector.getY();
            this.movementZ = vector.getZ();
        }

        @Override
        public void write(SimpleByteBuf buf) {
            buf.writeVarInt(entityId);

            buf.writeShort((short) (movementX * 4096.0));
            buf.writeShort((short) (movementY * 4096.0));
            buf.writeShort((short) (movementZ * 4096.0));

            buf.writeByte((byte) (yaw * 256.0f / 360.0f));
            buf.writeByte((byte) (pitch * 256.0f / 360.0f));

            buf.writeBoolean(onGround);
        }

        @Override
        public void read(SimpleByteBuf buf) {
            this.entityId = buf.readVarInt();

            this.movementX = buf.readShort() / 4096.0;
            this.movementY = buf.readShort() / 4096.0;
            this.movementZ = buf.readShort() / 4096.0;

            this.yaw = (buf.readByte() & 255) * 360.0f / 256.0f;
            this.pitch = (buf.readByte() & 255) * 360.0f / 256.0f;

            this.onGround = buf.readBoolean();
        }

        @Override
        public PositionRotation copy() {
            return new PositionRotation(entityId, movementX, movementY, movementZ, yaw, pitch, onGround);
        }

        @Override
        public boolean hasMovement() {
            return true;
        }

        @Override
        public boolean hasRotation() {
            return true;
        }
    }
}