package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - Item Drop Chance")
@Description("The chance of the item being dropped upon this item frame's destruction.\nCan be set, must be within 0 and 1.\n1 = always drops; 0 = never drops.")
@Examples("set item frame drop chance of {_itemFrame} to 1 # always drops")
@Since("1.3")
public class ExprItemFrameDropChance extends SimplePropertyExpression<Entity,Number> {

    static {
        register(ExprItemFrameDropChance.class, Number.class, "item[ |-]frame [item] drop chance", "entities");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET -> new Class[] { Number.class };
            case RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        float chance;
        if (delta != null && delta[0] instanceof Number number) {
            chance = number.floatValue();
        } else if (mode == Changer.ChangeMode.RESET) {
            chance = 1f;
        } else {
            chance = 0f;
        }
        getExpr().stream(event).forEach(entity -> {
            if (entity instanceof ItemFrame itemFrame) {
                itemFrame.setItemDropChance(Math.clamp(switch (mode) {
                    case ADD -> itemFrame.getItemDropChance() + chance;
                    case REMOVE -> itemFrame.getItemDropChance() - chance;
                    default -> chance;
                },0,1));
            }
        });
    }

    @Override
    public @Nullable Number convert(Entity from) {
        return from instanceof ItemFrame itemFrame ? itemFrame.getItemDropChance() : null;
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
