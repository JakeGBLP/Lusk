package it.jakegblp.lusk.elements.minecraft.inventory.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.bukkit.inventory.EquipmentSlot;

@SuppressWarnings("unused")
public class InventoryClassInfos {
    static {
        if (Skript.classExists("org.bukkit.inventory.EquipmentSlot") && Classes.getExactClassInfo(EquipmentSlot.class) == null) {
            EnumWrapper<EquipmentSlot> EQUIPMENTSLOT_ENUM = new EnumWrapper<>(EquipmentSlot.class, null, "slot");
            Classes.registerClass(EQUIPMENTSLOT_ENUM.getClassInfo("equipmentslot")
                    .user("equipment ?slots?")
                    .name("Equipment Slot")
                    .description("All the Equipment Slots.")
                    .documentationId("8849")
                    .since("1.0.0"));
        }
    }
}
