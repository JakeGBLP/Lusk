package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Fox;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Fox - Trusted Players")
@Description("""
        Returns the first, second, or both trusted players of 1 or more foxes.
        
        *NOTES*:
        - The first and second trusted players can both be set to a single `offline player`, the trusted players can be set to 2 offline players (or 1, which only sets the first one).
        - The second trusted player must be set after the first one, which also means you can't clear the first one if the second one is set, both actions will fail silently.
        - If you wish to set/clear both, do so by using the second pattern: `clear trusted players of {_fox}`
        """)
@Examples({"broadcast first trusted player of {_fox}", "set trusted players of {_fox} to {_notch} and {_steve}"})
@Since("1.2")
@SuppressWarnings("unused")
public class ExprFoxTrustedPlayers extends PropertyExpression<Fox, OfflinePlayer> {

    static {
        register(ExprFoxTrustedPlayers.class, OfflinePlayer.class, "([first|:second] trusted player|both:trusted players)", "foxes");
    }

    int state;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        state = parseResult.hasTag("both") ? 1 : parseResult.hasTag("second") ? 0 : -1;
        //state = matchedPattern > 1 ? 2 : (parseResult.hasTag("second") ? 1 : 0);
        setExpr((Expression<? extends Fox>) expressions[0]);
        return true;
    }

    @Override
    protected OfflinePlayer @NotNull [] get(@NotNull Event event, Fox @NotNull [] source) {
        List<OfflinePlayer> trusted = new ArrayList<>();
        for (Fox fox : source) {
            if (state != 0 && fox.getFirstTrustedPlayer() instanceof OfflinePlayer player)
                trusted.add(player);
            if (state != -1 && fox.getSecondTrustedPlayer() instanceof OfflinePlayer player)
                trusted.add(player);
        }
        return trusted.toArray(new OfflinePlayer[0]);
    }

    @Override
    public boolean isSingle() {
        return state == 1 || super.isSingle();
    }

    @Override
    public @NotNull Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) return new Class[0];
        else if (mode == Changer.ChangeMode.SET) {
            return new Class[]{state == 1 ? OfflinePlayer[].class : OfflinePlayer.class};
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            if (state == -1) {
                for (Fox fox : getExpr().getArray(event)) {
                    if (fox.getSecondTrustedPlayer() == null)
                        fox.setFirstTrustedPlayer(null);
                }
            } else if (state == 0) {
                for (Fox fox : getExpr().getArray(event)) {
                    fox.setSecondTrustedPlayer(null);
                }
            } else {
                for (Fox fox : getExpr().getArray(event)) {
                    fox.setSecondTrustedPlayer(null);
                    fox.setFirstTrustedPlayer(null);
                }
            }
        } else if (mode == Changer.ChangeMode.SET) {
            if (delta == null || delta[0] == null) return;
            if (state == 1 && delta instanceof OfflinePlayer[] offlinePlayers) {
                for (Fox fox : getExpr().getArray(event)) {
                    if (offlinePlayers[0] != null) fox.setFirstTrustedPlayer(offlinePlayers[0]);
                    if (offlinePlayers[1] != null) fox.setFirstTrustedPlayer(offlinePlayers[1]);
                }
            } else if (delta[0] instanceof OfflinePlayer offlinePlayer) {
                if (state == -1) {
                    for (Fox fox : getExpr().getArray(event)) {
                        fox.setFirstTrustedPlayer(offlinePlayer);
                    }
                } else if (state == 0) {
                    for (Fox fox : getExpr().getArray(event)) {
                        if (fox.getFirstTrustedPlayer() != null)
                            fox.setSecondTrustedPlayer(offlinePlayer);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        StringBuilder builder = new StringBuilder();
        if (state == 1) builder.append("trusted players");
        else builder.append(state == -1 ? "first" : "second").append(" player");
        return builder.append(" of ").append(getExpr().toString(event, debug)).toString();
    }
}
