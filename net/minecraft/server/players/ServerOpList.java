package net.minecraft.server.players;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.File;
import com.mojang.authlib.GameProfile;

public class ServerOpList extends StoredUserList<GameProfile, ServerOpListEntry> {
    public ServerOpList(final File file) {
        super(file);
    }
    
    @Override
    protected StoredUserEntry<GameProfile> createEntry(final JsonObject jsonObject) {
        return new ServerOpListEntry(jsonObject);
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
    
    public boolean canBypassPlayerLimit(final GameProfile gameProfile) {
        final ServerOpListEntry acu3 = this.get(gameProfile);
        return acu3 != null && acu3.getBypassesPlayerLimit();
    }
    
    @Override
    protected String getKeyForUser(final GameProfile gameProfile) {
        return gameProfile.getId().toString();
    }
}
