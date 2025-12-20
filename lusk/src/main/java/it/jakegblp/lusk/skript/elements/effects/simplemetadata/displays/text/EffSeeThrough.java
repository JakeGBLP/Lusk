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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Display See Through")
@Description("""
        Sets display see though of a real or fake entity per player
        """)
@Examples({"""
        make display with id 12 have see though for all players
        make display with id 12 not have see though for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffSeeThrough extends Effect {

    static {
        Skript.registerEffect(EffSeeThrough.class,
                "make [display|display with id] %displays/numbers%['s] be see through for %player%",
                "make [display|display with id] %displays/numbers%['s] not be see through for %player%"
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


    private static final EntityMetadata packetDataTrue = new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.SEE_THROUGH, true));
    private static final EntityMetadata packetDataFalse = new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.SEE_THROUGH, false));

    @Override
    protected void execute(Event event) {
        EntityMetadata packetData = pattern == 0 ? packetDataTrue : packetDataFalse;
        
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), packetData, entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display see though";
    }


}
