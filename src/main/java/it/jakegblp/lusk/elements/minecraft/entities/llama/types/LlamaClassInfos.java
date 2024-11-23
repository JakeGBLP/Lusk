package it.jakegblp.lusk.elements.minecraft.entities.llama.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Llama;

@SuppressWarnings("unused")
public class LlamaClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Llama$Color") && Classes.getExactClassInfo(Llama.Color.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Llama.Color.class, null, "llama_color")
                            .getClassInfo("llamacolor")
                            .user("llama ?(variant|colou?r|type)s?")
                            .description("All the Llama colors.")
                            .since("1.3"));
        }

    }
}
