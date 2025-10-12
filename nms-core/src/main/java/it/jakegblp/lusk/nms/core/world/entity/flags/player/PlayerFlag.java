package it.jakegblp.lusk.nms.core.world.entity.flags.player;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum PlayerFlag implements BooleanFlag {
    CAPE,
    JACKET,
    LEFT_SLEEVE,
    RIGHT_SLEEVE,
    LEFT_PANTS,
    RIGHT_PANTS,
    HAT,
    UNUSED;
}