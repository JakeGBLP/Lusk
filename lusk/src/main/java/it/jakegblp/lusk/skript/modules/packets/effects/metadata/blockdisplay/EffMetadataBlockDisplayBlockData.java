package it.jakegblp.lusk.skript.modules.packets.effects.metadata.blockdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataBlockDisplayBlockData extends SimpleMetadataEffect<Component> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataBlockDisplayBlockData.class, EffMetadataBlockDisplayBlockData::new, "[display[ed]] text [component]", "textcomponent");
    }

    @Override
    public MetadataKey<? extends Entity, Component> getMetadataKey() {
        return metadataKeyRegistry().TEXT_DISPLAY.TEXT;
    }

    @Override
    public String getMetadataPropertyName() {
        return "displayed text";
    }
}
