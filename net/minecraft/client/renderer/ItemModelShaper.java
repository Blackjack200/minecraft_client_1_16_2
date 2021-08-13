package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.ItemLike;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class ItemModelShaper {
    public final Int2ObjectMap<ModelResourceLocation> shapes;
    private final Int2ObjectMap<BakedModel> shapesCache;
    private final ModelManager modelManager;
    
    public ItemModelShaper(final ModelManager ell) {
        this.shapes = (Int2ObjectMap<ModelResourceLocation>)new Int2ObjectOpenHashMap(256);
        this.shapesCache = (Int2ObjectMap<BakedModel>)new Int2ObjectOpenHashMap(256);
        this.modelManager = ell;
    }
    
    public TextureAtlasSprite getParticleIcon(final ItemLike brt) {
        return this.getParticleIcon(new ItemStack(brt));
    }
    
    public TextureAtlasSprite getParticleIcon(final ItemStack bly) {
        final BakedModel elg3 = this.getItemModel(bly);
        if (elg3 == this.modelManager.getMissingModel() && bly.getItem() instanceof BlockItem) {
            return this.modelManager.getBlockModelShaper().getParticleIcon(((BlockItem)bly.getItem()).getBlock().defaultBlockState());
        }
        return elg3.getParticleIcon();
    }
    
    public BakedModel getItemModel(final ItemStack bly) {
        final BakedModel elg3 = this.getItemModel(bly.getItem());
        return (elg3 == null) ? this.modelManager.getMissingModel() : elg3;
    }
    
    @Nullable
    public BakedModel getItemModel(final Item blu) {
        return (BakedModel)this.shapesCache.get(getIndex(blu));
    }
    
    private static int getIndex(final Item blu) {
        return Item.getId(blu);
    }
    
    public void register(final Item blu, final ModelResourceLocation elm) {
        this.shapes.put(getIndex(blu), elm);
    }
    
    public ModelManager getModelManager() {
        return this.modelManager;
    }
    
    public void rebuildCache() {
        this.shapesCache.clear();
        for (final Map.Entry<Integer, ModelResourceLocation> entry3 : this.shapes.entrySet()) {
            this.shapesCache.put((Integer)entry3.getKey(), this.modelManager.getModel((ModelResourceLocation)entry3.getValue()));
        }
    }
}
