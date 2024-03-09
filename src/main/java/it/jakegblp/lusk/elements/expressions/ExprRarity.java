package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item/Enchantment Rarity")
@Description("Returns the rarity of an item/enchantment")
@Examples({"broadcast rarity of tool\nbroadcast rarity of mending"})
@Since("1.0.0")
public class ExprRarity extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprRarity.class, String.class, ExpressionType.SIMPLE,
                "rarity of %enchantment/itemtype%",
                "%enchantment/itemtype%'[s] rarity");
    }

    private Expression<Object> object;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        object = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        Object o = object.getSingle(e);
        String string;
        if (o instanceof ItemType) {
            string = new ItemStack(((ItemType) o).getMaterial()).getRarity().toString();
        } else if (o instanceof Enchantment) {
            string = ((Enchantment) o).getRarity().toString();
        } else {
            return new String[]{};
        }
        return new String[]{string};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "rarity of " + (e == null ? "" : object.toString(e,debug));
    }
}
