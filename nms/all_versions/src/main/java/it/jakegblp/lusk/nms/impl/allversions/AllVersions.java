package it.jakegblp.lusk.nms.impl.allversions;

import io.papermc.paper.adventure.PaperAdventure;
import it.jakegblp.lusk.nms.core.adapters.AdventureAdapter;
import net.kyori.adventure.key.Key;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;

public class AllVersions implements AdventureAdapter<
        ResourceLocation,
        Component
        > {
    @Override
    public ResourceLocation asNMSNamespacedKey(NamespacedKey key) {
        return PaperAdventure.asVanillaNullable(key);
    }

    @Override
    public NamespacedKey asNamespacedKey(ResourceLocation resourceLocation) {
        Key key = PaperAdventure.asAdventure(resourceLocation);
        return new NamespacedKey(key.namespace(), key.value());
    }

    @Override
    public Component asNMSComponent(net.kyori.adventure.text.Component component) {
        return PaperAdventure.asVanilla(component);
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(Component component) {
        return PaperAdventure.asAdventure(component);
    }
}
