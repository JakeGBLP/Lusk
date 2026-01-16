package it.jakegblp.lusk.nms.impl.v1_21_10;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.adapters.SharedBiomeAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.coppergolem.CopperGolemState;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_21_10 extends AbstractNMS {

    public v1_21_10(JavaPlugin plugin, SharedBehaviorAdapter sharedBehaviorAdapter, SharedBiomeAdapter sharedBiomeAdapter) {
        super(plugin, Version.of(1, 21, 10), sharedBehaviorAdapter, sharedBiomeAdapter);
    }

    @Override
    public void init() {
        registerEntityDataSerializer(this, Byte.class, BYTE);
        registerEntityDataSerializer(this, Integer.class, INT);
        registerEntityDataSerializer(this, Long.class, LONG);
        registerEntityDataSerializer(this, Float.class, FLOAT);
        registerEntityDataSerializer(this, String.class, STRING);
        registerEntityDataSerializer(this, Component.class, COMPONENT);
        registerOptionalEntityDataSerializer(this, Component.class, OPTIONAL_COMPONENT);
        registerEntityDataSerializer(this, ItemStack.class, ITEM_STACK);
        registerEntityDataSerializer(this, BlockState.class, BLOCK_STATE);
        registerOptionalEntityDataSerializer(this, BlockState.class, OPTIONAL_BLOCK_STATE);
        registerEntityDataSerializer(this, Boolean.class, BOOLEAN);
        registerEntityDataSerializer(this, ParticleOptions.class, PARTICLE);
        registerOptionalEntityDataSerializer(this, ParticleOptions.class, PARTICLES);
        registerEntityDataSerializer(this, Rotations.class, ROTATIONS);
        registerEntityDataSerializer(this, BlockPos.class, BLOCK_POS);
        registerOptionalEntityDataSerializer(this, BlockPos.class, OPTIONAL_BLOCK_POS);
        registerEntityDataSerializer(this, Direction.class, DIRECTION);
        // todo: handle, fully replaces compound tag
        registerOptionalEntityDataSerializer(this, EntityReference.class, OPTIONAL_LIVING_ENTITY_REFERENCE);
        registerOptionalEntityDataSerializer(this, GlobalPos.class, OPTIONAL_GLOBAL_POS);
        registerEntityDataSerializer(this, VillagerData.class, VILLAGER_DATA);
        registerOptionalEntityDataSerializer(this, Integer.class, OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(this, Pose.class, POSE);
        registerHolderEntityDataSerializer(this, CatVariant.class, CAT_VARIANT);
        registerHolderEntityDataSerializer(this, ChickenVariant.class, CHICKEN_VARIANT);
        registerHolderEntityDataSerializer(this, CowVariant.class, COW_VARIANT);
        registerHolderEntityDataSerializer(this, WolfVariant.class, WOLF_VARIANT);
        registerHolderEntityDataSerializer(this, WolfSoundVariant.class, WOLF_SOUND_VARIANT);
        registerHolderEntityDataSerializer(this, FrogVariant.class, FROG_VARIANT);
        registerHolderEntityDataSerializer(this, PigVariant.class, PIG_VARIANT);
        registerHolderEntityDataSerializer(this, PaintingVariant.class, PAINTING_VARIANT);
        registerEntityDataSerializer(this, Sniffer.State.class, SNIFFER_STATE);
        registerEntityDataSerializer(this, WeatheringCopper.WeatherState.class, WEATHERING_COPPER_STATE);
        registerEntityDataSerializer(this, CopperGolemState.class, COPPER_GOLEM_STATE);
        registerEntityDataSerializer(this, Vector3f.class, VECTOR3);
        registerEntityDataSerializer(this, Quaternionf.class, QUATERNION);
        registerEntityDataSerializer(this, ResolvableProfile.class, RESOLVABLE_PROFILE);
    }

}
