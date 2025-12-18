package it.jakegblp.lusk.nms.core.util;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface Displayable {

    void display(Player... players);

    default void display(Collection<? extends Player> players) {
        display(players.toArray(new Player[0]));
    }

    void remove(Player... players);

    default void remove(Collection<? extends Player> players) {
        remove(players.toArray(new Player[0]));
    }
}
