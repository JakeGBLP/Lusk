package it.jakegblp.lusk.elements.minecraft.blocks.cauldron.events.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent$ChangeReason") && Classes.getExactClassInfo(CauldronLevelChangeEvent.ChangeReason.class) == null) {
            EnumWrapper<CauldronLevelChangeEvent.ChangeReason> CAULDRONCHANGEREASON_ENUM = new EnumWrapper<>(CauldronLevelChangeEvent.ChangeReason.class);
            Classes.registerClass(CAULDRONCHANGEREASON_ENUM.getClassInfo("cauldronchangereason")
                    .user("cauldron ?change ?reasons?")
                    .name("Cauldron Change Reason")
                    .description("All the Cauldron Change Reasons.") // add example
                    .since("1.0.2"));
        }
    }
}
