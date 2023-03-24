package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import me.jake.lusk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
@SuppressWarnings("unused")
public class EvtBlockTill extends SkriptEvent {

    static {
        Skript.registerEvent("Till", EvtBlockTill.class, PlayerInteractEvent.class, "[dirt] till[ing|ed]")
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
        PlayerInteractEvent event = (PlayerInteractEvent)e;
        if (event.getAction().isRightClick()) {
            if (Utils.isHoe(event.getMaterial())) {
                Material material = Objects.requireNonNull(event.getClickedBlock()).getType();
                if (material == Material.ROOTED_DIRT) {
                    return true;
                }
                if (event.getClickedBlock().getLocation().add(0,1,0).getBlock().getType().isAir()) {
                    return Utils.isTillable(material);
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