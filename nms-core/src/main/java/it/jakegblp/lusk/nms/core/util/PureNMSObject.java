package it.jakegblp.lusk.nms.core.util;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface PureNMSObject<T> extends NMSObject<T> {
    @Override
    default T asNMS() {
        return NMS.toNMS(this);
    }
}
