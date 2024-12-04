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
public class ExprItemFrameDropChance extends SimplePropertyExpression<Entity,Double> {

    static {
        register(ExprItemFrameDropChance.class, Double.class, "item[ |-]frame [item] drop chance", "entities");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET -> new Class[] { Double.class };
            case RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        double chance;
        if (delta != null && delta[0] instanceof Double d) {
            chance = d;
        } else if (mode == Changer.ChangeMode.RESET) {
            chance = 1d;
        } else {
            chance = 0d;
        }
        getExpr().stream(event).forEach(entity -> {
            if (entity instanceof ItemFrame itemFrame) {
                itemFrame.setItemDropChance((float) Math.clamp(switch (mode) {
                    case ADD -> itemFrame.getItemDropChance() + chance;
                    case REMOVE -> itemFrame.getItemDropChance() - chance;
                    case SET -> chance;
                    default -> 0;
                },0,1));
            }
        });
    }

    @Override
    public @Nullable Double convert(Entity from) {
        return from instanceof ItemFrame itemFrame ? (double) itemFrame.getItemDropChance() : null;
    }

    @Override
    protected String getPropertyName() {
        return "item frame item drop chance";
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }
}
