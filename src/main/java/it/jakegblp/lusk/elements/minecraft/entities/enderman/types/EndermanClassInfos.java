package it.jakegblp.lusk.elements.minecraft.entities.enderman.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import it.jakegblp.lusk.api.skript.EnumWrapper;

@SuppressWarnings("unused")
public class EndermanClassInfos {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent$Reason") && Classes.getExactClassInfo(EndermanEscapeEvent.Reason.class) == null) {
            EnumWrapper<EndermanEscapeEvent.Reason> ENDERMANESCAPEREASON_ENUM = new EnumWrapper<>(EndermanEscapeEvent.Reason.class);
            Classes.registerClass(ENDERMANESCAPEREASON_ENUM.getClassInfo("endermanescapereason")
                    .user("enderman ?escape ?reasons?")
                    .name("Enderman Escape Reason")
                    .description("All the Valid Enderman Escape Reasons.") // add example
                    .documentationId("11207")
                    .since("1.0.0"));
        }
    }
}
