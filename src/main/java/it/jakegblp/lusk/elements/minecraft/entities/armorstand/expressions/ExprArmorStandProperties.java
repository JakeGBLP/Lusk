package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static it.jakegblp.lusk.utils.Constants.ARMORS_STAND_PREFIX;
import static it.jakegblp.lusk.utils.Constants.STATE_OR_PROPERTY;

@Name("Armor Stand - Properties")
@Description("""
Gets and sets a specific armor stand property.
This includes:
- Is Small;
- Has Arms;
- Has Base Plate;
- Is Marker;
- Is Invisible (or Visible);
- Can Tick;
- Can Move;

These properties (except `Can Tick` and `Can Move`) can be used with the armor stand item when using Paper.
""")
@Examples({"broadcast arms of target", "set marker of target to true"})
@Since("1.0.2, 1.3 (invisible, item)")
@SuppressWarnings("unused")
public class ExprArmorStandProperties extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprArmorStandProperties.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] " + ARMORS_STAND_PREFIX + " [is] small "+STATE_OR_PROPERTY+" of %livingentities/itemtypes%",
                "[the] " + ARMORS_STAND_PREFIX + " ([has|shows|should show] arms|[has] arms [:in]visibility) "+STATE_OR_PROPERTY+" of %livingentities/itemtypes%",
                "[the] " + ARMORS_STAND_PREFIX + " ([has|shows|should show] base plate|[has] base plate [:in]visibility) "+STATE_OR_PROPERTY+" of %livingentities/itemtypes%",
                "[the] " + ARMORS_STAND_PREFIX + " [is] marker "+STATE_OR_PROPERTY+" of %livingentities/itemtypes%",
                "[the] " + ARMORS_STAND_PREFIX + " ([is] [:in]visible|[:in]visibility) "+STATE_OR_PROPERTY+" of %livingentities/itemtypes%",
                "[the] " + ARMORS_STAND_PREFIX + " [can] tick "+STATE_OR_PROPERTY+" of %livingentities%",
                "[the] " + ARMORS_STAND_PREFIX + " [can] move "+STATE_OR_PROPERTY+" of %livingentities%");
    }

    private Expression<Object> objectExpression;
    private int property;
    private boolean not;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        objectExpression = (Expression<Object>) exprs[0];
        property = matchedPattern;
        not = parseResult.hasTag("in");
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        return objectExpression.stream(e).map(object -> switch (property) {
            case 0 -> ArmorStandUtils.isSmall(object);
            case 1 -> ArmorStandUtils.hasArms(object) ^ !not;
            case 2 -> ArmorStandUtils.hasBasePlate(object) ^ !not;
            case 3 -> ArmorStandUtils.isMarker(object);
            case 4 -> ArmorStandUtils.isInvisible(object) ^ !not;
            case 5 -> ArmorStandUtils.canTick(object);
            case 6 -> ArmorStandUtils.canMove(object);
            default -> null;
        }).filter(Objects::nonNull).toArray(Boolean[]::new);
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean bool) {
            for (Object object : objectExpression.getAll(e)) {
                switch (property) {
                    case 0 -> ArmorStandUtils.setIsSmall(object, bool);
                    case 1 -> ArmorStandUtils.setHasArms(object, bool ^ !not);
                    case 2 -> ArmorStandUtils.setHasBasePlate(object, bool ^ not); // no idea why this one work without the negation operator.
                    case 3 -> ArmorStandUtils.setIsMarker(object, bool);
                    case 4 -> ArmorStandUtils.setIsInvisible(object, bool ^ !not);
                    case 5 -> ArmorStandUtils.setCanTick(object, bool);
                    case 6 -> ArmorStandUtils.setCanMove(object, bool);
                }
            }
        }
    }

    @Override
    public boolean isSingle() {
        return objectExpression.isSingle();
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the armorstand " + switch (property) {
            case 0 -> "is small";
            case 1 -> "has arms "+(not ? "in" : "")+"visibility";
            case 2 -> "has base plate "+(not ? "in" : "")+"visibility";
            case 3 -> "is marker";
            case 4 -> (not ? "in" : "") + "visible";
            case 5 -> "can tick";
            case 6 -> "can move";
            default -> null;
        } + " property of " + objectExpression.toString(e, debug);
    }
}