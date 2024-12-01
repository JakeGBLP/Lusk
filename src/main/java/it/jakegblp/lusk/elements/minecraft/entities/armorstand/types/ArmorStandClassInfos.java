package it.jakegblp.lusk.elements.minecraft.entities.armorstand.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.enums.ArmorStandInteraction;
import it.jakegblp.lusk.api.enums.BodyPart;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;

@SuppressWarnings("unused")
public class ArmorStandClassInfos {
    static {
        EnumWrapper<ArmorStandInteraction> ARMOR_STAND_INTERACTION_ENUM = new EnumWrapper<>(ArmorStandInteraction.class);
        Classes.registerClass(ARMOR_STAND_INTERACTION_ENUM.getClassInfo("armorstandinteraction")
                .user("armor( |-)?stand interactions?")
                .name("Armor Stand Interaction")
                .description("All the Armor Stand Interactions.") // add example
                .documentationId("11913")
                .since("1.1.1"));
        EnumWrapper<BodyPart> BODY_PARTS_INTERACTION_ENUM = new EnumWrapper<>(BodyPart.class);
        Classes.registerClass(BODY_PARTS_INTERACTION_ENUM.getClassInfo("bodypart")
                .user("body ?parts?")
                .name("Body Parts")
                .description("All the Body Parts.") // add example
                .since("1.3"));
    }
}
