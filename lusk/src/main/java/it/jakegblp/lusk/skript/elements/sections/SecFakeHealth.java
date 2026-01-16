package it.jakegblp.lusk.skript.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


@Name("Player - Update All Client Vital Stats (Hunger/Health/Saturation)")
@Examples({"""
        make {_player} see their vitals:
            hunger: 12
            health: 3
            saturation: 10
        """
})
@Since("2.0.0")
public class SecFakeHealth extends Section {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("health", null, true, Double.class))
                .addEntryData(new ExpressionEntryData<>("hunger", null, true, Integer.class)) // todo: add food bar option, requires entry api expansion
                .addEntryData(new ExpressionEntryData<>("saturation", null, true, Float.class))
                .build();
        Skript.registerSection(SecFakeHealth.class,
                "make [player[s]] %players% see (their|its) (vitals|vital stats) as",
                "update %players%'[s] (vitals|vital stats)",
                "update (vitals|vital stats) (of|for) %players%");
    }

    private Expression<Player> playerExpression;
    private Expression<Number> healthExpression;
    private Expression<Number> hungerExpression;
    private Expression<Number> saturationExpression;


    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        EntryContainer entryContainer = VALIDATOR.validate(sectionNode);
        if (entryContainer == null) return false;
        playerExpression = (Expression<Player>) expressions[0];
        healthExpression = (Expression<Number>) entryContainer.getOptional("health", false);
        hungerExpression = (Expression<Number>) entryContainer.getOptional("hunger", false);
        saturationExpression = (Expression<Number>) entryContainer.getOptional("saturation", true);
        if (Stream.of(healthExpression, hungerExpression, saturationExpression).noneMatch(Objects::nonNull)) {
            Skript.error("At least one vital stat entry must be present.");
            return false;
        }
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
        return "update vitals for "+playerExpression.toString(event, debug);
    }

}
