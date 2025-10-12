package it.jakegblp.lusk.nms.core.world.entity.flags.abstracthorse;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum AbstractHorseFlag implements BooleanFlag {
    UNUSED_1,
    IS_TAME,
    UNUSED_2,
    HAS_BRED,
    IS_EATING,
    IS_REARING,
    HAS_MOUTH_OPEN;
}