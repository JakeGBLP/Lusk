package it.jakegblp.lusk.nms.core.adapters;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;

public interface AdventureAdapter<
        NMSNamespacedKey,
        NMSComponent> {

    NMSNamespacedKey asNMSNamespacedKey(NamespacedKey key);

    NamespacedKey asNamespacedKey(NMSNamespacedKey nmsNamespacedKey);

    NMSComponent asNMSComponent(Component component);

    Component asComponent(NMSComponent nmsComponent);

}
