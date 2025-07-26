package it.jakegblp.lusk.elements.minecraft.entities.bucketable.expressions;

import ch.njol.skript.doc.*;
import io.papermc.paper.entity.Bucketable;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18;

@Name("Bucketable - is From a Bucket (Property)")
@Description("Gets whether a living entity was previously in a bucket.\nCan be set and reset.")
@Examples({"send is from a bucket state of {_fish}"})
@Since("1.3.8")
@RequiredPlugins("Paper 1.18+")
public class ExprBucketableIsFromBucket extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (PAPER_1_18)
            register(ExprBucketableIsFromBucket.class, Boolean.class, "[bucketable]", "[is] from [a] bucket", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Bucketable bucketable && bucketable.isFromBucket();
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Bucketable bucketable) {
            bucketable.setFromBucket(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "bucketable is from bucket";
    }
}