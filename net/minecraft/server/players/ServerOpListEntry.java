package net.minecraft.server.players;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class ServerOpListEntry extends StoredUserEntry<GameProfile> {
    private final int level;
    private final boolean bypassesPlayerLimit;
    
    public ServerOpListEntry(final GameProfile gameProfile, final int integer, final boolean boolean3) {
        super(gameProfile);
        this.level = integer;
        this.bypassesPlayerLimit = boolean3;
    }
    
    public ServerOpListEntry(final JsonObject jsonObject) {
        super(createGameProfile(jsonObject));
        this.level = (jsonObject.has("level") ? jsonObject.get("level").getAsInt() : 0);
        this.bypassesPlayerLimit = (jsonObject.has("bypassesPlayerLimit") && jsonObject.get("bypassesPlayerLimit").getAsBoolean());
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public boolean getBypassesPlayerLimit() {
        return this.bypassesPlayerLimit;
    }
    
    @Override
    protected void serialize(final JsonObject jsonObject) {
        if (this.getUser() == null) {
            return;
        }
        jsonObject.addProperty("uuid", (this.getUser().getId() == null) ? "" : this.getUser().getId().toString());
        jsonObject.addProperty("name", this.getUser().getName());
        jsonObject.addProperty("level", (Number)this.level);
        jsonObject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
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
