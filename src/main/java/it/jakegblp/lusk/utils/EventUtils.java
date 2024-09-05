package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;
import java.util.List;

public class EventUtils {
    private static final List<InventoryAction> DROP = new ArrayList<>(List.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ONE_CURSOR));

    public static boolean willItemsDrop(BlockBreakEvent event) {
        if (Skript.methodExists(BlockBreakEvent.class, "isDropsItems")) {
            return event.isDropItems();
        } else {
            return !event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand()).isEmpty();
        }
    }

    public static boolean matchesDropEvent(InventoryClickEvent event, PlayerDropItemEvent dropItemEvent) {
        return event != null && dropsAtAll(event)
                && event.getWhoClicked().getUniqueId().equals(dropItemEvent.getPlayer().getUniqueId());
    }

    public static boolean dropsAtAll(InventoryClickEvent event) {
        if (!dropsFromCursor(event) && event.getCurrentItem() == null)
            return false;
        return DROP.contains(event.getAction());
    }

    public static boolean dropsFromCursor(InventoryClickEvent event) {
        return DROP.indexOf(event.getAction()) > 1;
    }

    public static boolean dropsAllItems(InventoryClickEvent event) {
        return DROP.indexOf(event.getAction()) > 0 && DROP.indexOf(event.getAction()) < 3;
    }
}
