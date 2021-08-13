package net.minecraft.client.renderer.texture;

import java.util.Collection;

public class StitcherException extends RuntimeException {
    private final Collection<TextureAtlasSprite.Info> allSprites;
    
    public StitcherException(final TextureAtlasSprite.Info a, final Collection<TextureAtlasSprite.Info> collection) {
        super(String.format("Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] { a.name(), a.width(), a.height() }));
        this.allSprites = collection;
    }
    
    public Collection<TextureAtlasSprite.Info> getAllSprites() {
        return this.allSprites;
    }
}
