package it.jakegblp.lusk.nms.core.protocol.packets;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

public interface GenericPositionPacket {

    double getPositionX();
    double getPositionY();
    double getPositionZ();
    @Contract("-> new")
    Vector getPosition();
    void setPositionX(double x);
    void setPositionY(double y);
    void setPositionZ(double z);
    void setPosition(Vector vector);
}
