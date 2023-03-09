package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

@Name("Can Be Enchanted")
@Description("Checks if an item can be enchanted with an Enchantment")
@Examples({"if tool of player can be enchanted with sharpness:"})
@Since("1.0.0")
public class CondBeEnchanted extends Condition {

    static {
        Skript.registerCondition(CondBeEnchanted.class, "%itemstack% can be enchanted with %enchantment%",
                                                                "%itemstack% can(n't|not) be enchanted with %enchantment%");
    }

    private Expression<ItemStack> item;
    private Expression<Enchantment> enchantment;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        item = (Expression<ItemStack>) expressions[0];
        enchantment = (Expression<Enchantment>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        assert event != null;
        return Objects.requireNonNull(item.getSingle(event)) + " can" + (isNegated() ? "'t" : "") + " be enchanted with " + Objects.requireNonNull(enchantment.getSingle(event));
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ Objects.requireNonNull(enchantment.getSingle(event)).canEnchantItem(Objects.requireNonNull(item.getSingle(event)));
    }
}