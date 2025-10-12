package it.jakegblp.lusk.nms.core.world.entity.flags.projectile.arrow;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum AbstractArrowFlag implements BooleanFlag {
    IS_CRITICAL,
    NO_CLIP;
}