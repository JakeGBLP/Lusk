package me.jake.lusk.utils;

import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class TransformationUtils {
    public static void setScale(Display display, float x, float y, float z) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = new Vector3f(x,y,z);
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setScaleX(Display display, float x) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = transformation.getScale();
        float y = scale.y;
        float z = scale.z;
        scale = new Vector3f(x,y,z);
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setScaleY(Display display, float y) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = transformation.getScale();
        float x = scale.x;
        float z = scale.z;
        scale = new Vector3f(x,y,z);
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setScaleZ(Display display, float z) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = transformation.getScale();
        float x = scale.x;
        float y = scale.y;
        scale = new Vector3f(x,y,z);
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setTranslation(Display display, float x, float y, float z) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = transformation.getScale();
        Vector3f translation = new Vector3f(x,y,z);
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setLeftRotation(Display display, float x, float y, float z, float w) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = quaternion(new Vector(x,y,z),w);
        Quaternionf rightRotation = transformation.getRightRotation();
        Vector3f scale = transformation.getScale();
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static void setRightRotation(Display display, float x, float y, float z, float w) {
        Transformation transformation = display.getTransformation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Quaternionf rightRotation = quaternion(new Vector(x,y,z),w);
        Vector3f scale = transformation.getScale();
        Vector3f translation = transformation.getTranslation();
        display.setTransformation(new Transformation(translation,leftRotation,scale,rightRotation));
    }
    public static Quaternionf quaternion(Vector vector, float angle) {
        Vector axisNorm = vector.clone().normalize();
        float halfAngle = angle / 2;
        float sin = (float) Math.sin(halfAngle);
        float w = (float) Math.cos(halfAngle);
        float x = (float) (axisNorm.getX() * sin);
        float y = (float) (axisNorm.getY() * sin);
        float z = (float) (axisNorm.getZ() * sin);
        return new Quaternionf(x,y,z,w);
    }

}
