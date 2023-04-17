package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.jake.lusk.classes.Version;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Variant")
@Description("Returns the Variant of an Entity.\nParrot: blue, cyan, green, red\nFrog: cold, temperate, warm\nMushroom Cow: red, brown\nAxolotl: blue, cyan, gold, lucy (pink), wild (brown)")
@Examples({"broadcast variant of event-entity\nset variant of target to \"red\""})
@Since("1.0.0, 1.0.2 (Set)")
public class ExprVariant extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprVariant.class, Object.class, ExpressionType.COMBINED,
                "[the] variant of %livingentity%",
                "%livingentity%'[s] variant");

    }
    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }
    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        Object variant = null;
        if (livingEntity instanceof Axolotl axolotl) {
            variant = axolotl.getVariant();
        } else if (livingEntity instanceof Frog frog) {
            variant = frog.getVariant();
        } else if (livingEntity instanceof MushroomCow mushroomCow) {
            variant = mushroomCow.getVariant();
        } else if (livingEntity instanceof Parrot parrot) {
            variant = parrot.getVariant();
        }
        if (variant != null) {
            return new Object[]{variant};
        }
        return new Object[0];
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        String variant = delta instanceof String[] ? ((String[]) delta)[0] : null;
        if (variant == null) return;
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity instanceof Axolotl axolotl) {
            for (Axolotl.Variant variantObject : Axolotl.Variant.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    axolotl.setVariant(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Frog frog) {
            for (Frog.Variant variantObject : Frog.Variant.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    frog.setVariant(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof MushroomCow mushroomCow) {
            for (MushroomCow.Variant variantObject : MushroomCow.Variant.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    mushroomCow.setVariant(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Parrot parrot) {
            for (Parrot.Variant variantObject : Parrot.Variant.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    parrot.setVariant(variantObject);
                    return;
                }
            }
        }
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
        return "the variant of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}