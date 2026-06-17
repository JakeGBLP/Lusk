package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SystemChatPacket;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
public class SystemChatPacketEvent extends ClientPacketEvent<SystemChatPacket> {

    private static final HandlerList handlers = new HandlerList();


    protected Component message;
    protected boolean actionBar;

    public SystemChatPacketEvent(SystemChatPacket packet, Player player, boolean async) {
        super(player, async);
        this.message = packet.getComponent();
        this.actionBar = packet.isOverlay();
    }

    public void setActionBar(boolean actionBar) {
        markModified();
        this.actionBar = actionBar;
    }

    public void setMessage(Component message) {
        markModified();
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public SystemChatPacket createPacket() {
        return new SystemChatPacket(getMessage(), isActionBar());
    }
}
