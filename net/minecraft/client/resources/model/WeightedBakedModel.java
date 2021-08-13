package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.Random;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.WeighedRandom;
import java.util.List;

public class WeightedBakedModel implements BakedModel {
    private final int totalWeight;
    private final List<WeightedModel> list;
    private final BakedModel wrapped;
    
    public WeightedBakedModel(final List<WeightedModel> list) {
        this.list = list;
        this.totalWeight = WeighedRandom.getTotalWeight(list);
        this.wrapped = ((WeightedModel)list.get(0)).model;
    }
    
    public List<BakedQuad> getQuads(@Nullable final BlockState cee, @Nullable final Direction gc, final Random random) {
        return WeighedRandom.<WeightedModel>getWeightedItem(this.list, Math.abs((int)random.nextLong()) % this.totalWeight).model.getQuads(cee, gc, random);
    }
    
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }
    
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }
    
    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }
    
    public boolean isCustomRenderer() {
        return this.wrapped.isCustomRenderer();
    }
    
    public TextureAtlasSprite getParticleIcon() {
        return this.wrapped.getParticleIcon();
    }
    
    public ItemTransforms getTransforms() {
        return this.wrapped.getTransforms();
    }
    
    public ItemOverrides getOverrides() {
        return this.wrapped.getOverrides();
    }
    
    public static class Builder {
        private final List<WeightedModel> list;
        
        public Builder() {
            this.list = (List<WeightedModel>)Lists.newArrayList();
        }
        
        public Builder add(@Nullable final BakedModel elg, final int integer) {
            if (elg != null) {
                this.list.add(new WeightedModel(elg, integer));
            }
            return this;
        }
        
        @Nullable
        public BakedModel build() {
            if (this.list.isEmpty()) {
                return null;
            }
            if (this.list.size() == 1) {
                return ((WeightedModel)this.list.get(0)).model;
            }
            return new WeightedBakedModel(this.list);
        }
    }
    
    static class WeightedModel extends WeighedRandom.WeighedRandomItem {
        protected final BakedModel model;
        
        public WeightedModel(final BakedModel elg, final int integer) {
            super(integer);
            this.model = elg;
        }
    }
}
