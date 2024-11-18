package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Item - Blank")
@Description("Returns the given items with everything hidden. The return of the famous \"with all flags hidden\"")
@Examples({"set {_blank} to blank tool", "set slot 0 of {_gui} to black glass pane named \"&0\" with all flags hidden"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBlank extends SimpleExpression<ItemStack> {

    static {
        Skript.registerExpression(ExprBlank.class, ItemStack.class, ExpressionType.COMBINED,
                "blank %itemstacks%", "%itemstacks% with (all flags|everything) hidden");
    }

    Expression<ItemStack> itemStackExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemStackExpression = (Expression<ItemStack>) expressions[0];
        return true;
    }
    @Nullable
    @Override
    protected ItemStack[] get(Event event) {
        return itemStackExpression.stream(event).peek(itemStack -> itemStack.addItemFlags(ItemFlag.values())).toArray(ItemStack[]::new);
    }

    @Override
    public boolean isSingle() {
        return itemStackExpression.isSingle();
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "blank "+itemStackExpression.toString(event, debug);
    }
}
