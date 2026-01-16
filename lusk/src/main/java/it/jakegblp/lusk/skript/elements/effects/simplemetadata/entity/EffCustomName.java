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
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Entity Name")
@Description("""
        Sets custom name of a real or fake entity per player
        """)
@Examples({"""
        make target entity custom name "&4COOL NAME" for all players
        make target entity custom name "<rainbow>COOL NAME" for all players # String supports minimessage
        
        
        make entity with id 12 custom name "&4COOL NAME" for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffCustomName extends Effect {

    static {
        Skript.registerEffect(EffCustomName.class,
                "(make|fake) [entity|entity with id] %entities/numbers%['s] custom name [to] %textcomponent% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<Component> nameExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        nameExpression = (Expression<Component>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Component name = nameExpression.getSingle(event);
        if (name == null) return;
        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.CUSTOM_NAME, name)), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client custom name";
    }

}
