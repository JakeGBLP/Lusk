package it.jakegblp.lusk.nms.impl.from_1_21_5_to_1_21_10;

import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.PigVariant;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;

public class From_1_21_5_To_1_21_10 {
    public static void registerCodecs(Mappings mappings, BufferCodecs codecs) {
        var chickenVariantMapper = mappings.registerRegistryUnsafe(ChickenVariant.class, Chicken.Variant.class, Registries.CHICKEN_VARIANT);
        codecs.registerUnsafe(chickenVariantMapper, ChickenVariant.STREAM_CODEC);

        var cowVariantMapper = mappings.registerRegistryUnsafe(CowVariant.class, Cow.Variant.class, Registries.COW_VARIANT);
        codecs.registerUnsafe(cowVariantMapper, CowVariant.STREAM_CODEC);

        var pigVariantMapper = mappings.registerRegistryUnsafe(PigVariant.class, Pig.Variant.class, Registries.PIG_VARIANT);
        codecs.registerUnsafe(pigVariantMapper, PigVariant.STREAM_CODEC);
    }
}