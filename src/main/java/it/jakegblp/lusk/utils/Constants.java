package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import com.google.common.collect.ImmutableMap;
import com.vdurmont.semver4j.Semver;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Fox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static ch.njol.skript.Skript.*;
import static ch.njol.skript.Skript.methodExists;
import static it.jakegblp.lusk.utils.LuskUtils.parseVersion;
import static it.jakegblp.lusk.utils.RegistryUtils.generateRegistries;

public class Constants {

    public static final double EPSILON = 1e-7;

    public static final String[] LUSK_COLORS = new String[]{"&7", "&9"};

    public static final String
            ANVIL_GUI_PREFIX = "[lusk] anvil[(-| )gui]",
            ARMORS_STAND_PREFIX = "[armor[ |-]stand]",
            LUSK_PREFIX = MessageFormat.format("{0}[{1}Lusk{0}] ", LUSK_COLORS[0], LUSK_COLORS[1]);

    public static final Semver
            VERSION_SERVER = parseVersion(String.valueOf(Skript.getMinecraftVersion())),
            VERSION_SKRIPT = parseVersion(Skript.getVersion().toString()),
            OLDEST_SUPPORTED = parseVersion("1.16"),
            NEWEST_SUPPORTED = parseVersion("1.21.4");

    public static final List<InventoryAction>
            DROP_ACTION_DATA = List.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ONE_CURSOR),
            PICKUP_ACTION_DATA = List.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME),
            PLACE_ACTION_DATA = List.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME);

    public static final boolean
            //todo: see how things play out with old, papermc item rarity
            HAS_BELL_RESONATE_EVENT = classExists("org.bukkit.event.block.BellResonateEvent"),
            SPIGOT_HAS_BELL_RING_EVENT = classExists("org.bukkit.event.block.BellRingEvent"),
            HAS_VOXEL_SHAPE = classExists("org.bukkit.util.VoxelShape"),
            HAS_SPAWN_CATEGORY = classExists("org.bukkit.entity.SpawnCategory"),
            HAS_STRUCTURE_PIECE = classExists("org.bukkit.generator.structure.StructurePiece"),
            HAS_GENERATED_STRUCTURE = classExists(" org.bukkit.generator.structure.GeneratedStructure"),
            SPIGOT_HAS_ITEM_RARITY = classExists("org.bukkit.inventory.ItemRarity"),
            HAS_WARDEN = classExists("org.bukkit.entity.Warden"),
            HAS_SALMON_VARIANT = classExists("org.bukkit.entity.Salmon$Variant"),
            HAS_WOLF_VARIANT = classExists("org.bukkit.entity.Wolf$Variant"),
            HAS_FROG_VARIANT = classExists("org.bukkit.entity.Frog$Variant"),
            HAS_AXOLOTL_VARIANT = classExists("org.bukkit.entity.Axolotl$Variant"),
            HAS_REMOVE_ENCHANTMENTS_METHOD = methodExists(ItemStack.class, "removeEnchantments"),
            //HAS_GENERIC_SCALE_ATTRIBUTE = Skript.fieldExists(Attribute.class, "GENERIC_SCALE"),
            HAS_SCALE_ATTRIBUTE = fieldExists(Attribute.class, "SCALE"),
            HAS_BLOCK_BREAK_EVENT_DROPS_ITEMS = methodExists(BlockBreakEvent.class, "isDropsItems"),
            /**
             * Whether the current skript version is greater than or equal to 2.9
             */
            SKRIPT_2_9 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.9")),
            SKRIPT_2_10 = VERSION_SKRIPT.isGreaterThanOrEqualTo(parseVersion("2.10")), //I forgot why I added this
            SKRIPT_HAS_TIMESPAN_TIMEPERIOD = classExists("ch.njol.skript.util.Timespan$TimePeriod"),
            HAS_START_RIPTIDE_ATTACK = methodExists(HumanEntity.class, "startRiptideAttack", int.class, float.class, ItemStack.class),
            HAS_HOPPER_INVENTORY_SEARCH_EVENT = classExists("org.bukkit.event.inventory.HopperInventorySearchEvent"),

            PAPER_HAS_ARMOR_STAND_META = classExists("com.destroystokyo.paper.inventory.meta.ArmorStandMeta"),
            PAPER_HAS_FOX_API = methodExists(Fox.class,"isInterested"),

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

    public static final Pattern NUMBER_WITH_DECIMAL = Pattern.compile("(\\d+.\\d+)");

    public static final ImmutableMap<Class<?>, Registry<?>> REGISTRIES = generateRegistries();

    //todo: 1. Make file-reading system to allow users to update this. 2. idk i never ended up typing this one out
    public static final HashMap<Integer, Semver> versions = new HashMap<>() {{
        put(4, parseVersion("1.7.5"));
        put(5, parseVersion("1.7.10"));
        put(47, parseVersion("1.8.9"));
        put(107, parseVersion("1.9"));
        put(108, parseVersion("1.9.1"));
        put(109, parseVersion("1.9.2"));
        put(110, parseVersion("1.9.4"));
        put(210, parseVersion("1.10.2"));
        put(315, parseVersion("1.11.0"));
        put(316, parseVersion("1.11.2"));
        put(335, parseVersion("1.12.0"));
        put(338, parseVersion("1.12.1"));
        put(340, parseVersion("1.12.2"));
        put(393, parseVersion("1.13"));
        put(401, parseVersion("1.13.1"));
        put(404, parseVersion("1.13.2"));
        put(477, parseVersion("1.14.0"));
        put(480, parseVersion("1.14.1"));
        put(485, parseVersion("1.14.2"));
        put(490, parseVersion("1.14.3"));
        put(498, parseVersion("1.14.4"));
        put(573, parseVersion("1.15"));
        put(575, parseVersion("1.15.1"));
        put(577, parseVersion("1.15.2"));
        put(735, parseVersion("1.16.0"));
        put(736, parseVersion("1.16.1"));
        put(751, parseVersion("1.16.2"));
        put(753, parseVersion("1.16.3"));
        put(754, parseVersion("1.16.5"));
        put(755, parseVersion("1.17"));
        put(756, parseVersion("1.17.1"));
        put(757, parseVersion("1.18.1"));
        put(758, parseVersion("1.18.2"));
        put(759, parseVersion("1.19"));
        put(760, parseVersion("1.19.2"));
        put(761, parseVersion("1.19.3"));
        put(762, parseVersion("1.19.4"));
        put(763, parseVersion("1.20.1"));
        put(764, parseVersion("1.20.2"));
        put(765, parseVersion("1.20.4"));
        put(766, parseVersion("1.20.6"));
        put(767, parseVersion("1.21.1"));
        put(768, parseVersion("1.21.3"));
    }};
}
