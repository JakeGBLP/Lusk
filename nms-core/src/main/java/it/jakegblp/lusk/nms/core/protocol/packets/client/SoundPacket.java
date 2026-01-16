package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@AllArgsConstructor
@NullMarked
public class SoundPacket implements BufferSerializableClientboundPacket {

    protected Sound sound;
    protected SoundCategory soundCategory;
    protected int x,y,z;
    protected float volume, pitch;
    protected long seed;

    public SoundPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public void setPosition(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public void setPosition(Vector vector) {
        this.x = vector.getBlockX();
        this.y = vector.getBlockY();
        this.z = vector.getBlockZ();
    }

    @Contract(pure = true)
    public Vector getPosition() {
        return new Vector(x,y,z);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeSound(sound);
        buffer.writeSoundCategory(soundCategory);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
        buffer.writeLong(seed);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.sound = buffer.readSound();
        this.soundCategory = buffer.readSoundCategory();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.seed = buffer.readLong();
    }

    @Override
    public SoundPacket copy() {
        return new SoundPacket(sound, soundCategory, x, y, z, volume, pitch, seed);
    }
}
