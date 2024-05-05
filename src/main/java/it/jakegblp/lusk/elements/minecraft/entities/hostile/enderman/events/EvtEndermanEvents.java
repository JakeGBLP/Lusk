package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderman.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;

public class EvtEndermanEvents {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerEvent("Enderman - on Escape", SimpleEvent.class, EndermanEscapeEvent.class, "enderman escape")
                    .description("""
                            This event requires Paper.
                                                        
                            Called when an enderman escapes.
                                                        
                            CRITICAL_HIT: The enderman has teleported away due to a critical hit
                            DROWN: Specific case for CRITICAL_HIT where the enderman is taking rain damage
                            INDIRECT: The enderman has teleported away due to indirect damage (ranged)
                            RUNAWAY: The enderman has stopped attacking and ran away
                            STARE: The enderman has teleported away due to the player staring at it during combat""")
                    .examples("")
                    .since("1.0.0");
        }
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent")) {
            Skript.registerEvent("Enderman - on Attack Decide", SimpleEvent.class, EndermanAttackPlayerEvent.class, "enderman [attack] decide", "enderman decide to attack")
                    .description("""
                            This event requires Paper.
                                                        
                            Fired when an Enderman determines if it should attack a player or not.
                            Starts off cancelled if the player is wearing a pumpkin head or is not looking at the Enderman, according to Vanilla rules.""")
                    .examples("")
                    .since("1.0.0");
        }
    }
}
