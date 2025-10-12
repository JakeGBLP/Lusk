package it.jakegblp.lusk.nms.core.world.entity.flags.panda;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum PandaFlag implements BooleanFlag {
    UNUSED,
    IS_SNEEZING,
    IS_ROLLING,
    IS_SITTING,
    IS_ON_BACK;
}