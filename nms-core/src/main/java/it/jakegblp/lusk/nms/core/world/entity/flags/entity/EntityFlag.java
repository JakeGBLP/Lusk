package it.jakegblp.lusk.nms.core.world.entity.flags.entity;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum EntityFlag implements BooleanFlag {
    BURNING,
    SNEAKING,
    UNUSED, // Previously riding
    SPRINTING,
    SWIMMING,
    INVISIBLE,
    GLOWING,
    GLIDING;
}