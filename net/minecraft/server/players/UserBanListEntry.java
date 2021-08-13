package net.minecraft.server.players;

import java.util.UUID;
import net.minecraft.network.chat.TextComponent;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import java.util.Date;
import com.mojang.authlib.GameProfile;

public class UserBanListEntry extends BanListEntry<GameProfile> {
    public UserBanListEntry(final GameProfile gameProfile) {
        this(gameProfile, null, null, null, null);
    }
    
    public UserBanListEntry(final GameProfile gameProfile, @Nullable final Date date2, @Nullable final String string3, @Nullable final Date date4, @Nullable final String string5) {
        super(gameProfile, date2, string3, date4, string5);
    }
    
    public UserBanListEntry(final JsonObject jsonObject) {
        super(createGameProfile(jsonObject), jsonObject);
    }
    
    @Override
    protected void serialize(final JsonObject jsonObject) {
        if (this.getUser() == null) {
            return;
        }
        jsonObject.addProperty("uuid", (this.getUser().getId() == null) ? "" : this.getUser().getId().toString());
        jsonObject.addProperty("name", this.getUser().getName());
        super.serialize(jsonObject);
    }
    
    @Override
    public Component getDisplayName() {
        final GameProfile gameProfile2 = this.getUser();
        return new TextComponent((gameProfile2.getName() != null) ? gameProfile2.getName() : Objects.toString(gameProfile2.getId(), "(Unknown)"));
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
