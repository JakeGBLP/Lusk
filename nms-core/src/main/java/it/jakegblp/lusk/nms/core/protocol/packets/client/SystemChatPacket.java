package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.SystemChatPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class SystemChatPacket implements BufferSerializableClientboundPacket<SystemChatPacketEvent> {

    protected Component component;
    protected boolean overlay;

    public SystemChatPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeComponent(component);
        buffer.writeBoolean(overlay);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        component = buffer.readComponent();
        overlay = buffer.readBoolean();
    }

    @Override
    public SystemChatPacketEvent createEvent(Player player, boolean async) {
        return new SystemChatPacketEvent(this, player, async);
    }

    @Override
    public SystemChatPacket copy() {
        return new SystemChatPacket(component, overlay);
    }
}
