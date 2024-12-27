package it.jakegblp.lusk.elements.anvilgui.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import it.jakegblp.lusk.api.events.*;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

public class EvtAnvilGuiEvents {
    static {
        Skript.registerEvent("Anvil GUI - on Anvil Gui Open", SimpleEvent.class, AnvilGuiOpenEvent.class, ANVIL_GUI_PREFIX + " open[ed]")
                .description("""
                        Called when an Anvil Gui is opened.
                        Can be cancelled.
                        """)
                .examples("on anvil gui open:")
                .since("1.3");
        Skript.registerEvent("Anvil GUI - on Anvil Gui Click", SimpleEvent.class, AnvilGuiClickEvent.class, ANVIL_GUI_PREFIX + " click[ed]")
                .description("""
                        Called when an Anvil Gui is clicked.
                        """)
                .examples("on anvil gui click:")
                .since("1.3");
        Skript.registerEvent("Anvil GUI - on Anvil Gui Close", SimpleEvent.class, AnvilGuiCloseEvent.class, ANVIL_GUI_PREFIX + " close[d]")
                .description("""
                        Called when an Anvil Gui is opened.
                        *Note*:
                        - To cancel this event you'll need to use the `Anvil GUI - Prevent Closing` effect before this event is called.
                        """)
                .examples("on anvil gui close:")
                .since("1.3");

        EventValues.registerEventValue(AnvilGuiClickEvent.class, Integer.class, new Getter<>() {
            @Override
            public Integer get(final AnvilGuiClickEvent e) {
                return e.getSlot();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(AnvilGuiClickEvent.class, Slot.class, new Getter<>() {
            @Override
            public Slot get(final AnvilGuiClickEvent e) {
                return new InventorySlot(e.getInventory(), e.getSlot());
            }
        }, EventValues.TIME_NOW);

        // click and close only
        EventValues.registerEventValue(AnvilGuiSnapshotEvent.class, String.class, new Getter<>() {
            @Override
            public @NotNull String get(final AnvilGuiSnapshotEvent e) {
                return e.getText();
            }
        }, EventValues.TIME_NOW);

        // common event values
        EventValues.registerEventValue(AnvilGuiEvent.class, AnvilGuiWrapper.class, new Getter<>() {
            @Override
            public @NotNull AnvilGuiWrapper get(final AnvilGuiEvent e) {
                return e.getAnvil();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(AnvilGuiEvent.class, Inventory.class, new Getter<>() {
            @Override
            public @NotNull Inventory get(final AnvilGuiEvent e) {
                return e.getInventory();
            }
        }, EventValues.TIME_NOW);

    }
}
