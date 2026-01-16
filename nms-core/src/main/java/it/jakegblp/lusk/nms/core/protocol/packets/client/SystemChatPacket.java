package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
@Getter
@Setter
public class SystemChatPacket implements BufferSerializableClientboundPacket {

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
    public SystemChatPacket copy() {
        return new SystemChatPacket(component, overlay);
    }
}
