package it.jakegblp.lusk.skript.modules.base.sections;

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
import it.jakegblp.lusk.skript.api.entry.PatternEntryData;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Name("Player - Update All Client Vital Stats (Hunger/Health/Saturation)")
@Examples({"""
        make {_player} see their vitals as:
            hunger: 12
            health: 3
            saturation: 10
        """
})
@Since("2.0.0")
public class SecClientSidedHealthHungerSaturation extends Section {

    private static final EntryValidator VALIDATOR = EntryValidator.builder()
            .addEntryData(new ExpressionEntryData<>("health", null, true, Double.class))
            .addEntryData(new PatternEntryData<>("(food|hunger) [level|bar]", "hunger", true, Integer.class))
            .addEntryData(new ExpressionEntryData<>("saturation", null, true, Float.class))
            .build();

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerSection(syntaxRegistry, SecClientSidedHealthHungerSaturation.class, SecClientSidedHealthHungerSaturation::new,
                "make %players% see (their|its) (vitals|vital stats) as",
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
        hungerExpression = PatternEntryData.getSimpleOptional(entryContainer, "hunger");
        saturationExpression = (Expression<Number>) entryContainer.getOptional("saturation", false);
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
