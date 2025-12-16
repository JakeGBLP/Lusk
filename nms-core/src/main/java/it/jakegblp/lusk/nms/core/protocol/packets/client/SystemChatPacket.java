package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class SystemChatPacket implements ClientboundPacket{

    Component component;
    boolean overlay;

    @Override
    public Object asNMS() {
        return NMS.toNMSSystemChatPacket(this);
    }
}
