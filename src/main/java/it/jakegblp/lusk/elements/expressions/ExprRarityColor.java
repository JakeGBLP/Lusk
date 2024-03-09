package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item Rarity Color")
@Description("Returns the color of an item's rarity.\nSome enchantment rarities happen to be named the same as some item rarities, this expression is solely for Item Rarities.")
@Examples({"broadcast rarity color of rarity of tool\nbroadcast rarity color of rarity of heart of the sea"})
@Since("1.0.0")
public class ExprRarityColor extends SimpleExpression<SkriptColor> {
    static {
        Skript.registerExpression(ExprRarityColor.class, SkriptColor.class, ExpressionType.SIMPLE,
                "rarity color of %string%");
    }

    private Expression<String> color;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        color = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected SkriptColor @NotNull [] get(@NotNull Event e) {
        String c = color.getSingle(e);
        if (Constants.rarityColors.containsKey(c)) return new SkriptColor[]{Constants.rarityColors.get(c)};
        return new SkriptColor[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends SkriptColor> getReturnType() {
        return SkriptColor.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "rarity color of " + (e == null ? "" : color.toString(e,debug));
    }
}