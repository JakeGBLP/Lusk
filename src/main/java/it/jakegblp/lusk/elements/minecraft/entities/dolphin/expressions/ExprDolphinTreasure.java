package it.jakegblp.lusk.elements.minecraft.entities.dolphin.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.Location;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;

@Name("Dolphin - Treasure Chest Location")
@Description("""
Gets the treasure location the provided dolphins try to guide players to.

This value is calculated if the player has fed the dolphin a fish (which Lusk includes), `DOLPHIN_SWIM_TO_TREASURE` goal.

Can be set, but it only has an effect if the dolphin is currently leading a player, as this value is recalculated next time it leads a player.

The world of the location you give it does not matter, as the dolphin will always use the world it is currently in.
""")
@Examples({"broadcast dolphin treasure location of target"})
@Since("1.0.3")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprDolphinTreasure extends SimplerPropertyExpression<LivingEntity, Location> {
    static {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API)
            register(ExprDolphinTreasure.class,Location.class,"dolphin treasure [chest] location", "livingentities");
    }

    @Override
    public @Nullable Location convert(LivingEntity from) {
        return from instanceof Dolphin dolphin ? dolphin.getTreasureLocation() : null;
    }

    @Override
    public boolean allowSet() { // todo: allow vector as this always uses the dolphin's world.
        return true;
    }

    @Override
    public void set(LivingEntity from, Location to) {
        if (from instanceof Dolphin dolphin) {
            dolphin.setTreasureLocation(to);
        }
    }

    @Override
    protected String getPropertyName() {
        return "dolphin treasure location";
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }
}