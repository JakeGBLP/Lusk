package it.jakegblp.lusk.skript.elements.effects.simplemetadata.displays.text;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Display Alignment")
@Description("""
        Sets display alignment of a real or fake entity per player
        """)
@Examples({"""
        make display with id 12 alignment to right for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffAlignment extends Effect {

    static {
        Skript.registerEffect(EffAlignment.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] alignment [to] %textalignment% for %player%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<TextDisplay.TextAlignment> alignmentExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[2];
        alignmentExpression = (Expression<TextDisplay.TextAlignment>) expressions[1];
        return true;
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.ALIGNMENT, alignmentExpression.getSingle(event))), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display alignment";
    }


}
