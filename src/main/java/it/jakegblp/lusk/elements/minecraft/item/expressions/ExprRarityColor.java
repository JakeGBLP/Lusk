package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.SkriptColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.SPIGOT_HAS_ITEM_RARITY;

@Name("Item Rarity - Color")
@Description("Returns the color of an item's rarity.\nCan be used with the item itself and it will get its rarity's color without getting the rarity itself if you don't need it.")
@Examples({"broadcast rarity color of tool", "broadcast rarity color of item rarity of heart of the sea"})
@Since("1.0.0, 1.2 (ItemRarity)")
@RequiredPlugins("1.20.5")
@SuppressWarnings("unused")
public class ExprRarityColor extends SimplePropertyExpression<Object, SkriptColor> {
    static {
        if (SPIGOT_HAS_ITEM_RARITY) {
            register(ExprRarityColor.class, SkriptColor.class, "rarity color", "itemrarities/itemtypes");
        }
    }

    @Override
    public @Nullable SkriptColor convert(Object from) {
        if (from instanceof ItemType itemType) {
            ItemMeta meta = itemType.getItemMeta();
            if (meta.hasRarity()) {
                from = meta.getRarity();
            } else {
                return null;
            }
        }
        if (from instanceof ItemRarity itemRarity) {
            TextColor textColor = itemRarity.color();
            return SkriptColor.fromBukkitColor(Color.fromRGB(textColor.red(), textColor.green(), textColor.blue()));
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "rarity color";
    }

    @Override
    public @NotNull Class<? extends SkriptColor> getReturnType() {
        return SkriptColor.class;
    }
}