package it.jakegblp.lusk.nms.impl.from_1_20_6;


import com.mojang.datafixers.util.Pair;
import it.jakegblp.lusk.nms.core.adapters.SetEquipmentPacketAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static it.jakegblp.lusk.common.StructureTranslation.fromMapToPairList;
import static it.jakegblp.lusk.common.StructureTranslation.fromPairListToMap;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class From_1_20_6 implements SetEquipmentPacketAdapter<ClientboundSetEquipmentPacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from) {
        Object list = fromMapToPairList(from.getEquipment(), slot -> NMS.asNMSEquipmentSlot(slot), itemStack -> NMS.asNMSItemStack(itemStack));
        return new ClientboundSetEquipmentPacket(from.getId(), (List<Pair<EquipmentSlot, ItemStack>>) list);
    }

    @Override
    public SetEquipmentPacket fromNMSSetEquipmentPacket(ClientboundSetEquipmentPacket from) {
        return new SetEquipmentPacket(from.getEntity(), fromPairListToMap(from.getSlots(), slot -> NMS.asEquipmentSlot(slot), itemStack -> NMS.asItemStack(itemStack)));
    }

    @Override
    public Class<ClientboundSetEquipmentPacket> getNMSSetEquipmentPacketClass() {
        return ClientboundSetEquipmentPacket.class;
    }
}