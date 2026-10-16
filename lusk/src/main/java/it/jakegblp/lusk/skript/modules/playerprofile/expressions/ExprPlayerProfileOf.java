package it.jakegblp.lusk.skript.modules.playerprofile.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.destroystokyo.paper.profile.PlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.profile.CompletablePlayerProfile;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerProfileOf extends SimplePropertyExpression<Object, CompletablePlayerProfile> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprPlayerProfileOf.class, ExprPlayerProfileOf::new, CompletablePlayerProfile.class)
                .propertyOf("[player] profile", "offlineplayers/itemtypes/blocks/blockstates")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable CompletablePlayerProfile convert(Object from) {
        PlayerProfile playerProfile = null;
        switch (from) {
            case OfflinePlayer player -> playerProfile = player.getPlayerProfile();
            case ItemType itemType -> {
                var item = itemType.getRandom();
                playerProfile = item != null && item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta skullMeta ? skullMeta.getPlayerProfile() : null;
            }
            case Block block when block.getState() instanceof Skull skull -> playerProfile = skull.getPlayerProfile();
            case Skull skull -> playerProfile = skull.getPlayerProfile();
            case null, default -> {
            }
        }
        if (playerProfile == null) return null;
        return new CompletablePlayerProfile(playerProfile);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (delta != null && delta.length > 0) {
            PlayerProfile playerProfile;
            if (delta[0] instanceof PlayerProfile profile)
                playerProfile = profile;
            else if (delta[0] instanceof CompletablePlayerProfile profile) {
                if (profile.shouldComplete()) profile.complete();
                playerProfile = profile.getPlayerProfile();
            } else return;
            for (Object object : getExpr().getArray(event)) {
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
        return mode == Changer.ChangeMode.SET ? new Class[]{PlayerProfile.class, CompletablePlayerProfile.class} : null;
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
