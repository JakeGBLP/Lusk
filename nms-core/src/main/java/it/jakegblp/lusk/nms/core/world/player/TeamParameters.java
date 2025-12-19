package it.jakegblp.lusk.nms.core.world.player;

import it.jakegblp.lusk.nms.core.util.NMSObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scoreboard.Team;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class TeamParameters implements NMSObject<Object>, Cloneable {

    protected Component displayName, playerPrefix, playerSuffix;
    protected Team.OptionStatus nametagVisibility, collisionRule;
    protected NamedTextColor color; // todo: expand to other formatting styles?
    protected boolean allowsFriendlyFire, canSeeFriendlyInvisibles;

    public TeamParameters(
            Component displayName,
            Component playerPrefix,
            Component playerSuffix,
            Team.OptionStatus nametagVisibility,
            Team.OptionStatus collisionRule,
            NamedTextColor color,
            int options
    ) {
        this(displayName, playerPrefix, playerSuffix, nametagVisibility, collisionRule, color, (options & 1) > 0, (options & 2) > 0);
    }

    public int getOptions() {
        int i = 0;
        if (allowsFriendlyFire)
            i |= 1;
        if (canSeeFriendlyInvisibles)
            i |= 2;
        return i;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSTeamParameters(this);
    }

    @Override
    public TeamParameters clone() throws CloneNotSupportedException {
        return (TeamParameters) super.clone();
    }
}
