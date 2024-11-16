package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import io.papermc.paper.registry.RegistryKey;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_PAPER_REGISTRY_KEY;

@SuppressWarnings("unused")
public class PaperUtils {
    /**
     * Check if a PaperMC registry exists
     *
     * @param registry Registry to check for (Fully qualified name of registry)
     * @return True if registry exists else false
     */
    public static boolean registryExists(String registry) {
        return PAPER_HAS_PAPER_REGISTRY_KEY && Skript.fieldExists(RegistryKey.class, registry);
    }

}
