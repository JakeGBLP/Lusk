package it.jakegblp.lusk.elements.minecraft.items.item.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.bukkit.inventory.ItemRarity;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.inventory.ItemRarity") && Classes.getExactClassInfo(ItemRarity.class) == null) {
            EnumWrapper<ItemRarity> ITEMRARITY_ENUM = new EnumWrapper<>(ItemRarity.class);
            Classes.registerClass(ITEMRARITY_ENUM.getClassInfo("itemrarity")
                    .user("item ?rarit(y|ies)")
                    .name("Item Rarity")
                    .description("""
                            The Rarity of an item.
                            
                            COMMON = White
                            UNCOMMON = Yellow
                            RARE = Aqua
                            EPIC = Light Purple
                            """)
                    .examples("item rarity of dragon egg")
                    .since("1.2"));
        }
    }
}
