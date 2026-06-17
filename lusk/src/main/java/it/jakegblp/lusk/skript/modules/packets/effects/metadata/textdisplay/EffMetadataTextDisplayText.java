package it.jakegblp.lusk.skript.modules.packets.effects.metadata.textdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataTextDisplayText extends SimpleMetadataEffect<BlockData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataTextDisplayText.class, EffMetadataTextDisplayText::new, "[display[ed]] block[[ |-]data]", "blockdata");
    }

    @Override
    public MetadataKey<? extends Entity, BlockData> getMetadataKey() {
        return metadataKeyRegistry().BLOCK_DISPLAY.DISPLAY_BLOCK;
    }

    @Override
    public String getMetadataPropertyName() {
        return "displayed block data";
    }
}
