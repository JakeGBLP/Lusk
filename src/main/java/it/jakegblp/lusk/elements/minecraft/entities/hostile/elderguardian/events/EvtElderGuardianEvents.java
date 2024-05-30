package it.jakegblp.lusk.elements.minecraft.entities.hostile.elderguardian.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.entity.ElderGuardianAppearanceEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EvtElderGuardianEvents {
    static {

        if (Skript.classExists("io.papermc.paper.event.entity.ElderGuardianAppearanceEvent")) {
            Skript.registerEvent("Elder Guardian - on Appear", SimpleEvent.class, ElderGuardianAppearanceEvent.class, "elder guardian appear[ing]")
                    .description("""
                            Called when an ElderGuardian appears in front of a Player.""")
                    .examples("")
                    .since("1.0.2")
                    .requiredPlugins("Paper");
            EventValues.registerEventValue(ElderGuardianAppearanceEvent.class, Player.class, new Getter<>() {
                @Override
                public @NotNull Player get(final ElderGuardianAppearanceEvent e) {
                    return e.getAffectedPlayer();
                }
            }, EventValues.TIME_NOW);
        }
    }
}
