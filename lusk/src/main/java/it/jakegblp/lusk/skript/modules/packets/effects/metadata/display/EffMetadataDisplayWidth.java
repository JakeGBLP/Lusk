package it.jakegblp.lusk.skript.modules.packets.effects.metadata.display;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataDisplayWidth extends SimpleMetadataEffect<Float> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataDisplayWidth.class, EffMetadataDisplayWidth::new, "display width", "number");
    }

    @Override
    public MetadataKey<? extends Entity, Float> getMetadataKey() {
        return metadataKeyRegistry().DISPLAY.DISPLAY_WIDTH;
    }

    @Override
    public String getMetadataPropertyName() {
        return "display width";
    }
}
