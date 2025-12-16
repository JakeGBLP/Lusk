package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;


@AllArgsConstructor
@Getter
@Setter
public class SystemChatMessagePacket implements ClientboundPacket{

    protected Component component;
    protected boolean overlay;

    @Override
    public Object asNMS() {
        return NMS.toNMSSystemChatMessagePacket(this);
    }

    @Override
    @SneakyThrows
    public SystemChatMessagePacket clone() {
        return (SystemChatMessagePacket) super.clone();
    }
}
