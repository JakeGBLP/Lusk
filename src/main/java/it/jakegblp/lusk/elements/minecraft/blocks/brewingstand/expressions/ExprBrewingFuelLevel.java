package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Fuel Level")
@Description("Returns the brewing fuel level of a Brewing Stand.\nCan be set, reset and deleted.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel level of event-block"})
@Since("1.0.2, 1.3 (Plural, Blockstate, Item)")
@SuppressWarnings("unused")
public class ExprBrewingFuelLevel extends SimplerPropertyExpression<Object,Integer> {

    static {
        register(ExprBrewingFuelLevel.class,Integer.class,"brewing [stand] fuel level", "blocks/blockstates/itemtypes");
    }

    @Override
    public @Nullable Integer convert(Object from) {
        return new BlockWrapper(from).getBrewingFuelLevel();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(Object from, Integer to) {
        new BlockWrapper(from).setBrewingFuelLevel(to);
    }

    @Override
    public void delete(Object from) {
        set(from, null);
    }

    @Override
    public void reset(Object from) {
        delete(from);
    }

    @Override
    protected String getPropertyName() {
        return "brewing stand fuel level";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}