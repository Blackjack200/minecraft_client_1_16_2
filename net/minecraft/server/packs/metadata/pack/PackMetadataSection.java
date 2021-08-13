package net.minecraft.server.packs.metadata.pack;

import net.minecraft.network.chat.Component;

public class PackMetadataSection {
    public static final PackMetadataSectionSerializer SERIALIZER;
    private final Component description;
    private final int packFormat;
    
    public PackMetadataSection(final Component nr, final int integer) {
        this.description = nr;
        this.packFormat = integer;
    }
    
    public Component getDescription() {
        return this.description;
    }
    
    public int getPackFormat() {
        return this.packFormat;
    }
    
    static {
        SERIALIZER = new PackMetadataSectionSerializer();
    }
}
