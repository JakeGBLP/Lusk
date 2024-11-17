package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

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
import it.jakegblp.lusk.utils.EntityUtils;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Variant")
@Description("""
        Returns the Variant of an Entity.
        Parrot: blue, cyan, green, red
        Frog: cold, temperate, warm
        Mushroom Cow: red, brown
        Axolotl: blue, cyan, gold, lucy, wild
        Llama: brown, creamy, gray, white
        Fox: red, snow
        Cat: all_black, black, british_shorthair, calico, jellie, persian, ragdoll, red, siamese, tabby, white
        Rabbit: black, black_and_white, brown, gold, salt_and_pepper, the_killer_bunny, white
        Panda: aggressive, brown, lazy, normal, playful, weak, worried
        Tropical Fish: betty, blockfish, brinely, clayfish, dasher, flopper, glitter, kob, snooper, spotty, stripey, sunstreak
        """)
@Examples({"broadcast variant of event-entity\nset variant of target to \"red\""})
@Since("1.0.0, 1.0.2 (Set), 1.0.3 (String + More)")
@SuppressWarnings({"unused", "ConstantConditions"})
public class ExprEntityVariant extends SimpleExpression<Object> {

    static {
        // TODO: PROPERTY EXPR?
        // todo: make plural, find better way to make this work,
        //  return not as string, work around deprecation (registries!)
        Skript.registerExpression(ExprEntityVariant.class, Object.class, ExpressionType.PROPERTY,
                "[the] entity variant of %livingentity%",
                "%livingentity%'[s] entity variant");
    }

    private Expression<LivingEntity> livingEntitiesExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntitiesExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        return livingEntitiesExpression.stream(e).map(EntityUtils::getVariant).toArray();
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Object.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        livingEntitiesExpression.stream(e).forEach(livingEntity -> EntityUtils.setVariant(livingEntity,delta[0]));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the entity variant of " + livingEntitiesExpression.toString(e, debug);
    }
}