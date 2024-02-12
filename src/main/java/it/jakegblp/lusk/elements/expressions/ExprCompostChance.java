package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item - Compost Chance")
@Description("Returns an item's chance of successfully composting.")
@Examples({""})
@Since("1.0.1")
public class ExprCompostChance extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprCompostChance.class, Integer.class, ExpressionType.COMBINED,
                "[the] compost[ing] chance of %itemtype%");
    }

    private Expression<ItemType> itemType;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        itemType = (Expression<ItemType>) exprs[0];
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        ItemType i = itemType.getSingle(e);
        if (i != null) {
            if (Utils.isCompostable(i.getMaterial())) {
                return new Integer[]{Utils.getCompostChance(i.getMaterial())};
            }
        }
        return new Integer[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        assert e != null;
        return "the composting chance of  " + itemType.getSingle(e);
    }
}
