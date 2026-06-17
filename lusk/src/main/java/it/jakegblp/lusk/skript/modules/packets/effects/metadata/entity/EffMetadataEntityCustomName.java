package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Optional;

public class EffMetadataEntityCustomName extends SimpleMetadataEffect<Optional<Component>> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataEntityCustomName.class, EffMetadataEntityCustomName::new, "custom name", "textcomponent");
    }

    @Override
    public MetadataKey<? extends Entity, Optional<Component>> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.CUSTOM_NAME;
    }

    @Override
    public String getMetadataPropertyName() {
        return "custom name";
    }

}
