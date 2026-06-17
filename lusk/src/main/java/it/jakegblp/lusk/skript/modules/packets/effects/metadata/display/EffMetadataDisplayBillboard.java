package it.jakegblp.lusk.skript.modules.packets.effects.metadata.display;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.effect.SimpleMetadataEffect;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffMetadataDisplayBillboard extends SimpleMetadataEffect<Display.Billboard> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        registerSimple(syntaxRegistry, EffMetadataDisplayBillboard.class, EffMetadataDisplayBillboard::new, "billboard [setting]", "billboard");
    }

    @Override
    public MetadataKey<? extends Entity, Display.Billboard> getMetadataKey() {
        return metadataKeyRegistry().DISPLAY.BILLBOARD;

    }

    @Override
    public String getMetadataPropertyName() {
        return "billboard";
    }
}
