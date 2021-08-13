package net.minecraft.client.resources.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.BlockModel;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

public class SimpleBakedModel implements BakedModel {
    protected final List<BakedQuad> unculledFaces;
    protected final Map<Direction, List<BakedQuad>> culledFaces;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;
    
    public SimpleBakedModel(final List<BakedQuad> list, final Map<Direction, List<BakedQuad>> map, final boolean boolean3, final boolean boolean4, final boolean boolean5, final TextureAtlasSprite eju, final ItemTransforms ebe, final ItemOverrides ebc) {
        this.unculledFaces = list;
        this.culledFaces = map;
        this.hasAmbientOcclusion = boolean3;
        this.isGui3d = boolean5;
        this.usesBlockLight = boolean4;
        this.particleIcon = eju;
        this.transforms = ebe;
        this.overrides = ebc;
    }
    
    public List<BakedQuad> getQuads(@Nullable final BlockState cee, @Nullable final Direction gc, final Random random) {
        return (List<BakedQuad>)((gc == null) ? this.unculledFaces : ((List)this.culledFaces.get(gc)));
    }
    
    public boolean useAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }
    
    public boolean isGui3d() {
        return this.isGui3d;
    }
    
    public boolean usesBlockLight() {
        return this.usesBlockLight;
    }
    
    public boolean isCustomRenderer() {
        return false;
    }
    
    public TextureAtlasSprite getParticleIcon() {
        return this.particleIcon;
    }
    
    public ItemTransforms getTransforms() {
        return this.transforms;
    }
    
    public ItemOverrides getOverrides() {
        return this.overrides;
    }
    
    public static class Builder {
        private final List<BakedQuad> unculledFaces;
        private final Map<Direction, List<BakedQuad>> culledFaces;
        private final ItemOverrides overrides;
        private final boolean hasAmbientOcclusion;
        private TextureAtlasSprite particleIcon;
        private final boolean usesBlockLight;
        private final boolean isGui3d;
        private final ItemTransforms transforms;
        
        public Builder(final BlockModel eax, final ItemOverrides ebc, final boolean boolean3) {
            this(eax.hasAmbientOcclusion(), eax.getGuiLight().lightLikeBlock(), boolean3, eax.getTransforms(), ebc);
        }
        
        private Builder(final boolean boolean1, final boolean boolean2, final boolean boolean3, final ItemTransforms ebe, final ItemOverrides ebc) {
            this.unculledFaces = (List<BakedQuad>)Lists.newArrayList();
            this.culledFaces = (Map<Direction, List<BakedQuad>>)Maps.newEnumMap((Class)Direction.class);
            for (final Direction gc10 : Direction.values()) {
                this.culledFaces.put(gc10, Lists.newArrayList());
            }
            this.overrides = ebc;
            this.hasAmbientOcclusion = boolean1;
            this.usesBlockLight = boolean2;
            this.isGui3d = boolean3;
            this.transforms = ebe;
        }
        
        public Builder addCulledFace(final Direction gc, final BakedQuad eas) {
            ((List)this.culledFaces.get(gc)).add(eas);
            return this;
        }
        
        public Builder addUnculledFace(final BakedQuad eas) {
            this.unculledFaces.add(eas);
            return this;
        }
        
        public Builder particle(final TextureAtlasSprite eju) {
            this.particleIcon = eju;
            return this;
        }
        
        public BakedModel build() {
            if (this.particleIcon == null) {
                throw new RuntimeException("Missing particle!");
            }
            return new SimpleBakedModel(this.unculledFaces, this.culledFaces, this.hasAmbientOcclusion, this.usesBlockLight, this.isGui3d, this.particleIcon, this.transforms, this.overrides);
        }
    }
}
