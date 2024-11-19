package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

@Name("Item - Preferred Enchantments")
@Description("Returns the Enchantments this item can normally have.")
@Examples({"broadcast preferred enchantments of tool"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprPreferredEnchantments extends PropertyExpression<ItemStack, Enchantment> {

    static {
        register(ExprPreferredEnchantments.class,Enchantment.class, "(preferred|supported) enchantments", "itemstacks");
    }

    @Override
    protected Enchantment[] get(Event event, ItemStack[] source) {
        return Arrays.stream(source).flatMap(itemStack -> Stream.of(ItemUtils.getPreferredEnchantments(itemStack))).toArray(Enchantment[]::new);
    }

    @Override
    public Class<? extends Enchantment> getReturnType() {
        return Enchantment.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "preferred enchantments of " + getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemStack>) expressions[0]);
        return true;
    }
}
//public class ExprPreferredEnchantments extends SimplePropertyExpression<Enchantment, Enchantment> {
//    static {
//        register(ExprPreferredEnchantments.class, Integer.class, "[vanilla] max[imum] enchant[ment] level", "enchantments");
//    }
//
//    @Override
//    public @NotNull Class<? extends Integer> getReturnType() {
//        return Integer.class;
//    }
//
//    @Nullable
//    public Integer convert(Enchantment enchantment) {
//        return enchantment.getMaxLevel();
//    }
//
//    @Override
//    protected @NotNull String getPropertyName() {
//        return "vanilla maximum enchantment level";
//    }
//}