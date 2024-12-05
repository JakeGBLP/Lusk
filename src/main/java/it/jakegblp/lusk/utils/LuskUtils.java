package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAPIException;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.Kleenean;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;

import static it.jakegblp.lusk.utils.Constants.*;
import static it.jakegblp.lusk.utils.DeprecationUtils.getScaleAttribute;
import static it.jakegblp.lusk.utils.NumberUtils.areDoublesRoughlyEqual;

public class LuskUtils {

    public static Semver parseVersion(String s) {
        return new Semver(s, Semver.SemverType.LOOSE);
    }

    /**
     * Uses MiniMessage and MessageFormat.
     * @param format Minimessage format
     * @param args MessageFormat arguments
     */
    public static void consoleLog(String format, Object... args) {
        send(Bukkit.getConsoleSender(), format, args);
    }

    /**
     * Uses MiniMessage and MessageFormat.
     * For console logging use {@link #consoleLog(String, Object...) the console log method}
     * @param sender A CommandSender
     * @param format Minimessage format
     * @param args MessageFormat arguments
     */
    public static void send(CommandSender sender, String format, Object... args) {
        sender.sendRichMessage(LUSK_PREFIX + MessageFormat.format(format, args));
    }

    /**
     * Checks whether a player is crawling; this takes into account the scale attribute since it modifies the
     * size of the player and makes the calculations done in this method incorrect.
     * @param player the player to check for
     * @return whether the player is crawling
     */
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

    /**
     * @return the indices of the non-null values in the given list.
     */
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

    /**
     * Registers a property expression that includes the following patterns:<br>
     * 1. <code>[the] %property% [property] of %fromType%</code><br>
     * 2. <code>%fromType%'[s] %property% [property]</code><br>
     *
     * @param expressionClass the class of the expression to register
     * @param type the class of the return type
     * @param property a string indicates what this expression will return based on the given object
     * @param fromType a string containing lowercase classinfos that indicate what this expression can be used against
     * @param <T> a generic of the returned object
     * @see LuskUtils#registerVerboseBooleanPropertyExpression(Class, Class, String, String, String)
     */
    public static <T> void registerVerbosePropertyExpression(
            Class<? extends Expression<T>> expressionClass,
            Class<T> type,
            String property,
            String fromType) {
        String[] patterns = {
                "[the] " + property + " [property] of %" + fromType + "%",
                "%" + fromType + "%'[s] " + property + " [property]"
        };
        Skript.registerExpression(expressionClass, type, ExpressionType.PROPERTY,patterns);
    }

    /**
     * Registers a property expression that gets or is set to a boolean; includes the following patterns:<br>
     * 1. <code>[the] %prefix% %property% [state|property] of %fromType%</code><br>
     * 2. <code>%fromType%'[s] %prefix% %property% [state|property]</code><br>
     * 3. <code>whether or not [the] %prefix% %fromType% %property%</code><br>
     * 4. <code>whether [the] %prefix% %fromType% %property% [or not]</code>
     *
     * @param expressionClass the class of the expression to register
     * @param type the class of the return type
     * @param prefix a (usually optional) string that comes before the property to indicate what kind of object it can be used for
     * @param property a string indicates what this expression will return based on the given object
     * @param fromType a string containing lowercase classinfos that indicate what this expression can be used against
     * @param <T> a generic of the returned object
     * @see LuskUtils#registerVerbosePropertyExpression(Class, Class, String, String)
     */
    public static <T> void registerVerboseBooleanPropertyExpression(
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
    public static void registerPrefixedPropertyCondition(
            Class<? extends Condition> condition,
            String prefix,
            String property,
            String type) {
        registerPrefixedPropertyCondition(condition, PropertyCondition.PropertyType.BE, prefix, property, type);
    }
    public static void registerPrefixedPropertyCondition(
            Class<? extends Condition> condition,
            PropertyCondition.PropertyType propertyType,
            String prefix,
            String property,
            String type) {
        if (type.contains("%"))
            throw new SkriptAPIException("The type argument must not contain any '%'s");
        switch (propertyType) {
            case BE:
                Skript.registerCondition(condition,
                        prefix + " %" + type + "% (is|are) " + property,
                        prefix + " %" + type + "% (isn't|is not|aren't|are not) " + property);
                break;
            case CAN:
                Skript.registerCondition(condition,
                        prefix + " %" + type + "% can " + property,
                        prefix + " %" + type + "% (can't|cannot|can not) " + property);
                break;
            case HAVE:
                Skript.registerCondition(condition,
                        prefix + " %" + type + "% (has|have) " + property,
                        prefix + " %" + type + "% (doesn't|does not|do not|don't) have " + property);
                break;
            case WILL:
                Skript.registerCondition(condition,
                        prefix + " %" + type + "% will " + property,
                        prefix + " %" + type + "% (will (not|neither)|won't) " + property);
                break;
            default:
                assert false;
        }

    }
}
