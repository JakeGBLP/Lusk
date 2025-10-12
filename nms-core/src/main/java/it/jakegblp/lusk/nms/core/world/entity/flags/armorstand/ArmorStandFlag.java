package it.jakegblp.lusk.nms.core.world.entity.flags.armorstand;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum ArmorStandFlag implements BooleanFlag {
    IS_SMALL,
    UNUSED,
    HAS_ARMS,
    HAS_NO_BASEPLATE,
    IS_MARKER;
}