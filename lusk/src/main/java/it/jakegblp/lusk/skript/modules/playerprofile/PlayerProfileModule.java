package it.jakegblp.lusk.skript.modules.playerprofile;

import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.nms.core.world.player.profile.MutableProfileProperty;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.playerprofile.effects.EffPlayerProfileMakeCompletable;
import it.jakegblp.lusk.skript.modules.playerprofile.expressions.*;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class PlayerProfileModule extends LuskModule {

    @Override
    protected void registration(SyntaxRegistry registry) {
        EffPlayerProfileMakeCompletable.register(registry);
        ExprPlayerProfile.register(registry);
        ExprPlayerProfileCompletable.register(registry);
        ExprPlayerProfileFromTexturePayload.register(registry);
        ExprPlayerProfileOf.register(registry);
        ExprPlayerProfileProperties.register(registry);
        ExprPlayerProfileProperty.register(registry);
        ExprPlayerProfileSkinURL.register(registry);
        ExprSecPlayerProfile.register(registry);
        ExprSecPlayerProfileProperty.register(registry);
        ExprSecTexturePayLoad.register(registry);
    }

    @Override
    public void init(SkriptAddon addon) {
        /*
        Mutability and cross-addon support: if another addon registers the immutable object as a classinfo,
         instances of the mutable one can be converted to that type.
         */
        Converters.registerConverter(MutableProfileProperty.class, ProfileProperty.class, MutableProfileProperty::immutable);
        Converters.registerConverter(ProfileProperty.class, MutableProfileProperty.class, MutableProfileProperty::new);
    }

    @Override
    public String name() {
        return "player profile";
    }
}
