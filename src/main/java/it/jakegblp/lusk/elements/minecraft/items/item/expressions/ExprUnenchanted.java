package it.jakegblp.lusk.elements.minecraft.items.item.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item - Unenchanted")
@Description("Returns an item without enchantments")
@Examples({"set tool to unenchanted tool\n# removes all the enchantments"})
@Since("1.0.2")
public class ExprUnenchanted extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprUnenchanted.class, ItemType.class, ExpressionType.SIMPLE,
                "unenchanted %itemtype%");
    }

    private Expression<ItemType> itemTypeExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        itemTypeExpression = (Expression<ItemType>) exprs[0];
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        ItemType itemType = itemTypeExpression.getSingle(e);
        if (itemType == null) return new ItemType[0];
        ItemStack itemStack = itemType.getRandom();
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            itemStack.removeEnchantment(enchantment);
        }
        return new ItemType[]{new ItemType(itemStack)};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "unenchanted " + (e == null ? "" : itemTypeExpression.toString(e, debug));
    }
}
