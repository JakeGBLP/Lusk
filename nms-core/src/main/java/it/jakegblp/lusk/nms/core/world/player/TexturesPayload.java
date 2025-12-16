package it.jakegblp.lusk.nms.core.world.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.*;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TexturesPayload {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private long timestamp;
    private UUID profileId;
    private String profileName, signature;
    private Boolean signatureRequired;
    private Map<TextureType, TextureEntry> textures;

    public TexturesPayload(long timestamp, UUID profileId, String profileName, String signature, Boolean signatureRequired, List<TextureEntry> textures) {
        this(timestamp, profileId, profileName, signature, signatureRequired, textures.stream().collect(Collectors.toMap(TextureEntry::getTextureType, texture -> texture)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextureEntry {
        private TextureType textureType;
        private String url;
        private TextureMetadata metadata;

        public TextureEntry(TextureType textureType, String url) {
            this.textureType = textureType;
            this.url = url;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TextureMetadata {
            private PlayerTextures.SkinModel model;
        }
    }

    public static @NotNull TexturesPayload fromJson(@NotNull String json, @Nullable String signature) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        TexturesPayload payload = new TexturesPayload();
        payload.timestamp = root.get("timestamp").getAsLong();
        payload.profileId = NullabilityUtils.convertIfNotNull(root.get("profileId").getAsString(), CommonUtils::fromCompactUUID);
        payload.profileName = root.get("profileName").getAsString();
        JsonElement signatureRequired = root.get("signatureRequired");
        payload.signatureRequired = signatureRequired != null && !signatureRequired.isJsonNull() ? signatureRequired.getAsBoolean() : null;
        JsonObject textures = root.getAsJsonObject("textures");
        Map<TextureType, TextureEntry> map = new HashMap<>();
        for (String key : textures.keySet()) {
            TextureType textureType = CommonUtils.safeValueOf(TextureType.class, key);
            if (textureType == null) continue;
            JsonObject texture = textures.getAsJsonObject(key);
            TextureEntry entry = new TextureEntry();
            entry.setUrl(texture.get("url").getAsString());
            entry.setTextureType(textureType);
            if (texture.has("metadata")) {
                JsonObject metadata = texture.getAsJsonObject("metadata");
                if (metadata.has("model")) {
                    var skinModel = CommonUtils.safeValueOf(PlayerTextures.SkinModel.class , metadata.get("model").getAsString());
                    if (skinModel == null) continue;
                    entry.setMetadata(new TextureEntry.TextureMetadata(skinModel));
                }
            }
            map.put(textureType, entry);
        }
        payload.textures = map;
        payload.signature = signature;
        return payload;
    }

    public static @NotNull TexturesPayload fromBase64(@NotNull String base64, @Nullable String signature) {
        return fromJson(new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8), signature);
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("timestamp", timestamp);
        if (profileId != null)
            root.addProperty("profileId", NullabilityUtils.convertIfNotNull(profileId, CommonUtils::toCompactUUID));
        root.addProperty("profileName", profileName);
        if (signatureRequired != null)
            root.addProperty("signatureRequired", signatureRequired);
        JsonObject texturesObj = new JsonObject();
        for (Map.Entry<TextureType, TextureEntry> entry : textures.entrySet()) {
            JsonObject texObj = new JsonObject();
            TextureEntry textureEntry = entry.getValue();
            texObj.addProperty("url", textureEntry.getUrl());
            if (textureEntry.getMetadata() != null) {
                JsonObject metaObj = new JsonObject();
                metaObj.addProperty("model", textureEntry.getMetadata().getModel().name());
                texObj.add("metadata", metaObj);
            }
            texturesObj.add(entry.getKey().name(), texObj);
        }
        root.add("textures", texturesObj);
        return root;
    }

    public String toPrettyJson() {
        return GSON.toJson(toJson()).replaceAll("(\"\\w+\"):", "$1 :");
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(toPrettyJson().getBytes(StandardCharsets.UTF_8));
    }

    public PlayerProfile asPlayerProfile() {
        PlayerProfile profile = Bukkit.createProfileExact(profileId, profileName);
        profile.getProperties().clear();
        profile.getProperties().add(new ProfileProperty("textures", toBase64(), signature));
        return profile;
    }

    /**
     * Adds the texture properties of the provided base64 to this one if those same properties are missing.
     */
    public void mergeBase64(String otherBase64) {
        TexturesPayload other = fromBase64(otherBase64, null);
        merge(other);
    }

    /**
     * Adds the texture properties of the provided base64 to this one if those same properties are missing.
     */
    public void merge(TexturesPayload other) {
        if (other == null || other.getTextures() == null) return;
        if (this.textures == null)
            this.textures = new HashMap<>(other.textures);
        else
            for (Map.Entry<TextureType, TextureEntry> entry : other.getTextures().entrySet())
                this.textures.putIfAbsent(entry.getKey(), entry.getValue());
    }

    public void setModel(PlayerTextures.SkinModel model) {
        if (model == null) return;
        if (this.textures == null) return;
        TextureEntry skin = this.textures.get(TextureType.SKIN);
        if (skin == null) return;
        if (skin.getMetadata() == null)
            skin.setMetadata(new TextureEntry.TextureMetadata());
        skin.getMetadata().setModel(PlayerTextures.SkinModel.SLIM);
    }
}
