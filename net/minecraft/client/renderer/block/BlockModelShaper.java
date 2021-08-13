package net.minecraft.client.renderer.block;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Registry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;

public class BlockModelShaper {
    private final Map<BlockState, BakedModel> modelByStateCache;
    private final ModelManager modelManager;
    
    public BlockModelShaper(final ModelManager ell) {
        this.modelByStateCache = (Map<BlockState, BakedModel>)Maps.newIdentityHashMap();
        this.modelManager = ell;
    }
    
    public TextureAtlasSprite getParticleIcon(final BlockState cee) {
        return this.getBlockModel(cee).getParticleIcon();
    }
    
    public BakedModel getBlockModel(final BlockState cee) {
        BakedModel elg3 = (BakedModel)this.modelByStateCache.get(cee);
        if (elg3 == null) {
            elg3 = this.modelManager.getMissingModel();
        }
        return elg3;
    }
    
    public ModelManager getModelManager() {
        return this.modelManager;
    }
    
    public void rebuildCache() {
        this.modelByStateCache.clear();
        for (final Block bul3 : Registry.BLOCK) {
            bul3.getStateDefinition().getPossibleStates().forEach(cee -> {
                final BakedModel bakedModel = (BakedModel)this.modelByStateCache.put(cee, this.modelManager.getModel(stateToModelLocation(cee)));
            });
        }
    }
    
    public static ModelResourceLocation stateToModelLocation(final BlockState cee) {
        return stateToModelLocation(Registry.BLOCK.getKey(cee.getBlock()), cee);
    }
    
    public static ModelResourceLocation stateToModelLocation(final ResourceLocation vk, final BlockState cee) {
        return new ModelResourceLocation(vk, statePropertiesToString((Map<Property<?>, Comparable<?>>)cee.getValues()));
    }
    
    public static String statePropertiesToString(final Map<Property<?>, Comparable<?>> map) {
        final StringBuilder stringBuilder2 = new StringBuilder();
        for (final Map.Entry<Property<?>, Comparable<?>> entry4 : map.entrySet()) {
            if (stringBuilder2.length() != 0) {
                stringBuilder2.append(',');
            }
            final Property<?> cfg5 = entry4.getKey();
            stringBuilder2.append(cfg5.getName());
            stringBuilder2.append('=');
            stringBuilder2.append(BlockModelShaper.getValue(cfg5, entry4.getValue()));
        }
        return stringBuilder2.toString();
    }
    
    private static <T extends Comparable<T>> String getValue(final Property<T> cfg, final Comparable<?> comparable) {
        return cfg.getName((T)comparable);
    }
}
