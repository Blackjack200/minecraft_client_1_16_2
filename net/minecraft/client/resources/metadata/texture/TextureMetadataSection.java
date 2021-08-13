package net.minecraft.client.resources.metadata.texture;

public class TextureMetadataSection {
    public static final TextureMetadataSectionSerializer SERIALIZER;
    private final boolean blur;
    private final boolean clamp;
    
    public TextureMetadataSection(final boolean boolean1, final boolean boolean2) {
        this.blur = boolean1;
        this.clamp = boolean2;
    }
    
    public boolean isBlur() {
        return this.blur;
    }
    
    public boolean isClamp() {
        return this.clamp;
    }
    
    static {
        SERIALIZER = new TextureMetadataSectionSerializer();
    }
}
