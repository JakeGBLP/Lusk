package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.util.VectorMath;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {
    public final static boolean SKRIPT_2_9 = Constants.skriptVersion.getMinor() >= 9;
    public final static Pattern NUMBER_WITH_DECIMAL = Pattern.compile("(\\d+.\\d+)");

    public static Semver Version(String s) {
        return new Semver(s, Semver.SemverType.LOOSE);
    }

    public static boolean isCrawling(Player player) {
        if (!player.isSwimming() && !player.isGliding()) {
            return Math.round(player.getHeight() * 10) == 6;
        }
        return false;
    }
    // I am well aware this has no usages
    public static boolean isInteger(String string) {
        return string.matches("\\d+");
    }
    // I am well aware this has no usages
    public static boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }

    public static String toSkriptConfigNumberAccuracy(String s) {
        return NUMBER_WITH_DECIMAL.matcher(s).replaceAll(n -> Skript.toString(Double.parseDouble(n.group())));
    }

    public static ItemType[] toItemTypes(List<Material> materials) {
        ItemType[] itemTypes = new ItemType[materials.size() - 1];
        for (int index = 0; index < itemTypes.length; index++) {
            itemTypes[index] = new ItemType(materials.get(index));
        }
        return itemTypes;
    }

    public static boolean canCriticalDamage(Entity e) {
        return (e instanceof Player p && p.getFallDistance() < 0 && !e.isOnGround() && !p.hasPotionEffect(PotionEffectType.BLINDNESS) && p.hasPotionEffect(PotionEffectType.SLOW_FALLING) && !p.isClimbing() && p.getAttackCooldown() > 0.9 && !p.isSprinting() && !p.isInWater() && p.getVehicle() == null);
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

    // I am well aware this has no usages
    public static boolean isEven(int integer) {
        return (integer % 2 == 0);
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
