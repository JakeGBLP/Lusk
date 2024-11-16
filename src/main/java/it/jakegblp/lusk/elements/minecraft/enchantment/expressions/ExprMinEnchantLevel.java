package it.jakegblp.lusk.elements.minecraft.enchantment.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enchantment - Minimum Level")
@Description("Returns the Minimum Vanilla Level for an Enchantment.")
@Examples({"broadcast minimum enchantment level of protection"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class ExprMinEnchantLevel extends SimplePropertyExpression<Enchantment, Integer> {
    static {
        register(ExprMinEnchantLevel.class, Integer.class, "[vanilla] min[imum] enchant[ment] level", "enchantments");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Nullable
    public Integer convert(Enchantment enchantment) {
        return enchantment.getStartLevel();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "vanilla minimum enchantment level";
    }
}