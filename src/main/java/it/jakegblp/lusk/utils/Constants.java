package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import com.google.common.collect.ImmutableMap;
import com.vdurmont.semver4j.Semver;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.api.SkriptAdapter;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.njol.skript.Skript.classExists;
import static ch.njol.skript.Skript.methodExists;
import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.LuskUtils.parseVersion;
import static it.jakegblp.lusk.utils.LuskUtils.parseVersionTruncated;
import static it.jakegblp.lusk.utils.RegistryUtils.generateRegistries;
import static java.util.Map.entry;

public class Constants {

    public static final double EPSILON = 1e-7;

    public static final String[] LUSK_COLORS = new String[]{"&7", "&9"};

    public static final Pattern
            REGEX_TRUNCATED_VERSION = Pattern.compile("^\\d+\\.\\d+(\\.\\d+)?"),
            REGEX_VERSION = Pattern.compile("^\\d+\\.\\d+(\\.\\d+)?[-+a-zA-Z0-9.]*$"),
            REGEX_NUMBER_WITH_DECIMAL = Pattern.compile("(\\d+.\\d+)");

    public static final Semver
            VERSION_SERVER = parseVersion(String.valueOf(Skript.getMinecraftVersion())),
            VERSION_SERVER_OLDEST_SUPPORTED = parseVersion("1.16"),
            VERSION_SERVER_NEWEST_SUPPORTED = parseVersion("1.21.4"),
            VERSION_SKRIPT = parseVersionTruncated(Skript.getVersion().toString()),
            VERSION_SKRIPT_OLDEST_SUPPORTED = parseVersion("2.6"),
            VERSION_SKRIPT_NEWEST_SUPPORTED = parseVersion("2.10");

    public static final SkriptAdapter ADAPTER_SKRIPT = VersionResolver.getSkriptAdapter(VERSION_SKRIPT);
    //public static final MinecraftAdapter ADAPTER_MINECRAFT = VersionResolver.getMinecraftAdapter(VERSION_SERVER);

    public static final List<InventoryAction>
            DROP_ACTION_DATA = List.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ONE_CURSOR),
            PICKUP_ACTION_DATA = List.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME),
            PLACE_ACTION_DATA = List.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME);

    public static final boolean
            HAS_BELL_RESONATE_EVENT = classExists("org.bukkit.event.block.BellResonateEvent"),
            SPIGOT_HAS_BELL_RING_EVENT = classExists("org.bukkit.event.block.BellRingEvent"),
            HAS_VOXEL_SHAPE = classExists("org.bukkit.util.VoxelShape"),
            HAS_SPAWN_CATEGORY = classExists("org.bukkit.entity.SpawnCategory"),
            HAS_ENTITY_SNAPSHOT = classExists("org.bukkit.entity.EntitySnapshot"),
            HAS_ENTITY_SNAPSHOT_GET_AS_STRING = HAS_ENTITY_SNAPSHOT && methodExists(EntitySnapshot.class, "getAsString"),
            HAS_STRUCTURE_PIECE = classExists("org.bukkit.generator.structure.StructurePiece"),
            HAS_GENERATED_STRUCTURE = classExists("org.bukkit.generator.structure.GeneratedStructure"),
            SPIGOT_HAS_ITEM_RARITY = classExists("org.bukkit.inventory.ItemRarity"),
            HAS_WARDEN = classExists("org.bukkit.entity.Warden"),
            HAS_SALMON_VARIANT = classExists("org.bukkit.entity.Salmon$Variant"),
            HAS_WOLF_VARIANT = classExists("org.bukkit.entity.Wolf$Variant"),
            HAS_COW_VARIANT = classExists("org.bukkit.entity.Cow$Variant"),
            HAS_PIG_VARIANT = classExists("org.bukkit.entity.Pig$Variant"),
            HAS_CHICKEN_VARIANT = classExists("org.bukkit.entity.Chicken$Variant"),
            HAS_FROG_VARIANT = classExists("org.bukkit.entity.Frog$Variant"),
            HAS_AXOLOTL_VARIANT = classExists("org.bukkit.entity.Axolotl$Variant"),
            HAS_REMOVE_ENCHANTMENTS_METHOD = methodExists(ItemStack.class, "removeEnchantments"),
            HAS_BLOCK_BREAK_EVENT_DROPS_ITEMS = methodExists(BlockBreakEvent.class, "isDropsItems"),
            /**
             * Whether the current skript version is greater than or equal to 2.7
             */
            SKRIPT_2_7 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.7")),
            /**
             * Whether the current skript version is greater than or equal to 2.8
             */
            SKRIPT_2_8 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.8")),
            /**
             * Whether the current skript version is greater than or equal to 2.9
             */
            SKRIPT_2_9 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.9")),
            /**
             * Whether the current skript version is greater than or equal to 2.10
             */
            SKRIPT_2_10 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.10")),
            /**
             * Whether the current server version is greater than or equal to 1.18
             */
            MINECRAFT_1_18 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.18")),
            /**
             * Whether the current server version is greater than or equal to 1.18.2
             */
            MINECRAFT_1_18_2 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.18.2")),
            /**
             * Whether the current server version is greater than or equal to 1.19.2
             */
            MINECRAFT_1_19_2 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.19.2")),
            ///**
            // * Whether the current server version is greater than or equal to 1.19.3
            // */
            //MINECRAFT_1_19_3 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.19.3")),
            /**
             * Whether the current server version is greater than or equal to 1.20
             */
            MINECRAFT_1_20 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20")),
            /**
             * Whether the current server version is greater than or equal to 1.20.1
             */
            MINECRAFT_1_20_1 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20.1")),
            /**
             * Whether the current server version is greater than or equal to 1.20.2
             */
            MINECRAFT_1_20_2 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20.2")),
            /**
             * Whether the current server version is greater than or equal to 1.20.4
             */
            MINECRAFT_1_20_4 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20.4")),
            /**
             * Whether the current server version is greater than or equal to 1.20.5
             */
            MINECRAFT_1_20_5 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20.5")),
            /**
             * Whether the current server version is greater than or equal to 1.20.6
             */
            MINECRAFT_1_20_6 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.20.6")),
            /**
             * Whether the current server version is greater than or equal to 1.21
             */
            MINECRAFT_1_21 = VERSION_SERVER.isGreaterThanOrEqualTo(parseVersion("1.21")),
            SKRIPT_HAS_TIMESPAN_TIMEPERIOD = classExists("ch.njol.skript.util.Timespan$TimePeriod"),
            HAS_START_RIPTIDE_ATTACK = methodExists(HumanEntity.class, "startRiptideAttack", int.class, float.class, ItemStack.class),
            HAS_HOPPER_INVENTORY_SEARCH_EVENT = classExists("org.bukkit.event.inventory.HopperInventorySearchEvent"),

            PAPER_HAS_ARMOR_STAND_META = classExists("com.destroystokyo.paper.inventory.meta.ArmorStandMeta"),
            PAPER_1_18 = MINECRAFT_1_18 && isPaper(),
            PAPER_HAS_1_18_2_EXTENDED_ENTITY_API = MINECRAFT_1_18_2 && isPaper(),
            /**
             * Whether, at runtime, the server includes APIs for
             * {@link Fox}, {@link Cat}, {@link Dolphin}, {@link Enderman}, {@link EnderDragon}.
             */
            PAPER_HAS_1_19_2_EXTENDED_ENTITY_API = MINECRAFT_1_19_2 && isPaper(),
            // https://github.com/PaperMC/Paper/commit/ec772bb8b8a185ffaea7db643f612211d43c9528
            //PAPER_HAS_1_19_3_EXTENDED_ENTITY_API = MINECRAFT_1_19_3 && isPaper(),
            PAPER_1_20_1 = MINECRAFT_1_20_1 && isPaper(),
            PAPER_1_20_4 = MINECRAFT_1_20_4 && isPaper(),
            PAPER_1_20_6 = MINECRAFT_1_20_6 && isPaper(),
            PAPER_1_21 = MINECRAFT_1_21 && isPaper(),

            PAPER_HAS_ENDER_SIGNAL_SET_TARGET_LOCATION = methodExists(EnderSignal.class, "setTargetLocation", Location.class, boolean.class),
            PAPER_HAS_WORLD_BORDER_EVENT = classExists("io.papermc.paper.event.world.border.WorldBorderEvent"),
            PAPER_HAS_PLAYER_ARM_SWING_EVENT = classExists("io.papermc.paper.event.player.PlayerArmSwingEvent"),
            PAPER_HAS_PLAYER_ARM_SWING_EVENT_HAND = PAPER_HAS_PLAYER_ARM_SWING_EVENT && methodExists(PlayerArmSwingEvent.class, "getHand"),
            PAPER_HAS_ENTITY_LOAD_CROSSBOW_EVENT = classExists("io.papermc.paper.event.entity.EntityLoadCrossbowEvent"),
            PAPER_HAS_ENTITY_LOAD_CROSSBOW_EVENT_HAND = PAPER_HAS_ENTITY_LOAD_CROSSBOW_EVENT && methodExists(EntityLoadCrossbowEvent.class, "getHand"),
            PAPER_HAS_PLAYER_SHEAR_BLOCK_EVENT = classExists("io.papermc.paper.event.block.PlayerShearBlockEvent"),
            PAPER_HAS_PLAYER_SHEAR_BLOCK_EVENT_HAND = PAPER_HAS_PLAYER_SHEAR_BLOCK_EVENT && methodExists(PlayerShearBlockEvent.class, "getHand"),
            PAPER_HAS_PLAYER_ELYTRA_BOOST_EVENT = classExists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"),
            PAPER_HAS_PLAYER_ELYTRA_BOOST_EVENT_HAND = PAPER_HAS_PLAYER_ELYTRA_BOOST_EVENT && methodExists(PlayerElytraBoostEvent.class, "getHand"),
            PAPER_HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT = classExists("com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent"),
            PAPER_HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT_HAND = PAPER_HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT && methodExists(PlayerUseUnknownEntityEvent.class, "getHand"),
            PAPER_HAS_PLAYER_JUMP_EVENT = classExists("com.destroystokyo.paper.event.player.PlayerJumpEvent"),
            PAPER_HAS_ENTITY_JUMP_EVENT = classExists("com.destroystokyo.paper.event.entity.EntityJumpEvent"),
            PAPER_HAS_PAPER_REGISTRY_ACCESS = classExists("io.papermc.paper.registry.RegistryAccess") && methodExists(RegistryAccess.class, "registryAccess"),
            PAPER_HAS_PAPER_REGISTRY_KEY = PAPER_HAS_PAPER_REGISTRY_ACCESS && classExists("io.papermc.paper.registry.RegistryKey") && methodExists(RegistryAccess.class, "getRegistry", RegistryKey.class);

    public static final String
            ANVIL_GUI_PREFIX = "[lusk] anvil[(-| )gui]",
            ARMOR_STAND_PREFIX = "[armor[ |-]stand]",
            ARMOR_STAND_TYPES = "livingentity" + (PAPER_HAS_ARMOR_STAND_META ? "/itemtypes" : ""),
            LUSK_PREFIX = MessageFormat.format("{0}[{1}Lusk{0}] ", LUSK_COLORS[0], LUSK_COLORS[1]);


    public static final ExpressionType EVENT_OR_SIMPLE = SKRIPT_2_8 ? ExpressionType.EVENT : ExpressionType.SIMPLE;

    public static final ImmutableMap<Class<?>, Registry<?>> REGISTRIES = generateRegistries();

    // todo: 1. Make file-reading system to allow users to update this. 2. idk i never ended up typing this one out
    public static final Map<Integer, Semver> versions = Map.ofEntries(
        entry(4, parseVersion("1.7.5")),
        entry(5, parseVersion("1.7.10")),
        entry(47, parseVersion("1.8.9")),
        entry(107, parseVersion("1.9")),
        entry(108, parseVersion("1.9.1")),
        entry(109, parseVersion("1.9.2")),
        entry(110, parseVersion("1.9.4")),
        entry(210, parseVersion("1.10.2")),
        entry(315, parseVersion("1.11")),
        entry(316, parseVersion("1.11.2")),
        entry(335, parseVersion("1.12.0")),
        entry(338, parseVersion("1.12.1")),
        entry(340, parseVersion("1.12.2")),
        entry(393, parseVersion("1.13")),
        entry(401, parseVersion("1.13.1")),
        entry(404, parseVersion("1.13.2")),
        entry(477, parseVersion("1.14")),
        entry(480, parseVersion("1.14.1")),
        entry(485, parseVersion("1.14.2")),
        entry(490, parseVersion("1.14.3")),
        entry(498, parseVersion("1.14.4")),
        entry(573, parseVersion("1.15")),
        entry(575, parseVersion("1.15.1")),
        entry(577, parseVersion("1.15.2")),
        entry(735, parseVersion("1.16")),
        entry(736, parseVersion("1.16.1")),
        entry(751, parseVersion("1.16.2")),
        entry(753, parseVersion("1.16.3")),
        entry(754, parseVersion("1.16.5")),
        entry(755, parseVersion("1.17")),
        entry(756, parseVersion("1.17.1")),
        entry(757, parseVersion("1.18.1")),
        entry(758, parseVersion("1.18.2")),
        entry(759, parseVersion("1.19")),
        entry(760, parseVersion("1.19.2")),
        entry(761, parseVersion("1.19.3")),
        entry(762, parseVersion("1.19.4")),
        entry(763, parseVersion("1.20.1")),
        entry(764, parseVersion("1.20.2")),
        entry(765, parseVersion("1.20.4")),
        entry(766, parseVersion("1.20.6")),
        entry(767, parseVersion("1.21.1")),
        entry(768, parseVersion("1.21.3")),
        entry(769, parseVersion("1.21.4")),
        entry(770, parseVersion("1.21.5")),
        entry(771, parseVersion("1.21.6")),
        entry(772, parseVersion("1.21.7"))
    );
}
