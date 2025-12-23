package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
@Getter
@Setter
public class SoundEntityPacket implements ClientboundPacket {

    Sound sound;
    SoundCategory soundSource;
    int id;
    float volume;
    float pitch;
    long seed;
    Entity entity;

    public SoundEntityPacket(Sound sound, SoundCategory category, Entity entity, float volume, float pitch, long seed){
        this.sound = sound;
        this.soundSource = category;
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;

        this.id = entity.getEntityId();
    }

    public SoundEntityPacket(Sound sound, SoundCategory category, int entityID, float volume, float pitch, long seed){
        this.sound = sound;
        this.soundSource = category;
        this.id = entityID;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }


    @Override
    public Object asNMS() {
        return null;
    }
}
