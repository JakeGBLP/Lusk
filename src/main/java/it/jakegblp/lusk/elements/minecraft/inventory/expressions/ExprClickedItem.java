package it.jakegblp.lusk.elements.minecraft.inventory.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Inventory Click - Clicked Item")
@Description({"Can be set."})
@Examples({"set clicked itemtype to stone"})
@Since("1.3")
public class ExprClickedItem extends EventValueExpression<ItemStack> {
	// todo: implement this for future events
	static {
		register(ExprClickedItem.class, ItemStack.class, "[event-|the ][clicked ]item[stack|type]");
	}

	public ExprClickedItem() {
		super(ItemStack.class,true);
	}

	@Override
	public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return switch (mode) {
            case SET-> new Class[]{ItemStack.class};
			case DELETE, RESET -> new Class[0];
			case ADD, REMOVE, REMOVE_ALL -> null;
        };
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
		if (event instanceof InventoryClickEvent inventoryClickEvent) {
			if (mode == Changer.ChangeMode.SET &&
					delta != null &&
					delta[0] instanceof ItemStack itemStack) {
				inventoryClickEvent.setCurrentItem(itemStack);
			} else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
				inventoryClickEvent.setCurrentItem(ItemStack.empty());
			}
		}
	}
}