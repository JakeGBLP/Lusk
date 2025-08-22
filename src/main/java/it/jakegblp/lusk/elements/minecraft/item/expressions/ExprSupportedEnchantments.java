package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

@Name("Item - Supported/Preferred Enchantments")
@Description("Returns the Enchantments this item can normally have.")
@Examples({"broadcast supported enchantments of tool"})
@Since("1.3, 1.3.9 (Fixed Plurality)")
@SuppressWarnings("unused")
public class ExprSupportedEnchantments extends PropertyExpression<ItemStack, Enchantment> {

    static {
        register(ExprSupportedEnchantments.class,Enchantment.class, "(preferred|supported) enchantments", "itemstacks");
    }

    @Override
    protected Enchantment[] get(Event event, ItemStack[] source) {
        return Arrays.stream(source).flatMap(itemStack -> Stream.of(ItemUtils.getSupportedEnchantments(itemStack))).toArray(Enchantment[]::new);
    }

    @Override
    public Class<? extends Enchantment> getReturnType() {
        return Enchantment.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "supported enchantments of " + getExpr().toString(event, debug);
    }

    @Override
    public boolean isSingle() {
        return false; // getExpr() instanceof Literal<? extends ItemStack> literal && literal.isSingle() && literal.getSingle().getEnchantments().size() <= 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemStack>) expressions[0]);
        return true;
    }
}