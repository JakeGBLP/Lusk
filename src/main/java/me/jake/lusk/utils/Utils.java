package me.jake.lusk.utils;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.util.SkriptColor;
import me.jake.lusk.classes.Version;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Version serverVersion = Version.parse(Bukkit.getMinecraftVersion());

    public static HashMap<String, String> getLetters() {
        HashMap<String, String> letters = new HashMap<>();
        letters.put("a", "ᴀ");
        letters.put("b", "ʙ");
        letters.put("c", "ᴄ");
        letters.put("d", "ᴅ");
        letters.put("e", "ᴇ");
        letters.put("f", "ғ");
        letters.put("g", "ɢ");
        letters.put("h", "ʜ");
        letters.put("i", "ɪ");
        letters.put("j", "ᴊ");
        letters.put("k", "ᴋ");
        letters.put("l", "ʟ");
        letters.put("m", "ᴍ");
        letters.put("n", "ɴ");
        letters.put("o", "ᴏ");
        letters.put("p", "ᴘ");
        letters.put("q", "ǫ");
        letters.put("r", "ʀ");
        letters.put("s", "s");
        letters.put("t", "ᴛ");
        letters.put("u", "ᴜ");
        letters.put("v", "ᴠ");
        letters.put("w", "ᴡ");
        letters.put("x", "x");
        letters.put("y", "ʏ");
        letters.put("z", "ᴢ");
        return letters;
    }


    public static String toSmallFont(String string, boolean fully) {
        if (fully) {
            string =  string.toLowerCase();
        }
        for (Map.Entry<String, String> entry : getLetters().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            string = string.replaceAll(key, value);
        }
        return string;
    }
    public static HashMap<String, Version> versions = new HashMap<>() {{
        put("4", new Version(1,7,5));
        put("5",new Version(1,7,10));
        put("47",new Version(1,8,9));
        put("107",new Version(1,9));
        put("108",new Version(1,9,1));
        put("109",new Version(1,9,2));
        put("110",new Version(1,9,4));
        put("210",new Version(1,10,2));
        put("315",new Version(1,11));
        put("316",new Version(1,11,2));
        put("335",new Version(1,12));
        put("338",new Version(1,12,1));
        put("340",new Version(1,12,2));
        put("393",new Version(1,13));
        put("401",new Version(1,13,1));
        put("404",new Version(1,13,2));
        put("477",new Version(1,14));
        put("480",new Version(1,14,1));
        put("485",new Version(1,14,2));
        put("490",new Version(1,14,3));
        put("498",new Version(1,14,4));
        put("573",new Version(1,15));
        put("575",new Version(1,15,1));
        put("577",new Version(1,15,2));
        put("735",new Version(1,16));
        put("736",new Version(1,16,1));
        put("751",new Version(1,16,2));
        put("753",new Version(1,16,3));
        put("754",new Version(1,16,5));
        put("755",new Version(1,17));
        put("756",new Version(1,17,1));
        put("757",new Version(1,18,1));
        put("758",new Version(1,18,2));
        put("759",new Version(1,19));
        put("760",new Version(1,19,2));
        put("761",new Version(1,19,3));
    }};

    public static HashMap<String, SkriptColor> rarityColors = new HashMap<>() {{
        put("COMMON", SkriptColor.WHITE);
        put("UNCOMMON", SkriptColor.YELLOW);
        put("RARE", SkriptColor.LIGHT_CYAN);
        put("EPIC", SkriptColor.LIGHT_PURPLE);
    }};

    public static boolean isCrawling(Player player) {
        if (!player.isSwimming() && !player.isGliding()) {
            return Math.round(player.getHeight() * 10) == 6;
        }
        return false;
    }
    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }

    public static double roundToDecimal(double number, double index, int round) {
        double pow = Math.pow(10.0, index);
        double num = pow*number;
        Number result;
        switch (round) {
            case -1 -> result = Math.floor(num) / pow;
            case 1 -> result = Math.ceil(num) / pow;
            default -> result = Math.round(num) / pow;
        }
        return result.doubleValue();
    }

    public static boolean isStrippable(Material material) {
        String type = material.toString();
        return ((type.contains("_LOG") || type.contains("_WOOD") || type.contains("WARPED_STEM") || type.contains("CRIMSON_STEM") || type.contains("HYPHAE") || type.contains("EXPOSED") || type.contains("WEATHERED") || type.contains("OXIDIZED")) && (!type.contains("STRIPPED")));
    }
    public static boolean isAxe(Material material) {
        return (material.toString().contains("_AXE"));
    }
    public static boolean isHoe(Material material) {
        return (material.toString().contains("_HOE"));
    }
    public static boolean isShovel(Material material) {
        return (material.toString().contains("_SHOVEL"));
    }

    public static ArrayList<Material> getPathables() {
        return new ArrayList<Material>(){{
            add(Material.DIRT);
            add(Material.COARSE_DIRT);
            add(Material.GRASS_BLOCK);
            add(Material.ROOTED_DIRT);
            add(Material.MYCELIUM);
            add(Material.PODZOL);
        }};
    }

    public static ArrayList<Material> getTillables() {
        return new ArrayList<Material>(){{
            add(Material.DIRT);
            add(Material.COARSE_DIRT);
            add(Material.DIRT_PATH);
            add(Material.GRASS_BLOCK);
            add(Material.ROOTED_DIRT);
        }};
    }
    public static boolean isPathable(Material material) {
        return getPathables().contains(material);
    }

    public static boolean isTillable(Material material) {
        return getTillables().contains(material);
    }

    public static ItemType[] toItemTypes(List<Material> materials) {
        ItemType[] itemTypes = new ItemType[materials.size()-1];
        for (int index = 0; index < itemTypes.length; index++) {
            itemTypes[index] = new ItemType(materials.get(index));
        }
        return itemTypes;
    }
}
