package it.jakegblp.lusk.nms.core.world.level;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class LevelUtil {

    @Nullable
    public static Entity getEntityFromID(int id, World world){
        return NMS.getEntityFromId(id, world);
    }



}
