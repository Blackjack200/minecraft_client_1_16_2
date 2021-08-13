package net.minecraft.server.players;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserWhiteListEntry extends StoredUserEntry<GameProfile> {
    public UserWhiteListEntry(final GameProfile gameProfile) {
        super(gameProfile);
    }
    
    public UserWhiteListEntry(final JsonObject jsonObject) {
        super(createGameProfile(jsonObject));
    }
    
    @Override
    protected void serialize(final JsonObject jsonObject) {
        if (this.getUser() == null) {
            return;
        }
        jsonObject.addProperty("uuid", (this.getUser().getId() == null) ? "" : this.getUser().getId().toString());
        jsonObject.addProperty("name", this.getUser().getName());
    }
    
    private static GameProfile createGameProfile(final JsonObject jsonObject) {
        if (!jsonObject.has("uuid") || !jsonObject.has("name")) {
            return null;
        }
        final String string2 = jsonObject.get("uuid").getAsString();
        UUID uUID3;
        try {
            uUID3 = UUID.fromString(string2);
        }
        catch (Throwable throwable4) {
            return null;
        }
        return new GameProfile(uUID3, jsonObject.get("name").getAsString());
    }
}
