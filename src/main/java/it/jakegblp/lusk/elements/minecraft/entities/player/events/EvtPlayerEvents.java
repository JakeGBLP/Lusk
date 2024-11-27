package it.jakegblp.lusk.elements.minecraft.entities.player.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.CursorSlot;
import ch.njol.skript.util.slot.DroppedItemSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import it.jakegblp.lusk.api.events.PlayerInventorySlotDropEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

@SuppressWarnings("unused")
public class EvtPlayerEvents {
    static {
        // todo: cache all Skript.classExists calls for multiple usages
        if (Skript.classExists("org.bukkit.event.player.PlayerChangedMainHandEvent")) {
            Skript.registerEvent("Player - on Main Hand Change", SimpleEvent.class, PlayerChangedMainHandEvent.class, "main hand switch[ed|ing]")
                    .description("Called when a player changes their main hand in the client settings.")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("io.papermc.paper.event.player.PlayerBedFailEnterEvent")) {
            Skript.registerEvent("Player - on Sleep Fail", SimpleEvent.class, PlayerBedFailEnterEvent.class, "(sleep|bed [enter]) [attempt] fail", "fail[ed] to (sleep|enter [the] bed)")
                    .description("Called when a player attempts to sleep but fails..")
                    .examples("")
                    .since("1.0.0")
                    .requiredPlugins("Paper");
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(final PlayerBedFailEnterEvent e) {
                    return e.getBed();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerBedFailEnterEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerBedFailEnterEvent e) {
                    return e.getBed().getLocation();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            Skript.registerEvent("Player - on Pre Damage", SimpleEvent.class, PrePlayerAttackEntityEvent.class, "pre[-| ]damage")
                    .description("""
                            Called when the player tries to attack an entity. This occurs before any of the damage logic, so cancelling this event will prevent any sort of sounds from being played when attacking. This event will fire as cancelled for certain entities, use the "will be damaged" condition to check if the entity will not actually be attacked.
                            Note: there may be other factors (invulnerability, etc) that will prevent this entity from being attacked that this event will not cover.""")
                    .examples("")
                    .since("1.0.0")
                    .requiredPlugins("Paper");
            EventValues.registerEventValue(PrePlayerAttackEntityEvent.class, Entity.class, new Getter<>() {
                @Override
                public @NotNull Entity get(final PrePlayerAttackEntityEvent e) {
                    return e.getAttacked();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerPostRespawnEvent")) {
            Skript.registerEvent("Player - on Post-Respawn", SimpleEvent.class, PlayerPostRespawnEvent.class, "post[-| ]respawn")
                    .description("""
                            Fired after a player has respawned.""")
                    .examples("")
                    .since("1.0.0")
                    .requiredPlugins("Paper");
            EventValues.registerEventValue(PlayerPostRespawnEvent.class, Location.class, new Getter<>() {
                @Override
                public @NotNull Location get(final PlayerPostRespawnEvent e) {
                    return e.getRespawnedLocation();
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent")) {
            Skript.registerEvent("Whitelist - on Player Profile Verify", SimpleEvent.class, ProfileWhitelistVerifyEvent.class, "[player] [profile] whitelist verify")
                    .description("""
                            Fires when the server needs to verify if a player is whitelisted. Plugins may override/control the servers whitelist with this event, and dynamically change the kick message.""")
                    .examples("")
                    .since("1.0.2")
                    .requiredPlugins("Paper");
            EventValues.registerEventValue(ProfileWhitelistVerifyEvent.class, OfflinePlayer.class, new Getter<>() {
                @Override
                public @Nullable OfflinePlayer get(final ProfileWhitelistVerifyEvent e) {
                    UUID id = e.getPlayerProfile().getId();
                    if (id != null) {
                        return Bukkit.getOfflinePlayer(id);
                    }
                    return null;
                }
            }, EventValues.TIME_NOW);
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerAttemptPickupItemEvent")) {
            Skript.registerEvent("Player - on Pickup Attempt", SimpleEvent.class, PlayerAttemptPickupItemEvent.class, "[player] pickup attempt", "[player] attempt to pickup")
                    .description("""
                            Called when a player attempts to pick an item up from the ground.""")
                    .examples("")
                    .since("1.0.4");
        }
        if (Skript.classExists("org.bukkit.event.player.PlayerHarvestBlockEvent")) {
            Skript.registerEvent("Player - on Block Harvest", SimpleEvent.class, PlayerHarvestBlockEvent.class, "[block] harvest")
                    .description("""
                            This event is called whenever a player harvests a block.
                            A 'harvest' is when a block drops an item (usually some sort of crop) and changes state, but is not broken in order to drop the item.
                            This event is not called for when a block is broken.""")
                    .examples("")
                    .since("1.1.1");
            EventValues.registerEventValue(PlayerHarvestBlockEvent.class, Block.class, new Getter<>() {
                @Override
                public @NotNull Block get(PlayerHarvestBlockEvent event) {
                    return event.getHarvestedBlock();
                }
            }, EventValues.TIME_NOW);
            EventValues.registerEventValue(PlayerHarvestBlockEvent.class, ItemStack[].class, new Getter<>() {
                @Override
                public @NotNull ItemStack[] get(PlayerHarvestBlockEvent event) {
                    return event.getItemsHarvested().toArray(new ItemStack[0]);
                }
            }, EventValues.TIME_NOW);
        }

        Skript.registerEvent("Player - on Inventory Slot Drop", SimpleEvent.class, PlayerInventorySlotDropEvent.class, "player slot drop")
                .description("""
                        Called when a player drops an item from an inventory (or their own).
                        """)
                .examples("")
                .since("1.3");
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, Slot.class, new Getter<>() {
            @Override
            public @NotNull Slot get(PlayerInventorySlotDropEvent event) {
                if (event.getSlot() >= 36) {
                    return new ch.njol.skript.util.slot.EquipmentSlot(event.getPlayer(), event.getSlot());
                } else if (event.isDropsFromCursor()) {
                    return new CursorSlot(event.getPlayer(), event.getItem());
                } else {
                    return new InventorySlot(event.getInventory(), event.getSlot());
                }
            }
        }, EventValues.TIME_PAST);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, Slot.class, new Getter<>() {
            @Override
            public @NotNull Slot get(PlayerInventorySlotDropEvent event) {
                return new DroppedItemSlot(event.getItemEntity());
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, Item.class, new Getter<>() {
            @Override
            public @NotNull Item get(PlayerInventorySlotDropEvent event) {
                return event.getItemEntity();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, ItemStack.class, new Getter<>() {
            @Override
            public @NotNull ItemStack get(PlayerInventorySlotDropEvent event) {
                return event.getOriginalItem();
            }
        }, EventValues.TIME_PAST);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, ItemStack.class, new Getter<>() {
            @Override
            public @NotNull ItemStack get(PlayerInventorySlotDropEvent event) {
                return event.getItem();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, Inventory.class, new Getter<>() {
            @Override
            public @NotNull Inventory get(PlayerInventorySlotDropEvent event) {
                return event.getInventory();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(PlayerInventorySlotDropEvent.class, Integer.class, new Getter<>() {
            @Override
            public @NotNull Integer get(PlayerInventorySlotDropEvent event) {
                return event.getSlot();
            }
        }, EventValues.TIME_NOW);
    }
}