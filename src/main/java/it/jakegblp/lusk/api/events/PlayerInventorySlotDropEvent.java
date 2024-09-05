package it.jakegblp.lusk.api.events;

import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.utils.EventUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerInventorySlotDropEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private static InventoryClickEvent LAST_INVENTORY_CLICK_EVENT = null;

    private final InventoryClickEvent inventoryClickEvent;
    private final PlayerDropItemEvent playerDropItemEvent;
    private final Inventory inventory;
    private final ItemStack originalItem;
    private final int slot;
    private final boolean dropsFromCursor;
    private final boolean dropsAll;
    private final boolean dropsFromInventory;

    public PlayerInventorySlotDropEvent(
            @Nullable InventoryClickEvent inventoryClickEvent,
            @NotNull PlayerDropItemEvent playerDropItemEvent) {
        super(playerDropItemEvent.getPlayer());
        this.playerDropItemEvent = playerDropItemEvent;
        this.inventoryClickEvent = inventoryClickEvent;
        this.dropsFromInventory = EventUtils.matchesDropEvent(inventoryClickEvent, playerDropItemEvent);
        if (dropsFromInventory) {
            this.dropsFromCursor = EventUtils.dropsFromCursor(inventoryClickEvent);
            this.inventory = inventoryClickEvent.getClickedInventory() != null ?
                    inventoryClickEvent.getClickedInventory() : player.getInventory();
            this.slot = inventoryClickEvent.getSlot();
            this.dropsAll = EventUtils.dropsAllItems(inventoryClickEvent);
        } else {
            this.dropsFromCursor = false;
            this.inventory = player.getInventory();
            this.slot = player.getInventory().getHeldItemSlot();
            this.dropsAll = this.getItem().getAmount() > 1;
        }
        this.originalItem = this.getItem().clone();
        if (dropsAll) originalItem.add();
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        if (dropsFromInventory) {
            return dropsFromCursor ? inventoryClickEvent.getCursor() : inventoryClickEvent.getCurrentItem();
        }
        return playerDropItemEvent.getItemDrop().getItemStack();
    }

    public ItemStack getOriginalItem() {
        return originalItem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Nullable
    public static InventoryClickEvent getLastInventoryClickEvent() {
        return LAST_INVENTORY_CLICK_EVENT;
    }

    public Item getItemEntity() {
        return playerDropItemEvent.getItemDrop();
    }

    public PlayerDropItemEvent getPlayerDropItemEvent() {
        return playerDropItemEvent;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }

    public boolean isDropsAll() {
        return dropsAll;
    }

    public boolean isDropsFromCursor() {
        return dropsFromCursor;
    }

    public static void setLastInventoryClickEvent(@NotNull InventoryClickEvent lastInventoryClickEvent) {
        LAST_INVENTORY_CLICK_EVENT = lastInventoryClickEvent;
        Bukkit.getScheduler().runTaskLater(Lusk.getInstance(), () -> {
            if (lastInventoryClickEvent.equals(LAST_INVENTORY_CLICK_EVENT)) {
                LAST_INVENTORY_CLICK_EVENT = null;
            }
        }, 1);
    }

    @Override
    public boolean isCancelled() {
        return playerDropItemEvent.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        playerDropItemEvent.setCancelled(cancel);
    }
}
