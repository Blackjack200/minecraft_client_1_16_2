package net.minecraft.realms;

import net.minecraft.client.multiplayer.ServerAddress;

public class RealmsServerAddress {
    private final String host;
    private final int port;
    
    protected RealmsServerAddress(final String string, final int integer) {
        this.host = string;
        this.port = integer;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public static RealmsServerAddress parseString(final String string) {
        final ServerAddress dwq2 = ServerAddress.parseString(string);
        return new RealmsServerAddress(dwq2.getHost(), dwq2.getPort());
    }
}
