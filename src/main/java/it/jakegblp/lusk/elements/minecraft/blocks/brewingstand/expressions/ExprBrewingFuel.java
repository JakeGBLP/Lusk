package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;

@Name("BrewingStand - Fuel Item")
@Description("Returns the brewing fuel item of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2, 1.3 (Plural, Blockstate, Item)")
@SuppressWarnings("unused")
public class ExprBrewingFuel extends SimplerPropertyExpression<Object, ItemType> {

    static {
        register(ExprBrewingFuel.class, ItemType.class, "brewing fuel [item]", "blocks/blockstates/itemtypes");
    }

    @Override
    public @Nullable ItemType convert(Object from) {
        return new BlockWrapper(from).getBrewingFuel();
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
    public void set(Object from, ItemType to) {
        new BlockWrapper(from).setBrewingFuel(to);
    }

    @Override
    public void delete(Object from) {
        set(from,null);
    }

    @Override
    protected String getPropertyName() {
        return "brewing fuel item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

}
