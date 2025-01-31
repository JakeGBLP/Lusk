package it.jakegblp.lusk.versions.nms.v1_19_4;

import it.jakegblp.lusk.api.NMSAdapter;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftIronGolem;
import org.bukkit.entity.IronGolem;

public class NMS_1_19_4 implements NMSAdapter {

    @Override
    public void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        ((CraftIronGolem) ironGolem).getHandle().offerFlower(shouldOffer);
    }

}
