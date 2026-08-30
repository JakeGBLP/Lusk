package it.jakegblp.lusk.nms.core.protocol.packets;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

public interface GenericVelocityPacket {

    double getVelocityX();
    double getVelocityY();
    double getVelocityZ();
    @Contract("-> new")
    Vector getVelocity();
    void setVelocityX(double x);
    void setVelocityY(double y);
    void setVelocityZ(double z);
    void setVelocity(Vector velocity);
}
