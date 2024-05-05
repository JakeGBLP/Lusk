package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderdragon.events.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import org.bukkit.entity.EnderDragon;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.entity.EnderDragon$Phase") && Classes.getExactClassInfo(EnderDragon.Phase.class) == null) {
            EnumWrapper<EnderDragon.Phase> ENDERDRAGONPHASE_ENUM = new EnumWrapper<>(EnderDragon.Phase.class);
            Classes.registerClass(ENDERDRAGONPHASE_ENUM.getClassInfo("enderdragonphase")
                    .user("ender ?dragon ? phases?")
                    .name("Ender Dragon Phase")
                    .description("All the Ender Dragon Phases.") // add example
                    .since("1.0.2"));
        }
    }
}
