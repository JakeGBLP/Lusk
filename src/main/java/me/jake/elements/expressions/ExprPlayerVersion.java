package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.jake.lusk.classes.Version;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Player Version")
@Description("Returns the Minecraft Version of a player, only major versions ) are included.")
@Examples({"broadcast version of player\n\nkick player due to \"stop using 1.8!\" if version of player = \"1.8.9\""})
@Since("1.0.0")
public class ExprPlayerVersion extends SimplePropertyExpression<Player, Version> {
    static {
        register(ExprPlayerVersion.class, Version.class, "[minecraft|client|player] version", "player");
    }

    @Override
    public @NotNull Class<? extends Version> getReturnType() {
        return Version.class;
    }

    @Override
    @Nullable
    public Version convert(Player p) {
        if (p != null) {
            return Utils.getPlayerVersion(p.getProtocolVersion());
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "client version";
    }
}