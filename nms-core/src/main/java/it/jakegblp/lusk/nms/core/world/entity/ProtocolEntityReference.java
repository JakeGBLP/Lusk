package it.jakegblp.lusk.nms.core.world.entity;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ProtocolEntityReference {

    int getId();

    static ProtocolEntityReference of(Entity entity) {
        return entity::getEntityId;
    }

    static ProtocolEntityReference of(int id) {
        return () -> id;
    }
}
