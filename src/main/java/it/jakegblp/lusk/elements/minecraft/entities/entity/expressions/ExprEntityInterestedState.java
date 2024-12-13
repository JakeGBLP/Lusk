package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.isInterested;
import static it.jakegblp.lusk.utils.EntityUtils.setIsInterested;

@Name("Wolf/Fox - is Interested (Property)")
@Description("Returns whether or not a fox or wolf is interested.\nPaper is required to use this with foxes.")
@Examples({"broadcast interested state of target"})
@Since("1.0.2, 1.3 (Plural)")
@DocumentationId("9056")
@SuppressWarnings("unused")
public class ExprEntityInterestedState extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        register(ExprEntityInterestedState.class,Boolean.class,
                "[[living[ |-]]entity]", "[is] interested", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return isInterested(from);
    }

    @Override
    protected String getPropertyName() {
        return "entity is interested property";
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Boolean bool) {
        setIsInterested(from,bool);
    }

    @Override
    public void reset(LivingEntity from) {
        setIsInterested(from,false);
    }
}