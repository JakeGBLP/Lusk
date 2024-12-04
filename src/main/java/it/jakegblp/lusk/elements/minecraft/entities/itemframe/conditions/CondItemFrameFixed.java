package it.jakegblp.lusk.elements.minecraft.entities.itemframe.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Item Frame - is Fixed")
@Description("Returns whether the item frame is \"fixed\" or not.\nWhen true it's not possible to destroy/move the frame (e. g. by damage, interaction, pistons, or missing supporting blocks), rotate the item or place/remove items.")
@Examples("if item frame {_itemFrame} is fixed:")
@Since("1.3")
public class CondItemFrameFixed extends PropertyCondition<Entity> {

    static {
        registerPrefixedPropertyCondition(CondItemFrameFixed.class, "item[ |-]frame", "fixed", "entities");
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof ItemFrame itemFrame && itemFrame.isFixed();
    }

    @Override
    protected String getPropertyName() {
        return "fixed";
    }
}
