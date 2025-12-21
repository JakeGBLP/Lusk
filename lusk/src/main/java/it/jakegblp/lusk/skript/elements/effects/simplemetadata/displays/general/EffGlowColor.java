package it.jakegblp.lusk.skript.elements.effects.simplemetadata.displays.general;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Display Glow Override")
@Description("""
        Sets display glow override of a real or fake entity per player
        """)
@Examples({"""
        fake display with id 12 glow override to -65536 for all players
        fake display with id 12 glow override to red for all players
         fake display with id 12 glow override to rgb(255, 0, 0, 255) for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffGlowColor extends Effect {

    static {
        Skript.registerEffect(EffGlowColor.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] glow override [to] %color/number% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Object> objectExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[2];
        objectExpression = (Expression<Object>) expressions[1];
        return true;
    }


    @Override
    protected void execute(Event event) {
        int color;
        final Object single = objectExpression.getSingle(event);
        if(single instanceof Number number)
            color = number.intValue();
        else if(single instanceof Color c)
            color = c.asARGB();
        else
            return;

        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), new EntityMetadata(Map.of(MetadataKeys.DisplayKeys.GLOW_COLOR, color)), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display glow override";
    }


}
