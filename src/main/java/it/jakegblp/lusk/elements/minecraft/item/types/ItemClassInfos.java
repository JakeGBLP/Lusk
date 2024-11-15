package it.jakegblp.lusk.elements.minecraft.item.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.bukkit.inventory.ItemRarity;

@SuppressWarnings("unused")
public class ItemClassInfos {
    static {
        if (Skript.classExists("org.bukkit.inventory.ItemRarity") && Classes.getExactClassInfo(ItemRarity.class) == null) {
            EnumWrapper<ItemRarity> ITEM_RARITY_ENUM = new EnumWrapper<>(ItemRarity.class);
            Classes.registerClass(ITEM_RARITY_ENUM.getClassInfo("itemrarity")
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
                    .documentationId("11914")
                    .since("1.2"));
        }
    }
}
