package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface SpriteSet {
    TextureAtlasSprite get(final int integer1, final int integer2);
    
    TextureAtlasSprite get(final Random random);
}
