package it.jakegblp.lusk.utils;

import it.jakegblp.lusk.api.enums.Axis4D;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.lang.Math;

public class VectorUtils {

    public static EulerAngle toEulerAngle(Vector vector) {
        return new EulerAngle(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector toVector(EulerAngle eulerAngle) {
        return new Vector(eulerAngle.getX(), eulerAngle.getY(), eulerAngle.getZ());
    }

    public static Vector toDegreesVector(Vector vector) {
        return new Vector(Math.toDegrees(vector.getX()), Math.toDegrees(vector.getY()), Math.toDegrees(vector.getZ()));
    }

    public static Vector toDegreesVector(EulerAngle eulerAngle) {
        return new Vector(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ()));
    }

    public static Vector toRadiansVector(Vector vector) {
        return new Vector(Math.toRadians(vector.getX()), Math.toRadians(vector.getY()), Math.toRadians(vector.getZ()));
    }

    public static EulerAngle toRadiansEulerAngle(Vector vector) {
        return new EulerAngle(Math.toRadians(vector.getX()), Math.toRadians(vector.getY()), Math.toRadians(vector.getZ()));
    }

    @Nullable
    public static Number getCoordinate(Object object, Axis4D axis) {
        if (object instanceof Location location) {
            return switch (axis) {
                case X -> location.getX();
                case Y -> location.getY();
                case Z -> location.getZ();
                case W -> null;
            };
        } else if (object instanceof Vector vector) {
            return switch (axis) {
                case X -> vector.getX();
                case Y -> vector.getY();
                case Z -> vector.getZ();
                case W -> null;
            };
        } else if (object instanceof Chunk chunk) {
            return switch (axis) {
                case X -> chunk.getX();
                case Z -> chunk.getZ();
                case Y, W -> null;
            };
        } else if (object instanceof EulerAngle eulerAngle) {
            return switch (axis) {
                case X -> eulerAngle.getX();
                case Y -> eulerAngle.getY();
                case Z -> eulerAngle.getZ();
                case W -> null;
            };
        } else if (object instanceof Quaternionfc quaternionfc) {
            return switch (axis) {
                case X -> quaternionfc.x();
                case Y -> quaternionfc.y();
                case Z -> quaternionfc.z();
                case W -> quaternionfc.w();
            };
        } else if (object instanceof Quaterniondc quaterniondc) {
            return switch (axis) {
                case X -> quaterniondc.x();
                case Y -> quaterniondc.y();
                case Z -> quaterniondc.z();
                case W -> quaterniondc.w();
            };
        } else if (object instanceof AxisAngle4f axisAngle4f) {
            return switch (axis) {
                case X -> axisAngle4f.x;
                case Y -> axisAngle4f.y;
                case Z -> axisAngle4f.z;
                case W -> axisAngle4f.angle;
            };
        } else if (object instanceof AxisAngle4d axisAngle4d) {
            return switch (axis) {
                case X -> axisAngle4d.x;
                case Y -> axisAngle4d.y;
                case Z -> axisAngle4d.z;
                case W -> axisAngle4d.angle;
            };
        } else if (object instanceof Vector2d vector2d) {
            return switch (axis) {
                case X -> vector2d.x;
                case Y -> vector2d.y;
                case Z, W -> null;
            };
        } else if (object instanceof Vector2i vector2i) {
            return switch (axis) {
                case X -> vector2i.x;
                case Y -> vector2i.y;
                case Z, W -> null;
            };
        } else if (object instanceof Vector3d vector3d) {
            return switch (axis) {
                case X -> vector3d.x;
                case Y -> vector3d.y;
                case Z -> vector3d.z;
                case W -> null;
            };
        } else if (object instanceof Vector3f vector3f) {
            return switch (axis) {
                case X -> vector3f.x;
                case Y -> vector3f.y;
                case Z -> vector3f.z;
                case W -> null;
            };
        } else if (object instanceof Vector3i vector3i) {
            return switch (axis) {
                case X -> vector3i.x;
                case Y -> vector3i.y;
                case Z -> vector3i.z;
                case W -> null;
            };
        } else if (object instanceof Vector4d vector4d) {
            return switch (axis) {
                case X -> vector4d.x;
                case Y -> vector4d.y;
                case Z -> vector4d.z;
                case W -> vector4d.w;
            };
        } else if (object instanceof Vector4f vector4f) {
            return switch (axis) {
                case X -> vector4f.x;
                case Y -> vector4f.y;
                case Z -> vector4f.z;
                case W -> vector4f.w;
            };
        } else if (object instanceof Vector4i vector4i) {
            return switch (axis) {
                case X -> vector4i.x;
                case Y -> vector4i.y;
                case Z -> vector4i.z;
                case W -> vector4i.w;
            };
        }
        return null;

    }
}
