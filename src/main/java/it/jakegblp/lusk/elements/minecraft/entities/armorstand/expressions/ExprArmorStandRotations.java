package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.enums.BodyPart;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;
import static it.jakegblp.lusk.utils.EntityUtils.getArmorStandRotation;
import static it.jakegblp.lusk.utils.EntityUtils.setArmorStandRotation;
import static it.jakegblp.lusk.utils.VectorUtils.toVector;

@Name("Armor Stand - Rotations")
@Description("Gets and sets a specific armor stand rotation.")
@Examples({"broadcast rotation of target", "set head rotation of target to vector(45,0,0)\n# looks down a a 45 degrees - sad armor stand :("})
@Since("1.0.2, 1.3 (Degrees)")
@SuppressWarnings("unused")
public class ExprArmorStandRotations extends PropertyExpression<LivingEntity,Vector> {
    // todo: allow radians too, perhaps make a reusable system that supports either
    static {
        register(ExprArmorStandRotations.class, Vector.class,
                ARMOR_STAND_PREFIX +" %bodyparts% (rotation|pose)[s]", "livingentities");
    }

    private Expression<BodyPart> bodyPartExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            bodyPartExpression = (Expression<BodyPart>) expressions[0];
            setExpr((Expression<? extends ArmorStand>) expressions[1]);
        } else {
            setExpr((Expression<? extends ArmorStand>) expressions[0]);
            bodyPartExpression = (Expression<BodyPart>) expressions[1];
        }
        return true;
    }

    @Override
    public boolean isSingle() {
        return super.isSingle() && bodyPartExpression.isSingle();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Vector.class, EulerAngle.class};
            case RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Stream<ArmorStand> armorStands = getExpr().stream(event)
                .filter(ArmorStand.class::isInstance)
                .map(ArmorStand.class::cast);
        if (mode == Changer.ChangeMode.RESET) {
            armorStands.forEach(armorStand ->
                    bodyPartExpression.stream(event)
                            .forEach(bodyPart -> setArmorStandRotation(armorStand, bodyPart, EulerAngle.ZERO)));
        } else {
            Vector vector, current;
            if (delta[0] instanceof Vector v) {
                vector = v;
            } else if (delta[0] instanceof EulerAngle eulerAngle) {
                vector = toVector(eulerAngle);
            } else {
                vector = null;
            }
            if (vector != null) {
                armorStands.forEach(armorStand ->
                        bodyPartExpression.stream(event)
                                .forEach(bodyPart -> setArmorStandRotation(armorStand, bodyPart, switch (mode) {
                                    case SET -> vector;
                                    case ADD -> getArmorStandRotation(armorStand, bodyPart).add(vector);
                                    case REMOVE -> getArmorStandRotation(armorStand, bodyPart).subtract(vector);
                                    default -> new Vector();
                                }))
                );
            }
        }
    }

    @Override
    public Class<? extends Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    protected Vector[] get(Event event, LivingEntity[] source) {
        return Arrays.stream(source)
                .filter(ArmorStand.class::isInstance)
                .map(ArmorStand.class::cast)
                .flatMap(armorStand -> bodyPartExpression.stream(event)
                        .map(bodyPart -> getArmorStandRotation(armorStand, bodyPart))).toArray(Vector[]::new);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "the armor stand "+bodyPartExpression.toString(event, debug)+" rotations of "+getExpr().toString(event, debug);
    }
}