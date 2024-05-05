package it.jakegblp.lusk.elements.minecraft.inventory.types;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.registrations.Classes;
import org.bukkit.inventory.EquipmentSlot;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.inventory.EquipmentSlot") && Classes.getExactClassInfo(EquipmentSlot.class) == null) {
            Classes.registerClass(new EnumClassInfo<>(EquipmentSlot.class, "equipmentslot", "equipment slots")
                    .user("equipment ?slots?")
                    .name("Equipment Slot")
                    .description("All the Equipment Slots.")
                    .since("1.0.0"));
        }
    }
}
