package it.jakegblp.lusk.nms.core.util;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.PseudoEnum;
import it.jakegblp.lusk.common.PseudoEnumSet;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.ProfilePublicKey;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import static it.jakegblp.lusk.common.CommonUtils.*;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class SimpleByteBuf {

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

    public void writeByte(int value) {
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


    public void writeVarIntArray(int[] values) {
        writeVarInt(values.length);
        for (int value : values) {
            writeVarInt(value);
        }
    }

    public int[] readVarIntArray() {
        int length = readVarInt();
        int[] values = new int[length];
        for (int i = 0; i < length; i++) {
            values[i] = readVarInt();
        }
        return values;
    }

    public @NotNull IntList readIntList() {
        int varInt = this.readVarInt();
        IntList list = new IntArrayList();
        for(int i = 0; i < varInt; ++i)
            list.add(this.readVarInt());
        return list;
    }

    public void writeIntList(@NotNull IntList intList) {
        this.writeVarInt(intList.size());
        intList.forEach(this::writeVarInt);
    }

    public long readVarLong() {
        long value = 0;
        int position = 0;

        while (true) {
            byte current = buf.readByte();
            value |= (long) (current & 0x7F) << position;
            if ((current & 0x80) == 0)
                return value;
            position += 7;
            if (position >= 64)
                throw new IllegalStateException("VarLong too big");
        }
    }

    public void writeVarLong(long value) {
        while ((value & ~0x7FL) != 0) {
            buf.writeByte((int) (value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buf.writeByte((int) value);
    }

    public @NotNull String readString() {
        return readString(Short.MAX_VALUE);
    }

    @Contract("_ -> new")
    public @NotNull String readString(int maxLength) {
        int i = ByteBufUtil.utf8MaxBytes(maxLength);
        int j = readVarInt();
        if (j > i)
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i + ")");
        else if (j < 0)
            throw new DecoderException("The received encoded string buffer length is less than zero!");
        else {
            int k = buf.readableBytes();
            if (j > k)
                throw new DecoderException("Not enough bytes in buffer, expected " + j + ", but got " + k);
            else {
                String string = buf.toString(buf.readerIndex(), j, StandardCharsets.UTF_8);
                buf.readerIndex(buf.readerIndex() + j);
                if (string.length() > maxLength) {
                    int var10002 = string.length();
                    throw new DecoderException("The received string length is longer than maximum allowed (" + var10002 + " > " + maxLength + ")");
                } else
                    return string;
            }
        }
    }

    public void writeString(@NotNull String string) {
        writeString(string, Short.MAX_VALUE);
    }

    public void writeString(@NotNull String string, int maxLength) {
        if (string.length() > maxLength) {
            int var10002 = string.length();
            throw new EncoderException("String too big (was " + var10002 + " characters, max " + maxLength + ")");
        } else {
            int i = ByteBufUtil.utf8MaxBytes(string);
            ByteBuf byteBuf = buf.alloc().buffer(i);
            try {
                int j = ByteBufUtil.writeUtf8(byteBuf, string);
                int k = ByteBufUtil.utf8MaxBytes(maxLength);
                if (j > k)
                    throw new EncoderException("String too big (was " + j + " bytes encoded, max " + k + ")");
                writeVarInt(j);
                buf.writeBytes(byteBuf);
            } finally {
                byteBuf.release();
            }
        }
    }

    @Contract(" -> new")
    public @NotNull UUID readUUID() {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public void writeUUID(@NotNull UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public int readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    public void writeUnsignedByte(int value) {
        buf.writeByte(value & 0xFF);
    }

    public <F, T> T readTo(@NotNull SimpleBufferCodec<F, T> bufferCodec) {
        return bufferCodec.readTo(this);
    }

    public <F, T> F readFrom(@NotNull SimpleBufferCodec<F, T> bufferCodec) {
        return bufferCodec.readFrom(this);
    }

    public <T> void writeTo(@NotNull SimpleBufferCodec<?, T> bufferCodec, T value) {
        bufferCodec.writeTo(value, this);
    }

    public <F> void writeFrom(@NotNull SimpleBufferCodec<F, ?> bufferCodec, F value) {
        bufferCodec.writeFrom(value, this);
    }

    public void writeVector(@NotNull Vector vector) {
        writeDouble(vector.getX());
        writeDouble(vector.getY());
        writeDouble(vector.getZ());
    }
    @Contract(" -> new")
    public @NotNull Vector readVector() {
        return new Vector(readDouble(), readDouble(), readDouble());
    }

    public void writeBlockVector(@NotNull BlockVector blockVector) {
        writeLong(CommonUtils.packBlockVector(blockVector));
    }
    @Contract(" -> new")
    public @NotNull BlockVector readBlockVector() {
        return CommonUtils.unpackBlockVector(readLong());
    }

    public void writeEntityType(EntityType entityType) {
        NMS.write(entityType, this);
    }

    public EntityType readEntityType() {
        return NMS.read(EntityType.class, this);
    }

    public void writeComponent(Component component) {
        NMS.write(component, this);
    }

    public ProfilePropertySet readPlayerProfileProperties() {
        return NMS.read(ProfilePropertySet.class, this);
    }

    public void writePlayerProfileProperties(ProfilePropertySet profileProperties) {
        NMS.write(profileProperties, this);
    }

    public Component readComponent() {
        return NMS.read(Component.class, this);
    }

    public void writeNamedTextColor(NamedTextColor color) {
        NMS.write(color, this);
    }

    public NamedTextColor readNamedTextColor() {
        return NMS.read(NamedTextColor.class, this);
    }

    public void writeTeamOptionStatus(Team.OptionStatus optionStatus) {
        NMS.write(optionStatus, this);
    }

    public Team.OptionStatus readTeamOptionStatus() {
        return NMS.read(Team.OptionStatus.class, this);
    }

    public void writeLowPrecisionVector(double vx, double vy, double vz) {
        vx = lowPrecision(vx);
        vy = lowPrecision(vy);
        vz = lowPrecision(vz);

        double absMax = Math.max(Math.abs(vx), Math.max(Math.abs(vy), Math.abs(vz)));
        if (absMax < 3.051944088384301E-5D) {
            writeByte((byte) 0);
            return;
        }

        long magnitude = (long) Math.ceil(absMax);
        boolean hasFlag = (magnitude & 3L) != magnitude;
        long j = hasFlag ? (magnitude & 3L) | 4L : magnitude;

        long packedX = pack(vx / magnitude) << 3;
        long packedY = pack(vy / magnitude) << 18;
        long packedZ = pack(vz / magnitude) << 33;

        long finalPacked = j | packedX | packedY | packedZ;

        writeByte((byte) (finalPacked));
        writeByte((byte) (finalPacked >> 8));
        writeInt((int) (finalPacked >> 16));

        if (hasFlag) {
            writeVarInt((int) (magnitude >> 2));
        }
    }

    public void writeUnsignedInt(long value) {
        if (value < 0 || value > 0xFFFFFFFFL)
            throw new IllegalArgumentException("Value out of range for unsigned int: " + value);
        writeInt((int) value);
    }

    public long readUnsignedInt() {
        int signed = readInt();
        return signed & 0xFFFFFFFFL;
    }

    public double[] readLowPrecisionVector() {
        int first = readUnsignedByte();
        if (first == 0) return new double[]{0, 0, 0};

        int second = readUnsignedByte();
        long i = readUnsignedInt();
        long j = (i << 16) | ((long) second << 8) | (long) first;
        long k = first & 3;

        if ((first & 4) == 4)
            k |= ((long) readVarInt() & 0xFFFFFFFFL) << 2;

        return new double[]{
                unpack(j >> 3) * k,
                unpack(j >> 18) * k,
                unpack(j >> 33) * k
        };
    }

    public void readBytes(byte[] destination) {
        buf.readBytes(destination);
    }

    public void writeBytes(byte[] source) {
        buf.writeBytes(source);
    }



    public BitSet readFixedBitSet(int size) {
        byte[] bytes = new byte[Math.ceilDiv(size, 8)];
        this.readBytes(bytes);
        return BitSet.valueOf(bytes);
    }

    public void writeFixedBitSet(BitSet bitSet, int size) {
        if (bitSet.length() > size) {
            int var10002 = bitSet.length();
            throw new EncoderException("BitSet is larger than expected size (" + var10002 + ">" + size + ")");
        } else {
            byte[] bytes = bitSet.toByteArray();
            this.writeBytes(Arrays.copyOf(bytes, Math.ceilDiv(size, 8)));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T readEnum(@NotNull Class<T> enumClass) {
        return (T)((Enum<T>[])enumClass.getEnumConstants())[readVarInt()];
    }

    public void writeEnum(@NotNull Enum<?> value) {
        writeVarInt(value.ordinal());
    }

    public <E extends Enum<E>> void writeEnumSet(EnumSet<E> enumSet, Class<E> enumClass) {
        E[] enums = enumClass.getEnumConstants();
        BitSet bitSet = new BitSet(enums.length);
        for(int i = 0; i < enums.length; ++i)
            bitSet.set(i, enumSet.contains(enums[i]));
        writeFixedBitSet(bitSet, enums.length);
    }

    public <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> enumClass) {
        E[] enums = enumClass.getEnumConstants();
        BitSet fixedBitSet = readFixedBitSet(enums.length);
        EnumSet<E> set = EnumSet.noneOf(enumClass);
        for(int i = 0; i < enums.length; ++i)
            if (fixedBitSet.get(i))
                set.add(enums[i]);
        return set;
    }

    public <T extends PseudoEnum> T readPseudoEnum(@NotNull Class<T> pseudoEnumClass) {
        return PseudoEnum.values(pseudoEnumClass)[readVarInt()];
    }

    public void writePseudoEnum(@NotNull PseudoEnum value) {
        writeVarInt(value.ordinal());
    }

    // todo: in the future, make this be able to get the constants from a registry, right now providing them directly is totally fine
    public <E extends PseudoEnum> void writePseudoEnumSet(PseudoEnumSet<E> pseudoEnumSet, E[] constants) {
        var totalAmount = constants.length;
        BitSet bitSet = new BitSet(totalAmount);
        for(int i = 0; i < totalAmount; ++i)
            bitSet.set(i, pseudoEnumSet.contains(constants[i]));
        writeFixedBitSet(bitSet, totalAmount);
    }

    public <E extends PseudoEnum> PseudoEnumSet<E> readPseudoEnumSet(Class<E> pseudoEnumClass) {
        E[] enums = pseudoEnumClass.getEnumConstants();
        BitSet fixedBitSet = readFixedBitSet(enums.length);
        PseudoEnumSet<E> set = PseudoEnumSet.noneOf(pseudoEnumClass);
        for(int i = 0; i < enums.length; ++i)
            if (fixedBitSet.get(i))
                set.add(enums[i]);
        return set;
    }
    public <T, C extends Collection<T>> C readCollection(@NotNull IntFunction<C> collectionFactory, Function<? super SimpleByteBuf, T> elementReader) {
        return readCollection(collectionFactory, elementReader, Integer.MAX_VALUE);
    }

    public <T, C extends Collection<T>> C readCollection(@NotNull IntFunction<C> collectionFactory, Function<? super SimpleByteBuf, T> elementReader, int maxSize) {
        int varInt = readCount(maxSize);
        C collection = collectionFactory.apply(Math.min(varInt, 65536));
        for (int i = 0; i < varInt; ++i)
            collection.add(elementReader.apply(this));
        return collection;
    }

    public <T> void writeCollection(@NotNull Collection<T> collection, BiConsumer<? super SimpleByteBuf, T> elementWriter, int maxSize) {
        writeCount(collection.size(), maxSize);
        for(T object : collection)
            elementWriter.accept(this, object);
    }

    public <T> void writeCollection(@NotNull Collection<T> collection, BiConsumer<? super SimpleByteBuf, T> elementWriter) {
        writeCollection(collection, elementWriter, Integer.MAX_VALUE);
    }

    public <T> List<T> readList(Function<? super SimpleByteBuf, T> elementReader) {
        return this.readCollection(Lists::newArrayListWithCapacity, elementReader);
    }

    public <T> Set<T> readSet(Function<? super SimpleByteBuf, T> elementReader) {
        return this.readCollection(HashSet::new, elementReader);
    }

    public int readCount(int maxSize) {
        int i = readVarInt();
        if (i > maxSize)
            throw new DecoderException(i + " elements exceeded max size of: " + maxSize);
        else
            return i;
    }

    public void writeCount(int count, int maxSize) {
        if (count > maxSize)
            throw new EncoderException(count + " elements exceeded max size of: " + maxSize);
        else
            writeVarInt(count);
    }

    @Nullable
    public <T> T readNullable(Function<SimpleByteBuf, T> codec) {
        return readBoolean() ? codec.apply(this) : null;
    }

    public <T> void writeNullable(@Nullable T value, BiConsumer<SimpleByteBuf, T> codec) {
        if (value != null) {
            writeBoolean(true);
            codec.accept(this, value);
        } else
            writeBoolean(false);
    }

    public void writeNullable(@Nullable BufferSerializable<?> value) {
        if (value != null) {
            writeBoolean(true);
            value.write(this);
        } else
            writeBoolean(false);
    }

    public void write(BufferSerializable<?> serializable) {
        serializable.write(this);
    }

    public <T extends BufferSerializable<?>> T read(T serializable) {
        serializable.read(this);
        return serializable;
    }

    public void writeInstant(Instant instant) {
        writeLong(instant.toEpochMilli());
    }

    public Instant readInstant() {
        return Instant.ofEpochMilli(this.readLong());
    }

    public static void write(SimpleByteBuf buffer, BufferSerializable<?> bufferSerializable) {
        bufferSerializable.write(buffer);
    }

    public void writeChatSessionData(ChatSessionData chatSessionData) {
        chatSessionData.write(this);
    }

    public ChatSessionData readChatSessionData() {
        return new ChatSessionData(this);
    }

    public void writeProfilePublicKey(ProfilePublicKey profilePublicKey) {
        profilePublicKey.write(this);
    }

    public ProfilePublicKey readProfilePublicKey() {
        return new ProfilePublicKey(this);
    }

    public void writePublicKey(PublicKey publicKey) {
        this.writeByteArray(publicKey.getEncoded());
    }

    public PublicKey readPublicKey() {
        try {
            return CommonUtils.byteToPublicKey(this.readByteArray(512));
        } catch (IllegalArgumentException var2) {
            throw new DecoderException("Malformed public key ByteArray", var2);
        }
    }

    public void writeByteArray(byte[] array) {
        writeVarInt(array.length);
        buf.writeBytes(array);
    }

    public byte[] readByteArray(int maxLength) {
        int i = readVarInt();
        if (i > maxLength)
            throw new DecoderException(i + " elements exceeded max size of " + maxLength + " for ByteArray");
        else {
            byte[] bytes = new byte[i];
            buf.readBytes(bytes);
            return bytes;
        }
    }

    public BlockData readBlockData() {
        return NMS.read(BlockData.class, this);
    }

    public void writeBlockData(BlockData blockData) {
        NMS.write(blockData, this);
    }

    public Attribute readAttribute() {
        return NMS.read(Attribute.class, this);
    }

    public void writeAttribute(Attribute attribute) {
        NMS.write(attribute, this);
    }

    public AttributeModifier readAttributeModifier() {
        return NMS.read(AttributeModifier.class, this);
    }

    public void writeAttributeModifier(AttributeModifier attributeModifier) {
        NMS.write(attributeModifier, this);
    }

    public AttributeSnapshot readAttributeSnapshot() {
        return NMS.read(AttributeSnapshot.class, this);
    }

    public void writeAttributeSnapshot(AttributeSnapshot attributeSnapshot) {
        NMS.write(attributeSnapshot, this);
    }

    public Sound readSound() {
        return NMS.read(Sound.class, this);
    }

    public void writeSound(Sound sound) {
        NMS.write(sound, this);
    }

    public SoundCategory readSoundCategory() {
        return NMS.read(SoundCategory.class, this);
    }

    public void writeSoundCategory(SoundCategory soundCategory) {
        NMS.write(soundCategory, this);
    }

    public ParticleWrapper readParticle() {
        return NMS.read(ParticleWrapper.class, this);
    }

    public void writeParticle(Particle particle) {
        NMS.write(particle, this);
    }

    public void writeParticle(ParticleWrapper particle) {
        NMS.write(particle, this);
    }

    public Quaternionf readQuaternionf() {
        return new Quaternionf(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public void writeQuaternionf(Quaternionf quaternionf) {
        writeFloat(quaternionf.x);
        writeFloat(quaternionf.y);
        writeFloat(quaternionf.z);
        writeFloat(quaternionf.w);
    }



}
