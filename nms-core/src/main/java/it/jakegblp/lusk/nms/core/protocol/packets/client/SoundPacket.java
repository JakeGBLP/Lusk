package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

@Getter
@Setter
@AllArgsConstructor
public class SoundPacket implements ClientboundPacket{

    Sound sound;
    SoundCategory soundSource;
    double x,y,z;
    float volume, pitch;
    long seed;

    @Override
    public Object asNMS() {
        return AbstractNMS.NMS.toNMSSoundPacket(this);
    }
}
