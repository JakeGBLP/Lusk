package it.jakegblp.lusk.elements.minecraft.blocks.jukebox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Jukebox - is Playing")
@Description("Checks if a jukebox is currently playing a music disc.")
@Examples({"if {_j} is playing:"})
@Since("1.0.3, 1.3 (Blockstate)")
@DocumentationId("9121")
public class CondJukeboxPlaying extends PropertyCondition<Object> {
    static {
        register(CondJukeboxPlaying.class, "playing [[a] ([music] disc|record)]", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isJukeboxPlaying();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "playing a music record";
    }
}