package it.jakegblp.lusk.nms.impl.from_1_21_11;

import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mapper;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.npc.villager.VillagerData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.ZombieNautilus;

public class From_1_21_11 {
    public static void registerCodecs(Mappings mappings, BufferCodecs codecs) {
        var registryAccess = (((CraftServer) Bukkit.getServer()).getServer()).registryAccess();

        var villagerDataMapper = mappings.register(
                VillagerData.class, it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData.class,
                villagerData -> {
                    Mapper<Identifier, NamespacedKey> mapper = (Mapper<Identifier, NamespacedKey>) mappings.getMapperFromForwardClass(NamespacedKey.class);
                    //System.out.println("type key = " + villagerData.type().unwrapKey().orElse(null));
                    //System.out.println("profession key = " + villagerData.profession().unwrapKey().orElse(null));
                    //System.out.println("nms before api: " + new GsonBuilder().setPrettyPrinting().create().toJson(villagerData));
                    return new it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData(
                            Registry.VILLAGER_TYPE.getOrThrow(mapper.to(villagerData.type().unwrapKey().orElseThrow().identifier())),
                            Registry.VILLAGER_PROFESSION.getOrThrow(mapper.to(villagerData.profession().unwrapKey().orElseThrow().identifier())),
                            villagerData.level());
                    //System.out.println("nms -> api: " + new GsonBuilder().setPrettyPrinting().create().toJson(a));
                    //System.out.println("back to nms villager type: " + registryAccess.lookupOrThrow(Registries.VILLAGER_TYPE).getOrThrow(ResourceKey.create(Registries.VILLAGER_TYPE, codec.decode(a.type().getKey()))));
                    //System.out.println("back to nms villager profession: " + registryAccess.lookupOrThrow(Registries.VILLAGER_PROFESSION).getOrThrow(ResourceKey.create(Registries.VILLAGER_PROFESSION, codec.decode(a.profession().getKey()))));
                    //return a;
                },
                villagerData -> {
                    //System.out.println("before nms: " + new GsonBuilder().setPrettyPrinting().create().toJson(villagerData));
                    Mapper<Identifier, NamespacedKey> mapper = (Mapper<Identifier, NamespacedKey>) mappings.getMapperFromForwardClass(NamespacedKey.class);
                    return new VillagerData(
                            registryAccess.lookupOrThrow(Registries.VILLAGER_TYPE).getOrThrow(ResourceKey.create(Registries.VILLAGER_TYPE, mapper.from(villagerData.type().getKey()))),
                            registryAccess.lookupOrThrow(Registries.VILLAGER_PROFESSION).getOrThrow(ResourceKey.create(Registries.VILLAGER_PROFESSION, mapper.from(villagerData.profession().getKey()))),
                            villagerData.level());
                    //System.out.println("api -> nms: " + new GsonBuilder().setPrettyPrinting().create().toJson(a));
                    //return a;
                });
        codecs.register(
                villagerDataMapper,
                (buffer, value) -> VillagerData.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buffer.unwrap(), registryAccess), value),
                buffer -> VillagerData.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buffer.unwrap(), registryAccess))
        );

        var chickenVariantMapper = mappings.registerRegistryUnsafe(ChickenVariant.class, Chicken.Variant.class, Registries.CHICKEN_VARIANT);
        codecs.registerUnsafe(chickenVariantMapper, ChickenVariant.STREAM_CODEC);

        var cowVariantMapper = mappings.registerRegistryUnsafe(CowVariant.class, Cow.Variant.class, Registries.COW_VARIANT);
        codecs.registerUnsafe(cowVariantMapper, CowVariant.STREAM_CODEC);

        var pigVariantMapper = mappings.registerRegistryUnsafe(PigVariant.class, Pig.Variant.class, Registries.PIG_VARIANT);
        codecs.registerUnsafe(pigVariantMapper, PigVariant.STREAM_CODEC);

        var zombieNautilusVariantMapper = mappings.registerRegistryUnsafe(ZombieNautilusVariant.class, ZombieNautilus.Variant.class, Registries.ZOMBIE_NAUTILUS_VARIANT);
        codecs.registerUnsafe(zombieNautilusVariantMapper, ZombieNautilusVariant.STREAM_CODEC);
    }
}