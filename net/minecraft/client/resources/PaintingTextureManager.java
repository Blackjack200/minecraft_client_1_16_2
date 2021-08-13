package net.minecraft.client.resources;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.core.Registry;
import java.util.stream.Stream;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class PaintingTextureManager extends TextureAtlasHolder {
    private static final ResourceLocation BACK_SPRITE_LOCATION;
    
    public PaintingTextureManager(final TextureManager ejv) {
        super(ejv, new ResourceLocation("textures/atlas/paintings.png"), "painting");
    }
    
    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        return (Stream<ResourceLocation>)Stream.concat(Registry.MOTIVE.keySet().stream(), Stream.of(PaintingTextureManager.BACK_SPRITE_LOCATION));
    }
    
    public TextureAtlasSprite get(final Motive bco) {
        return this.getSprite(Registry.MOTIVE.getKey(bco));
    }
    
    public TextureAtlasSprite getBackSprite() {
        return this.getSprite(PaintingTextureManager.BACK_SPRITE_LOCATION);
    }
    
    static {
        BACK_SPRITE_LOCATION = new ResourceLocation("back");
    }
}
