package it.jakegblp.lusk.nms.core.world.entity.flags.bee;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum BeeFlag implements BooleanFlag {
    UNUSED,
    IS_ANGRY,
    HAS_STUNG,
    HAS_NECTAR;
}