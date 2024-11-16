package it.jakegblp.lusk.elements.minecraft.item.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item - Can Be Enchanted with")
@Description("Checks if an item can be enchanted with an Enchantment.\nThis does not check if the enchantment conflicts with any enchantments already applied on the item.")
@Examples({"if tool of player can be enchanted with sharpness:"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class CondCanBeEnchanted extends Condition {
    static {
        Skript.registerCondition(CondCanBeEnchanted.class,
                "%itemtype% can be enchanted with %enchantment%",
                "%itemtype% can('|no)t be enchanted with %enchantment%");
    }

    private Expression<ItemType> item;
    private Expression<Enchantment> enchantment;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        item = (Expression<ItemType>) expressions[0];
        enchantment = (Expression<Enchantment>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        boolean e = event != null;
        return (e ? item.toString(event, debug) : "") + " can" + (isNegated() ? "'t" : "") + " be enchanted with " + (e ? enchantment.toString(event, debug) : "");
    }

    @Override
    public boolean check(@NotNull Event event) {
        Enchantment e = enchantment.getSingle(event);
        if (e == null) return false;
        ItemType i = item.getSingle(event);
        if (i == null) return false;
        // todo: null check
        return isNegated() ^ e.canEnchantItem(i.getRandom());
    }
}