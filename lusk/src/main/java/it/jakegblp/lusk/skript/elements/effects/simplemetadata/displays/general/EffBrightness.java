package it.jakegblp.lusk.skript.elements.effects.simplemetadata.displays.general;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Display Brightness")
@Description("""
        Sets display brightness of a real or fake entity per player
        """)
@Examples({"""
        fake display with id 12 sky brightness to 15 for all players
        fake display with id 12 block brightness to 15 for all players
        fake display with id 12 block brightness to 15 and sky brightness to 15 for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffBrightness extends Effect {

    static {
        Skript.registerEffect(EffBrightness.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] sky brightness [to] %number% for %players%",
                "(make|fake) [display|display with id] %displays/numbers%['s] block brightness [to] %number% for %players%",
                "(make|fake) [display|display with id] %displays/numbers%['s] block brightness [to] %number% and sky brightness to %number% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Number> numberExpression;
    private Expression<Number> number2Expression;
    private Expression<Player> playerExpression;
    int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        numberExpression = (Expression<Number>) expressions[1];
        if(pattern != 2)
            playerExpression = (Expression<Player>) expressions[2];
        else {
            playerExpression = (Expression<Player>) expressions[3];
            number2Expression = (Expression<Number>) expressions[2];
        }
        this.pattern = pattern;
        return true;
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        if (pattern == 0)
            AddonUtils.sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(Map.of(MetadataKeys.DisplayKeys.BRIGHTNESS, new Display.Brightness(0, numberExpression.getSingle(event).intValue()))), entityOrId.getArray(event));
        else if (pattern == 1)
            AddonUtils.sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(Map.of(MetadataKeys.DisplayKeys.BRIGHTNESS, new Display.Brightness(numberExpression.getSingle(event).intValue(), 0))), entityOrId.getArray(event));
        else
            AddonUtils.sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(Map.of(MetadataKeys.DisplayKeys.BRIGHTNESS, new Display.Brightness(numberExpression.getSingle(event).intValue(), number2Expression.getSingle(event).intValue()))), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display brightness";
    }


}
