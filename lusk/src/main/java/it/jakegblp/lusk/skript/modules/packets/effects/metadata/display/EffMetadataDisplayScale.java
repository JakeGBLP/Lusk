package it.jakegblp.lusk.skript.modules.packets.effects.metadata.display;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataDisplayScale extends SimpleMetadataEffect<Vector> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataDisplayScale.class, EffMetadataDisplayScale::new, "(display|[display] transformation) scale", "vector");
    }

    @Override
    public MetadataKey<? extends Entity, Vector> getMetadataKey() {
        return metadataKeyRegistry().DISPLAY.SCALE;
    }

    @Override
    public String getMetadataPropertyName() {
        return "display scale";
    }
}
