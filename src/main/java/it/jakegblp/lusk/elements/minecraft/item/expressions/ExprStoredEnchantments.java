package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.EnchantmentType;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Enchanted Book - Stored Enchantments")
@Description("Returns the Enchantments stored in an enchanted book.\nCan be set, added to, removed from, reset and deleted.")
@Examples({"broadcast stored enchantments of tool"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprStoredEnchantments extends PropertyExpression<ItemType, EnchantmentType> {

    static {
        Skript.registerExpression(ExprStoredEnchantments.class, EnchantmentType.class, ExpressionType.PROPERTY,
                "[the] enchantment[ type]s stored in %itemtypes%",
                "[the] stored enchantment[ type]s of %itemtypes%",
                "%itemtypes%'[s] stored enchantment[ type]s");
    }

    @Override
    protected EnchantmentType[] get(Event event, ItemType[] source) {
        return Arrays.stream(source).flatMap(itemType -> Arrays.stream(ItemUtils.getStoredEnchantments(itemType))).toArray(EnchantmentType[]::new);
    }

    @Override
    public Class<? extends EnchantmentType> getReturnType() {
        return EnchantmentType.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "stored enchantments of " + getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemType>) expressions[0]);
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, REMOVE_ALL, SET -> CollectionUtils.array(Enchantment[].class, EnchantmentType[].class);
            case DELETE, RESET -> CollectionUtils.array();
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemType[] source = getExpr().getArray(event);

        EnchantmentType[] enchants;
        if ((mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) && delta == null) {
            enchants = null;
        } else {
            enchants = ItemUtils.asEnchantmentTypes(delta);
        }

        switch (mode) {
            case ADD -> {
                for (ItemType item : source)
                    ItemUtils.addStoredEnchantments(item, enchants);
            }
            case REMOVE, REMOVE_ALL -> {
                for (ItemType item : source)
                    ItemUtils.removeStoredEnchantments(item, enchants);
            }
            case SET, DELETE, RESET -> {
                for (ItemType item : source)
                    ItemUtils.setStoredEnchantments(item, enchants);
            }
        }
    }
}