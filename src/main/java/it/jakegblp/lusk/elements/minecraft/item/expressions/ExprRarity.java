package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_SPIGOT_ITEM_RARITY;

@Name("Item - Rarity")
@Description("Returns the rarity of an item.\nCan be set.\n\nBefore Lusk 1.2 (and Minecraft 1.20.5), this expression returned strings and also worked for enchantments, due to some major changes enchantments no longer have a rarity.")
@Examples({"broadcast item rarity of tool", "set item rarity of {_sword} to epic"})
@Since("1.0.0+, 1.2+ (ItemRarity)")
@SuppressWarnings("unused")
public class ExprRarity extends SimpleExpression<ItemRarity> {
    static {
        // TODO: PROPERTY EXPR
        if (HAS_SPIGOT_ITEM_RARITY) {
            Skript.registerExpression(ExprRarity.class, ItemRarity.class, ExpressionType.PROPERTY,
                    "item rarity of %itemtype%",
                    "%itemtype%'[s] item rarity");
        }
    }

    private Expression<ItemType> item;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        item = (Expression<ItemType>) exprs[0];
        return true;
    }

    @Override
    protected ItemRarity @NotNull [] get(@NotNull Event e) {
        ItemType itemType = item.getSingle(e);
        if (itemType != null) {
            ItemMeta meta = itemType.getItemMeta();
            if (meta.hasRarity()) {
                return new ItemRarity[]{meta.getRarity()};
            }
        }
        return new ItemRarity[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemRarity> getReturnType() {
        return ItemRarity.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{ItemRarity.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        ItemType itemType = item.getSingle(e);
        if (itemType != null) {
            if (delta[0] instanceof ItemRarity itemRarity) {
                ItemMeta meta = itemType.getItemMeta();
                meta.setRarity(itemRarity);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "item rarity of " + (e == null ? "" : item.toString(e, debug));
    }
}
