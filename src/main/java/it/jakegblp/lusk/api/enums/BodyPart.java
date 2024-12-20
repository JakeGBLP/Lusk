package it.jakegblp.lusk.api.enums;

import it.jakegblp.lusk.utils.VectorUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Body parts for armor stands, this can be extended in the future to include other things.<br>
 * Each rotation/pose is originally an {@link EulerAngle} and is then converted into a Vector,
 * the same is done in reverse.
 */
public enum BodyPart {
    LEFT_ARM(ArmorStand::setLeftArmPose, ArmorStand::getLeftArmPose),
    RIGHT_ARM(ArmorStand::setRightArmPose, ArmorStand::getRightArmPose),
    LEFT_LEG(ArmorStand::setLeftLegPose, ArmorStand::getLeftLegPose),
    RIGHT_LEG(ArmorStand::setRightLegPose, ArmorStand::getRightLegPose),
    HEAD(ArmorStand::setHeadPose, ArmorStand::getHeadPose),
    BODY(ArmorStand::setBodyPose, ArmorStand::getBodyPose);

    private final BiConsumer<ArmorStand, EulerAngle> poseSetter;
    private final Function<ArmorStand, EulerAngle> poseGetter;

    BodyPart(BiConsumer<ArmorStand, EulerAngle> poseSetter, Function<ArmorStand, EulerAngle> poseGetter) {
        this.poseSetter = poseSetter;
        this.poseGetter = poseGetter;
    }

    /**
     * Sets the pose of the body part for the given ArmorStand.
     *
     * @param armorStand the armor stand whose body part pose is being set.
     * @param vector the new {@link Vector} degree rotation; if null, the rotation will be reset.
     */
    public void set(@NotNull ArmorStand armorStand, @Nullable Vector vector) {
        poseSetter.accept(armorStand, vector == null ? EulerAngle.ZERO : VectorUtils.toRadiansEulerAngle(vector));
    }

    /**
     * Gets the current pose of the body part for the given ArmorStand.
     *
     * @param armorStand the armor stand whose body part pose is being retrieved.
     * @return the provided armor stand's rotation of this bodypart in degrees as a {@link Vector}.
     */
    public Vector get(@NotNull ArmorStand armorStand) {
        return VectorUtils.toDegreesVector(poseGetter.apply(armorStand));
    }
}