package it.jakegblp.lusk.nms.core.protocol.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class SystemChatPacket implements ClientboundPacket, Cloneable{

    Component component;
    boolean overlay;

    @Override
    public Object asNMS() {
        return NMS.toNMSSystemChatPacket(this);
    }

    @Override
    public SystemChatPacket clone() {
        try {
            SystemChatPacket clone = (SystemChatPacket) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
