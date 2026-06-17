package it.jakegblp.lusk.nms.core.protocol.packets;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GenericMovementPacket {

    double getMovementX();
    double getMovementY();
    double getMovementZ();
    @Contract("-> new")
    Vector getMovement();
    void setMovementX(double x);
    void setMovementY(double y);
    void setMovementZ(double z);
    void setMovement(Vector movement);
}
