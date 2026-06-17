package it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataEntityPose extends SimpleMetadataEffect<Pose> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataEntityPose.class, EffMetadataEntityPose::new, "pose", "pose");
    }

    @Override
    public MetadataKey<? extends Entity, Pose> getMetadataKey() {
        return metadataKeyRegistry().ENTITY.POSE;
    }

    @Override
    public String getMetadataPropertyName() {
        return "pose";
    }

}
