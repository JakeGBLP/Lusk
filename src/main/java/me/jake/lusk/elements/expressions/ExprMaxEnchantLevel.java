package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Max Enchantment Level")
@Description("Returns the Maximum Vanilla Level for an Enchantment.")
@Examples({"broadcast max level of sharpness"})
@Since("1.0.0")
public class ExprMaxEnchantLevel extends SimplePropertyExpression<Enchantment, Integer> {
    static {
        register(ExprMaxEnchantLevel.class, Integer.class, "max[imum] [enchant[ment]] level", "enchantment");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Nullable
    public Integer convert(Enchantment enchantment) {
        if (enchantment != null) {
            return enchantment.getMaxLevel();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "max enchant level";
    }
}