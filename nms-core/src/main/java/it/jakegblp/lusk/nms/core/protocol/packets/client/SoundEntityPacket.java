package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@AllArgsConstructor
public class SoundEntityPacket implements ClientboundPacketWithId {
    protected Sound sound;
    protected SoundCategory soundCategory;
    protected int id;
    protected float volume, pitch;
    protected long seed;

    public SoundEntityPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeSound(sound);
        buffer.writeSoundCategory(soundCategory);
        buffer.writeVarInt(id);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
        buffer.writeLong(seed);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.sound = buffer.readSound();
        this.soundCategory = buffer.readSoundCategory();
        this.id = buffer.readVarInt();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.seed = buffer.readLong();
    }

    /**
     * @return the id of the entity that plays the sound, 0 is none
     */
    @Override
    public int getId() {
        return id;
    }

    public @Nullable Entity getEntity(World world) {
        return NMS.getEntityFromId(id, world);
    }

    public @Nullable Location getLocation(World world) {
        var entity = getEntity(world);
        return entity != null ? entity.getLocation() : null;
    }

    @Override
    public SoundEntityPacket copy() {
        return new SoundEntityPacket(sound, soundCategory, id, volume, pitch, seed);
    }
}
