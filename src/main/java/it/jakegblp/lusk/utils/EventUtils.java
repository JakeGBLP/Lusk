package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import org.bukkit.event.block.BlockBreakEvent;

public class EventUtils {
    public static boolean willItemsDrop(BlockBreakEvent event) {
        if (Skript.methodExists(BlockBreakEvent.class, "isDropsItems")) {
            return event.isDropItems();
        } else {
            return !event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand()).isEmpty();
        }
    }
}
