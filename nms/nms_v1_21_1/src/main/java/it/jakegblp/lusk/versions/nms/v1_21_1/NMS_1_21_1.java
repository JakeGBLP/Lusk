package it.jakegblp.lusk.versions.nms.v1_21_1;

import it.jakegblp.lusk.api.NMSAdapter;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftIronGolem;
import org.bukkit.entity.IronGolem;

public class NMS_1_21_1 implements NMSAdapter {

    @Override
    public void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        ((CraftIronGolem) ironGolem).getHandle().offerFlower(shouldOffer);
    }

}
