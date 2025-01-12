package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Suspicious Stew - Custom Potion Effects")
@Description("""
        Gets the custom potion effects applied to the provided suspicious stew.
        
        Can be: set, added to, removed from, removed all x from, cleared and reset.
        Clear and reset do the same thing, same for remove and remove all.
        Remove and remove all can take either potion effects or potion effect types.
        """)
@Examples("set suspicious stew potion effects to strength potion effect of strength of tier 1 for 15 seconds")
@Since("1.3.3")
public class ExprSuspiciousStewCustomPotionEffects extends PropertyExpression<ItemType, PotionEffect> {

    static {
        register(ExprSuspiciousStewCustomPotionEffects.class, PotionEffect.class, "suspicious stew [custom] potion effects", "itemtypes");
    }

    @Override
    protected PotionEffect[] get(Event event, ItemType[] source) {
        return Arrays.stream(source).flatMap(itemType ->
                Arrays.stream(ItemUtils.getSuspiciousStewPotionEffects(itemType))).toArray(PotionEffect[]::new);
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, SET -> CollectionUtils.array(PotionEffect[].class);
            case REMOVE, REMOVE_ALL -> CollectionUtils.array(PotionEffect[].class, PotionEffectType[].class);
            case DELETE, RESET -> CollectionUtils.array();
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemType[] itemTypes = getExpr().getArray(event);
        switch (mode) {
            case ADD -> {
                for (ItemType item : itemTypes)
                    ItemUtils.addSuspiciousStewPotionEffects(item, (PotionEffect[]) delta);
            }
            case REMOVE, REMOVE_ALL -> {
                for (ItemType item : itemTypes)
                    ItemUtils.removeSuspiciousStewPotionEffects(item,delta);
            }
            case SET -> {
                for (ItemType item : itemTypes)
                    ItemUtils.setSuspiciousStewPotionEffects(item, (PotionEffect[]) delta);
            }
            case DELETE, RESET -> {
                for (ItemType item : itemTypes)
                    ItemUtils.clearSuspiciousStewPotionEffects(item);
            }
        }
    }

    @Override
    public Class<? extends PotionEffect> getReturnType() {
        return PotionEffect.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "suspicious stew potion effects of " + getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemType>) expressions[0]);
        return true;
    }
}
