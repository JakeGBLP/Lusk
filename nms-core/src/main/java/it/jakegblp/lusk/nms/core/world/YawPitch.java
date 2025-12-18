package it.jakegblp.lusk.nms.core.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record YawPitch(double yaw, double pitch) {

    public static YawPitch between(Vector from, Vector to) {
        Vector dir = to.clone().subtract(from).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        yaw = ((yaw % 360) + 360) % 360;
        if (yaw > 180) yaw -= 360;
        return new YawPitch(yaw, (float) Math.toDegrees(-Math.asin(dir.getY())));
    }

    public float getYaw() {
        return (float) yaw;
    }

    public float getPitch() {
        return (float) pitch;
    }

    public Vector toDirection() {
        double rotX = Math.toRadians(-pitch);
        double rotY = Math.toRadians(-yaw - 180);
        double x = Math.sin(rotY) * Math.cos(rotX);
        double y = Math.sin(rotX);
        double z = Math.cos(rotY) * Math.cos(rotX);
        return new Vector(x, y, z).normalize();
    }

    public static YawPitch between(Location from, Location to) {
        return between(from.toVector(), to.toVector());
    }

    public Location applyTo(Location location) {
        location = location.clone();
        location.setYaw((float) yaw);
        location.setPitch((float) pitch);
        return location;
    }

    public Location applyTo(World world, Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ(), (float) yaw, (float) pitch);
    }
}
