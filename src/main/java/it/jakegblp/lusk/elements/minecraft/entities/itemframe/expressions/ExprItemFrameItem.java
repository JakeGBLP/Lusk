package it.jakegblp.lusk.elements.minecraft.entities.itemframe.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.elements.other.sections.SecSilent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - Item (Silently Set)")
@Description("Gets a copy of the item in the provided item frames.\nCan be set and deleted, this expression supports the Silent Section, see examples.")
@Examples({
        "send item frame item of {_frame}",
        """
        silently:
          set item frame item of {_frame} to stone
        """
})
public class ExprItemFrameItem extends SimplerPropertyExpression<Entity, ItemType> {

    static {
        register(ExprItemFrameItem.class, ItemType.class, "item[ |-]frame item", "entities");
    }

    private boolean silently;

    @Override
    public @Nullable ItemType convert(Entity from) {
        if (from instanceof ItemFrame itemFrame) {
            return new ItemType(itemFrame.getItem());
        }
        return null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(Entity from, ItemType to) {
        if (from instanceof ItemFrame itemFrame) {
            itemFrame.setItem(to == null ? null : to.getRandom(),!silently);
        }
    }

    @Override
    public void delete(Entity from) {
        set(from,null);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        silently = getParser().isCurrentSection(SecSilent.class);
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected String getPropertyName() {
        return "item frame item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}
