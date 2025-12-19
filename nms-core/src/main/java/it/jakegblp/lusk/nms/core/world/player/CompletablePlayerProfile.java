package it.jakegblp.lusk.nms.core.world.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@EqualsAndHashCode
@Getter
public final class CompletablePlayerProfile implements Cloneable, Asyncable, NMSObject<Object> {
    @Delegate
    private final @NotNull PlayerProfile playerProfile;
    @Setter
    private boolean shouldComplete;
    @Setter
    private @NotNull ExecutionMode executionMode;

    public CompletablePlayerProfile(
            @NotNull PlayerProfile playerProfile,
            boolean shouldComplete,
            @NotNull ExecutionMode executionMode
    ) {
        this.playerProfile = playerProfile;
        this.shouldComplete = shouldComplete;
        this.executionMode = executionMode;
    }

    public CompletablePlayerProfile(
            @NotNull TexturesPayload texturesPayload,
            boolean shouldComplete,
            @NotNull ExecutionMode executionMode
    ) {

        this.playerProfile = texturesPayload.asPlayerProfile();
        this.shouldComplete = shouldComplete;
        this.executionMode = executionMode;
    }

    public CompletablePlayerProfile(
            @NotNull TexturesPayload texturesPayload
    ) {
        this(texturesPayload, false, ExecutionMode.INHERITED);
    }

    public CompletablePlayerProfile(
            @NotNull PlayerProfile playerProfile
    ) {
        this(playerProfile, false, ExecutionMode.INHERITED);
    }

    @Override
    public void process() {
        if (!shouldComplete) return;
        playerProfile.complete();
    }

    public TexturesPayload getTextures() {
        if (!playerProfile.hasProperty("textures")) return null;
        for (ProfileProperty property : playerProfile.getProperties())
            if ("textures".equals(property.getName()))
                return TexturesPayload.fromBase64(property.getValue(), property.getSignature());
        return null;
    }

    @Override
    public CompletablePlayerProfile clone() throws CloneNotSupportedException {
        return new CompletablePlayerProfile(playerProfile.clone(), shouldComplete, executionMode);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSPlayerProfile(playerProfile);
    }

}
