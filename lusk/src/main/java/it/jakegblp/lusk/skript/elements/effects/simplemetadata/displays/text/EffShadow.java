package it.jakegblp.lusk.skript.elements.effects.simplemetadata.displays.text;

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


@Name("[NMS] Player - Fake Display Drop Shadow")
@Description("""
        Sets display drop shadow of a real or fake entity per player
        """)
@Examples({"""
        make display with id 12 have drop shadow for all players
        make display with id 12 not have drop shadow for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffShadow extends Effect {

    static {
        Skript.registerEffect(EffShadow.class,
                "make [display|display with id] %displays/numbers%['s] have [drop] shadow for %players%",
                "make [display|display with id] %displays/numbers%['s] not have [drop] shadow for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Player> playerExpression;
    int pattern;



    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        this.pattern = pattern;
        return true;
    }



    @Override
    protected void execute(Event event) {
        EntityMetadata packetData = new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.HAS_SHADOW, pattern == 0));

        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), packetData, entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display drop shadow";
    }


}
