package it.jakegblp.lusk.elements.minecraft.blocks.block.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBlockTill extends SkriptEvent {
    static {
        Skript.registerEvent("Block - on Block Till", EvtBlockTill.class, PlayerInteractEvent.class, "[dirt] till[ing|ed]")
                .description("Called when a player changes a block by right clicking it with a hoe.")
                .examples("on till:\n\tbroadcast \"a block has been tilled!\"")
                .since("1.0.0");
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        PlayerInteractEvent event = (PlayerInteractEvent) e;
        if (event.getAction().isRightClick()) {
            if (Constants.hoes.contains(event.getMaterial())) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    Material material = block.getType();
                    if (material == Material.ROOTED_DIRT) {
                        return true;
                    }
                    if (block.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
                        return Constants.tillables.contains(material);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "dirt till";
    }

}