package it.jakegblp.lusk.utils;

import ch.njol.skript.aliases.ItemType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.Constants.HAS_REMOVE_ENCHANTMENTS_METHOD;

public class ItemUtils {

    @Nullable
    public static ItemMeta getItemMetaFromUnknown(Object object) {
        if (object instanceof ItemStack itemStack) {
            return itemStack.getItemMeta();
        } else if (object instanceof ItemType itemType) {
            return itemType.getItemMeta();
        } else if (object instanceof ItemMeta itemMeta) {
            return itemMeta;
        }
        return null;
    }

    public static final Function<ItemStack, ItemStack> UNENCHANT = HAS_REMOVE_ENCHANTMENTS_METHOD ? itemStack -> {
        itemStack.removeEnchantments();
        return itemStack;
    } : itemStack -> {
        for (Enchantment enchantment : itemStack.getEnchantments().keySet())
            itemStack.removeEnchantment(enchantment);
        return itemStack;
    };

    /**
     * Filters a list of materials and returns an array of all the materials that can become items.
     *
     * @param materials the materials to filter
     * @return A stream of materials that can become items.
     * @see Material#isItem()
     */
    private static Stream<Material> validMaterials(@NotNull Material... materials) {
        return Arrays.stream(materials).filter(Material::isItem);
    }

    public static ItemStack[] unenchant(@NotNull ItemStack... itemStacks) {
        return Arrays.stream(itemStacks).map(UNENCHANT).toArray(ItemStack[]::new);
    }

    public static ItemType[] unenchant(@NotNull ItemType... itemTypes) {
        return Arrays.stream(itemTypes)
                .map(itemType -> {
                    ItemStack itemStack = itemType.getRandom();
                    if (itemStack != null) {
                        ItemStack unenchantItemStack = UNENCHANT.apply(itemStack);
                        return new ItemType(unenchantItemStack);
                    }
                    return null;
                })
                .toArray(ItemType[]::new);
    }


    public static ItemStack[] toItemStacks(@NotNull Material... materials) {
        return validMaterials(materials).map(ItemStack::new).toArray(ItemStack[]::new);
    }

    public static ItemType[] toItemTypes(@NotNull Material... materials) {
        return validMaterials(materials).map(ItemType::new).toArray(ItemType[]::new);
    }

    public static Enchantment[] getPreferredEnchantments(@NotNull ItemStack itemStack) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .filter(enchantment -> enchantment.canEnchantItem(itemStack)).toArray(Enchantment[]::new);
    }
}
