package it.jakegblp.lusk.elements.expressions;

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
import ch.njol.util.coll.CollectionUtils;
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
@Since("1.0.0, 1.0.2 (Set), 1.0.3 (Returns a string. Added: Llama, Fox, Boat, Cat, Rabbit, Villager, Zombie Villager, Panda and Tropical Fish)")
public class ExprEntityVariant extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprEntityVariant.class, Object.class, ExpressionType.COMBINED,
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
        String variant = null;
        if (livingEntity instanceof Axolotl axolotl) {
            variant = axolotl.getVariant().name();
        } else if (livingEntity instanceof Frog frog) {
            variant = frog.getVariant().name();
        } else if (livingEntity instanceof MushroomCow mushroomCow) {
            variant = mushroomCow.getVariant().name();
        } else if (livingEntity instanceof Parrot parrot) {
            variant = parrot.getVariant().name();
        } else if (livingEntity instanceof Llama llama) {
            variant = llama.getColor().name();
        } else if (livingEntity instanceof Fox fox) {
            variant = fox.getFoxType().name();
        } else if (livingEntity instanceof Cat cat) {
            variant = cat.getCatType().name();
        } else if (livingEntity instanceof Rabbit rabbit) {
            variant = rabbit.getRabbitType().name();
        } else if (livingEntity instanceof Panda panda) {
            variant = panda.getMainGene().name();
        } else if (livingEntity instanceof TropicalFish tropicalFish) {
            variant = tropicalFish.getPattern().name();
        }
        if (variant != null) {
            return new String[]{variant};
        }
        return new String[0];
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
        } else if (livingEntity instanceof Llama llama) {
            for (Llama.Color variantObject : Llama.Color.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    llama.setColor(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Fox fox) {
            for (Fox.Type variantObject : Fox.Type.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    fox.setFoxType(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Cat cat) {
            for (Cat.Type variantObject : Cat.Type.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    cat.setCatType(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Rabbit rabbit) {
            for (Rabbit.Type variantObject : Rabbit.Type.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    rabbit.setRabbitType(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof Panda panda) {
            for (Panda.Gene variantObject : Panda.Gene.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    panda.setMainGene(variantObject);
                    return;
                }
            }
        } else if (livingEntity instanceof TropicalFish tropicalFish) {
            for (TropicalFish.Pattern variantObject : TropicalFish.Pattern.values()) {
                if (variant.toUpperCase().equals(variantObject.name())) {
                    tropicalFish.setPattern(variantObject);
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