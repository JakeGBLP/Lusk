package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - is Fixed (Property)")
@Description("Gets whether the item frame is \"fixed\" or not.\nCan be set and reset.\nWhen true it's not possible to destroy/move the frame (e. g. by damage, interaction, pistons, or missing supporting blocks), rotate the item or place/remove items.")
@Examples({"set whether item frame {_itemFrame} is fixed to true", "broadcast whether item frame target is fixed"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprItemFrameIsFixed extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprItemFrameIsFixed.class, Boolean.class, "item[ |-]frame", "[is] fixed", "entities");
    }

    @Override
    public void set(Entity from, Boolean to) {
        if (from instanceof ItemFrame itemFrame) {
            itemFrame.setFixed(to);
        }
    }

    @Override
    public void reset(Entity from) {
        set(from, false);
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
    public @Nullable Boolean convert(Entity from) {
        return from instanceof ItemFrame itemFrame && itemFrame.isFixed();
    }

    @Override
    protected String getPropertyName() {
        return "item frame is fixed property";
    }
}