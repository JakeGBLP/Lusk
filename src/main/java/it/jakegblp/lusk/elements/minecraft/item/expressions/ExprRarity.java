package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.SPIGOT_HAS_ITEM_RARITY;

@Name("Item - Rarity")
@Description("Returns the rarity of an item.\nCan be set.\n\nBefore Lusk 1.2 (and Minecraft 1.20.5), this expression returned strings and also worked for enchantments, due to some major changes enchantments no longer have a rarity.")
@Examples({"broadcast item rarity of tool", "set item rarity of {_sword} to epic"})
@Since("1.0.0, 1.2 (ItemRarity)")
@RequiredPlugins("1.20.5")
@SuppressWarnings("unused")
public class ExprRarity extends SimplePropertyExpression<ItemType,ItemRarity> {
    //todo: add tests
    static {
        if (SPIGOT_HAS_ITEM_RARITY) {
            register(ExprRarity.class, ItemRarity.class, "item rarity", "itemtypes");
        }
    }

    @Override
    public @Nullable ItemRarity convert(ItemType from) {
        ItemMeta itemMeta = from.getItemMeta();
        if (itemMeta.hasRarity()) return itemMeta.getRarity();
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "item rarity";
    }

    @Override
    public Class<? extends ItemRarity> getReturnType() {
        return ItemRarity.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {ItemRarity.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof ItemRarity itemRarity) {
            for (ItemType itemType : getExpr().getArray(event)) {
                ItemMeta itemMeta = itemType.getItemMeta();
                itemMeta.setRarity(itemRarity);
                itemType.setItemMeta(itemMeta);
            }
        }
    }
}