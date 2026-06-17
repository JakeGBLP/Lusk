package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleBooleanVerbMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntitySprinting extends SimpleBooleanVerbMetadataEffect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerVerb(syntaxRegistry, EffMetadataEntitySprinting.class, EffMetadataEntitySprinting::new, "sprint");
    }

    @Override
    public MetadataKey<? extends Entity, Boolean> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.SPRINTING;
    }

    @Override
    public String getMakePropertyName() {
        return "sprint";
    }
}
