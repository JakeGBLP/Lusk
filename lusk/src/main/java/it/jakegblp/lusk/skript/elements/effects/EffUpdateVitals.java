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

@Name("Player - Update Client Vital Stats (Hunger/Health/Saturation)")
public class EffUpdateVitals extends Effect {

    static {
        Skript.registerEffect(EffUpdateVitals.class,
                "make [player[s]] %players% see (their|its) health as %number%",
                "make [player[s]] %players% see (their|its) (food|hunger) [level|bar] as %number%",
                "make [player[s]] %players% see (their|its) saturation as %number%"
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
        Number number = numberExpression.getSingle(event);
        if (number == null) return;
        for (Player player : playerExpression.getArray(event))
            switch (pattern) {
                case 0 -> player.sendHealthUpdate(number.doubleValue(), player.getFoodLevel(), player.getSaturation());
                case 1 -> player.sendHealthUpdate(player.getHealth(), number.intValue(), player.getSaturation());
                case 2 -> player.sendHealthUpdate(player.getHealth(), player.getFoodLevel(), number.floatValue());
            }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make players " + playerExpression.toString(event,debug) + " see their " + switch (pattern){
            case 0 -> "health";
            case 1 -> "hunger";
            default -> "saturation";
        } + " as " + numberExpression.toString(event,debug);
    }
}
