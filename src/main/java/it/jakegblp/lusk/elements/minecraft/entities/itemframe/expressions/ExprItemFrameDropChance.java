package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.NumberUtils.roundFloatPrecision;

@Name("Item Frame - Item Drop Chance")
@Description("""
The chance of the item being dropped upon this item frame's destruction.
Can be set, removed from, added to, reset (1) and deleted (0).

The final value is always clamped between 0 and 1.

1 = always drops; 0 = never drops.""")
@Examples("set item frame drop chance of {_itemFrame} to 1 # always drops")
@Since("1.3")
public class ExprItemFrameDropChance extends SimplerPropertyExpression<Entity,Number> {

    static {
        register(ExprItemFrameDropChance.class, Number.class, "[item[ |-]frame] [item] drop chance", "entities");
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
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(Entity from, Number to) {
        if (from instanceof ItemFrame itemFrame) {
            itemFrame.setItemDropChance(Math.clamp(to.floatValue(),0,1));
        }
    }

    @Override
    public void add(Entity from, Number to) {
        if (from instanceof ItemFrame itemFrame) {
            set(from, itemFrame.getItemDropChance()+to.floatValue());
        }
    }

    @Override
    public void remove(Entity from, Number to) {
        if (from instanceof ItemFrame itemFrame) {
            set(from, itemFrame.getItemDropChance()-to.floatValue());
        }
    }

    @Override
    public void delete(Entity from) {
        set(from, 0);
    }

    @Override
    public void reset(Entity from) {
        set(from, 1);
    }

    @Override
    public @Nullable Number convert(Entity from) {
        return from instanceof ItemFrame itemFrame ? roundFloatPrecision(itemFrame.getItemDropChance()) : null;
    }

    @Override
    protected String getPropertyName() {
        return "item frame item drop chance";
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
}
