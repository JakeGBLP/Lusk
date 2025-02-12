package it.jakegblp.lusk.elements.packets.entities.entity.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.enums.EntityAnimation;
import it.jakegblp.lusk.api.skript.EnumWrapper;

//todo: figure out
@SuppressWarnings("unused")
public class PacketsEntityClassInfos {
    static {
        EnumWrapper<EntityAnimation> ENTITY_ANIMATION_ENUM = new EnumWrapper<>(EntityAnimation.class, null, "animation");
        Classes.registerClass(ENTITY_ANIMATION_ENUM.getClassInfo("entityanimation")
                .user("entity ?animations?")
                .name("Packets | Entity Animation")
                .description("All the Entity Animation.") // add example
                .since("1.4"));
    }
}
