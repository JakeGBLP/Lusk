package it.jakegblp.lusk.elements.version.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.vdurmont.semver4j.Semver;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("Player - Client Version")
@Description("Returns the Minecraft version of a player, snapshots are not included.")
@Examples("""
        broadcast version of player
        
        if version of player = 1.19.3:
            send "why so specific of a version?"
        
        if version of player < 1.9.0:
            send "i think you should update but you do you"
        """)
@Since("1.0.0, 1.3.9 (Plural)")
@SuppressWarnings("unused")
public class ExprPlayerVersion extends SimplePropertyExpression<Player, Semver> {
    static {
        register(ExprPlayerVersion.class, Semver.class, "[minecraft|client|player] version", "players");
    }

    @Override
    public @NotNull Class<? extends Semver> getReturnType() {
        return Semver.class;
    }

    @Override
    public Semver convert(Player p) {
        return Constants.versions.get(p.getProtocolVersion());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "client version";
    }
}