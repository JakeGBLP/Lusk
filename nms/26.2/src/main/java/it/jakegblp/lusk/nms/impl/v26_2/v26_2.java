package it.jakegblp.lusk.nms.impl.v26_2;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapter.SharedBehaviorAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.golem.CopperGolemState;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.pig.PigSoundVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v26_2 extends AbstractNMS {

    @SuppressWarnings("rawtypes")
    public v26_2(JavaPlugin plugin, SharedBehaviorAdapter sharedBehaviorAdapter) {
        super(plugin, Version.of(26, 2), sharedBehaviorAdapter);
    }

    @Override
    public void init() {
        var codecs = getCodecs();
        registerEntityDataSerializer(codecs, Byte.class, BYTE);
        registerEntityDataSerializer(codecs, Integer.class, INT);
        registerEntityDataSerializer(codecs, Long.class, LONG);
        registerEntityDataSerializer(codecs, Float.class, FLOAT);
        registerEntityDataSerializer(codecs, String.class, STRING);
        registerEntityDataSerializer(codecs, Component.class, COMPONENT);
        registerOptionalEntityDataSerializer(codecs, Component.class, OPTIONAL_COMPONENT);
        registerEntityDataSerializer(codecs, ItemStack.class, ITEM_STACK);
        registerEntityDataSerializer(codecs, BlockState.class, BLOCK_STATE);
        registerOptionalEntityDataSerializer(codecs, BlockState.class, OPTIONAL_BLOCK_STATE);
        registerEntityDataSerializer(codecs, Boolean.class, BOOLEAN);
        registerEntityDataSerializer(codecs, ParticleOptions.class, PARTICLE);
        registerListEntityDataSerializer(codecs, ParticleOptions.class, PARTICLES);
        registerEntityDataSerializer(codecs, Rotations.class, ROTATIONS);
        registerEntityDataSerializer(codecs, BlockPos.class, BLOCK_POS);
        registerOptionalEntityDataSerializer(codecs, BlockPos.class, OPTIONAL_BLOCK_POS);
        registerEntityDataSerializer(codecs, Direction.class, DIRECTION);
        registerOptionalEntityDataSerializer(codecs, EntityReference.class, OPTIONAL_LIVING_ENTITY_REFERENCE);
        registerOptionalEntityDataSerializer(codecs, GlobalPos.class, OPTIONAL_GLOBAL_POS);
        registerEntityDataSerializer(codecs, VillagerData.class, VILLAGER_DATA);
        registerOptionalEntityDataSerializer(codecs, Integer.class, OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(codecs, Pose.class, POSE);
        registerHolderEntityDataSerializer(codecs, CatVariant.class, CAT_VARIANT);
        registerHolderEntityDataSerializer(codecs, CatSoundVariant.class, CAT_SOUND_VARIANT);
        registerHolderEntityDataSerializer(codecs, ChickenVariant.class, CHICKEN_VARIANT);
        registerHolderEntityDataSerializer(codecs, ChickenSoundVariant.class, CHICKEN_SOUND_VARIANT);
        registerHolderEntityDataSerializer(codecs, CowVariant.class, COW_VARIANT);
        registerHolderEntityDataSerializer(codecs, CowSoundVariant.class, COW_SOUND_VARIANT);
        registerHolderEntityDataSerializer(codecs, WolfVariant.class, WOLF_VARIANT);
        registerHolderEntityDataSerializer(codecs, WolfSoundVariant.class, WOLF_SOUND_VARIANT);
        registerHolderEntityDataSerializer(codecs, FrogVariant.class, FROG_VARIANT);
        registerHolderEntityDataSerializer(codecs, PigVariant.class, PIG_VARIANT);
        registerHolderEntityDataSerializer(codecs, PigSoundVariant.class, PIG_SOUND_VARIANT);
        registerHolderEntityDataSerializer(codecs, ZombieNautilusVariant.class, ZOMBIE_NAUTILUS_VARIANT);
        registerHolderEntityDataSerializer(codecs, PaintingVariant.class, PAINTING_VARIANT);
        registerEntityDataSerializer(codecs, Armadillo.ArmadilloState.class, ARMADILLO_STATE);
        registerEntityDataSerializer(codecs, Sniffer.State.class, SNIFFER_STATE);
        registerEntityDataSerializer(codecs, WeatheringCopper.WeatherState.class, WEATHERING_COPPER_STATE);
        registerEntityDataSerializer(codecs, CopperGolemState.class, COPPER_GOLEM_STATE);
        registerEntityDataSerializer(codecs, Vector3f.class, VECTOR3);
        registerEntityDataSerializer(codecs, Quaternionfc.class, QUATERNION);
        registerEntityDataSerializer(codecs, ResolvableProfile.class, RESOLVABLE_PROFILE);
        registerEntityDataSerializer(codecs, HumanoidArm.class, HUMANOID_ARM);
    }

}
