package it.jakegblp.lusk.nms.core.world.player;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.BufferSerializable;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scoreboard.Team;

@AllArgsConstructor
@Getter
@Setter
public class TeamParameters implements PureNMSObject<Object>, BufferSerializable<TeamParameters>, Copyable<TeamParameters> {

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

    public TeamParameters(SimpleByteBuf buffer) {
        read(buffer);
    }

    public int getOptions() {
        int i = 0;
        if (allowsFriendlyFire)
            i |= 1;
        if (canSeeFriendlyInvisibles)
            i |= 2;
        return i;
    }

    public void setOptions(int flags) {
        this.allowsFriendlyFire = (flags & 1) != 0;
        this.canSeeFriendlyInvisibles = (flags & 2) != 0;
    }

    @Override
    public TeamParameters copy() {
        return new TeamParameters(displayName, playerPrefix, playerSuffix, nametagVisibility, collisionRule, color, allowsFriendlyFire, canSeeFriendlyInvisibles);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeComponent(displayName);
        buffer.writeEnum(nametagVisibility);
        buffer.writeByte(getOptions());
        buffer.writeEnum(collisionRule);
        buffer.writeNamedTextColor(color);
        buffer.writeComponent(playerPrefix);
        buffer.writeComponent(playerSuffix);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        displayName = buffer.readComponent();
        nametagVisibility = buffer.readEnum(Team.OptionStatus.class);
        setOptions(buffer.readByte());
        collisionRule = buffer.readEnum(Team.OptionStatus.class);
        color = buffer.readNamedTextColor();
        playerPrefix = buffer.readComponent();
        playerSuffix = buffer.readComponent();
    }
}
