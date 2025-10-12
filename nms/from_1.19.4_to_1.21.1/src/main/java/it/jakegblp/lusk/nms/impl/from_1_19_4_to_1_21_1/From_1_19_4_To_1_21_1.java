package it.jakegblp.lusk.nms.impl.from_1_19_4_to_1_21_1;

import it.jakegblp.lusk.nms.core.adapters.PlayerPositionPacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.RelativeMovement;
import org.bukkit.util.Vector;

public class From_1_19_4_To_1_21_1 implements PlayerPositionPacketAdapter<RelativeMovement, ClientboundPlayerPositionPacket> {
    @Override
    public Class<RelativeMovement> getNMSRelativeFlagClass() {
        return RelativeMovement.class;
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
                packet.getTeleportId()
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
