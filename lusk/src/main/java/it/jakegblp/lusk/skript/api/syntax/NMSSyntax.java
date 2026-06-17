package it.jakegblp.lusk.skript.api.syntax;

import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeyRegistry;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

// todo: addon utils method to register nms syntax with NMS instance
public interface NMSSyntax {

    default MetadataKeyRegistry metadataKeyRegistry() {
        return NMS.getMetadataKeyRegistry();
    }
}
