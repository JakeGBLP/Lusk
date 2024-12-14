package it.jakegblp.lusk.elements.minecraft.entities.endercrystal.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("End Crystal - Beam Target")
@Description("Gets the location that the provided end crystals are pointing their beam to.\nCan be set, if the provided location is in a different world it will fail silently.")
@Examples("set end crystal beam target to {_location}")
@Since("1.3")
public class ExprEndCrystalBeamTarget extends SimplerPropertyExpression<Entity, Location> {

    static {
        register(ExprEndCrystalBeamTarget.class,Location.class,"[end[er] crystal] beam target [location]", "entities");
    }

    @Override
    public @Nullable Location convert(Entity from) {
        return from instanceof EnderCrystal enderCrystal ? enderCrystal.getBeamTarget() : null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(Entity from, @Nullable Location to) {
        if (to != null && from.getWorld() != to.getWorld()) return;
        if (from instanceof EnderCrystal enderCrystal) {
            enderCrystal.setBeamTarget(to);
        }
    }

    @Override
    public void delete(Entity from) {
        set(from, null);
    }

    @Override
    protected String getPropertyName() {
        return "ender crystal beam target";
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }
}
