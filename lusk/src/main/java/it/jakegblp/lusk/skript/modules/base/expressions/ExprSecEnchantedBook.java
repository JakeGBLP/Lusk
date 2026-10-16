package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.EnchantmentType;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

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
@Keywords({"enchanted", "book"})
public class ExprSecEnchantedBook extends SimpleExpression<ItemType> {

    private Expression<? extends EnchantmentType> enchantmentTypeExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSecEnchantedBook.class, ExprSecEnchantedBook::new, ItemType.class)
                .patterns("[a] new enchanted book (with|containing|from) [[stored] enchantments] %enchantmenttypes%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        enchantmentTypeExpression = (Expression<? extends EnchantmentType>) expressions[0];
        return true;
    }

    @Override
    protected ItemType @Nullable [] get(Event event) {
        ItemType item = new ItemType(Material.ENCHANTED_BOOK);
        if (enchantmentTypeExpression == null) return new ItemType[]{item};
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (var enchantmentType : enchantmentTypeExpression.getAll(event))
            meta.addStoredEnchant(enchantmentType.getType(), enchantmentType.getLevel(), true);
        item.setItemMeta(meta);
        return new ItemType[]{item};
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
