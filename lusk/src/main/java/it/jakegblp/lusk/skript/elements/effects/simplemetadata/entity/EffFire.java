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


@Name("[NMS] Player - Fake Entity Burning")
@Description("Sets fire state of a real or fake entity")
@Examples({"""
        make target entity on fire for all players
        extinguish target entity for all players
        
        make entity with id 12 on fire for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffFire extends Effect {

    static {
        Skript.registerEffect(EffFire.class,
                "(make|fake) [entity|entity with id] %entities/numbers%['s] on fire for %player%",
                "(make|fake) [entity|entity with id] %entities/numbers%['s] not on fire for %player%",
                "extinguish [entity|entity with id] %entities/numbers%['s] for %player%"
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


    private static final EntityMetadata packetDataTrue = new EntityMetadata(Map.of(MetadataKeys.EntityKeys.BURNING, true));
    private static final EntityMetadata packetDataFalse = new EntityMetadata(Map.of(MetadataKeys.EntityKeys.BURNING, false));


    @Override
    protected void execute(Event event) {
        EntityMetadata packetData = pattern == 0 ? packetDataTrue : packetDataFalse;

        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), packetData, entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client fire";
    }


}
