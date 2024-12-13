package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.setShouldBurnDuringTheDay;
import static it.jakegblp.lusk.utils.EntityUtils.shouldBurnDuringTheDay;
import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Zombie/Skeleton(s)/Phantom - Should Burn During The Day (Property)")
@Description("Returns whether or not an entity should burn during the day.\nCan be set.")
@Examples({"broadcast the should burn under sunlight property of target"})
@Since("1.0.3, 1.1.1 (Skeleton,Phantom)")
@SuppressWarnings("unused")
public class ExprEntityShouldBurnInDayState extends SimplePropertyExpression<LivingEntity,Boolean> {
    static {
        registerVerboseBooleanPropertyExpression(ExprEntityShouldBurnInDayState.class,Boolean.class,
                "[[living[ |-]]entity]","(should|will|would) burn (during the day|(in|under) [the] (sun|day)light)","livingentities");
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            for (LivingEntity livingEntity : getExpr().getAll(e)) {
                setShouldBurnDuringTheDay(livingEntity,aBoolean);
            }
        }
    }
    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return shouldBurnDuringTheDay(from);
    }

    @Override
    protected String getPropertyName() {
        return "living entity should burn under the sunlight property";
    }
}