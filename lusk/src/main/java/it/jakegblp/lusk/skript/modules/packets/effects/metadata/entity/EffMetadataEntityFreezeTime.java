package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleTimeMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntityFreezeTime extends SimpleTimeMetadataEffect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        register(syntaxRegistry, EffMetadataEntityFreezeTime.class, EffMetadataEntityFreezeTime::new,
                "freeze ticks",
                "freeze time");
    }

    public EffMetadataEntityFreezeTime(String firstTypePropertyName, String secondTypePropertyName) {
        super(firstTypePropertyName, secondTypePropertyName);
    }

    @Override
    public MetadataKey<? extends Entity, Integer> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.TICKS_FROZEN;
    }

}
