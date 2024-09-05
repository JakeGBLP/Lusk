package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.util.VectorMath;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.regex.Pattern;

public class LuskUtils {
    public final static boolean HAS_GENERIC_SCALE_ATTRIBUTE = Skript.fieldExists(Attribute.class, "GENERIC_SCALE");

    public final static boolean SKRIPT_2_9 = Constants.skriptVersion.isGreaterThanOrEqualTo(Version("2.9"));
    public final static Pattern NUMBER_WITH_DECIMAL = Pattern.compile("(\\d+.\\d+)");

    public static Semver Version(String s) {
        return new Semver(s, Semver.SemverType.LOOSE);
    }

    public static boolean isCrawling(Player player) {
        double height = player.getHeight();
        if (HAS_GENERIC_SCALE_ATTRIBUTE) {
            AttributeInstance instance = player.getAttribute(Attribute.GENERIC_SCALE);
            if (instance != null) height /= instance.getValue();
        }
        if (!player.isSwimming() && !player.isGliding() && !player.isSleeping() && !player.isSneaking() && !player.isRiptiding()) {
            return areDoublesEqual(height, 0.6);
        }
        return false;
    }

    public static boolean areDoublesEqual(double a, double b) {
        return Math.abs(a - b) < Math.min(a, b) * 1e-6;
    }

    /**
     * Replaces numbers in a string and truncates the number of decimals to the one specified in Skript's own config.
     * (Number Accuracy)
     *
     * @param string The string to replace numbers in.
     * @return The provided string with all numbers replaced with themselves limited to Skript's number accuracy from its config file.
     */
    public static String toSkriptConfigNumberAccuracy(String string) {
        return NUMBER_WITH_DECIMAL.matcher(string).replaceAll(n -> Skript.toString(Double.parseDouble(n.group())));
    }

    public static ItemType[] toItemTypes(List<Material> materials) {
        ItemType[] itemTypes = new ItemType[materials.size() - 1];
        for (int index = 0; index < itemTypes.length; index++) {
            itemTypes[index] = new ItemType(materials.get(index));
        }
        return itemTypes;
    }

    /**
     * Checks if an entity is in the position to inflict critical damage to another entity
     * (doesn't take into account if there is an entity to inflict the critical damage to).
     *
     * @param entity The entity to check this against.
     * @return Whether the given entity can inflict critical damage.
     */
    public static boolean canCriticalDamage(Entity entity) {
        return (
                entity instanceof Player p
                        && p.getFallDistance() > 0
                        && !entity.isOnGround()
                        && !p.isClimbing()
                        && !p.isInWater()
                        && !p.hasPotionEffect(PotionEffectType.BLINDNESS)
                        && !p.hasPotionEffect(PotionEffectType.SLOW_FALLING)
                        && p.getVehicle() == null
                        && p.getAttackCooldown() > 0.9
                        && !p.isSprinting());
    }

    public static int getTotalNeededXP(int level) {
        if (level <= 15)
            return (level * level) + (6 * level);
        if (level <= 30)
            return (int) (2.5 * (level * level) - (40.5 * level) + 360);
        return (int) (4.5 * (level * level) - (162.5 * level) + 2220);
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
        armorStand.setLeftArmPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setRightArmRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRightArmPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setLeftLegRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setLeftLegPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setRightLegRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRightLegPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setBodyRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setBodyPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setHeadRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setHeadPose(LuskUtils.toEulerAngle(vector));
    }

    public static void setFullRotation(ArmorStand armorStand, Vector vector) {
        armorStand.setRotation(VectorMath.notchYaw(VectorMath.getYaw(vector)), VectorMath.notchPitch(VectorMath.getPitch(vector)));
    }
}
