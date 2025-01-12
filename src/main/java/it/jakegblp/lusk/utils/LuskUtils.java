package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.util.Kleenean;
import com.vdurmont.semver4j.Semver;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;

import static it.jakegblp.lusk.utils.Constants.*;

public class LuskUtils {

    public static Semver parseVersion(String s) {
        return new Semver(s, Semver.SemverType.LOOSE);
    }

    /**
     * Uses MessageFormat.
     * @param message the message to send, uses '&' formatting
     * @param args MessageFormat arguments
     */
    public static void consoleLog(String message, Object... args) {
        send(Bukkit.getConsoleSender(), message, args);
    }


    /**
     * Sends a warning in the console, the warning always starts with "&eWARNING: " and the leading color is yellow ('&e');
     * this should not be changed.<br>
     * Uses MessageFormat.
     * @param message the message to send, uses '&' formatting
     * @param args MessageFormat arguments
     */
    public static void warning(String message, Object... args) {
        send(Bukkit.getConsoleSender(), "&eWARNING: " + message, args);
    }

    /**
     * Uses MessageFormat.
     * For console logging use {@link #consoleLog(String, Object...) the console log method}
     * @param sender A CommandSender
     * @param message the message to send, uses '&' formatting
     * @param args MessageFormat arguments
     */
    public static void send(@NotNull CommandSender sender, String message, Object... args) {
        sendMessage(sender,LUSK_PREFIX + "&7" + MessageFormat.format(message, args));
    }
    /**
     * Sends a message with `&` color codes to a CommandSender.
     *
     * @param sender  The CommandSender to send the message to.
     * @param message the message to send, uses '&' formatting
     */
    @SuppressWarnings("deprecation")
    public static void sendMessage(@NotNull CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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

}
