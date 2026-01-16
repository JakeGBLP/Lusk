package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;

public enum EntityFlag implements BooleanFlag {
    BURNING,
    SNEAKING,
    UNUSED, // Previously riding
    SPRINTING,
    SWIMMING,
    INVISIBLE,
    GLOWING,
    GLIDING
}