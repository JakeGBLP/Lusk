package it.jakegblp.lusk.elements.minecraft.item.types;

import it.jakegblp.lusk.api.GenericRelation;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerComparator;

@SuppressWarnings("unused")
public class ItemComparators {
    static {
        registerComparator(ItemStack.class, ItemRarity.class, (itemStack, itemRarity) ->
                itemStack.getItemMeta().getRarity().equals(itemRarity) ? GenericRelation.EQUAL
                        : GenericRelation.NOT_EQUAL);
    }
}
