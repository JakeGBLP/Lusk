package it.jakegblp.lusk.skript.modules.packets.effects.metadata.display;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataDisplayHeight extends SimpleMetadataEffect<Float> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataDisplayHeight.class, EffMetadataDisplayHeight::new, "display height", "number");
    }

    @Override
    public MetadataKey<? extends Entity, Float> getMetadataKey() {
        return metadataKeyRegistry().DISPLAY.DISPLAY_HEIGHT;
    }

    @Override
    public String getMetadataPropertyName() {
        return "display height";
    }
}
