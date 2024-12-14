package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - Rotation")
@Description("Returns the rotation of an Item Frame.")
@Examples({"broadcast item frame rotation of target"})
@Since("1.0.2, 1.3 (Reworked)")
@SuppressWarnings("unused")
public class ExprItemFrameRotation extends SimplerPropertyExpression<Entity, Rotation> {
    //todo: add radians support
    static {
        register(ExprItemFrameRotation.class, Rotation.class, "item[ |-]frame rotation", "entities");
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
    public void set(Entity from, Rotation to) {
        if (from instanceof ItemFrame itemFrame) {
            itemFrame.setRotation(to);
        }
    }

    @Override
    public void delete(Entity from) {
        reset(from);
    }

    @Override
    public void reset(Entity from) {
        set(from, Rotation.NONE);
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
