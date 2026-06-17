package it.jakegblp.lusk.skript.modules.packets.effects.metadata.itemdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataItemDisplayItem extends SimpleMetadataEffect<ItemStack> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataItemDisplayItem.class, EffMetadataItemDisplayItem::new, "[display[ed]] item", "itemstack");
    }

    @Override
    public MetadataKey<? extends Entity, ItemStack> getMetadataKey() {
        return metadataKeyRegistry().ITEM_DISPLAY.DISPLAY_ITEM;
    }

    @Override
    public String getMetadataPropertyName() {
        return "item";
    }
}
