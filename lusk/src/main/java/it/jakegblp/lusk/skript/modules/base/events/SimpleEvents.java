package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import io.papermc.paper.event.block.CompostItemEvent;
import io.papermc.paper.event.entity.EntityCompostItemEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.CampfireStartEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class SimpleEvents {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(FluidLevelChangeEvent.class)
                .patterns("fluid level chang(e[d]|ing)")
                .name("Fluid Level Change")
                .description("Called when the fluid level of a block changes due to changes in adjacent blocks.")
                .since("2.0.0")
                .keywords("water", "fluid", "lava")
                .examples("on fluid level change:")
                .register(syntaxRegistry);
        Registrations.registerEventValue(FluidLevelChangeEvent.class, BlockData.class, FluidLevelChangeEvent::getNewData);

        Registrations.event(BlockBreakProgressUpdateEvent.class)
                .patterns("block damag(ing|e) update", "block break[ing] progress [update]")
                .name("Block Break Progress Update")
                .description("""
                        Called when the progress of a block break is updated.
                        `event-number` = the block damage progress, ranges from 0.0 to 1.0, where 0 is no damage and 1.0 is the most damage.""")
                .since("1.3")
                .keywords("block damage", "block break", "breaking")
                .examples("on block damage update:")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockBreakProgressUpdateEvent.class, Number.class, BlockBreakProgressUpdateEvent::getProgress);
        Registrations.registerEventValue(BlockBreakProgressUpdateEvent.class, Entity.class, BlockBreakProgressUpdateEvent::getEntity);

        Registrations.event(CampfireStartEvent.class)
                .patterns("campfire start[ed|ing] [to cook]", "campfire beg(an|un|in[ning])")
                .name("Campfire Start")
                .examples("on campfire start:")
                .description("Called when a Campfire starts to cook.")
                .since("1.0.2")
                .keywords("campfire", "cook")
                .register(syntaxRegistry);

        Registrations.event(CauldronLevelChangeEvent.class)
                .patterns("cauldron [[level] chang(e[d]|ing)]")
                .name("Cauldron Level Change")
                .examples("on cauldron level change:")
                .description("Called when a Cauldron's level changes.")
                .since("1.0.2")
                .keywords("cauldron", "level")
                .register(syntaxRegistry);
        Registrations.registerEventValue(CauldronLevelChangeEvent.class, Entity.class, CauldronLevelChangeEvent::getEntity);
        Registrations.registerFutureEventValue(CauldronLevelChangeEvent.class, BlockState.class, CauldronLevelChangeEvent::getNewState);
        Registrations.registerEventValue(CauldronLevelChangeEvent.class, CauldronLevelChangeEvent.ChangeReason.class, CauldronLevelChangeEvent::getReason);

        Registrations.event(EntityCompostItemEvent.class)
                .patterns("entity compost item")
                .name("Entity Compost Item")
                .description("Called when an item is about to be composted by an entity.")
                .examples("on entity compost item:")
                .since("1.0.1")
                .keywords("compost", "composter")
                .register(syntaxRegistry);
        Registrations.registerEventValue(EntityCompostItemEvent.class, Entity.class, EntityCompostItemEvent::getEntity);

        Registrations.event(CompostItemEvent.class)
                .patterns("[item] compost")
                .name("Item Compost")
                .description("""
                        Called when an item is about to be composted.
                        Called when hoppers move items into composters; to prevent that from happening you must cancel the inventory move event.""")
                .examples("on compost:")
                .since("1.0.1")
                .keywords("compost", "composter", "hopper")
                .register(syntaxRegistry);
        Registrations.registerEventValue(CompostItemEvent.class, ItemType.class, event -> new ItemType(event.getItem()));

        Registrations.event(BlockDispenseArmorEvent.class)
                .patterns("armor dispens(e[d]|ing)")
                .name("Armor Dispense")
                .description("Called when an equipable item is dispensed from a block and equipped on a nearby entity.")
                .examples("on armor dispense:")
                .since("1.0.0")
                .keywords("armor", "dispenser", "dispense", "equip", "equipable")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockDispenseArmorEvent.class, Entity.class, BlockDispenseArmorEvent::getTargetEntity);

        Registrations.event(BlockFailedDispenseEvent.class)
                .patterns("dispense fail", "failed [to] dispense")
                .name("Dispense Fail")
                .description("Called when a block tries to dispense an item, but its inventory is empty.")
                .examples("on armor dispense:")
                .since("1.0.2")
                .keywords("fail", "dispenser", "dispense", "failed")
                .register(syntaxRegistry);
        Registrations.event(BlockPreDispenseEvent.class)
                .patterns("pre[-| ]dispens(e[d]|ing)")
                .name("Dispense Fail")
                .description("Called when a block is about to dispense an item.")
                .examples("on pre dispense:")
                .since("1.0.2")
                .keywords("fail", "dispenser", "dispense", "failed")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockPreDispenseEvent.class, Integer.class, BlockPreDispenseEvent::getSlot);
        Registrations.registerEventValue(BlockPreDispenseEvent.class, ItemType.class, event -> new ItemType(event.getItemStack()));
        Registrations.registerEventValue(BlockPreDispenseEvent.class, Slot.class, event -> {
            if (event.getBlock().getState() instanceof InventoryHolder holder)
                return new InventorySlot(holder.getInventory(), event.getSlot());
            else return null;
        });

        Registrations.event(PrepareGrindstoneEvent.class)
                .patterns("grindstone prepar(e[d]|ing)", "prepar(e|ing) of grindstone")
                .name("Grindstone Prepare")
                .description("Called when an item is put in a slot for repair or unenchanting in a grindstone.")
                .examples("on grindstone prepare:")
                .keywords("grindstone", "prepare", "unenchanting")
                .since("1.0.0 (Paper), 1.0.3 (Spigot)")
                .register(syntaxRegistry);
        Registrations.registerEventValue(PrepareGrindstoneEvent.class, Inventory.class, PrepareGrindstoneEvent::getInventory);
        Registrations.registerEventValue(PrepareGrindstoneEvent.class, Location.class, e -> e.getInventory().getLocation());
        Registrations.registerEventValue(PrepareGrindstoneEvent.class, Slot.class, e -> new InventorySlot(e.getInventory(), 2));


        Registrations.event(SmithItemEvent.class)
                .patterns("item smith[ed|ing]")
                .name("Item Smith")
                .description("Called when the recipe of an item is completed inside a smithing table.")
                .examples("on item smith:")
                .keywords("smithing", "smith")
                .since("1.3.4-beta2")
                .register(syntaxRegistry);
    }
}
