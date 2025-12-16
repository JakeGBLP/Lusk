package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.destroystokyo.paper.profile.PlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerProfileOf extends SimplePropertyExpression<Object, CompletablePlayerProfile> {

    static {
        register(ExprPlayerProfileOf.class, CompletablePlayerProfile.class, "[player] profile", "offlineplayers/itemtypes/blocks/blockstates");
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable CompletablePlayerProfile convert(Object from) {
        PlayerProfile playerProfile = null;
        if (from instanceof OfflinePlayer player)
            playerProfile = player.getPlayerProfile();
        else if (from instanceof ItemType itemType) {
            var item = itemType.getRandom();
            playerProfile = item != null && item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta skullMeta ? skullMeta.getPlayerProfile() : null;
        } else if (from instanceof Block block && block.getState() instanceof Skull skull)
            playerProfile = skull.getPlayerProfile();
        else if (from instanceof Skull skull)
            playerProfile = skull.getPlayerProfile();
        if (playerProfile == null) return null;
        return new CompletablePlayerProfile(playerProfile);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (delta != null && delta.length > 0 && delta[0] instanceof PlayerProfile playerProfile) {
            for (Object object : getExpr().getAll(event)) {
                if (object instanceof Player player)
                    player.setPlayerProfile(playerProfile);
                else if (object instanceof ItemType itemType) {
                    if (itemType.getItemMeta() instanceof SkullMeta skullMeta) {
                        skullMeta.setPlayerProfile(playerProfile);
                        itemType.setItemMeta(skullMeta);
                    }
                } else if (object instanceof Block block && block.getState() instanceof Skull skull) {
                    skull.setPlayerProfile(playerProfile);
                    skull.update();
                } else if (object instanceof Skull skull) {
                    skull.setPlayerProfile(playerProfile);
                    skull.update();
                }
            }
        }
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{PlayerProfile.class} : new Class[0];
    }

    @Override
    protected String getPropertyName() {
        return "player profile";
    }

    @Override
    public Class<? extends CompletablePlayerProfile> getReturnType() {
        return CompletablePlayerProfile.class;
    }
}
