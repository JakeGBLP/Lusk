package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.BlockUtils.getBrewingIngredient;
import static it.jakegblp.lusk.utils.BlockUtils.setBrewingIngredient;

@Name("BrewingStand - Ingredient")
@Description("Returns the brewing ingredient of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing ingredient of event-block"})
@Since("1.0.2, 1.3 (plural)")
@SuppressWarnings("unused")
public class ExprBrewingIngredient extends SimplePropertyExpression<Block, ItemType> {

    static {
        register(ExprBrewingIngredient.class, ItemType.class, "brewing ingredient [item]", "blocks");
    }

    @Override
    public @Nullable ItemType convert(Block from) {
        return getBrewingIngredient(from);
    }

    @Override
    protected String getPropertyName() {
        return "brewing ingredient item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[] { ItemStack.class };
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemType fuel = (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof ItemType item) ? item : null;
        getExpr().stream(event).forEach(block -> setBrewingIngredient(block,fuel));
    }
}
