package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Jukebox - is Playing")
@Description("Checks if a jukebox is currently playing a music disc.")
@Example("if {_j} is playing:")
@Since("1.0.3, 1.3 (Blockstate)")
public class CondJukeboxPlaying extends PropertyCondition<BlockState> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondCanBeWaterLogged.class, CondCanBeWaterLogged::new)
                .property("blockstates", PropertyCondition.PropertyType.BE, "playing [[a] ([music] disc|record)]")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(BlockState state) {
        return state instanceof Jukebox jukebox && jukebox.isPlaying();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "playing a music disc";
    }
}