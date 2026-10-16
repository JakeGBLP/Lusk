package it.jakegblp.lusk.skript.modules.citizens.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Citizens - is NPC")
@Description("Checks if an entity is an NPC. (Citizens)")
@Examples({"on right click:\n\tif entity is npc:\n\t\tbroadcast \"you clicked an NPC!\""})
@Since("1.0.0")
public class CondNPC extends PropertyCondition<Entity> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondNPC.class, CondNPC::new)
                .property("entities", PropertyType.BE, "([a[n]] npc|[a] citizen)")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(Entity entity) {
        return entity.hasMetadata("NPC");
    }

    @Override
    protected String getPropertyName() {
        return "npc";
    }
}
