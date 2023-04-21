package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Name("Citizens - is NPC")
@Description("Checks if an entity is an NPC. (Citizens)")
@Examples({"on right click:\n\tif entity is npc:\n\t\tbroadcast \"you clicked an NPC!\""})
@Since("1.0.0")
public class CondNPC extends PropertyCondition<Entity> {
    static {
        register(CondNPC.class, "(npc|citizen)", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        return Utils.isNPC(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "npc";
    }
}