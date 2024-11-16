package it.jakegblp.lusk.elements.minecraft.enchantment.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enchantment - Maximum Level")
@Description("Returns the Maximum Vanilla Level for an Enchantment.")
@Examples({"broadcast max level of sharpness"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class ExprMaxEnchantLevel extends SimplePropertyExpression<Enchantment, Integer> {
    static {
        register(ExprMaxEnchantLevel.class, Integer.class, "[vanilla] max[imum] enchant[ment] level", "enchantments");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Nullable
    public Integer convert(Enchantment enchantment) {
        return enchantment.getMaxLevel();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "vanilla maximum enchantment level";
    }
}