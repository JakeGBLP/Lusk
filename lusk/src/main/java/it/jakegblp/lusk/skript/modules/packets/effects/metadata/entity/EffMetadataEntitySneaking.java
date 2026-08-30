package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleBooleanVerbMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntitySneaking extends SimpleBooleanVerbMetadataEffect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerVerb(syntaxRegistry, EffMetadataEntitySneaking.class, EffMetadataEntitySneaking::new, "sneak");
    }

    @Override
    public EntityDataKey<? extends Entity, Boolean, ?, ?, ?> getMetadataKey(){
        return metadataKeyRegistry().ENTITY.SNEAKING;
    }

    @Override
    public String getMakePropertyName() {
        return "sneak";
    }
}
