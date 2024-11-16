package it.jakegblp.lusk.elements.minecraft.entities.dolphin.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Location;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Dolphin - Treasure")
@Description("Returns the treasure location this dolphin tries to guide players to.\nCan be set.")
@Examples({"broadcast dolphin treasure location of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprDolphinTreasure extends SimplePropertyExpression<LivingEntity, Location> {

    static {
        register(ExprDolphinTreasure.class, Location.class, "dolphin treasure location", "livingentities");
    }

    @Override
    @Nullable
    public Location convert(LivingEntity entity) {
        if (entity instanceof Dolphin dolphin) return dolphin.getTreasureLocation();
        return null;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Location.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta[0] instanceof Location location)
            for (LivingEntity entity : getExpr().getArray(event)) {
                if (entity instanceof Dolphin dolphin) dolphin.setTreasureLocation(location);
            }
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "dolphin treasure location";
    }

}