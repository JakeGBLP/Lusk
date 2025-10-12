package it.jakegblp.lusk.nms.impl.from_1_19_4;

import it.jakegblp.lusk.nms.core.adapters.ClientBundlePacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

import java.util.ArrayList;
import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class From_1_19_4 implements ClientBundlePacketAdapter<ClientboundBundlePacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSClientBundlePacket(ClientBundlePacket from) {
        List<Packet<ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public ClientBundlePacket fromNMSClientBundlePacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.lusk.nms.core.protocol.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new ClientBundlePacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSClientBundlePacketClass() {
        return ClientboundBundlePacket.class;
    }
}