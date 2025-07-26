package it.jakegblp.lusk.elements.minecraft.entities.bucketable.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import io.papermc.paper.entity.Bucketable;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_18;

@Name("Bucketable - Base Bucket Item")
@Description("Gets the base item of this entity in a bucket form.")
@Examples({"set {_bucket} to base bucket item of {_fish}"})
@Since("1.3.8")
@RequiredPlugins("Paper 1.18+")
public class ExprBucketableBaseBucketItem extends SimplerPropertyExpression<LivingEntity, ItemType> {

    static {
        if (PAPER_1_18)
            register(ExprBucketableBaseBucketItem.class, ItemType.class, "base bucket item", "livingentities");
    }

    @Override
    public @Nullable ItemType convert(LivingEntity from) {
        return from instanceof Bucketable bucketable ? new ItemType(bucketable.getBaseBucketItem()) : null;
    }

    @Override
    protected String getPropertyName() {
        return "base bucket item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}
