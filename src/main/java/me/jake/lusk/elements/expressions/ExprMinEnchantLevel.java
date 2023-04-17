package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enchantment - Minimum Level")
@Description("Returns the Minimum Vanilla Level for an Enchantment.\nThere really isn't much of a reason to use this expression as every enchantment starts at 0.. \n\nUnless Mojang wants to prove me wrong.")
@Examples({"broadcast minimum enchantment level of protection"})
@Since("1.0.0")
public class ExprMinEnchantLevel extends SimplePropertyExpression<Enchantment, Integer> {
    static {
        register(ExprMinEnchantLevel.class, Integer.class, "min[imum] [enchant[ment]] level", "enchantment");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Nullable
    public Integer convert(Enchantment enchantment) {
        if (enchantment != null) {
            return enchantment.getStartLevel();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "min enchantment level";
    }
}