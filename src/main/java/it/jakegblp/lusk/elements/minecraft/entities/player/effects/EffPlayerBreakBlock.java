package it.jakegblp.lusk.elements.minecraft.entities.player.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Player Break Block")
@Description("Force a player to break one or more blocks, like they mined them themselves.\nUses player's tool, handles enchantments, durability, drops etc by itself\nTriggers `on break:` event")
@Examples({"make player break target block"})
@Since("CHANGE_ME")
public class EffPlayerBreakBlock extends Effect {

    static {
        Skript.registerEffect(EffPlayerBreakBlock.class,
                "(make|force) %player% [to] break [block[s]] %blocks%"
        );
    }

    private Expression<Player> playerExpr;
    private Expression<Block> blocksExpr;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        playerExpr = (Expression<Player>) expressions[0];
        blocksExpr = (Expression<Block>) expressions[1];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Player player = playerExpr.getSingle(event);
        if (player == null) return;

        Block[] blocks = blocksExpr.getArray(event);
        if (blocks == null || blocks.length == 0) return;

        for (Block block : blocks) {
            if (block == null || block.getType().equals(Material.AIR)) continue;

            boolean success = player.breakBlock(block);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + playerExpr.toString(event, debug) + " break " + blocksExpr.toString(event, debug);
    }
}
