package it.jakegblp.lusk.elements.minecraft.entities.other.armorstand.events.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.classes.ArmorStandInteraction;
import it.jakegblp.lusk.wrappers.EnumWrapper;

public class Types {
    static {
        EnumWrapper<ArmorStandInteraction> ARMORSTANDINTERACTION_ENUM = new EnumWrapper<>(ArmorStandInteraction.class);
        Classes.registerClass(ARMORSTANDINTERACTION_ENUM.getClassInfo("armorstandinteraction")
                .user("armor( |-)?stand interactions?")
                .name("Armor Stand Interaction")
                .description("All the Armor Stand Interactions.") // add example
                .since("1.1.1"));
    }
}
