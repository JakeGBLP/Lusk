package it.jakegblp.lusk.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

//todo: figure out
public class PacketReceiveEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    //private final PacketWrapper packet;
//
    //public PacketReceiveEvent(PacketWrapper packet) {
    //    this.packet = packet;
    //}


    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
