package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.BooleanMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntityCustomNameVisibility extends BooleanMetadataEffect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        register(syntaxRegistry, EffMetadataEntityCustomNameVisibility.class, EffMetadataEntityCustomNameVisibility::new, "custom name visibility");
    }

    @Override
    public MetadataKey<? extends Entity, Boolean> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.CUSTOM_NAME_VISIBILITY;
    }

    @Override
    public String getMetadataPropertyName() {
        return "custom name visibility";
    }

}
