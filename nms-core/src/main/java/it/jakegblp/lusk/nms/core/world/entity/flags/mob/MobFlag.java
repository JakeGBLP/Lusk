package it.jakegblp.lusk.nms.core.world.entity.flags.mob;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum MobFlag implements BooleanFlag {
    NO_AI,
    IS_LEFT_HANDED,
    IS_AGGRESSIVE;
}