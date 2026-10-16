package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.SimpleList;
import it.jakegblp.lusk.nms.core.event.client.PlayerInfoRemovePacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PlayerInfoRemovePacket implements ClientboundPacket<PlayerInfoRemovePacketEvent>, SimpleList<UUID> {
    protected List<UUID> players;

    public PlayerInfoRemovePacket(List<UUID> players) {
        this.players = new ArrayList<>(players);
    }

    public PlayerInfoRemovePacket(UUID... players) {
        this(List.of(players));
    }

    public PlayerInfoRemovePacket() {
        this(new ArrayList<>());
    }
    public PlayerInfoRemovePacket(SimpleByteBuf buffer) {
        this(buffer.readList(SimpleByteBuf::readUUID));
    }

    @Override
    public List<UUID> get() {
        return players;
    }

    public void add(UUID... players) {
        add(List.of(players));
    }

    public void add(List<UUID> players) {
        this.players.addAll(players);
    }

    public void remove(UUID... players) {
        remove(List.of(players));
    }

    public void remove(List<UUID> players) {
        this.players.removeAll(players);
    }

    public void set(UUID... players) {
        set(List.of(players));
    }

    public void set(List<UUID> players) {
        this.players = new ArrayList<>(players);
    }

    public void clear() {
        this.players.clear();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeCollection(this.players, SimpleByteBuf::writeUUID);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.players = buffer.readList(SimpleByteBuf::readUUID);
    }

    @Override
    public PlayerInfoRemovePacketEvent createEvent(Player player, boolean async) {
        return new PlayerInfoRemovePacketEvent(this, player, async);
    }

    @Override
    public PlayerInfoRemovePacket copy() {
        return new PlayerInfoRemovePacket(players);
    }
}
