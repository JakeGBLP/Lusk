package it.jakegblp.lusk.nms.core.protocol;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PacketType(PacketFlow packetFlow, NamespacedKey key) {
}
