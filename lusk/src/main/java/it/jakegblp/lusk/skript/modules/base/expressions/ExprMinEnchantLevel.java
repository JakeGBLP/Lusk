package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Enchantment - Minimum Level")
@Description("Returns the Minimum Vanilla Level for an Enchantment.")
@Example("broadcast minimum enchantment level of protection")
@Since({"1.0.0", "2.0.0 (Pattern Change)"})
public class ExprMinEnchantLevel extends SimplePropertyExpression<Enchantment, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprMinEnchantLevel.class, ExprMinEnchantLevel::new, Integer.class)
                .propertyOf("min[imum] enchant[ment] level", "enchantments")
                .register(syntaxRegistry);
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    @Nullable
    public Integer convert(Enchantment enchantment) {
        return enchantment.getStartLevel();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "minimum enchantment level";
    }
}