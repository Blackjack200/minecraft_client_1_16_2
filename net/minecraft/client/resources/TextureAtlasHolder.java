package net.minecraft.client.resources;

import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.stream.Stream;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public abstract class TextureAtlasHolder extends SimplePreparableReloadListener<TextureAtlas.Preparations> implements AutoCloseable {
    private final TextureAtlas textureAtlas;
    private final String prefix;
    
    public TextureAtlasHolder(final TextureManager ejv, final ResourceLocation vk, final String string) {
        this.prefix = string;
        this.textureAtlas = new TextureAtlas(vk);
        ejv.register(this.textureAtlas.location(), this.textureAtlas);
    }
    
    protected abstract Stream<ResourceLocation> getResourcesToLoad();
    
    protected TextureAtlasSprite getSprite(final ResourceLocation vk) {
        return this.textureAtlas.getSprite(this.resolveLocation(vk));
    }
    
    private ResourceLocation resolveLocation(final ResourceLocation vk) {
        return new ResourceLocation(vk.getNamespace(), this.prefix + "/" + vk.getPath());
    }
    
    @Override
    protected TextureAtlas.Preparations prepare(final ResourceManager acf, final ProfilerFiller ant) {
        ant.startTick();
        ant.push("stitching");
        final TextureAtlas.Preparations a4 = this.textureAtlas.prepareToStitch(acf, (Stream<ResourceLocation>)this.getResourcesToLoad().map(this::resolveLocation), ant, 0);
        ant.pop();
        ant.endTick();
        return a4;
    }
    
    @Override
    protected void apply(final TextureAtlas.Preparations a, final ResourceManager acf, final ProfilerFiller ant) {
        ant.startTick();
        ant.push("upload");
        this.textureAtlas.reload(a);
        ant.pop();
        ant.endTick();
    }
    
    public void close() {
        this.textureAtlas.clearTextureData();
    }
}
