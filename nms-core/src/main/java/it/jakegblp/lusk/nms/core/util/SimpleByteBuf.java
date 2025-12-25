package it.jakegblp.lusk.nms.core.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.jakegblp.lusk.common.CommonUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.entity.EntityType;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public final class SimpleByteBuf {

    private final ByteBuf buf;

    public SimpleByteBuf() {
        this.buf = Unpooled.buffer();
    }

    public SimpleByteBuf(ByteBuf buf) {
        this.buf = buf;
    }

    public ByteBuf unwrap() {
        return buf;
    }

    public byte readByte() {
        return buf.readByte();
    }

    public void writeByte(byte value) {
        buf.writeByte(value);
    }

    public void writePackedDegrees(float value) {
        buf.writeByte(CommonUtils.packDegrees(value));
    }

    public float readPackedDegrees() {
        return CommonUtils.unpackDegrees(buf.readByte());
    }

    public boolean readBoolean() {
        return buf.readBoolean();
    }

    public void writeBoolean(boolean value) {
        buf.writeBoolean(value);
    }

    public short readShort() {
        return buf.readShort();
    }

    public void writeShort(short value) {
        buf.writeShort(value);
    }

    public int readInt() {
        return buf.readInt();
    }

    public void writeInt(int value) {
        buf.writeInt(value);
    }

    public long readLong() {
        return buf.readLong();
    }

    public void writeLong(long value) {
        buf.writeLong(value);
    }

    public float readFloat() {
        return buf.readFloat();
    }

    public void writeFloat(float value) {
        buf.writeFloat(value);
    }

    public double readDouble() {
        return buf.readDouble();
    }

    public void writeDouble(double value) {
        buf.writeDouble(value);
    }

    public int readVarInt() {
        int value = 0;
        int position = 0;

        while (true) {
            byte current = buf.readByte();
            value |= (current & 0x7F) << position;

            if ((current & 0x80) == 0) {
                return value;
            }

            position += 7;
            if (position >= 32) {
                throw new IllegalStateException("VarInt too big");
            }
        }
    }

    public void writeVarInt(int value) {
        while ((value & ~0x7F) != 0) {
            buf.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buf.writeByte(value);
    }

    public IntList readIntIdList() {
        int varInt = this.readVarInt();
        IntList list = new IntArrayList();
        for(int i = 0; i < varInt; ++i)
            list.add(this.readVarInt());
        return list;
    }

    public void writeIntIdList(IntList itIdList) {
        this.writeVarInt(itIdList.size());
        itIdList.forEach(this::writeVarInt);
    }

    public long readVarLong() {
        long value = 0;
        int position = 0;

        while (true) {
            byte current = buf.readByte();
            value |= (long) (current & 0x7F) << position;

            if ((current & 0x80) == 0) {
                return value;
            }

            position += 7;
            if (position >= 64) {
                throw new IllegalStateException("VarLong too big");
            }
        }
    }

    public void writeVarLong(long value) {
        while ((value & ~0x7FL) != 0) {
            buf.writeByte((int) (value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buf.writeByte((int) value);
    }

    public String readString(int maxChars) {
        int byteLength = readVarInt();
        if (byteLength > maxChars * 4)
            throw new IllegalStateException("String too long");

        byte[] bytes = new byte[byteLength];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public void writeStrings(String... values) {
        for (String value : values) {
            writeString(value);
        }
    }

    public UUID readUUID() {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public void writeUUID(UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public int readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    public void writeUnsignedByte(int value) {
        buf.writeByte(value & 0xFF);
    }


    public <F, T> T readTo(BufferCodec<F, T> bufferCodec) {
        return bufferCodec.readTo(this);
    }

    public <F, T> F readFrom(BufferCodec<F, T> bufferCodec) {
        return bufferCodec.readFrom(this);
    }

    public <T> void writeTo(BufferCodec<?, T> bufferCodec, T value) {
        bufferCodec.writeTo(value, this);
    }

    public <F> void writeFrom(BufferCodec<F, ?> bufferCodec, F value) {
        bufferCodec.writeFrom(value, this);
    }

    public void writeVector(Vector vector) {
        writeDouble(vector.getX());
        writeDouble(vector.getY());
        writeDouble(vector.getZ());
    }
    public Vector readVector() {
        return new Vector(readDouble(), readDouble(), readDouble());
    }

    public void writeBlockVector(BlockVector blockVector) {
        writeInt(blockVector.getBlockX());
        writeInt(blockVector.getBlockZ());
        writeInt(blockVector.getBlockZ());
    }
    public BlockVector readBlockVector() {
        return new BlockVector(readInt(), readInt(), readInt());
    }

    public void writeEntityType(EntityType entityType) {
        NMS.getCodec(EntityType.class).writeTo(entityType, this);
    }

    public EntityType readEntityType() {
        return (EntityType) NMS.getCodec(EntityType.class).readTo(this);
    }
}
