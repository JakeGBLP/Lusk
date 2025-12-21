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


@Name("[NMS] Player - Fake Display Background color")
@Description("""
        Sets display background color of a real or fake entity per player
        """)
@Examples({"""
        make display with id 12 background color to 12 for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffBackgroundColor extends Effect {

    static {
        Skript.registerEffect(EffBackgroundColor.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] [background] color [to] %number% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Color> colorExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[2];
        colorExpression = (Expression<Color>) expressions[1];
        return true;
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.BACKGROUND_COLOR, colorExpression.getSingle(event).asBukkitColor())), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display background color";
    }


}
