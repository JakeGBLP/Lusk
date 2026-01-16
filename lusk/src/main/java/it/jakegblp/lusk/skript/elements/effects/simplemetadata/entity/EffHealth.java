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


@Name("[NMS] Player - Fake Entity Health")
@Description("""
        Sets real or fake entity health
        """)
@Examples({"""
        fake target entity health 3 for all players
        
        make entity with id 12 health 3 for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffHealth extends Effect {

    static {
        Skript.registerEffect(EffHealth.class,
                "(make|fake) [entity|entity with id] %entities/numbers%['s] health %number% for %players%"
                );
    }

    private Expression<Object> entityOrId;
    private Expression<Player> playerExpression;
    private Expression<Number> healthExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        healthExpression = (Expression<Number>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }



    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(Map.of(MetadataKeys.LivingEntityKeys.HEALTH, healthExpression.getSingle(event).intValue())), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client entity health";
    }


}
