package it.jakegblp.lusk.api.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.function.Consumer;

public class BlockBreakListener implements Listener {
    private static final HashMap<Block, Consumer<BlockBreakEvent>> map = new HashMap<>();

    /**
     * Logs a Consumer along with its Block.
     *
     * @param consumer The consumer containing the section's contents and the variable management.
     * @param block    The block involved at runtime.
     */
    public static void log(Consumer<BlockBreakEvent> consumer, Block block) {
        map.put(block, consumer);
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (map.containsKey(block)) map.get(block).accept(event);
    }
}
