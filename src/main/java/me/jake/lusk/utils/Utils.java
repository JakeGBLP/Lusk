package me.jake.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.VectorMath;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Utils {

    // Fields
    private static Semver serverVersion;
    private static String smallCapsLetters;
    private static HashMap<Integer, Semver> versions;
    private static HashMap<String, SkriptColor> rarityColors;
    private static ArrayList<Material> tillables;
    private static ArrayList<Material> pathables;
    private static ArrayList<Material> axes;
    private static ArrayList<Material> hoes;
    private static ArrayList<Material> shovels;
    private static ArrayList<Material> strippables;
    private static ArrayList<Material> waxables;
    private static ArrayList<Material> axeables;
    private static ArrayList<Material> saplings;
    private static ArrayList<Material> leaves;
    private static ArrayList<Material> flowers;
    private static HashMap<Material, Integer> compostablesWithChances;
    private static ArrayList<Material> compostables;
    private static ArrayList<EntityData> shearables;
    private static ArrayList<EntityData> sittables;
    private static Semver skriptVersion;
    private static HashMap<Integer, Rotation> itemFrameRotations;

    // Main method
    public static void setEverything() {
        setServerVersion();
        setSmallCapsLetters();
        setVersions();
        setRarityColors();
        setTillables();
        setPathables();
        setStrippables();
        setWaxables();
        setAxeables();
        setHoes();
        setAxes();
        setShovels();
        setSaplings();
        setLeaves();
        setFlowers();
        setCompostablesWithChances();
        setCompostables();
        setShearables();
        setSittables();
        setSkriptVersion();
        setItemFrameRotations();
    }

    // Set
    public static void setItemFrameRotations() {
        itemFrameRotations = new HashMap<>() {{
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

    public static void setServerVersion() {
        serverVersion = new Semver(String.valueOf(Skript.getMinecraftVersion()));
    }

    public static void setSmallCapsLetters() {
        smallCapsLetters = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
    }

    public static void setVersions() {
        versions = new HashMap<>() {{
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
        }};
    }

    public static void setRarityColors() {
        rarityColors = new HashMap<>() {{
            put("COMMON", SkriptColor.WHITE);
            put("UNCOMMON", SkriptColor.YELLOW);
            put("RARE", SkriptColor.LIGHT_CYAN);
            put("EPIC", SkriptColor.LIGHT_PURPLE);
        }};
    }

    public static void setPathables() {
        pathables = new ArrayList<>() {{
            add(Material.DIRT);
            add(Material.COARSE_DIRT);
            add(Material.GRASS_BLOCK);
            add(Material.ROOTED_DIRT);
            add(Material.MYCELIUM);
            add(Material.PODZOL);
        }};
    }

    public static void setTillables() {
        tillables = new ArrayList<>() {{
            add(Material.DIRT);
            add(Material.COARSE_DIRT);
            add(Material.DIRT_PATH);
            add(Material.GRASS_BLOCK);
            add(Material.ROOTED_DIRT);
        }};
    }

    public static void setStrippables() {
        strippables = new ArrayList<>() {{
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
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
                add(Material.MANGROVE_LOG);
                add(Material.MANGROVE_WOOD);
            }
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.16.0"))) {
                add(Material.CRIMSON_HYPHAE);
                add(Material.CRIMSON_STEM);
                add(Material.WARPED_HYPHAE);
                add(Material.WARPED_STEM);
            }
        }};
    }

    public static void setWaxables() {
        waxables = new ArrayList<>() {{
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.17.0"))) {
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
        }};
    }

    public static void setAxeables() {
        axeables = new ArrayList<>() {{
            addAll(getWaxables());
            addAll(getStrippables());
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.20.0"))) {
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
        }};
    }

    public static void setSaplings() {
        saplings = new ArrayList<>() {{
            add(Material.OAK_SAPLING);
            add(Material.BIRCH_SAPLING);
            add(Material.SPRUCE_SAPLING);
            add(Material.JUNGLE_SAPLING);
            add(Material.DARK_OAK_SAPLING);
            add(Material.ACACIA_SAPLING);
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
                add(Material.MANGROVE_PROPAGULE);
            }
        }};
    }

    public static void setLeaves() {
        leaves = new ArrayList<>() {{
            add(Material.OAK_LEAVES);
            add(Material.BIRCH_LEAVES);
            add(Material.SPRUCE_LEAVES);
            add(Material.JUNGLE_LEAVES);
            add(Material.DARK_OAK_LEAVES);
            add(Material.ACACIA_LEAVES);
            add(Material.AZALEA_LEAVES);
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
                add(Material.MANGROVE_LEAVES);
            }
        }};
    }

    public static void setFlowers() {
        flowers = new ArrayList<>() {{
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
        }};
    }

    public static void setCompostablesWithChances() {
        compostablesWithChances = new HashMap<>() {{
            put(Material.BEETROOT_SEEDS, 30);
            put(Material.DRIED_KELP, 30);
            put(Material.GLOW_BERRIES, 30);
            put(Material.GRASS, 30);
            put(Material.HANGING_ROOTS, 30);
            if (getServerVersion().isGreaterThanOrEqualTo(new Semver("1.19.0"))) {
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
            for (Material material : getSaplings()) {
                put(material, 30);
            }
            for (Material material : getLeaves()) {
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
            for (Material material : getFlowers()) {
                put(material, 65);
            }
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
    }

    public static void setCompostables() {
        compostables = new ArrayList<>(getCompostablesWithChances().keySet().stream().toList());
    }

    public static void setAxes() {
        axes = new ArrayList<>() {{
            add(Material.WOODEN_AXE);
            add(Material.GOLDEN_AXE);
            add(Material.STONE_AXE);
            add(Material.IRON_AXE);
            add(Material.DIAMOND_AXE);
            add(Material.NETHERITE_AXE);
        }};
    }

    public static void setHoes() {
        hoes = new ArrayList<>() {{
            add(Material.WOODEN_HOE);
            add(Material.GOLDEN_HOE);
            add(Material.STONE_HOE);
            add(Material.IRON_HOE);
            add(Material.DIAMOND_HOE);
            add(Material.NETHERITE_HOE);
        }};
    }

    public static void setShovels() {
        shovels = new ArrayList<>() {{
            add(Material.WOODEN_SHOVEL);
            add(Material.GOLDEN_SHOVEL);
            add(Material.STONE_SHOVEL);
            add(Material.IRON_SHOVEL);
            add(Material.DIAMOND_SHOVEL);
            add(Material.NETHERITE_SHOVEL);
        }};
    }

    public static void setShearables() {
        shearables = new ArrayList<>() {{
            add(EntityUtils.toSkriptEntityData(EntityType.MUSHROOM_COW));
            add(EntityUtils.toSkriptEntityData(EntityType.SHEEP));
            add(EntityUtils.toSkriptEntityData(EntityType.SNOWMAN));
        }};
    }

    public static void setSittables() {
        sittables = new ArrayList<>() {{
            add(EntityUtils.toSkriptEntityData(EntityType.CAT));
            add(EntityUtils.toSkriptEntityData(EntityType.WOLF));
            add(EntityUtils.toSkriptEntityData(EntityType.PARROT));
            add(EntityUtils.toSkriptEntityData(EntityType.PANDA));
            add(EntityUtils.toSkriptEntityData(EntityType.FOX));
        }};
    }

    public static void setSkriptVersion() {
        skriptVersion = new Semver(Skript.getVersion().toString());
    }

    // Get
    public static HashMap<Integer, Rotation> getItemFrameRotations() {
        return itemFrameRotations;
    }

    public static Semver getServerVersion() {
        return serverVersion;
    }

    public static HashMap<Integer, Semver> getVersions() {
        return versions;
    }

    public static Semver getPlayerVersion(int protocolNumber) {
        return getVersions().get(protocolNumber);
    }

    public static String getSmallCapsLetters() {
        return smallCapsLetters;
    }

    public static HashMap<String, SkriptColor> getRarityColors() {
        return rarityColors;
    }

    public static ArrayList<Material> getPathables() {
        return pathables;
    }

    public static ArrayList<Material> getTillables() {
        return tillables;
    }

    public static ArrayList<Material> getStrippables() {
        return strippables;
    }

    public static ArrayList<Material> getWaxables() {
        return waxables;
    }

    public static ArrayList<Material> getAxeables() {
        return axeables;
    }

    public static ArrayList<Material> getAxes() {
        return axes;
    }

    public static ArrayList<Material> getHoes() {
        return hoes;
    }

    public static ArrayList<Material> getShovels() {
        return shovels;
    }

    public static ArrayList<Material> getSaplings() {
        return saplings;
    }

    public static ArrayList<Material> getLeaves() {
        return leaves;
    }

    public static ArrayList<Material> getFlowers() {
        return flowers;
    }

    public static HashMap<Material, Integer> getCompostablesWithChances() {
        return compostablesWithChances;
    }

    public static ArrayList<Material> getCompostables() {
        return compostables;
    }

    public static ArrayList<EntityData> getShearables() {
        return shearables;
    }

    public static ArrayList<EntityData> getSittables() {
        return sittables;
    }

    public static Semver getSkriptVersion() {
        return skriptVersion;
    }

    // Conditions
    public static boolean isCrawling(Player player) {
        if (!player.isSwimming() && !player.isGliding()) {
            return Math.round(player.getHeight() * 10) == 6;
        }
        return false;
    }

    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }

    public static boolean isInteger(String string) {
        return string.matches("\\d+");
    }

    public static boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }

    public static boolean isAxe(Material material) {
        return getAxes().contains(material);
    }

    public static boolean isHoe(Material material) {
        return getHoes().contains(material);
    }

    public static boolean isShovel(Material material) {
        return getShovels().contains(material);
    }

    public static boolean isPathable(Material material) {
        return getPathables().contains(material);
    }

    public static boolean isTillable(Material material) {
        return getTillables().contains(material);
    }

    public static boolean isAxeable(Material material) {
        return getAxeables().contains(material);
    }

    public static boolean isStrippable(Material material) {
        return getStrippables().contains(material);
    }

    public static boolean isWaxable(Material material) {
        return getWaxables().contains(material);
    }

    public static boolean isCompostable(Material material) {
        return getCompostables().contains(material);
    }

    public static boolean isShearable(EntityData entityData) {
        return getShearables().contains(entityData);
    }

    public static boolean isSittable(EntityData entityData) {
        return getSittables().contains(entityData);
    }
    // Other

    public static ItemType[] toItemTypes(List<Material> materials) {
        ItemType[] itemTypes = new ItemType[materials.size() - 1];
        for (int index = 0; index < itemTypes.length; index++) {
            itemTypes[index] = new ItemType(materials.get(index));
        }
        return itemTypes;
    }

    public static Object getBlockDataTag(BlockData blockData, String tag) {
        String fullData = blockData.getAsString();
        if (fullData.contains("[")) {
            String data = fullData.replaceAll("(minecraft:[a-z0-9_]+\\[|])", "");
            String splitDelimiter = null;
            if (data.contains(";" + tag + "=")) {
                splitDelimiter = ";" + tag + "=";
            } else if (data.startsWith(tag + "=")) {
                splitDelimiter = tag + "=";
            }
            if (splitDelimiter != null) {
                String[] split = data.split(splitDelimiter);
                String value = split[1].split(";")[0];
                if (isInteger(value)) {
                    return Integer.valueOf(value);
                } else if (isBoolean(value)) {
                    return Boolean.getBoolean(value);
                }
            }
        }
        return null;
    }

    public static int getCompostChance(Material material) {
        return getCompostablesWithChances().get(material) != null ? getCompostablesWithChances().get(material) : 0;
    }

    public static double roundToDecimal(double number, double index, int round) {
        double pow = Math.pow(10.0, index);
        double num = pow * number;
        Number result;
        switch (round) {
            case -1 -> result = Math.floor(num) / pow;
            case 1 -> result = Math.ceil(num) / pow;
            default -> result = Math.round(num) / pow;
        }
        return result.doubleValue();
    }

    public static String toSmallFont(String string, boolean fully) {
        if (fully) {
            string = string.toLowerCase();
        }
        for (String letter : string.split("")) {
            if (letter.matches("[a-z]")) {
                string = string.replaceAll(letter, String.valueOf(getSmallCapsLetters().charAt(Character.getNumericValue(letter.charAt(0)) - 10)));
            }
        }
        return string;
    }

    public static boolean canCriticalDamage(Entity entity) {
        if (entity instanceof Player attacker) {
            if (attacker.getFallDistance() > 0) {
                if (!entity.isOnGround()) {
                    if (!attacker.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        if (!attacker.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                            if (!attacker.isClimbing()) {
                                if (attacker.getAttackCooldown() > 0.9) {
                                    if (!attacker.isSprinting()) {
                                        if (!attacker.isInWater()) {
                                            return attacker.getVehicle() == null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static Entity getCriticalEntity(Event event) {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            if (damageByEntityEvent.getDamager() instanceof AbstractArrow arrow) {
                return arrow;
            }
            return damageByEntityEvent.getDamager();
        } else if (event instanceof EntityDeathEvent deathEvent) {
            return getCriticalEntity(deathEvent.getEntity().getLastDamageCause());
        } else if (event instanceof VehicleDamageEvent vehicleDamageEvent) {
            return vehicleDamageEvent.getAttacker();
        } else if (event instanceof VehicleDestroyEvent vehicleDestroyEvent) {
            return vehicleDestroyEvent.getAttacker();
        }
        return null;
    }

    public static int getTotalNeededXP(int level) {
        if (level <= 15)
            return (level * level) + (6 * level);
        if (level <= 30)
            return (int) (2.5 * (level * level) - (40.5 * level) + 360);
        return (int) (4.5 * (level * level) - (162.5 * level) + 2220);
    }

    @Nullable
    public static Object getHashMapKeyFromValue(HashMap<?, ?> hashMap, Object value) {
        for (Map.Entry<?, ?> entry : hashMap.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isEven(int integer) {
        return (integer % 2 == 0);
    }

    public static boolean isCritical(Event event) {
        Entity entity = getCriticalEntity(event);
        return (canCriticalDamage(entity));
    }

    public static Vector toBukkitVector(EulerAngle angle) {
        double x = angle.getX();
        double y = angle.getY();
        double z = angle.getZ();
        return new Vector(x, y, z);
    }

    public static EulerAngle toEulerAngle(Vector vector) {
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        return new EulerAngle(x, y, z);
    }

    public static void setLeftArmRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setLeftArmPose(Utils.toEulerAngle(vector));
    }

    public static void setRightArmRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRightArmPose(Utils.toEulerAngle(vector));
    }

    public static void setLeftLegRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setLeftLegPose(Utils.toEulerAngle(vector));
    }

    public static void setRightLegRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRightLegPose(Utils.toEulerAngle(vector));
    }

    public static void setBodyRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setBodyPose(Utils.toEulerAngle(vector));
    }

    public static void setHeadRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setHeadPose(Utils.toEulerAngle(vector));
    }

    public static void setFullRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRotation(VectorMath.notchYaw(VectorMath.getYaw(vector)), VectorMath.notchPitch(VectorMath.getPitch(vector)));
    }
}
