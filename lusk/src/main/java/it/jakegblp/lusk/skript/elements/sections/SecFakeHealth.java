package it.jakegblp.lusk.skript.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.api.expression.DefaultValueExpression;
import it.jakegblp.lusk.skript.elements.effects.EffDispatchPacket;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Name("Player - Client Hunger/Health/Saturation [Section]")
@Description("Fakes the client's health, hunger or saturation")
@Examples({"""
        make {_player} see their vitals:
            hunger: 12
            health: 3
            saturation: 10
        """

})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async", "client", "hunger", "health", "saturation"
})
@Since("2.0.0")
public class SecFakeHealth extends Section {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("health", null, false, Double.class))
                .addEntryData(new ExpressionEntryData<>("hunger", null, true, Integer.class))
                .addEntryData(new ExpressionEntryData<>("saturation", null, true, Float.class))
                .build();
        Skript.registerSection(SecFakeHealth.class,
                "make [player[s]] %players% see [their] vitals [as]");
    }

    private Expression<Player> playerExpression;
    private Expression<Number> healthExpression;
    private Expression<Number> hungerExpression;
    private Expression<Number> saturationExpression;


    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        playerExpression = (Expression<Player>) expressions[0];

        EntryContainer entryContainer = VALIDATOR.validate(sectionNode);
        if (entryContainer == null) return false;
        healthExpression = (Expression<Number>) entryContainer.getOptional("health", false);
        hungerExpression = (Expression<Number>) entryContainer.getOptional("hunger", false);
        saturationExpression = (Expression<Number>) entryContainer.getOptional("saturation", true);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        for (Player player : playerExpression.getArray(event)) {
            double health = AddonUtils.getSingleNullable(healthExpression, event, player.getHealth()).doubleValue();
            int hunger = AddonUtils.getSingleNullable(hungerExpression, event, player.getFoodLevel()).intValue();
            float saturation = AddonUtils.getSingleNullable(saturationExpression, event, player.getSaturation()).floatValue();

            player.sendHealthUpdate(health, hunger, saturation);
        }

        return super.walk(event, false);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fake health section";
    }

}
