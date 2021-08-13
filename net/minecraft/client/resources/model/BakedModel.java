package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import java.util.Random;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;

public interface BakedModel {
    List<BakedQuad> getQuads(@Nullable final BlockState cee, @Nullable final Direction gc, final Random random);
    
    boolean useAmbientOcclusion();
    
    boolean isGui3d();
    
    boolean usesBlockLight();
    
    boolean isCustomRenderer();
    
    TextureAtlasSprite getParticleIcon();
    
    ItemTransforms getTransforms();
    
    ItemOverrides getOverrides();
}
