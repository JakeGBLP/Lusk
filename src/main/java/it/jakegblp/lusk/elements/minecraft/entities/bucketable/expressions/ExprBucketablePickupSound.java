package it.jakegblp.lusk.elements.minecraft.entities.bucketable.expressions;

import ch.njol.skript.doc.*;
import io.papermc.paper.entity.Bucketable;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18;

@Name("Bucketable - Bucket Pickup Sound")
@Description("Gets the sound that is played when the provided entity is picked up in a bucket.")
@Examples({"set {_sound} to bucket pickup sound of {_fish}"})
@Since("1.3.8")
@RequiredPlugins("Paper 1.18+")
public class ExprBucketablePickupSound extends SimplerPropertyExpression<LivingEntity, Sound> {

    static {
        if (PAPER_1_18)
            register(ExprBucketablePickupSound.class, Sound.class, "bucket pickup sound", "livingentities");
    }

    @Override
    public @Nullable Sound convert(LivingEntity from) {
        return from instanceof Bucketable bucketable ? bucketable.getPickupSound() : null;
    }

    @Override
    protected String getPropertyName() {
        return "bucket pickup sound";
    }

    @Override
    public Class<? extends Sound> getReturnType() {
        return Sound.class;
    }
}
