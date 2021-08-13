package net.minecraft.server.players;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import java.util.Date;

public class IpBanListEntry extends BanListEntry<String> {
    public IpBanListEntry(final String string) {
        this(string, null, null, null, null);
    }
    
    public IpBanListEntry(final String string1, @Nullable final Date date2, @Nullable final String string3, @Nullable final Date date4, @Nullable final String string5) {
        super(string1, date2, string3, date4, string5);
    }
    
    @Override
    public Component getDisplayName() {
        return new TextComponent(this.getUser());
    }
    
    public IpBanListEntry(final JsonObject jsonObject) {
        super(createIpInfo(jsonObject), jsonObject);
    }
    
    private static String createIpInfo(final JsonObject jsonObject) {
        return jsonObject.has("ip") ? jsonObject.get("ip").getAsString() : null;
    }
    
    @Override
    protected void serialize(final JsonObject jsonObject) {
        if (this.getUser() == null) {
            return;
        }
        jsonObject.addProperty("ip", (String)this.getUser());
        super.serialize(jsonObject);
    }
}
