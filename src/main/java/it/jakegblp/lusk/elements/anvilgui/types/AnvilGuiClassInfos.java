package it.jakegblp.lusk.elements.anvilgui.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;

public class AnvilGuiClassInfos {
    static {
        Classes.registerClass(new ClassInfo<>(AnvilGuiWrapper.class, "anvilguiinventory")
                .user("anvil ?gui ?inventor(y|ies)")
                .name("Anvil Gui")
                .description("An anvil gui.") // add example
                .since("1.3"));
    }
}
