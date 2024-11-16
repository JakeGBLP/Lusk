package it.jakegblp.lusk.elements.minecraft.item.expressions;

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
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item - Unenchanted/Disenchanted")
@Description("Returns one or more items without enchantments.")
@Examples({"set tool to unenchanted tool\n# removes all the enchantments"})
@Since("1.0.2, 1.3 (plural)")
@SuppressWarnings("unused")
public class ExprUnenchanted extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprUnenchanted.class, ItemType.class, ExpressionType.COMBINED,
                "unenchanted %itemtypes%", "disenchanted %itemtypes%");
    }

    private Expression<ItemType> itemTypeExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        itemTypeExpression = (Expression<ItemType>) exprs[0];
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        return ItemUtils.unenchant(itemTypeExpression.getAll(e));
    }

    @Override
    public boolean isSingle() {
        return itemTypeExpression.isSingle();
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "unenchanted " + itemTypeExpression.toString(e, debug);
    }
}
