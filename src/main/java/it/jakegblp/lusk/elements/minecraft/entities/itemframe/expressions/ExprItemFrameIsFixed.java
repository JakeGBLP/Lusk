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

import static it.jakegblp.lusk.utils.LuskUtils.registerVerbosePropertyExpression;

@Name("Armor Stand - is Fixed (Property)")
@Description("Gets whether the item frame is \"fixed\" or not.\nCan be set.\nWhen true it's not possible to destroy/move the frame (e. g. by damage, interaction, pistons, or missing supporting blocks), rotate the item or place/remove items.")
@Examples({"set whether item frame {_itemFrame} is fixed to true", "broadcast whether item frame target is fixed"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprItemFrameIsFixed extends SimplePropertyExpression<Entity, Boolean> {

    static {
        registerVerbosePropertyExpression(ExprItemFrameIsFixed.class, Boolean.class, "[item[ |-]frame]", "[is] fixed", "livingentities");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {Boolean.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            getExpr().stream(event).forEach(entity -> {
                if (entity instanceof ItemFrame itemFrame)
                    itemFrame.setFixed(bool);
            });
        }
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from instanceof ItemFrame itemFrame && itemFrame.isFixed();
    }

    @Override
    protected String getPropertyName() {
        return "the item frame is fixed property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}