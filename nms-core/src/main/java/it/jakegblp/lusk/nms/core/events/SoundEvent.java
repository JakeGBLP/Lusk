package it.jakegblp.lusk.nms.core.events;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SoundEntityPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SoundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@NullMarked
public class SoundEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    protected Sound sound;
    protected SoundCategory soundCategory;
    @Nullable
    protected Location location;
    protected float pitch, volume;
    protected long seed;

    protected boolean isEntitySound;
    @Nullable
    protected Integer entityID;
    @Nullable
    protected Entity entity;

    protected boolean cancelled = false;

    public SoundEvent(Player player, Sound sound, SoundCategory soundCategory, Vector position, float pitch, float volume, long seed, boolean isEntitySound, @Nullable Integer entityID, @Nullable Entity entity) {
        super(player);
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.location = position.toLocation(player.getWorld());
        this.pitch = pitch;
        this.volume = volume;
        this.seed = seed;
        this.isEntitySound = isEntitySound;
        this.entityID = entityID;
        this.entity = entity;
    }

    public SoundEvent(Player player, boolean async, Sound sound, SoundCategory soundCategory, @Nullable Location position, float pitch, float volume, long seed, boolean isEntitySound, @Nullable Integer entityID, @Nullable Entity entity) {
        super(player, async);
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.location = position;
        this.pitch = pitch;
        this.volume = volume;
        this.seed = seed;
        this.isEntitySound = isEntitySound;
        this.entityID = entityID;
        this.entity = entity;
    }

    public SoundEvent(Player player, boolean async, SoundPacket soundPacket) {
        this(player, async, soundPacket.getSound(), soundPacket.getSoundCategory(), soundPacket.getPosition().toLocation(player.getWorld()), soundPacket.getPitch(), soundPacket.getVolume(), soundPacket.getSeed(), false, null, null);
    }

    public SoundEvent(Player player, boolean async, SoundEntityPacket soundEntityPacket) {
        this(player, async, soundEntityPacket.getSound(), soundEntityPacket.getSoundCategory(), soundEntityPacket.getLocation(player.getWorld()), soundEntityPacket.getPitch(), soundEntityPacket.getVolume(), soundEntityPacket.getSeed(), false, soundEntityPacket.getId(), soundEntityPacket.getEntity(player.getWorld()));
    }

}
