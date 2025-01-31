package it.jakegblp.lusk.versions.nms.v1_16_5;

import it.jakegblp.lusk.api.NMSAdapter;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftIronGolem;
import org.bukkit.entity.IronGolem;

public class NMS_1_16_5 implements NMSAdapter {

    @Override
    public void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        ((CraftIronGolem) ironGolem).getHandle().t(shouldOffer);
    }

}
