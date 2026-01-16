package it.jakegblp.lusk.nms.core.protocol.packets;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;

public interface Packet extends PureNMSObject<Object>, Asyncable, Copyable<Packet> {
}
