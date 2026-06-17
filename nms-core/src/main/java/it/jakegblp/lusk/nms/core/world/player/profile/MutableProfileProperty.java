package it.jakegblp.lusk.nms.core.world.player.profile;

import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.common.Mutable;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MutableProfileProperty implements Copyable<MutableProfileProperty>, Mutable<ProfileProperty>/*, NMSObject<Object> todo: implement, low priority*/{
    protected @NotNull String name;
    protected @NotNull String value;
    protected @Nullable String signature;

    public MutableProfileProperty(@NotNull TexturesPayload texturesPayload, @Nullable String signature) {
        this("textures", texturesPayload.toBase64(), signature);
    }

    public MutableProfileProperty(@NotNull String base64, @Nullable String signature) {
        this("textures", base64, signature);
    }

    public MutableProfileProperty(@NotNull ProfileProperty profileProperty) {
        this(profileProperty.getName(), profileProperty.getValue(), profileProperty.getSignature());
    }

    public ProfileProperty immutable() {
        return new ProfileProperty(name, value, signature);
    }

    @Override
    public MutableProfileProperty copy() {
        return new MutableProfileProperty(name, value, signature);
    }

    /*
    @Override
    public Object asNMS() {
        return null;
    }
     */
}
