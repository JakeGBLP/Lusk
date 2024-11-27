package it.jakegblp.lusk.elements.minecraft.entities.player.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_START_RIPTIDE_ATTACK;
import static it.jakegblp.lusk.utils.ItemUtils.getSingleItemTypeToItemStack;

@Name("Player - Start/Stop Riptiding")
@Description("""
Makes a player riptide. (also known as spin attack)

Stopping makes the player riptide for a tick, which stops it a tick later.

The duration must be greater than or equal to 1 tick.
Attack strength will default to 0

`if player is riptiding:` will return true for the given duration.

""")
@Examples({"make player riptide for 2 seconds with attack strength 4","make player stop riptiding"})
@Since("1.3")
@RequiredPlugins("1.21.1")
public class EffPlayerRiptide extends Effect {
    static {
        if (HAS_START_RIPTIDE_ATTACK) {
            Skript.registerEffect(EffPlayerRiptide.class,
                    "make %players% (start riptiding|riptide|spin attack) for %timespan% [using %-itemtype%] [with [attack] (strength|damage) %-number%]",
                    "make %players% (stop|finish) (riptiding|spin attacking)"
            );
        }
    }

    private Expression<Player> playerExpression;
    private Expression<Timespan> timespanExpression;
    private Expression<ItemType> itemTypeExpression;
    private Expression<Number> numberExpression;
    private boolean stop = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        playerExpression = (Expression<Player>) expressions[0];
        if (matchedPattern == 0) {
            timespanExpression = (Expression<Timespan>) expressions[1];
            itemTypeExpression = (Expression<ItemType>) expressions[2];
            numberExpression = (Expression<Number>) expressions[3];
        } else stop = true;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make "+playerExpression.toString(event, debug)+" for "+timespanExpression.toString(event, debug) +
                (itemTypeExpression == null ? "" : " using "+itemTypeExpression.toString(event, debug)) +
                " with attack strength "+numberExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        int duration;
        float attackStrength;
        ItemStack attackItem;
        if (stop) {
            attackItem = null;
            attackStrength = 0;
            duration = 1;
        } else {
            Timespan timespan = timespanExpression.getSingle(event);
            if (timespan == null) return;
            duration = (int) timespan.getTicks();
            attackStrength = numberExpression.getOptionalSingle(event).orElse(0).floatValue();
            if (attackStrength <= 0) return;
            attackItem = getSingleItemTypeToItemStack(itemTypeExpression,event);
        }

        playerExpression.stream(event).forEach(player -> {
            if (!stop || player.isRiptiding()) {
                player.startRiptideAttack(duration, attackStrength, attackItem);
            }
        });
    }
}