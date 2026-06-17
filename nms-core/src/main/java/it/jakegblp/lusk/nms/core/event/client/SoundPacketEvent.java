package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

@Getter
public abstract class SoundPacketEvent<P extends ClientboundPacket<?>> extends ClientPacketEvent<P> {

    protected Sound sound;
    protected SoundCategory soundCategory;
    protected float volume, pitch;
    protected long seed;

    public SoundPacketEvent(Player player, Sound sound, SoundCategory soundCategory, float volume, float pitch, long seed, boolean async) {
        super(player, async);
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    public void setPitch(float pitch) {
        markModified();
        this.pitch = pitch;
    }

    public void setSeed(long seed) {
        markModified();
        this.seed = seed;
    }

    public void setSound(Sound sound) {
        markModified();
        this.sound = sound;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        markModified();
        this.soundCategory = soundCategory;
    }

    public void setVolume(float volume) {
        markModified();
        this.volume = volume;
    }
}
