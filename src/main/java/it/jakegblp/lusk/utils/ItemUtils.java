package it.jakegblp.lusk.utils;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.EnchantmentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Objects;
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

    @Nullable
    public static ItemType getNullableItemType(ItemStack itemStack) {
        if (itemStack == null) return null;
        return new ItemType(itemStack);
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

    public static Enchantment[] getSupportedEnchantments(@NotNull ItemStack itemStack) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .filter(enchantment -> enchantment.canEnchantItem(itemStack)).toArray(Enchantment[]::new);
    }

    @Nullable
    public static ItemStack getSingleItemTypeToItemStack(@Nullable Expression<ItemType> expression, @NotNull Event event) {
        if (expression == null) return null;
        ItemType itemType = expression.getSingle(event);
        if (itemType == null) return null;
        return itemType.getRandom();
    }

    @NullMarked
    public static void addStoredEnchantments(ItemType itemType, EnchantmentType... enchantmentTypes) {
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemType.getItemMeta();
        for (EnchantmentType enchantmentType : enchantmentTypes) {
            if (enchantmentType.getType() != null) {
                itemType.clearEnchantments();
                enchantmentStorageMeta.addStoredEnchant(enchantmentType.getType(), enchantmentType.getLevel(), true);
            }
        }
        itemType.setItemMeta(enchantmentStorageMeta);
    }

    @NullMarked
    public static void removeStoredEnchantments(ItemType itemType, EnchantmentType... enchantmentTypes) {
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemType.getItemMeta();
        for (EnchantmentType enchantmentType : enchantmentTypes) {
            if (enchantmentType.getType() != null) {
                enchantmentStorageMeta.removeStoredEnchant(enchantmentType.getType());
            }
        }
        itemType.setItemMeta(enchantmentStorageMeta);
    }

    @NotNull
    public static EnchantmentType[] getStoredEnchantments(@NotNull ItemType itemType) {
        return ((EnchantmentStorageMeta) itemType.getItemMeta())
                .getStoredEnchants()
                .entrySet()
                .stream()
                .map(entry -> new EnchantmentType(entry.getKey(), entry.getValue()))
                .toArray(EnchantmentType[]::new);
    }

    @NullMarked
    public static void setStoredEnchantments(ItemType itemType,  EnchantmentType @Nullable ... enchantmentTypes) {
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemType.getItemMeta();
        if (enchantmentStorageMeta.hasStoredEnchants()) {
            enchantmentStorageMeta.getStoredEnchants().keySet().forEach(enchantmentStorageMeta::removeStoredEnchant);
        }
        if (enchantmentTypes != null) {
            for (EnchantmentType enchantmentType : enchantmentTypes) {
                if (enchantmentType.getType() != null) {
                    enchantmentStorageMeta.addStoredEnchant(enchantmentType.getType(), enchantmentType.getLevel(), true);
                }
            }
        }
    }

    public static EnchantmentType[] asEnchantmentTypes(Object[] objects) {
        return Arrays.stream(objects).map(object -> {
            if (object instanceof EnchantmentType enchantmentType) {
                return enchantmentType;
            } else if (object instanceof Enchantment enchantment) {
                return new EnchantmentType(enchantment);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).toArray(EnchantmentType[]::new);
    }
}
