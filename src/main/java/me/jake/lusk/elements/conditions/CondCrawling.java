package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("is Crawling")
@Description("Checks if a player is crawling")
@Examples({"on damage of player:\n\tif victim is crawling:\n\t\tif chance of 0.5:\n\t\t\tcancel event\n\t\t\tsend \"Your attack has failed!\" to attacker"})
@Since("1.0.0")
public class CondCrawling extends PropertyCondition<Player> {

    static {
        register(CondCrawling.class, "crawling", "player");
    }

    @Override
    public boolean check(Player player) {
        return Utils.isCrawling(player);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "crawling";
    }

}
