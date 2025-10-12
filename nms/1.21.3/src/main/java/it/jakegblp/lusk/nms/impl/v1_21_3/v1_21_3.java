package it.jakegblp.lusk.nms.impl.v1_21_3;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapters.*;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.OptionalInt;
import java.util.UUID;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_21_3 extends AbstractNMS<
        EntityDataSerializer<?>
        > {

    public v1_21_3(JavaPlugin plugin, EntityTypeAdapter<?> entityTypeAdapter, MajorChangesAdapter majorChangesAdapter, AddEntityPacketAdapter<?> addEntityPacketAdapter, EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter, PlayerRotationPacketAdapter playerRotationPacketAdapter, ClientBundlePacketAdapter<?> clientBundlePacketAdapter, SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter, AdventureAdapter adventureAdapter, PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter) {
        super(plugin, Version.of(1, 21, 3), entityTypeAdapter, majorChangesAdapter, addEntityPacketAdapter, entityMetadataPacketAdapter, playerRotationPacketAdapter, clientBundlePacketAdapter, setEquipmentPacketAdapter, adventureAdapter, playerPositionPacketAdapter);
    }

    @Override
    public void init() {
        registerEntityDataSerializer(EntitySerializerKey.normal(Byte.class), BYTE);
        registerEntityDataSerializer(EntitySerializerKey.normal(Integer.class), INT);
        registerEntityDataSerializer(EntitySerializerKey.normal(Long.class), LONG);
        registerEntityDataSerializer(EntitySerializerKey.normal(Float.class), FLOAT);
        registerEntityDataSerializer(EntitySerializerKey.normal(String.class), STRING);
        registerEntityDataSerializer(EntitySerializerKey.normal(Component.class), COMPONENT);
        registerEntityDataSerializer(EntitySerializerKey.optional(Component.class), OPTIONAL_COMPONENT);
        registerEntityDataSerializer(EntitySerializerKey.normal(ItemStack.class), ITEM_STACK);
        registerEntityDataSerializer(EntitySerializerKey.normal(Boolean.class), BOOLEAN);
        registerEntityDataSerializer(EntitySerializerKey.normal(Rotations.class), ROTATIONS);
        registerEntityDataSerializer(EntitySerializerKey.normal(BlockPos.class), BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerKey.optional(BlockPos.class), OPTIONAL_BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerKey.normal(Direction.class), DIRECTION);
        registerEntityDataSerializer(EntitySerializerKey.optional(UUID.class), OPTIONAL_UUID);
        registerEntityDataSerializer(EntitySerializerKey.normal(BlockState.class), BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerKey.optional(BlockState.class), OPTIONAL_BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerKey.normal(CompoundTag.class), COMPOUND_TAG);
        registerEntityDataSerializer(EntitySerializerKey.normal(ParticleOptions.class), PARTICLE);
        registerEntityDataSerializer(EntitySerializerKey.list(ParticleOptions.class), PARTICLES);
        registerEntityDataSerializer(EntitySerializerKey.normal(VillagerData.class), VILLAGER_DATA);
        registerEntityDataSerializer(EntitySerializerKey.normal(OptionalInt.class), OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(EntitySerializerKey.normal(Pose.class), POSE);
        registerEntityDataSerializer(EntitySerializerKey.holder(CatVariant.class), CAT_VARIANT);
        registerEntityDataSerializer(EntitySerializerKey.holder(WolfVariant.class), WOLF_VARIANT);
        registerEntityDataSerializer(EntitySerializerKey.holder(FrogVariant.class), FROG_VARIANT);
        registerEntityDataSerializer(EntitySerializerKey.optional(GlobalPos.class), OPTIONAL_GLOBAL_POS);
        registerEntityDataSerializer(EntitySerializerKey.holder(PaintingVariant.class), PAINTING_VARIANT);
        registerEntityDataSerializer(EntitySerializerKey.normal(Sniffer.State.class), SNIFFER_STATE);
        registerEntityDataSerializer(EntitySerializerKey.normal(Armadillo.ArmadilloState.class), ARMADILLO_STATE);
        registerEntityDataSerializer(EntitySerializerKey.normal(Vector3f.class), VECTOR3);
        registerEntityDataSerializer(EntitySerializerKey.normal(Quaternionf.class), QUATERNION);
    }

}