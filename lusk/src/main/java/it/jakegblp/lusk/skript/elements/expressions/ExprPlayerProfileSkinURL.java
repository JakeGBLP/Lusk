package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class ExprPlayerProfileSkinURL extends SimplePropertyExpression<Object, URL> {

    static {
        register(ExprPlayerProfileSkinURL.class, URL.class, "[player] [profile] (:skin|cape) url", "players/playerprofiles/itemtypes/blocks/blockstates");
    }

    private boolean skin;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        skin = parseResult.hasTag("skin");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable URL convert(Object from) {
        PlayerProfile profile;
        if (from instanceof PlayerProfile profile1)
            profile = profile1;
        else if (from instanceof Player player)
            profile = player.getPlayerProfile();
        else if (from instanceof ItemType itemType && itemType.getItemMeta() instanceof SkullMeta skullMeta)
            profile = skullMeta.getPlayerProfile();
        else if (from instanceof Block block && block.getState() instanceof Skull skull)
            profile = skull.getPlayerProfile();
        else if (from instanceof Skull skull)
            profile = skull.getPlayerProfile();
        else return null;
        if (profile == null || !profile.hasTextures()) return null;
        var textures = profile.getTextures();
        return skin ? textures.getSkin() : textures.getCape();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta.length > 0 && delta[0] instanceof URL url) {
            for (Object object : getExpr().getAll(event)) {
                PlayerProfile profile;
                if (object instanceof PlayerProfile playerProfile)
                    profile = playerProfile;
                else if (object instanceof Player player)
                    profile = player.getPlayerProfile();
                else if (object instanceof ItemType itemType && itemType.getItemMeta() instanceof SkullMeta skullMeta)
                    profile = skullMeta.getPlayerProfile();
                else if (object instanceof Block block && block.getState() instanceof Skull skull)
                    profile = skull.getPlayerProfile();
                else if (object instanceof Skull skull)
                    profile = skull.getPlayerProfile();
                else continue;
                if (profile == null) continue;
                var textures = profile.getTextures();
                if (skin) textures.setSkin(url);
                else textures.setCape(url);
            }
        }
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[]{URL.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    protected String getPropertyName() {
        return "player profile " + (skin ? "skin" : "cape") + " url";
    }

    @Override
    public Class<? extends URL> getReturnType() {
        return URL.class;
    }
}
