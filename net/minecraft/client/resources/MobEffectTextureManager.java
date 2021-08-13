package net.minecraft.client.resources;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.Registry;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;

public class MobEffectTextureManager extends TextureAtlasHolder {
    public MobEffectTextureManager(final TextureManager ejv) {
        super(ejv, new ResourceLocation("textures/atlas/mob_effects.png"), "mob_effect");
    }
    
    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        return (Stream<ResourceLocation>)Registry.MOB_EFFECT.keySet().stream();
    }
    
    public TextureAtlasSprite get(final MobEffect app) {
        return this.getSprite(Registry.MOB_EFFECT.getKey(app));
    }
}
