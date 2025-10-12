package it.jakegblp.lusk.nms.impl.v1_17_1;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapters.*;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.OptionalInt;
import java.util.UUID;

import static net.minecraft.network.syncher.DataWatcherRegistry.*;

public final class v1_17_1 extends AbstractNMS<
        DataWatcherSerializer<?>
        > {

    public v1_17_1(JavaPlugin plugin, EntityTypeAdapter<?> entityTypeAdapter, MajorChangesAdapter majorChangesAdapter, AddEntityPacketAdapter<?> addEntityPacketAdapter, EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter, PlayerRotationPacketAdapter playerRotationPacketAdapter, ClientBundlePacketAdapter<?> clientBundlePacketAdapter, SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter, AdventureAdapter adventureAdapter, PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter) {
        super(plugin, Version.of(1, 17, 1), entityTypeAdapter, majorChangesAdapter, addEntityPacketAdapter, entityMetadataPacketAdapter, playerRotationPacketAdapter, clientBundlePacketAdapter, setEquipmentPacketAdapter, adventureAdapter, playerPositionPacketAdapter);
    }

    @Override
    public void init() {
        registerEntityDataSerializer(EntitySerializerKey.normal(Byte.class), a);
        registerEntityDataSerializer(EntitySerializerKey.normal(Integer.class), b);
        registerEntityDataSerializer(EntitySerializerKey.normal(Float.class), c);
        registerEntityDataSerializer(EntitySerializerKey.normal(String.class), d);
        registerEntityDataSerializer(EntitySerializerKey.normal(IChatBaseComponent.class), e);
        registerEntityDataSerializer(EntitySerializerKey.optional(IChatBaseComponent.class), f);
        registerEntityDataSerializer(EntitySerializerKey.normal(ItemStack.class), g);
        registerEntityDataSerializer(EntitySerializerKey.optional(IBlockData.class), h);
        registerEntityDataSerializer(EntitySerializerKey.normal(Boolean.class), i);
        registerEntityDataSerializer(EntitySerializerKey.normal(ParticleParam.class), j);
        registerEntityDataSerializer(EntitySerializerKey.normal(Vector3f.class), k);
        registerEntityDataSerializer(EntitySerializerKey.normal(BlockPosition.class), l);
        registerEntityDataSerializer(EntitySerializerKey.optional(BlockPosition.class), m);
        registerEntityDataSerializer(EntitySerializerKey.normal(EnumDirection.class), n);
        registerEntityDataSerializer(EntitySerializerKey.optional(UUID.class), o);
        registerEntityDataSerializer(EntitySerializerKey.normal(NBTTagCompound.class), p);
        registerEntityDataSerializer(EntitySerializerKey.normal(VillagerData.class), q);
        registerEntityDataSerializer(EntitySerializerKey.normal(OptionalInt.class), r);
        registerEntityDataSerializer(EntitySerializerKey.normal(EntityPose.class), s);
    }

}