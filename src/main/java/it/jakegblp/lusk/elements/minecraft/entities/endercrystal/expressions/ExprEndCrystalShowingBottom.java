package it.jakegblp.lusk.elements.minecraft.entities.endercrystal.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("End Crystal - is Showing Bottom (Property)")
@Description("Returns whether or not the provided end crystals are showing the bedrock slate underneath them.\nCan be set.")
@Examples("set the is showing bottom plate property of {_crystal} to false")
@Since("1.3")
public class ExprEndCrystalShowingBottom extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprEndCrystalShowingBottom.class, Boolean.class, "[end[er] crystal]", "[is] showing [the] (bottom|bedrock) [plate|slate]", "entities");
    }

    @Override
    public void set(Entity from, Boolean to) {
        if (from instanceof EnderCrystal enderCrystal) {
            enderCrystal.setShowingBottom(to);
        }
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from instanceof EnderCrystal enderCrystal && enderCrystal.isShowingBottom();
    }

    @Override
    protected String getPropertyName() {
        return "";
    }
}
