package it.jakegblp.lusk.nms.core.world.player;

import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MutableProfileProperty implements Cloneable /*, NMSObject<Object> todo: implement, low priority*/{
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

    public ProfileProperty asImmutable() {
        return new ProfileProperty(name, value, signature);
    }

    @Override
    public MutableProfileProperty clone() {
        try {
            return (MutableProfileProperty) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /*
    @Override
    public Object asNMS() {
        return null;
    }
     */
}
