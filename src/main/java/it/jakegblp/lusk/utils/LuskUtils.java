package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.Kleenean;
import ch.njol.util.VectorMath;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;

import static it.jakegblp.lusk.utils.Constants.*;
import static it.jakegblp.lusk.utils.DeprecationUtils.getScaleAttribute;

public class LuskUtils {

    public static Semver parseVersion(String s) {
        return new Semver(s, Semver.SemverType.LOOSE);
    }

    /**
     * Uses MiniMessage and MessageFormat.
     * @param format Minimessage format.
     * @param args MessageFormat arguments.
     */
    public static void consoleLog(String format, Object... args) {
        send(Bukkit.getConsoleSender(), format, args);
    }

    /**
     * Uses MiniMessage and MessageFormat.
     * For console logging use {@link #consoleLog(String, Object...) the console log method}
     * @param sender A CommandSender
     * @param format Minimessage format.
     * @param args MessageFormat arguments.
     */
    public static void send(CommandSender sender, String format, Object... args) {
        sender.sendRichMessage(LUSK_PREFIX + MessageFormat.format(format, args));
    }

    public static boolean isCrawling(Player player) {
        double height = player.getHeight();
        Attribute scaleAttribute = getScaleAttribute();
        if (scaleAttribute != null) {
            AttributeInstance instance = player.getAttribute(scaleAttribute);
            if (instance != null) height /= instance.getValue();
        }
        if (!player.isInsideVehicle() && !player.isSwimming() && !player.isGliding() && !player.isSleeping() && !player.isSneaking() && !player.isRiptiding()) {
            return areDoublesRoughlyEqual(height, 0.6);
        }
        return false;
    }

    public static boolean areDoublesRoughlyEqual(double a, double b) {
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

    public static long getTotalNeededXP(int level) {
        long xp;
        if (level <= 0) {
            xp = 0;
        } else if (level <= 15) {
            xp = level * level + 6 * level;
        } else if (level <= 30) {
            xp = (long) (2.5 * level * level - 40.5 * level + 360);
        } else {
            xp = (long) (4.5 * level * level - 162.5 * level + 2220);
        }
        return xp;
    }

    public static int[] getNotNullIndices(List<?> list) {
        return IntStream.range(0, list.size())
                .filter(i -> list.get(i) != null)
                .toArray();
    }

    /**
     * Useful for when a and b can't both be true, and a fallback value is required.
     * @return a ? TRUE : b ? FALSE : UNKNOWN
     */
    @NotNull
    public static Kleenean getKleenean(boolean a, boolean b) {
        if (a) return Kleenean.TRUE;
        else if (b) return Kleenean.FALSE;
        else return Kleenean.UNKNOWN;
    }

    public static <T> void registerVerbosePropertyExpression(
            Class<? extends Expression<T>> expressionClass,
            Class<T> type,
            @Nullable String prefix,
            String property,
            String fromType) {
        prefix = prefix != null ? prefix + " " : "";
        String[] patterns = {
                "[the] " + prefix + property + " [state|property] of %" + fromType + "%",
                "%" + fromType + "%'[s] " + prefix + property + " [state|property]",
                "whether or not [the] " + prefix + "%" + fromType + "% " + property,
                "whether [the] " + prefix + "%" + fromType + "% " + property + " [or not]"
        };
        Skript.registerExpression(expressionClass, type, ExpressionType.PROPERTY,patterns);
    }


    //todo: remake these
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

    //todo: remake these
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
