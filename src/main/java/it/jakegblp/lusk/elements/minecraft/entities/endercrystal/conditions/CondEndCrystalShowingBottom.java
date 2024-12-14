package it.jakegblp.lusk.elements.minecraft.entities.endercrystal.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;

@Name("End Crystal - is Showing Bottom")
@Description("Returns whether or not the provided end crystals are showing the bedrock slate underneath them.")
@Examples("if {_endCrystal} is showing bottom:")
@Since("1.3")
public class CondEndCrystalShowingBottom extends PropertyCondition<Entity> {

    static {
        register(CondEndCrystalShowingBottom.class, "showing [its|the[ir]] (bottom|bedrock) [plate|slate]","entities");
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof EnderCrystal enderCrystal && enderCrystal.isShowingBottom();
    }

    @Override
    protected String getPropertyName() {
        return "showing bottom";
    }
}
