package it.jakegblp.lusk.elements.minecraft.entities.itemframe.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

@Name("Item Frame - is Fixed")
@Description("Returns whether the item frame is \"fixed\" or not.\nWhen true it's not possible to destroy/move the frame (e. g. by damage, interaction, pistons, or missing supporting blocks), rotate the item or place/remove items.")
@Examples("if item frame {_itemFrame} is fixed:")
@Since("1.3")
public class CondItemFrameFixed extends PrefixedPropertyCondition<Entity> {

    static {
        register(CondItemFrameFixed.class, "item[ |-]frame", "fixed", "entities");
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof ItemFrame itemFrame && itemFrame.isFixed();
    }

    @Override
    protected String getPropertyName() {
        return "fixed";
    }

    @Override
    public String getPrefix() {
        return "item frame";
    }
}
