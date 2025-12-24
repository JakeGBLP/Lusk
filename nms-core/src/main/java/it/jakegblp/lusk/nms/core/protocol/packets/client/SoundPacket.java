package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@AllArgsConstructor
@NullMarked
public class SoundPacket implements ClientboundPacket{

    protected Sound sound;
    protected SoundCategory soundSource;
    protected double x,y,z;
    protected float volume, pitch;
    protected long seed;

    public void setLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public void setLocation(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public Vector getLocation() {
        return new Vector(x,y,z);
    }

    @Override
    public Object asNMS() {
        return AbstractNMS.NMS.toNMSSoundPacket(this);
    }
}
