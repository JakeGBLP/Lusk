package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.setShouldBurnDuringTheDay;
import static it.jakegblp.lusk.utils.EntityUtils.shouldBurnDuringTheDay;

@Name("Entity - Should Burn During The Day (Property)")
@Description("""
        Returns whether or not the provided entities should burn during the day.
        Applies to zombies, skeletons, and phantoms
        Can be set.""")
@Examples({"broadcast the should burn under sunlight property of target","set the should burn during the day property of {_entity}"})
@Since("1.0.3, 1.1.1 (Skeleton,Phantom), 1.3 (Safety)")
@DocumentationId("11901")
@SuppressWarnings("unused")
public class ExprEntityShouldBurnInDay extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        register(ExprEntityShouldBurnInDay.class,Boolean.class, "[[living[ |-]]entity]",
                "(should|will) burn ((during|in) [the] day|(in|under) [the] (sun[light]|daylight))","livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return shouldBurnDuringTheDay(from);
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        setShouldBurnDuringTheDay(from, to);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "should burn under the sunlight";
    }
}