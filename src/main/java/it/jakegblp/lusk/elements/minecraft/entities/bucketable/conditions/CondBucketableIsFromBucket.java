package it.jakegblp.lusk.elements.minecraft.entities.bucketable.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import io.papermc.paper.entity.Bucketable;
import org.bukkit.entity.LivingEntity;

@Name("Bucketable - is From a Bucket")
@Description("Checks whether a living entity was previously in a bucket.")
@Examples({"if {_fish} is from a bucket:"})
@Since("1.3.8")
@RequiredPlugins("Paper 1.18+")
public class CondBucketableIsFromBucket extends PropertyCondition<LivingEntity> {

    static {
        register(CondBucketableIsFromBucket.class, "from [a] bucket", "livingentities");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        return livingEntity instanceof Bucketable bucketable && bucketable.isFromBucket();
    }

    @Override
    protected String getPropertyName() {
        return "from a bucket";
    }
}
