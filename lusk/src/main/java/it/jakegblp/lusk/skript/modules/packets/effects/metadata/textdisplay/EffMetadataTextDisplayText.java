package it.jakegblp.lusk.skript.modules.packets.effects.metadata.textdisplay;

import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataTextDisplayText extends SimpleMetadataEffect<Component> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataTextDisplayText.class, EffMetadataTextDisplayText::new, "[display[ed]] text [component]", "textcomponent");
    }

    @Override
    public EntityDataKey<? extends Entity, Component, ?, ?, ?> getMetadataKey() {
        return metadataKeyRegistry().TEXT_DISPLAY.TEXT;
    }

    @Override
    public String getMetadataPropertyName() {
        return "displayed text";
    }
}
