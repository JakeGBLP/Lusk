package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.avatar;

import com.destroystokyo.paper.SkinParts;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;

import java.util.List;


public class AvatarFlags extends FlagByte<LivingEntity> implements Copyable<AvatarFlags> {
    public static final BooleanFlag<LivingEntity> CAPE = Flag.complexBooleanFlag("CAPE", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasCapeEnabled, SkinParts.Mutable::setCapeEnabled);
    public static final BooleanFlag<LivingEntity> JACKET = Flag.complexBooleanFlag("JACKET", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasJacketEnabled, SkinParts.Mutable::setJacketEnabled);
    public static final BooleanFlag<LivingEntity> LEFT_SLEEVE = Flag.complexBooleanFlag("LEFT_SLEEVE", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasLeftSleeveEnabled, SkinParts.Mutable::setLeftSleeveEnabled);
    public static final BooleanFlag<LivingEntity> RIGHT_SLEEVE = Flag.complexBooleanFlag("RIGHT_SLEEVE", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasRightSleeveEnabled, SkinParts.Mutable::setRightSleeveEnabled);
    public static final BooleanFlag<LivingEntity> LEFT_PANTS = Flag.complexBooleanFlag("LEFT_PANTS", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasLeftPantsEnabled, SkinParts.Mutable::setLeftPantsEnabled);
    public static final BooleanFlag<LivingEntity> RIGHT_PANTS = Flag.complexBooleanFlag("RIGHT_PANTS", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasRightPantsEnabled, SkinParts.Mutable::setRightPantsEnabled);
    public static final BooleanFlag<LivingEntity> HAT = Flag.complexBooleanFlag("HAT", EntityUtils::getSkinParts, EntityUtils::setSkinParts, SkinParts::hasHatsEnabled, SkinParts.Mutable::setHatsEnabled);
    public static final BooleanFlag<LivingEntity> UNUSED = Flag.unusedBooleanFlag("UNUSED");

    @SuppressWarnings("unchecked")
    private static final Flag<LivingEntity, ?, ?>[] FLAGS = new Flag[] {CAPE, JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANTS, RIGHT_PANTS, HAT, UNUSED};

    public static Flag<LivingEntity, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<LivingEntity, ?, ?>[] getConstants() {
        return getFlags();
    }

    public AvatarFlags(Player entity) {
        super(entity);
    }

    public AvatarFlags() {
        super();
    }

    @Override
    public List<Class<? extends LivingEntity>> getFlagHolderClasses() {
        return List.of(Player.class, Mannequin.class);
    }

    public AvatarFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<LivingEntity> getFlagHolderClass() {
        return LivingEntity.class;
    }

    public boolean hasCape() {
        return get(CAPE);
    }

    public void setCape(boolean enabled) {
        set(CAPE, enabled);
    }

    public boolean hasJacket() {
        return get(JACKET);
    }

    public void setJacket(boolean enabled) {
        set(JACKET, enabled);
    }

    public boolean hasLeftSleeve() {
        return get(LEFT_SLEEVE);
    }

    public void setLeftSleeve(boolean enabled) {
        set(LEFT_SLEEVE, enabled);
    }

    public boolean hasRightSleeve() {
        return get(RIGHT_SLEEVE);
    }

    public void setRightSleeve(boolean enabled) {
        set(RIGHT_SLEEVE, enabled);
    }

    public boolean hasLeftPants() {
        return get(LEFT_PANTS);
    }

    public void setLeftPants(boolean enabled) {
        set(LEFT_PANTS, enabled);
    }

    public boolean hasRightPants() {
        return get(RIGHT_PANTS);
    }

    public void setRightPants(boolean enabled) {
        set(RIGHT_PANTS, enabled);
    }

    public boolean hasHat() {
        return get(HAT);
    }

    public void setHat(boolean enabled) {
        set(HAT, enabled);
    }

    @Override
    public AvatarFlags copy() {
        return new AvatarFlags(flags);
    }

}