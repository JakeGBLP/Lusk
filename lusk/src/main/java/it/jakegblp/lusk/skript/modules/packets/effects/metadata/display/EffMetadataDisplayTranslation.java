package it.jakegblp.lusk.skript.modules.packets.effects.metadata.display;

import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataDisplayTranslation extends SimpleMetadataEffect<Vector> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataDisplayTranslation.class, EffMetadataDisplayTranslation::new, "(display|[display] transformation) translation", "vector");
    }

    @Override
    public EntityDataKey<? extends Entity, Vector, ?, ?, ?> getMetadataKey() {
        return metadataKeyRegistry().DISPLAY.TRANSLATION;
    }

    @Override
    public String getMetadataPropertyName() {
        return "display translation";
    }
}
