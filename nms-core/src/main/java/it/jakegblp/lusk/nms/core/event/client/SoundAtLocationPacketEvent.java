package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SoundPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SoundAtLocationPacketEvent extends SoundPacketEvent<SoundPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected Vector position;

    public SoundAtLocationPacketEvent(SoundPacket packet, Player player, boolean async) {
        super(player, packet.getSound(), packet.getSoundCategory(), packet.getVolume(), packet.getPitch(), packet.getSeed(), async);
        this.position = packet.getPosition().clone();
    }

    public Vector getPosition() {
        return position.clone();
    }

    public void setPosition(Vector position) {
        markModified();
        this.position = position.clone();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public SoundPacket createPacket() {
        return new SoundPacket(sound, soundCategory, position, volume, pitch, seed);
    }
}
