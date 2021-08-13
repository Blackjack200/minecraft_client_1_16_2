package net.minecraft.server.players;

import java.net.SocketAddress;
import com.google.gson.JsonObject;
import java.io.File;

public class IpBanList extends StoredUserList<String, IpBanListEntry> {
    public IpBanList(final File file) {
        super(file);
    }
    
    @Override
    protected StoredUserEntry<String> createEntry(final JsonObject jsonObject) {
        return new IpBanListEntry(jsonObject);
    }
    
    public boolean isBanned(final SocketAddress socketAddress) {
        final String string3 = this.getIpFromAddress(socketAddress);
        return ((StoredUserList<String, V>)this).contains(string3);
    }
    
    public boolean isBanned(final String string) {
        return ((StoredUserList<String, V>)this).contains(string);
    }
    
    public IpBanListEntry get(final SocketAddress socketAddress) {
        final String string3 = this.getIpFromAddress(socketAddress);
        return this.get(string3);
    }
    
    private String getIpFromAddress(final SocketAddress socketAddress) {
        String string3 = socketAddress.toString();
        if (string3.contains("/")) {
            string3 = string3.substring(string3.indexOf(47) + 1);
        }
        if (string3.contains(":")) {
            string3 = string3.substring(0, string3.indexOf(58));
        }
        return string3;
    }
}
