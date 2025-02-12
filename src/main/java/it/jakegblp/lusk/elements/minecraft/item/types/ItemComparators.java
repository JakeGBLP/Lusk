package it.jakegblp.lusk.elements.minecraft.item.types;

import it.jakegblp.lusk.api.enums.GenericRelation;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import static it.jakegblp.lusk.utils.SkriptUtils.registerComparator;
import static it.jakegblp.lusk.utils.Constants.SPIGOT_HAS_ITEM_RARITY;

@SuppressWarnings("unused")
public class ItemComparators {
    static {
        if (SPIGOT_HAS_ITEM_RARITY) {
            registerComparator(ItemStack.class, ItemRarity.class, (itemStack, itemRarity) ->
                    itemStack.getItemMeta().getRarity().equals(itemRarity) ? GenericRelation.EQUAL
                            : GenericRelation.NOT_EQUAL);
        }
    }

    public ItemComparators() {}
}
