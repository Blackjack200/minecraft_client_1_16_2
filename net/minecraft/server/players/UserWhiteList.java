package net.minecraft.server.players;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.File;
import com.mojang.authlib.GameProfile;

public class UserWhiteList extends StoredUserList<GameProfile, UserWhiteListEntry> {
    public UserWhiteList(final File file) {
        super(file);
    }
    
    @Override
    protected StoredUserEntry<GameProfile> createEntry(final JsonObject jsonObject) {
        return new UserWhiteListEntry(jsonObject);
    }
    
    public boolean isWhiteListed(final GameProfile gameProfile) {
        return ((StoredUserList<GameProfile, V>)this).contains(gameProfile);
    }
    
    @Override
    public String[] getUserList() {
        final String[] arr2 = new String[this.getEntries().size()];
        int integer3 = 0;
        for (final StoredUserEntry<GameProfile> acv5 : this.getEntries()) {
            arr2[integer3++] = acv5.getUser().getName();
        }
        return arr2;
    }
    
    @Override
    protected String getKeyForUser(final GameProfile gameProfile) {
        return gameProfile.getId().toString();
    }
}
