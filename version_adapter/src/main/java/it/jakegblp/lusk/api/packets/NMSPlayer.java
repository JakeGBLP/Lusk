package it.jakegblp.lusk.api.packets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class NMSPlayer<
        NMSServerPlayer
        > {
    static final Map<String, NMSPlayer<?>> FAKE_PLAYERS = new HashMap<>();

    private final int id;
    private final NMSServerPlayer serverPlayer;

    public NMSPlayer(NMSServerPlayer serverPlayer, int id) {
        this.serverPlayer = serverPlayer;
        this.id = id;
    }

    public void update() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            update(onlinePlayer);
        }
    }

    public abstract void update(@NotNull Player player);

    public abstract void update(NMSServerPlayer serverPlayer);

    public int getID() {
        return id;
    }

    public NMSServerPlayer getServerPlayer() {
        return serverPlayer;
    }


}
