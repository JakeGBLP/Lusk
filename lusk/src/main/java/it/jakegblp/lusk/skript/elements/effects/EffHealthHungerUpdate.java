package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player - Client Hunger/Health/Saturation")
@Description("Fakes the client's health, hunger or saturation")
@Examples({
        "make {_player} see their health as 12",
        "make {_player} see their hunger as 12",
        "make {_player} see their saturation as 12"
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async", "client", "hunger", "health", "saturation"
})
@Since("2.0.0")
public class EffHealthHungerUpdate extends Effect {

    static {
        Skript.registerEffect(EffHealthHungerUpdate.class,
                "make [player[s]] %players% see [their] health as %number%",
                "make [player[s]] %players% see [their] hunger as %number%",
                "make [player[s]] %players% see [their] saturation as %number%"
        );
    }

    private Expression<Player> playerExpression;
    private Expression<Number> numberExpression;
    int pattern;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) expressions[0];
        numberExpression = (Expression<Number>) expressions[1];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (Player player : playerExpression.getArray(event)) {
            final double value = numberExpression.getSingle(event).doubleValue();
            switch (pattern){
                case 0 -> player.sendHealthUpdate(value, player.getFoodLevel(), player.getSaturation());
                case 1 -> player.sendHealthUpdate(player.getHealth(), (int) value, player.getSaturation());
                case 2 -> player.sendHealthUpdate(player.getHealth(), player.getFoodLevel(), (float) value);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fake health";
    }
}
