package net.minecraft.client.renderer.texture;

import net.minecraft.client.resources.model.Material;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class AtlasSet implements AutoCloseable {
    private final Map<ResourceLocation, TextureAtlas> atlases;
    
    public AtlasSet(final Collection<TextureAtlas> collection) {
        this.atlases = (Map<ResourceLocation, TextureAtlas>)collection.stream().collect(Collectors.toMap(TextureAtlas::location, Function.identity()));
    }
    
    public TextureAtlas getAtlas(final ResourceLocation vk) {
        return (TextureAtlas)this.atlases.get(vk);
    }
    
    public TextureAtlasSprite getSprite(final Material elj) {
        return ((TextureAtlas)this.atlases.get(elj.atlasLocation())).getSprite(elj.texture());
    }
    
    public void close() {
        this.atlases.values().forEach(TextureAtlas::clearTextureData);
        this.atlases.clear();
    }
}
