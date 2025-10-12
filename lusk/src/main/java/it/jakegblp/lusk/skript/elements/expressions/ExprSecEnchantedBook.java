package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.EnchantmentType;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.OptionallySectionExpression;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static it.jakegblp.lusk.skript.utils.Utils.parseSectionNodes;

@Name("Enchanted Book - With Enchantments")
@Description("Creates a new enchanted book with the provided enchantments, can be used both inline and as a section.")
@Examples({"""
        give player enchanted book with enchantments sharpness 2, unbreaking, mending and protection 12
        """,
        """
        give player enchanted book with enchantments:
            density 6
            protection 1
            sharpness 3
            infinity
            respiration 4
        """})
@Since("2.0.0")
@Keywords({"enchanted book", "book"})
public class ExprSecEnchantedBook extends OptionallySectionExpression<ItemType> {

    static {
        Skript.registerExpression(ExprSecEnchantedBook.class, ItemType.class, ExpressionType.SIMPLE,
                "[an|[a] new] enchanted book with [stored] enchantments [inline:%-enchantmenttypes%]");
    }

    private ExpressionList<? extends EnchantmentType> enchantmentExpressionList;

    @Override
    public boolean hasSection(int pattern, SkriptParser.ParseResult result) {
        return !result.hasTag("inline");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean initNormal(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        enchantmentExpressionList = new ExpressionList<EnchantmentType>(new Expression[] {expressions[0]}, EnchantmentType.class, true);
        return true;
    }

    @Override
    public boolean initSection(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        if (node == null) {
            Skript.error("Empty enchanted book section.");
            return false;
        }
        enchantmentExpressionList = parseSectionNodes(this.getAsSection(), node, EnchantmentType.class);
        if (enchantmentExpressionList == null) {
            Skript.error("Parsed enchanted book section.");
            return false;
        }
        return true;
    }

    @Override
    protected ItemType @Nullable [] get(Event event) {
        ItemType item = new ItemType(Material.ENCHANTED_BOOK);
        if (enchantmentExpressionList == null) return new ItemType[] { item };
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (EnchantmentType enchantmentType : enchantmentExpressionList.getAll(event)) {
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
        return "a new enchanted book with enchantments" + (hasSection() ? " " + enchantmentExpressionList.toString(event, debug) : "");
    }

}
