package it.jakegblp.lusk.elements.minecraft.item.types;

import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.comparator.Comparator;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import static it.jakegblp.lusk.utils.Constants.HAS_SPIGOT_ITEM_RARITY;

@SuppressWarnings("unused")
public class ItemComparators {
    static {
        if (HAS_SPIGOT_ITEM_RARITY) {
            Comparators.registerComparator(ItemStack.class, ItemRarity.class, new Comparator<>() {
                @Override
                public @NotNull Relation compare(ItemStack itemStack, ItemRarity itemRarity) {
                    return itemStack.getItemMeta().getRarity().equals(itemRarity) ? Relation.EQUAL : Relation.NOT_EQUAL;
                }

                @Override
                public boolean supportsOrdering() {
                    return true;
                }
            });
        }
    }

    public ItemComparators() {}
}
