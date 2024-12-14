package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - is Visible / is Invisible (Property)")
@Description("Gets whether the item frame is visible or not.\nCan be set.")
@Examples({"set item frame is visible property of {_itemFrame} to true", "broadcast item frame is visible property of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprItemFrameIsVisible extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprItemFrameIsVisible.class, Boolean.class, "item[ |-]frame", "[is] [:in]visible", "entities");
    }

    @Override
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return parseResult.hasTag("in");
    }

    @Override
    public void set(Entity from, Boolean to) {
        if (from instanceof ItemFrame itemFrame) {
            itemFrame.setInvisible(to);
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
        return from instanceof ItemFrame itemFrame && itemFrame.isVisible() ^ isNegated();
    }

    @Override
    protected String getPropertyName() {
        return "item frame is " + (isNegated() ? "in" : "") + "visible property";
    }
}