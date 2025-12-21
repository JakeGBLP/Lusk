package it.jakegblp.lusk.skript.elements.effects.simplemetadata.displays.item;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
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


@Name("[NMS] Player - Fake Display Item")
@Description("""
        Sets display item of a real or fake entity per player
        """)
@Examples({"""
        fake display with id 12 item to diamond sword for all players
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffItem extends Effect {

    static {
        Skript.registerEffect(EffItem.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] item [to] %itemtype% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<ItemType> itemExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[2];
        itemExpression = (Expression<ItemType>) expressions[1];
        return true;
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), new EntityMetadata(Map.of(MetadataKeys.ItemDisplayKeys.DISPLAY_ITEM, itemExpression.getSingle(event).getRandom())), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display item";
    }


}
