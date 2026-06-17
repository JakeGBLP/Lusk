package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleExpandedBooleanMetadataEffect;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntitySilent extends SimpleExpandedBooleanMetadataEffect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        register(syntaxRegistry, EffMetadataEntitySilent.class, EffMetadataEntitySilent::new, "silent", "silence");
    }

    @Override
    public MetadataKey<? extends Entity, Boolean> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.SILENT;
    }

    @Override
    public String getMakePropertyName() {
        return "silent";
    }

    @Override
    public String getStatePropertyName() {
        return "silence";
    }
}
