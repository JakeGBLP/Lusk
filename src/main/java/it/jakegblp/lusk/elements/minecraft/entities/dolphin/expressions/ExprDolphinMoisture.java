package it.jakegblp.lusk.elements.minecraft.entities.dolphin.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Dolphin - Moisture Level")
@Description("Returns the moisture level of the provided dolphins.\nCan be set, added to, and removed from.")
@Examples({"broadcast moisture of target"})
@Since("1.0.3, 1.3 (Plural, Add, Remove)")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprDolphinMoisture extends SimplerPropertyExpression<LivingEntity,Integer> {

    static {
        if (PAPER_1_18_2)
            register(ExprDolphinMoisture.class,Integer.class,
                    "dolphin moist(ure [level|amount]|ness [amount])", "livingentities");
    }

    @Override
    public @Nullable Integer convert(LivingEntity from) {
        return from instanceof Dolphin dolphin ? dolphin.getMoistness() : null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Integer to) {
        if (from instanceof Dolphin dolphin)
            dolphin.setMoistness(to);
    }

    @Override
    public void add(LivingEntity from, Integer to) {
        if (from instanceof Dolphin dolphin)
            dolphin.setMoistness(dolphin.getMoistness()+to);
    }

    @Override
    public void remove(LivingEntity from, Integer to) {
        if (from instanceof Dolphin dolphin)
            dolphin.setMoistness(dolphin.getMoistness() - to);
    }

    @Override
    protected String getPropertyName() {
        return "dolphin moisture level";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}