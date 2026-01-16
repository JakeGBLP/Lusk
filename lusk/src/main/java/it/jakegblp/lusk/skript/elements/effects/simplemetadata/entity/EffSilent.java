package it.jakegblp.lusk.skript.elements.effects.simplemetadata.entity;

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


@Name("[NMS] Player - Fake Entity Silent")
@Description("""
        Sets real or fake entity to silent
        """)
@Examples({"""
        make target entity silent for all players
        make target entity not silent for all players
        
        make entity with id 12 silent for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffSilent extends Effect {

    static {
        Skript.registerEffect(EffSilent.class,
                "(make|fake) [entity|entity with id] %entities/numbers%['s] silent for %players%",
                "(make|fake) [entity|entity with id] %entities/numbers%['s] not silent for %players%"
                );
    }

    private Expression<Object> entityOrId;
    private Expression<Player> playerExpression;
    private int pattern;

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
        EntityMetadata packetData = EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.SILENT, pattern == 0));

        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), packetData, entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client entity silent";
    }


}
