package it.jakegblp.lusk.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.profile.PlayerTextures;

public class PlayerUtils {
    /**
     * Checks whether a player is crawling; this takes into account the scale attribute since it modifies the
     * size of the player and makes the calculations done in this method incorrect.
     * @param player the player to check for
     * @return whether the player is crawling
     */
    public static boolean isCrawling(Player player) {
        return !player.isInWater() && player.getPose() == Pose.SWIMMING;
    }

    public static boolean isSlim(OfflinePlayer player) {
        PlayerProfile playerProfile = player.getPlayerProfile();
        if (!playerProfile.isComplete()) {
            playerProfile.complete();
        }
        return playerProfile.getTextures().getSkinModel().equals(PlayerTextures.SkinModel.SLIM);
    }
}
