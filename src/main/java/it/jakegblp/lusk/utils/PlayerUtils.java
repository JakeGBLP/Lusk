package it.jakegblp.lusk.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import static it.jakegblp.lusk.utils.DeprecationUtils.getScaleAttribute;
import static it.jakegblp.lusk.utils.NumberUtils.areDoublesRoughlyEqual;

public class PlayerUtils {
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

    public static boolean isSlim(OfflinePlayer player) {
        PlayerProfile playerProfile = player.getPlayerProfile();
        if (!playerProfile.isComplete()) {
            playerProfile.complete();
        }
        return playerProfile.getTextures().getSkinModel().equals(PlayerTextures.SkinModel.SLIM);
    }
}
