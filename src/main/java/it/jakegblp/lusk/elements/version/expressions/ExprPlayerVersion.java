package it.jakegblp.lusk.elements.version.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.vdurmont.semver4j.Semver;
import com.viaversion.viaversion.api.Via;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("Player - Client Version")
@Description("Returns the Minecraft version of a player, snapshots are not included.\nAdditionally, you can get the client's version with via.")
@Examples("""
        broadcast version of player
        
        if version of player = 1.19.3:
            send "why so specific of a version?"
        
        if version of player < 1.9.0:
            send "i think you should update but you do you"
        
        if via version of player = 26.1.2:
            send "good boy"
        """)
@Since("1.0.0, 1.3.9 (Plural), 1.3.14 (Via)")
@SuppressWarnings("unused")
public class ExprPlayerVersion extends SimplePropertyExpression<Player, Semver> {
    static {
        register(ExprPlayerVersion.class, Semver.class, "[via:via[[ ]version]] [minecraft|client|player] version", "players");
    }

    protected boolean via;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        via = parseResult.hasTag("via");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends Semver> getReturnType() {
        return Semver.class;
    }

    @Override
    public Semver convert(Player p) {
        return Constants.versions.get(via ? Via.getAPI().getPlayerVersion(p) : p.getProtocolVersion());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return (via ? "via " : "") + "client version";
    }
}