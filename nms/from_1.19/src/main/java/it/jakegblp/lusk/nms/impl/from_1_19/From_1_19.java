package it.jakegblp.lusk.nms.impl.from_1_19;

import it.jakegblp.lusk.nms.core.adapters.AddEntityPacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.AddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;


public class From_1_19 implements AddEntityPacketAdapter<
        ClientboundAddEntityPacket
        > {

    @Override
    @SuppressWarnings("ConstantConditions")
    public ClientboundAddEntityPacket toNMSAddEntityPacket(AddEntityPacket from) {
        return new ClientboundAddEntityPacket(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                (EntityType<?>) NMS.toNMSEntityType(from.getEntityType()),
                from.getData(),
                (Vec3) NMS.asNMSVector(from.getVelocity()),
                from.getHeadYaw());
    }

    @Override
    public AddEntityPacket fromNMSAddEntityPacket(ClientboundAddEntityPacket from) {
        return new AddEntityPacket(
                from.getId(),
                from.getUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getXRot(),
                from.getYRot(),
                NMS.fromNMSEntityType(from.getType()),
                from.getData(),
                new Vector(from.getXa(), from.getYa(), from.getZa()),
                from.getYHeadRot()
        );
    }

    @Override
    public Class<ClientboundAddEntityPacket> getNMSAddEntityPacketClass() {
        return ClientboundAddEntityPacket.class;
    }
}
