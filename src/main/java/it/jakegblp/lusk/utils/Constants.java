package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.util.SkriptColor;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

    public static final Semver serverVersion = new Semver(String.valueOf(Skript.getMinecraftVersion()));
    public static final HashMap<Integer, Semver> versions = new HashMap<>() {{
        put(4, new Semver("1.7.5"));
        put(5, new Semver("1.7.10"));
        put(47, new Semver("1.8.9"));
        put(107, new Semver("1.9.0"));
        put(108, new Semver("1.9.1"));
        put(109, new Semver("1.9.2"));
        put(110, new Semver("1.9.4"));
        put(210, new Semver("1.10.2"));
        put(315, new Semver("1.11.0"));
        put(316, new Semver("1.11.2"));
        put(335, new Semver("1.12.0"));
        put(338, new Semver("1.12.1"));
        put(340, new Semver("1.12.2"));
        put(393, new Semver("1.13.0"));
        put(401, new Semver("1.13.1"));
        put(404, new Semver("1.13.2"));
        put(477, new Semver("1.14.0"));
        put(480, new Semver("1.14.1"));
        put(485, new Semver("1.14.2"));
        put(490, new Semver("1.14.3"));
        put(498, new Semver("1.14.4"));
        put(573, new Semver("1.15.0"));
        put(575, new Semver("1.15.1"));
        put(577, new Semver("1.15.2"));
        put(735, new Semver("1.16.0"));
        put(736, new Semver("1.16.1"));
        put(751, new Semver("1.16.2"));
        put(753, new Semver("1.16.3"));
        put(754, new Semver("1.16.5"));
        put(755, new Semver("1.17.0"));
        put(756, new Semver("1.17.1"));
        put(757, new Semver("1.18.1"));
        put(758, new Semver("1.18.2"));
        put(759, new Semver("1.19.0"));
        put(760, new Semver("1.19.2"));
        put(761, new Semver("1.19.3"));
        put(762, new Semver("1.19.4"));
        put(763, new Semver("1.20.1"));
        put(764, new Semver("1.20.2"));
        put(765, new Semver("1.20.4"));
    }};
    public static final HashMap<String, SkriptColor> rarityColors = new HashMap<>() {{
        put("COMMON", SkriptColor.WHITE);
        put("UNCOMMON", SkriptColor.YELLOW);
        put("RARE", SkriptColor.LIGHT_CYAN);
        put("EPIC", SkriptColor.LIGHT_PURPLE);
    }};

    public static final HashMap<Material,Integer> compostablesWithChances = new HashMap<>() {{
        put(Material.BEETROOT_SEEDS, 30);
        put(Material.DRIED_KELP, 30);
        put(Material.GLOW_BERRIES, 30);
        put(Material.SHORT_GRASS, 30);
        put(Material.HANGING_ROOTS, 30);
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
            put(Material.MANGROVE_ROOTS, 30);
        }
        put(Material.KELP, 30);
        put(Material.MELON_SEEDS, 30);
        put(Material.PUMPKIN_SEEDS, 30);
        put(Material.MOSS_CARPET, 30);
        put(Material.SEAGRASS, 30);
        put(Material.SMALL_DRIPLEAF, 30);
        put(Material.SWEET_BERRIES, 30);
        put(Material.WHEAT_SEEDS, 30);
        for (Material material : saplings) {
            put(material, 30);
        }
        for (Material material : leaves) {
            put(material, 30);
        }
        put(Material.CACTUS, 50);
        put(Material.DRIED_KELP_BLOCK, 50);
        put(Material.FLOWERING_AZALEA_LEAVES, 50);
        put(Material.GLOW_LICHEN, 50);
        put(Material.MELON_SLICE, 50);
        put(Material.NETHER_SPROUTS, 50);
        put(Material.SUGAR_CANE, 50);
        put(Material.TALL_GRASS, 50);
        put(Material.TWISTING_VINES, 50);
        put(Material.VINE, 50);
        put(Material.WEEPING_VINES, 50);
        put(Material.APPLE, 65);
        put(Material.AZALEA, 65);
        put(Material.BEETROOT, 65);
        put(Material.BIG_DRIPLEAF, 65);
        put(Material.CARROT, 65);
        put(Material.COCOA_BEANS, 65);
        put(Material.FERN, 65);
        put(Material.LARGE_FERN, 65);
        flowers.forEach(m -> put(m,65));
        put(Material.CRIMSON_FUNGUS, 65);
        put(Material.WARPED_FUNGUS, 65);
        put(Material.LILY_PAD, 65);
        put(Material.MELON, 65);
        put(Material.MOSS_BLOCK, 65);
        put(Material.BROWN_MUSHROOM, 65);
        put(Material.RED_MUSHROOM, 65);
        put(Material.MUSHROOM_STEM, 65);
        put(Material.NETHER_WART, 65);
        put(Material.POTATO, 65);
        put(Material.PUMPKIN, 65);
        put(Material.CARVED_PUMPKIN, 65);
        put(Material.CRIMSON_ROOTS, 65);
        put(Material.WARPED_ROOTS, 65);
        put(Material.SEA_PICKLE, 65);
        put(Material.SHROOMLIGHT, 65);
        put(Material.SPORE_BLOSSOM, 65);
        put(Material.WHEAT, 65);
        put(Material.BAKED_POTATO, 85);
        put(Material.BREAD, 85);
        put(Material.COOKIE, 85);
        put(Material.FLOWERING_AZALEA, 85);
        put(Material.HAY_BLOCK, 85);
        put(Material.RED_MUSHROOM_BLOCK, 85);
        put(Material.BROWN_MUSHROOM_BLOCK, 85);
        put(Material.NETHER_WART_BLOCK, 85);
        put(Material.WARPED_WART_BLOCK, 85);
        put(Material.CAKE, 100);
        put(Material.PUMPKIN_PIE, 100);
    }};
    public static final ArrayList<Material> tillables = new ArrayList<>() {{
        add(Material.DIRT);
        add(Material.COARSE_DIRT);
        add(Material.DIRT_PATH);
        add(Material.GRASS_BLOCK);
        add(Material.ROOTED_DIRT);
    }},pathables = new ArrayList<>() {{
        add(Material.DIRT);
        add(Material.COARSE_DIRT);
        add(Material.GRASS_BLOCK);
        add(Material.ROOTED_DIRT);
        add(Material.MYCELIUM);
        add(Material.PODZOL);
    }},axes = new ArrayList<>() {{
        add(Material.WOODEN_AXE);
        add(Material.GOLDEN_AXE);
        add(Material.STONE_AXE);
        add(Material.IRON_AXE);
        add(Material.DIAMOND_AXE);
        add(Material.NETHERITE_AXE);
    }},hoes = new ArrayList<>() {{
        add(Material.WOODEN_HOE);
        add(Material.GOLDEN_HOE);
        add(Material.STONE_HOE);
        add(Material.IRON_HOE);
        add(Material.DIAMOND_HOE);
        add(Material.NETHERITE_HOE);
    }},shovels = new ArrayList<>() {{
        add(Material.WOODEN_SHOVEL);
        add(Material.GOLDEN_SHOVEL);
        add(Material.STONE_SHOVEL);
        add(Material.IRON_SHOVEL);
        add(Material.DIAMOND_SHOVEL);
        add(Material.NETHERITE_SHOVEL);
    }},strippables = new ArrayList<>() {{
        add(Material.OAK_LOG);
        add(Material.OAK_WOOD);
        add(Material.SPRUCE_LOG);
        add(Material.SPRUCE_WOOD);
        add(Material.BIRCH_LOG);
        add(Material.BIRCH_WOOD);
        add(Material.JUNGLE_LOG);
        add(Material.JUNGLE_WOOD);
        add(Material.DARK_OAK_LOG);
        add(Material.DARK_OAK_WOOD);
        add(Material.ACACIA_LOG);
        add(Material.ACACIA_WOOD);
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
            add(Material.MANGROVE_LOG);
            add(Material.MANGROVE_WOOD);
        }
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.16.0"))) {
            add(Material.CRIMSON_HYPHAE);
            add(Material.CRIMSON_STEM);
            add(Material.WARPED_HYPHAE);
            add(Material.WARPED_STEM);
        }
    }},waxables = new ArrayList<>() {{
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.17.0"))) {
            add(Material.OXIDIZED_COPPER);
            add(Material.OXIDIZED_CUT_COPPER);
            add(Material.OXIDIZED_CUT_COPPER_SLAB);
            add(Material.OXIDIZED_CUT_COPPER_STAIRS);
            add(Material.WAXED_OXIDIZED_COPPER);
            add(Material.WAXED_OXIDIZED_CUT_COPPER);
            add(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB);
            add(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
            add(Material.WEATHERED_COPPER);
            add(Material.WEATHERED_CUT_COPPER);
            add(Material.WEATHERED_CUT_COPPER_SLAB);
            add(Material.WEATHERED_CUT_COPPER_STAIRS);
            add(Material.WAXED_WEATHERED_COPPER);
            add(Material.WAXED_WEATHERED_CUT_COPPER);
            add(Material.WAXED_WEATHERED_CUT_COPPER_SLAB);
            add(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS);
            add(Material.EXPOSED_COPPER);
            add(Material.EXPOSED_CUT_COPPER);
            add(Material.EXPOSED_CUT_COPPER_SLAB);
            add(Material.EXPOSED_CUT_COPPER_STAIRS);
            add(Material.WAXED_EXPOSED_COPPER);
            add(Material.WAXED_EXPOSED_CUT_COPPER);
            add(Material.WAXED_EXPOSED_CUT_COPPER_SLAB);
            add(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS);

        }
    }},axeables = new ArrayList<>() {{
        addAll(waxables);
        addAll(strippables);
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.20.0"))) {
            add(Material.OAK_HANGING_SIGN);
            add(Material.SPRUCE_HANGING_SIGN);
            add(Material.BIRCH_HANGING_SIGN);
            add(Material.JUNGLE_HANGING_SIGN);
            add(Material.DARK_OAK_HANGING_SIGN);
            add(Material.ACACIA_HANGING_SIGN);
            add(Material.CRIMSON_HANGING_SIGN);
            add(Material.WARPED_HANGING_SIGN);
            add(Material.MANGROVE_HANGING_SIGN);
            add(Material.BAMBOO_HANGING_SIGN);
            add(Material.CHERRY_HANGING_SIGN);
        }
    }},saplings = new ArrayList<>() {{
        add(Material.OAK_SAPLING);
        add(Material.BIRCH_SAPLING);
        add(Material.SPRUCE_SAPLING);
        add(Material.JUNGLE_SAPLING);
        add(Material.DARK_OAK_SAPLING);
        add(Material.ACACIA_SAPLING);
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
            add(Material.MANGROVE_PROPAGULE);
        }
    }},leaves = new ArrayList<>() {{
        add(Material.OAK_LEAVES);
        add(Material.BIRCH_LEAVES);
        add(Material.SPRUCE_LEAVES);
        add(Material.JUNGLE_LEAVES);
        add(Material.DARK_OAK_LEAVES);
        add(Material.ACACIA_LEAVES);
        add(Material.AZALEA_LEAVES);
        if (serverVersion.isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
            add(Material.MANGROVE_LEAVES);
        }
    }},flowers = new ArrayList<>() {{
        add(Material.DANDELION);
        add(Material.POPPY);
        add(Material.BLUE_ORCHID);
        add(Material.ALLIUM);
        add(Material.AZURE_BLUET);
        add(Material.ORANGE_TULIP);
        add(Material.PINK_TULIP);
        add(Material.RED_TULIP);
        add(Material.WHITE_TULIP);
        add(Material.OXEYE_DAISY);
        add(Material.LILY_OF_THE_VALLEY);
        add(Material.SUNFLOWER);
        add(Material.LILAC);
        add(Material.ROSE_BUSH);
        add(Material.PEONY);
    }},compostables = new ArrayList<>(compostablesWithChances.keySet().stream().toList());

    public static final ArrayList<EntityData<? extends Entity>> shearables = new ArrayList<>() {{
        add(EntityUtils.toSkriptEntityData(EntityType.MUSHROOM_COW));
        add(EntityUtils.toSkriptEntityData(EntityType.SHEEP));
        add(EntityUtils.toSkriptEntityData(EntityType.SNOWMAN));
    }}, sittables = new ArrayList<>() {{
        add(EntityUtils.toSkriptEntityData(EntityType.CAT));
        add(EntityUtils.toSkriptEntityData(EntityType.WOLF));
        add(EntityUtils.toSkriptEntityData(EntityType.PARROT));
        add(EntityUtils.toSkriptEntityData(EntityType.PANDA));
        add(EntityUtils.toSkriptEntityData(EntityType.FOX));
    }};
    public static final Semver skriptVersion = new Semver(Skript.getVersion().toString());
    public static final HashMap<Integer, Rotation> itemFrameRotations = new HashMap<>() {{
        put(0, Rotation.NONE);
        put(45, Rotation.CLOCKWISE_45);
        put(90, Rotation.CLOCKWISE);
        put(135, Rotation.CLOCKWISE_135);
        put(180, Rotation.FLIPPED);
        put(225, Rotation.FLIPPED_45);
        put(270, Rotation.COUNTER_CLOCKWISE);
        put(315, Rotation.COUNTER_CLOCKWISE_45);
    }};
}
