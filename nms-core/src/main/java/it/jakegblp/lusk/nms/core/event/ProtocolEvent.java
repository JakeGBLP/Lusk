package it.jakegblp.lusk.nms.core.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

@Getter
@Setter
public abstract class ProtocolEvent extends PlayerEvent implements Cancellable {
    protected boolean cancelled;

    public ProtocolEvent(Player player, boolean isAsync) {
        super(player, isAsync);
    }
}
