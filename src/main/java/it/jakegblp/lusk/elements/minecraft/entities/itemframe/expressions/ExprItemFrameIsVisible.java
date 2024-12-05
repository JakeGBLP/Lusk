package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerbosePropertyExpression;

@Name("Item Frame - is Visible / is Invisible (Property)")
@Description("Gets whether the item frame is visible or not.\nCan be set.")
@Examples({"set item frame is visible property of {_itemFrame} to true", "broadcast item frame is visible property of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprItemFrameIsVisible extends SimplePropertyExpression<Entity, Boolean> {

    static {
        registerVerbosePropertyExpression(ExprItemFrameIsVisible.class, Boolean.class, "item[ |-]frame", "[is] [:in]visible", "entities");
    }

    private boolean invisible;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        invisible = parseResult.hasTag("in");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
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
                    itemFrame.setVisible(bool ^ invisible);
            });
        }
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from instanceof ItemFrame itemFrame && itemFrame.isVisible();
    }

    @Override
    protected String getPropertyName() {
        return "the item frame is " + (invisible ? "in" : "") + "visible property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}