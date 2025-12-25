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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Name("[NMS] Player - Fake Display Text")
@Description("""
        Sets display text of a real or fake entity per player
        """)
@Examples({"""
        make display with id 12 text to "<rainbow>COOL NAME" for all players # String supports minimessage
        
        make display with id 12 text to "&4COOL NAME" for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffText extends Effect {

    static {
        Skript.registerEffect(EffText.class,
                "(make|fake) [display|display with id] %displays/numbers%['s] text [to] %string% for %players%"
        );
    }

    private Expression<Object> entityOrId;
    private Expression<String> stringExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityOrId = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[2];
        stringExpression = (Expression<String>) expressions[1];
        return true;
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        final String nameString = stringExpression.getSingle(event);
        Component text;
        if (nameString.contains("§"))
            text = Component.text(nameString);
        else
            text = MiniMessage.miniMessage().deserialize(nameString);

        AddonUtils.sendEasyMetadata(playerExpression.getArray(event), new EntityMetadata(Map.of(MetadataKeys.TextDisplayKeys.TEXT, text)), entityOrId.getArray(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client display text";
    }


}
