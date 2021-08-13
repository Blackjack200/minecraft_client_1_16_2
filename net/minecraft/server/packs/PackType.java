package net.minecraft.server.packs;

public enum PackType {
    CLIENT_RESOURCES("assets"), 
    SERVER_DATA("data");
    
    private final String directory;
    
    private PackType(final String string3) {
        this.directory = string3;
    }
    
    public String getDirectory() {
        return this.directory;
    }
}
