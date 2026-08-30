package it.jakegblp.lusk.skript.modules.playerprofile;

import ch.njol.skript.registrations.Classes;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.nms.core.world.player.profile.MutableProfileProperty;
import it.jakegblp.lusk.skript.api.classinfo.SimpleClassInfo;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.playerprofile.effects.EffPlayerProfileMakeCompletable;
import it.jakegblp.lusk.skript.modules.playerprofile.expressions.*;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.IntConsumer;

public class PlayerProfileModule extends LuskModule {

    public PlayerProfileModule(IntConsumer adder) {
        super(adder);
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        EffPlayerProfileMakeCompletable.register(registry);
        ExprPlayerProfile.register(registry);
        ExprPlayerProfileCompletable.register(registry);
        ExprPlayerProfileFromTexturePayload.register(registry);
        ExprPlayerProfileOf.register(registry);
        ExprPlayerProfileProperties.register(registry);
        ExprPlayerProfileProperty.register(registry);
        ExprPlayerProfilePropertyData.register(registry);
        ExprPlayerProfilePropertyOf.register(registry);
        ExprPlayerProfileSkinURL.register(registry);
        ExprSecPlayerProfile.register(registry);
        ExprSecPlayerProfileProperty.register(registry);
        ExprSecTexturePayLoad.register(registry);
    }

    @Override
    public void init(SkriptAddon addon) {
        if (Classes.getExactClassInfo(PlayerProfile.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(PlayerProfile.class, "playerprofile")
                    .toString(playerProfile -> "Player Profile with name " + playerProfile.getName() + " and UUID " + playerProfile.getId())
                    .user("(player ?)?profiles?")
                    .name("Player - Profile")
                    .description("A player's profile.\n\nContains the player's UUID, name, skin, cape, player model (alex/steve) and profile properties.") // add example
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(ProfileProperty.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(ProfileProperty.class, "profileproperty")
                    .toString(profileProperty -> "profile property named \"" + profileProperty.getName() + "\" with value \"" + profileProperty.getValue() + "\"" + (profileProperty.getSignature() == null ? "" : " and signature \"" + profileProperty.getSignature() + "\""))
                    .user("profile ?propert(y|ies)")
                    .name("Player Profile - Property")
                    .description("A property for player profiles. Contains a name, value and optionally a signature.")
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }
        Classes.registerClass(new SimpleClassInfo<>(MutableProfileProperty.class, "mutableprofileproperty")
                .toString(profileProperty -> "mutable profile property named \"" + profileProperty.getName() + "\" with value \"" + profileProperty.getValue() + "\"" + (profileProperty.getSignature() == null ? "" : " and signature \"" + profileProperty.getSignature() + "\""))
                .user("mutable ?profile ?propert(y|ies)")
                .name("Player Profile - Mutable Property")
                .description("A property for player profiles. Contains a name, value and optionally a signature. Can be modified.")
                .since("2.0.0"));

        increaseClassInfoCount(1);

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
