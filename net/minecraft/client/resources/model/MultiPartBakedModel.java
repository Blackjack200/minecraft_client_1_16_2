package net.minecraft.client.resources.model;

import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.Collections;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.Random;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.Util;
import java.util.BitSet;
import java.util.Map;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class MultiPartBakedModel implements BakedModel {
    private final List<Pair<Predicate<BlockState>, BakedModel>> selectors;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;
    private final Map<BlockState, BitSet> selectorCache;
    
    public MultiPartBakedModel(final List<Pair<Predicate<BlockState>, BakedModel>> list) {
        this.selectorCache = (Map<BlockState, BitSet>)new Object2ObjectOpenCustomHashMap((Hash.Strategy)Util.identityStrategy());
        this.selectors = list;
        final BakedModel elg3 = (BakedModel)((Pair)list.iterator().next()).getRight();
        this.hasAmbientOcclusion = elg3.useAmbientOcclusion();
        this.isGui3d = elg3.isGui3d();
        this.usesBlockLight = elg3.usesBlockLight();
        this.particleIcon = elg3.getParticleIcon();
        this.transforms = elg3.getTransforms();
        this.overrides = elg3.getOverrides();
    }
    
    public List<BakedQuad> getQuads(@Nullable final BlockState cee, @Nullable final Direction gc, final Random random) {
        if (cee == null) {
            return (List<BakedQuad>)Collections.emptyList();
        }
        BitSet bitSet5 = (BitSet)this.selectorCache.get(cee);
        if (bitSet5 == null) {
            bitSet5 = new BitSet();
            for (int integer6 = 0; integer6 < this.selectors.size(); ++integer6) {
                final Pair<Predicate<BlockState>, BakedModel> pair7 = (Pair<Predicate<BlockState>, BakedModel>)this.selectors.get(integer6);
                if (((Predicate)pair7.getLeft()).test(cee)) {
                    bitSet5.set(integer6);
                }
            }
            this.selectorCache.put(cee, bitSet5);
        }
        final List<BakedQuad> list6 = (List<BakedQuad>)Lists.newArrayList();
        final long long7 = random.nextLong();
        for (int integer7 = 0; integer7 < bitSet5.length(); ++integer7) {
            if (bitSet5.get(integer7)) {
                list6.addAll((Collection)((BakedModel)((Pair)this.selectors.get(integer7)).getRight()).getQuads(cee, gc, new Random(long7)));
            }
        }
        return list6;
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
        private final List<Pair<Predicate<BlockState>, BakedModel>> selectors;
        
        public Builder() {
            this.selectors = (List<Pair<Predicate<BlockState>, BakedModel>>)Lists.newArrayList();
        }
        
        public void add(final Predicate<BlockState> predicate, final BakedModel elg) {
            this.selectors.add(Pair.of((Object)predicate, (Object)elg));
        }
        
        public BakedModel build() {
            return new MultiPartBakedModel(this.selectors);
        }
    }
}
