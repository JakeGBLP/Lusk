package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleTimeMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntityFreezeTime extends SimpleTimeMetadataEffect {

    public EffMetadataEntityFreezeTime(String firstTypePropertyName, String secondTypePropertyName) {
        super(firstTypePropertyName, secondTypePropertyName);
    }

    public static void register(SyntaxRegistry syntaxRegistry) {
        register(syntaxRegistry, EffMetadataEntityFreezeTime.class, EffMetadataEntityFreezeTime::new,
                "freeze ticks",
                "freeze time");
    }

    @Override
    public EntityDataKey<? extends Entity, Integer, ?, ?, ?> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.TICKS_FROZEN;
    }

}
