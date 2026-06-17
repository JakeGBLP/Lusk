package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.SoundAtEntityPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.level.LevelUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class SoundEntityPacket implements ClientboundPacketWithEntityId<SoundAtEntityPacketEvent> {
    protected Sound sound;
    protected SoundCategory soundCategory;
    protected int entityId;
    protected float volume, pitch;
    protected long seed;

    public SoundEntityPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeSound(sound);
        buffer.writeSoundCategory(soundCategory);
        buffer.writeVarInt(entityId);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
        buffer.writeLong(seed);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.sound = buffer.readSound();
        this.soundCategory = buffer.readSoundCategory();
        this.entityId = buffer.readVarInt();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.seed = buffer.readLong();
    }

    public @Nullable Entity getEntity(World world) {
        return LevelUtil.getEntityFromID(entityId, world);
    }

    public @Nullable Location getLocation(World world) {
        var entity = getEntity(world);
        return entity != null ? entity.getLocation() : null;
    }

    @Override
    public SoundAtEntityPacketEvent createEvent(Player player, boolean async) {
        return new SoundAtEntityPacketEvent(this, player, async);
    }

    @Override
    public SoundEntityPacket copy() {
        return new SoundEntityPacket(sound, soundCategory, entityId, volume, pitch, seed);
    }
}
