package it.jakegblp.lusk.elements.minecraft.entities.itemframe.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - set Item Silently")
@Description("Sets the item of the provided item frames silently.")
@Examples("silently set itemframe item of {_frame} to iron ingot")
@Since("1.3")
public class EffItemFrameSetItemSilently extends Effect {

    static {
        Skript.registerEffect(EffItemFrameSetItemSilently.class,
                "silently set [the] item[ |-]frame item of %entities% to %itemtype%",
                "silently set %entities%'[s] item[ |-]frame item to %itemtype%");
    }

    private Expression<Entity> entityExpression;
    private Expression<ItemType> itemTypeExpression;

    @Override
    protected void execute(Event event) {
        ItemType itemType = itemTypeExpression.getSingle(event);
        if (itemType == null) return;
        ItemStack itemStack = itemType.getRandom();
        for (Entity entity : entityExpression.getAll(event)) {
            if (entity instanceof ItemFrame itemFrame) {
                itemFrame.setItem(itemStack,false);
            }
        }

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "silently set item frame item of "
                + entityExpression.toString(event,debug) + " to "
                + itemTypeExpression.toString(event,debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) expressions[0];
        itemTypeExpression = (Expression<ItemType>) expressions[1];
        return true;
    }
}
