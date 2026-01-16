package it.jakegblp.lusk.nms.core.util;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface PureNMSObject<T> extends NMSObject<T> {
    @Override
    @SuppressWarnings("unchecked")
    default T asNMS() {
        return (T) NMS.fromNMS(this);
    }
}
