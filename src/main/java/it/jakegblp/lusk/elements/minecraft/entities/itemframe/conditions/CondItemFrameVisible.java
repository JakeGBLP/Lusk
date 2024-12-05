package it.jakegblp.lusk.elements.minecraft.entities.itemframe.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Item Frame - is Visible / is Invisible")
@Description("Returns whether the item frame is visible or not.")
@Examples("if item frame {_itemFrame} is visible:")
@Since("1.3")
public class CondItemFrameVisible extends PropertyCondition<Entity> {

    static {
        registerPrefixedPropertyCondition(CondItemFrameVisible.class, "item[ |-]frame", "[:in]visible", "entities");
    }

    private boolean invisible;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        invisible = parseResult.hasTag("in");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof ItemFrame itemFrame && itemFrame.isVisible() ^ invisible;
    }

    @Override
    protected String getPropertyName() {
        return (invisible ? "in" : "" )+"visible";
    }
}
