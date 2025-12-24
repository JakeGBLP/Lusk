package it.jakegblp.lusk.nms.impl.from_1_21_3;

import it.jakegblp.lusk.nms.core.adapters.PlayerPositionPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.PlayerRotationPacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class From_1_21_3 implements PlayerRotationPacketAdapter, PlayerPositionPacketAdapter<Relative, ClientboundPlayerPositionPacket> {

    @Override
    public Object toNMSPlayerRotationPacket(PlayerRotationPacket from) {
        return new ClientboundPlayerRotationPacket(from.getYaw(), from.getPitch());
    }

    @Override
    public PlayerRotationPacket fromNMSPlayerRotationPacket(Object from) {
        ClientboundPlayerRotationPacket packet = (ClientboundPlayerRotationPacket) from;
        return new PlayerRotationPacket(packet.yRot(), packet.xRot());
    }

    @Override
    public Class<?> getNMSPlayerRotationPacketClass() {
        return ClientboundPlayerRotationPacket.class;
    }

    @Override
    public Class<Relative> getNMSRelativeFlagClass() {
        return Relative.class;
    }

    @Override
    public ClientboundPlayerPositionPacket asNMSPlayerPositionPacket(PlayerPositionPacket packet) {
        return new ClientboundPlayerPositionPacket(packet.getTeleportId(),
                new PositionMoveRotation(
                        (Vec3) NMS.toNMS(packet.getPosition()),
                        (Vec3) NMS.toNMS(packet.getVelocity()),
                        packet.getYaw(),
                        packet.getPitch()
                ), asNMSRelativeFlags(packet.getRelativeFlags()));
    }

    @Override
    public PlayerPositionPacket fromNMSPlayerPositionPacket(ClientboundPlayerPositionPacket clientboundPlayerPositionPacket) {
        var movement = clientboundPlayerPositionPacket.change();
        return new PlayerPositionPacket(
                clientboundPlayerPositionPacket.id(),
                (Vector) NMS.fromNMS(movement.position()),
                (Vector) NMS.fromNMS(movement.deltaMovement()),
                movement.yRot(),
                movement.xRot(),
                fromNMSRelativeFlag(clientboundPlayerPositionPacket.relatives())
        );
    }

    @Override
    public Class<ClientboundPlayerPositionPacket> getNMSPlayerPositionPacketClass() {
        return ClientboundPlayerPositionPacket.class;
    }
}