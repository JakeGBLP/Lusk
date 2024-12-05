package it.jakegblp.lusk.utils;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.bukkit.entity.ArmorStand;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_ARMOR_STAND_META;
import static it.jakegblp.lusk.utils.ItemUtils.getItemMetaFromUnknown;

public class ArmorStandUtils {

    public static void setIsMarker(Object object, boolean marker) {
        if (object instanceof ArmorStand armorStand) {
            armorStand.setMarker(marker);
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            meta.setMarker(marker);
        }
    }

    public static void setHasArms(Object object, boolean arms) {
        if (object instanceof ArmorStand armorStand) {
            armorStand.setArms(arms);
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            meta.setShowArms(arms);
        }
    }
    public static void setHasBasePlate(Object object, boolean basePlate) {
        if (object instanceof ArmorStand armorStand) {
            armorStand.setBasePlate(basePlate);
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            meta.setNoBasePlate(!basePlate);
        }
    }
    public static void setIsSmall(Object object, boolean small) {
        if (object instanceof ArmorStand armorStand) {
            armorStand.setSmall(small);
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            meta.setSmall(small);
        }
    }
    public static void setIsInvisible(Object object, boolean invisible) {
        if (object instanceof ArmorStand armorStand) {
            armorStand.setInvisible(invisible);
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            meta.setInvisible(invisible);
        }
    }

    public static boolean isMarker(Object object) {
        if (object instanceof ArmorStand armorStand) {
            return armorStand.isMarker();
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            return meta.isMarker();
        }
        return false;
    }
    public static boolean hasArms(Object object) {
        if (object instanceof ArmorStand armorStand) {
            return armorStand.hasArms();
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            return meta.shouldShowArms();
        }
        return false;
    }
    public static boolean hasBasePlate(Object object) {
        if (object instanceof ArmorStand armorStand) {
            return armorStand.hasBasePlate();
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            return !meta.hasNoBasePlate();
        }
        return false;
    }
    public static boolean isSmall(Object object) {
        if (object instanceof ArmorStand armorStand) {
            return armorStand.isSmall();
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            return meta.isSmall();
        }
        return false;
    }
    public static boolean isInvisible(Object object) {
        if (object instanceof ArmorStand armorStand) {
            return armorStand.isInvisible();
        } else if (PAPER_HAS_ARMOR_STAND_META && getItemMetaFromUnknown(object) instanceof ArmorStandMeta meta) {
            return meta.isInvisible();
        }
        return false;
    }
}
