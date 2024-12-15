package it.jakegblp.lusk.elements.minecraft.entities.endersignal.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Ender Signal - Target Location")
@Description("""
Gets the location the provided ender signals are moving towards.
When set, the drop chance resets to a random value and the despawn timer gets set back to 0.
""")
@Examples("set ender signal target location of {_signal} to {_location}")
@Since("1.3")
public class ExprEnderSignalTargetLocation extends SimplerPropertyExpression<Entity, Location> {

    static {
        register(ExprEnderSignalTargetLocation.class, Location.class, "ender (signal|eye) target location", "entities");
    }

    @Override
    public @Nullable Location convert(Entity from) {
        return from instanceof EnderSignal enderSignal ? enderSignal.getTargetLocation() : null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Entity from, Location to) {
        if (from instanceof EnderSignal enderSignal) {
            enderSignal.setTargetLocation(to);
        }
    }

    @Override
    protected String getPropertyName() {
        return "ender signal target location";
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }
}
