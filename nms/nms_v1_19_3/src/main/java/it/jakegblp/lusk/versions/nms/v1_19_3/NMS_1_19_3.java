package it.jakegblp.lusk.versions.nms.v1_19_3;

import it.jakegblp.lusk.api.NMSAdapter;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftIronGolem;
import org.bukkit.entity.IronGolem;

public class NMS_1_19_3 implements NMSAdapter {

    @Override
    public void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        ((CraftIronGolem) ironGolem).getHandle().offerFlower(shouldOffer);
    }

}
