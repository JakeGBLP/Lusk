package it.jakegblp.lusk.nms.core.adapters;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public interface SharedBiomeAdapter {

    void sendBiomePacket(Set<Player> viewers, Location corner1, Location corner2, String biome);

}
