package it.jakegblp.lusk.nms.impl.from_1_18_to_1_19_3;

import it.jakegblp.lusk.nms.core.adapters.PlayerPositionPacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.bukkit.util.Vector;

import java.util.Set;

public class From_1_18_To_1_19_3 implements PlayerPositionPacketAdapter<ClientboundPlayerPositionPacket.RelativeArgument, ClientboundPlayerPositionPacket> {

    @Override
    public Class<ClientboundPlayerPositionPacket.RelativeArgument> getNMSRelativeFlagClass() {
        return ClientboundPlayerPositionPacket.RelativeArgument.class;
    }
    @Override
    public ClientboundPlayerPositionPacket asNMSPlayerPositionPacket(PlayerPositionPacket packet) {
        var position = packet.getPosition();
        return new ClientboundPlayerPositionPacket(
                position.getX(),
                position.getY(),
                position.getZ(),
                packet.getYaw(),
                packet.getPitch(),
                asNMSRelativeFlags(packet.getRelativeFlags()),
                packet.getTeleportId(),
                true
        );
    }

    @Override
    public PlayerPositionPacket fromNMSPlayerPositionPacket(ClientboundPlayerPositionPacket packet) {
        return new PlayerPositionPacket(
                packet.getId(),
                new Vector(packet.getX(), packet.getY(), packet.getZ()),
                packet.getYRot(),
                packet.getXRot(),
                fromNMSRelativeFlag(packet.getRelativeArguments())
        );
    }

    @Override
    public Class<ClientboundPlayerPositionPacket> getNMSPlayerPositionPacketClass() {
        return ClientboundPlayerPositionPacket.class;
    }
}