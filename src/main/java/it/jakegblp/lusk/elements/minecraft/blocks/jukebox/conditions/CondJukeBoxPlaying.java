package it.jakegblp.lusk.elements.minecraft.blocks.jukebox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.jetbrains.annotations.NotNull;

@Name("JukeBox - is Playing")
@Description("Checks if a jukebox is currently playing a music disc.")
@Examples({"if {_j} is playing:"})
@Since("1.0.3")
public class CondJukeBoxPlaying extends PropertyCondition<Block> {
    static {
        register(CondJukeBoxPlaying.class, "playing [[a] ([music] disc|record)]", "blocks");
    }

    @Override
    public boolean check(Block block) {
        return block.getState() instanceof Jukebox jukebox && jukebox.isPlaying();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "playing a music record";
    }
}