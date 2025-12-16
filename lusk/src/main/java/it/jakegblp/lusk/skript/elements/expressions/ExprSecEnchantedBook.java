package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.EnchantmentType;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;

@Name("Enchanted Book - With Enchantments")
@Description("Creates a new enchanted book with the provided enchantments.")
@Examples({"""
        give player enchanted book with enchantments sharpness 2, unbreaking, mending and protection 12
        """,
        """
        give player new enchanted book with enchantment type list:
          protection 1
          sharpness 3
          infinity
          respiration 4
        """})
@Since("2.0.0")
@Keywords({"enchanted book", "book"})
public class ExprSecEnchantedBook extends SimpleExpression<ItemType> {

    static {
        Skript.registerExpression(ExprSecEnchantedBook.class, ItemType.class, ExpressionType.SIMPLE,
                "[a] new enchanted book (with|containing|from) [[stored] enchantments] %enchantmenttypes%");
    }

    private Expression<? extends EnchantmentType> enchantmentTypeExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        enchantmentTypeExpression = (Expression<? extends EnchantmentType>) expressions[0];
        return true;
    }

    @Override
    protected ItemType @Nullable [] get(Event event) {
        ItemType item = new ItemType(Material.ENCHANTED_BOOK);
        if (enchantmentTypeExpression == null) return new ItemType[] { item };
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (EnchantmentType enchantmentType : enchantmentTypeExpression.getAll(event)) {
            meta.addStoredEnchant(enchantmentType.getType(), enchantmentType.getLevel(), true);
        }
        item.setItemMeta(meta);
        return new ItemType[] { item };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new enchanted book with " + enchantmentTypeExpression.toString(event, debug);
    }
}
