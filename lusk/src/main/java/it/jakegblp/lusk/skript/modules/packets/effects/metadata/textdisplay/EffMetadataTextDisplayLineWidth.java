package it.jakegblp.lusk.skript.modules.packets.effects.metadata.textdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataTextDisplayLineWidth extends SimpleMetadataEffect<Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataTextDisplayLineWidth.class, EffMetadataTextDisplayLineWidth::new, "line width", "integer");
    }

    @Override
    public MetadataKey<? extends Entity, Integer> getMetadataKey() {
        return metadataKeyRegistry().TEXT_DISPLAY.LINE_WIDTH;
    }

    @Override
    public String getMetadataPropertyName() {
        return "line width";
    }
}
