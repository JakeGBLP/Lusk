package it.jakegblp.lusk.nms.core.world.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ClientEntityReference {

    int getId();

    Player getPlayer();

    static ClientEntityReference of(Player player, Entity entity) {
        return new ClientEntityReference() {
            @Override
            public int getId() {
                return entity.getEntityId();
            }
            @Override
            public Player getPlayer() {
                return player;
            }
        };
    }

    static ClientEntityReference of(Player player, int id) {
        return new ClientEntityReference() {
            @Override
            public int getId() {
                return id;
            }
            @Override
            public Player getPlayer() {
                return player;
            }
        };
    }
}
