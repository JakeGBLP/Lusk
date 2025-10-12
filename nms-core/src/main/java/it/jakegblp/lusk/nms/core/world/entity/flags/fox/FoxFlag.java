package it.jakegblp.lusk.nms.core.world.entity.flags.fox;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum FoxFlag implements BooleanFlag {
    IS_SITTING,
    UNUSED,
    IS_CROUCHING,
    IS_INTERESTED,
    IS_POUNCING,
    IS_SLEEPING,
    IS_FACEPLANTED,
    IS_DEFENDING;

}