package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.world.entity.events.EntityEvents;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;


@Getter
@Setter
public class EntityEventPacket implements ClientboundPacket{

    int entityID;
    int eventID;
    EntityEvents eventEnum;

    public EntityEventPacket(int entityID, int eventID){
        this.entityID = entityID;
        this.eventID = eventID;
    }

    public EntityEventPacket(Entity entity, int eventID){
        this.entityID = entity.getEntityId();
        this.eventID = eventID;
    }

    public EntityEventPacket(Entity entity, EntityEvents event){
        this.entityID = entity.getEntityId();
        this.eventID = event.getId();
    }
    public EntityEventPacket(int entityID, EntityEvents event){
        this.entityID = entityID;
        this.eventID = event.getId();
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntityEventPacket(this);
    }
}
