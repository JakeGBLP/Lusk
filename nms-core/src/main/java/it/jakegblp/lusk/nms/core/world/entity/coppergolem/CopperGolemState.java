package it.jakegblp.lusk.nms.core.world.entity.coppergolem;

import it.jakegblp.lusk.nms.core.util.PureNMSObject;

// https://github.com/PaperMC/Paper/issues/13558
public enum CopperGolemState implements PureNMSObject<Object> {
    IDLE,
    GETTING_ITEM,
    GETTING_NO_ITEM,
    DROPPING_ITEM,
    DROPPING_NO_ITEM
}
