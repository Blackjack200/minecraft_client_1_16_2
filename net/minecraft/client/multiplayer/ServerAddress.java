package net.minecraft.client.multiplayer;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import com.mojang.datafixers.util.Pair;
import java.net.IDN;

public class ServerAddress {
    private final String host;
    private final int port;
    
    private ServerAddress(final String string, final int integer) {
        this.host = string;
        this.port = integer;
    }
    
    public String getHost() {
        try {
            return IDN.toASCII(this.host);
        }
        catch (IllegalArgumentException illegalArgumentException2) {
            return "";
        }
    }
    
    public int getPort() {
        return this.port;
    }
    
    public static ServerAddress parseString(final String string) {
        if (string == null) {
            return null;
        }
        String[] arr2 = string.split(":");
        if (string.startsWith("[")) {
            final int integer3 = string.indexOf("]");
            if (integer3 > 0) {
                final String string2 = string.substring(1, integer3);
                String string3 = string.substring(integer3 + 1).trim();
                if (string3.startsWith(":") && !string3.isEmpty()) {
                    string3 = string3.substring(1);
                    arr2 = new String[] { string2, string3 };
                }
                else {
                    arr2 = new String[] { string2 };
                }
            }
        }
        if (arr2.length > 2) {
            arr2 = new String[] { string };
        }
        String string4 = arr2[0];
        int integer4 = (arr2.length > 1) ? parseInt(arr2[1], 25565) : 25565;
        if (integer4 == 25565) {
            final Pair<String, Integer> pair5 = lookupSrv(string4);
            string4 = (String)pair5.getFirst();
            integer4 = (int)pair5.getSecond();
        }
        return new ServerAddress(string4, integer4);
    }
    
    private static Pair<String, Integer> lookupSrv(final String string) {
        try {
            final String string2 = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            final Hashtable<String, String> hashtable3 = (Hashtable<String, String>)new Hashtable();
            hashtable3.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            hashtable3.put("java.naming.provider.url", "dns:");
            hashtable3.put("com.sun.jndi.dns.timeout.retries", "1");
            final DirContext dirContext4 = (DirContext)new InitialDirContext((Hashtable)hashtable3);
            final Attributes attributes5 = dirContext4.getAttributes("_minecraft._tcp." + string, new String[] { "SRV" });
            final Attribute attribute6 = attributes5.get("srv");
            if (attribute6 != null) {
                final String[] arr7 = attribute6.get().toString().split(" ", 4);
                return (Pair<String, Integer>)Pair.of(arr7[3], parseInt(arr7[2], 25565));
            }
        }
        catch (Throwable t) {}
        return (Pair<String, Integer>)Pair.of(string, 25565);
    }
    
    private static int parseInt(final String string, final int integer) {
        try {
            return Integer.parseInt(string.trim());
        }
        catch (Exception ex) {
            return integer;
        }
    }
}
