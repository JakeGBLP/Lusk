package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Item Frame - Rotation")
@Description("Returns the rotation of an Item Frame.")
@Examples({"broadcast item frame rotation of target"})
@Since("1.0.2, 1.3 (Reworked)")
@SuppressWarnings("unused")
public class ExprItemFrameRotation extends SimplePropertyExpression<Entity, Rotation> {
    //todo: add radians support
    static {
        register(ExprItemFrameRotation.class, Rotation.class, "item[ |-]frame rotation", "entities");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[]{Rotation.class};
            case RESET, DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        List<ItemFrame> itemFrameList = getExpr().stream(event).filter(entity -> entity instanceof ItemFrame)
                .map(entity -> (ItemFrame) entity).toList();
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
            itemFrameList.forEach(itemFrame -> itemFrame.setRotation(Rotation.NONE));
        } else if (delta != null) {
            if (mode == Changer.ChangeMode.SET && delta[0] instanceof Rotation rotation) {
                itemFrameList.forEach(itemFrame -> itemFrame.setRotation(rotation));
            }
        }
    }

    @Override
    public @Nullable Rotation convert(Entity from) {
        if (from instanceof ItemFrame itemFrame) {
            return itemFrame.getRotation();
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "itemframe rotation";
    }

    @Override
    public Class<? extends Rotation> getReturnType() {
        return Rotation.class;
    }
}
