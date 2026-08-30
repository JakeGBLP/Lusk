package it.jakegblp.lusk.skript.modules.packets.effects.metadata.blockdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataBlockDisplayBlockData extends SimpleMetadataEffect<BlockData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataBlockDisplayBlockData.class, EffMetadataBlockDisplayBlockData::new, "[display[ed]] block[[ |-]data]", "blockdata");
    }

    @Override
    public EntityDataKey<? extends Entity, BlockData, ?, ?, ?> getMetadataKey(){
        return metadataKeyRegistry().BLOCK_DISPLAY.DISPLAY_BLOCK;
    }

    @Override
    public String getMetadataPropertyName() {
        return "displayed block data";
    }
}
