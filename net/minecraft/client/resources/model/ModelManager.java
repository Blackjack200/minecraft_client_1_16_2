package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.state.BlockState;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.block.BlockModelShaper;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public class ModelManager extends SimplePreparableReloadListener<ModelBakery> implements AutoCloseable {
    private Map<ResourceLocation, BakedModel> bakedRegistry;
    @Nullable
    private AtlasSet atlases;
    private final BlockModelShaper blockModelShaper;
    private final TextureManager textureManager;
    private final BlockColors blockColors;
    private int maxMipmapLevels;
    private BakedModel missingModel;
    private Object2IntMap<BlockState> modelGroups;
    
    public ModelManager(final TextureManager ejv, final BlockColors dkl, final int integer) {
        this.textureManager = ejv;
        this.blockColors = dkl;
        this.maxMipmapLevels = integer;
        this.blockModelShaper = new BlockModelShaper(this);
    }
    
    public BakedModel getModel(final ModelResourceLocation elm) {
        return (BakedModel)this.bakedRegistry.getOrDefault(elm, this.missingModel);
    }
    
    public BakedModel getMissingModel() {
        return this.missingModel;
    }
    
    public BlockModelShaper getBlockModelShaper() {
        return this.blockModelShaper;
    }
    
    @Override
    protected ModelBakery prepare(final ResourceManager acf, final ProfilerFiller ant) {
        ant.startTick();
        final ModelBakery elk4 = new ModelBakery(acf, this.blockColors, ant, this.maxMipmapLevels);
        ant.endTick();
        return elk4;
    }
    
    @Override
    protected void apply(final ModelBakery elk, final ResourceManager acf, final ProfilerFiller ant) {
        ant.startTick();
        ant.push("upload");
        if (this.atlases != null) {
            this.atlases.close();
        }
        this.atlases = elk.uploadTextures(this.textureManager, ant);
        this.bakedRegistry = elk.getBakedTopLevelModels();
        this.modelGroups = elk.getModelGroups();
        this.missingModel = (BakedModel)this.bakedRegistry.get(ModelBakery.MISSING_MODEL_LOCATION);
        ant.popPush("cache");
        this.blockModelShaper.rebuildCache();
        ant.pop();
        ant.endTick();
    }
    
    public boolean requiresRender(final BlockState cee1, final BlockState cee2) {
        if (cee1 == cee2) {
            return false;
        }
        final int integer4 = this.modelGroups.getInt(cee1);
        if (integer4 != -1) {
            final int integer5 = this.modelGroups.getInt(cee2);
            if (integer4 == integer5) {
                final FluidState cuu6 = cee1.getFluidState();
                final FluidState cuu7 = cee2.getFluidState();
                return cuu6 != cuu7;
            }
        }
        return true;
    }
    
    public TextureAtlas getAtlas(final ResourceLocation vk) {
        return this.atlases.getAtlas(vk);
    }
    
    public void close() {
        if (this.atlases != null) {
            this.atlases.close();
        }
    }
    
    public void updateMaxMipLevel(final int integer) {
        this.maxMipmapLevels = integer;
    }
}
