package it.jakegblp.lusk.elements.minecraft.blocks.cauldron.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumWrapper;
import org.bukkit.event.block.CauldronLevelChangeEvent;

@SuppressWarnings("unused")
public class CauldronClassInfos {
    static {
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent$ChangeReason") && Classes.getExactClassInfo(CauldronLevelChangeEvent.ChangeReason.class) == null) {
            EnumWrapper<CauldronLevelChangeEvent.ChangeReason> CAULDRON_CHANGE_REASON_ENUM = new EnumWrapper<>(CauldronLevelChangeEvent.ChangeReason.class);
            Classes.registerClass(CAULDRON_CHANGE_REASON_ENUM.getClassInfo("cauldronchangereason")
                    .user("cauldron ?change ?reasons?")
                    .name("Cauldron Change Reason")
                    .description("All the Cauldron Change Reasons.") // add example
                    .documentationId("9097")
                    .since("1.0.2"));
        }
    }
}
