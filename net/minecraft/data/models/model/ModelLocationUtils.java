package net.minecraft.data.models.model;

import net.minecraft.world.item.Item;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

public class ModelLocationUtils {
    @Deprecated
    public static ResourceLocation decorateBlockModelLocation(final String string) {
        return new ResourceLocation("minecraft", "block/" + string);
    }
    
    public static ResourceLocation decorateItemModelLocation(final String string) {
        return new ResourceLocation("minecraft", "item/" + string);
    }
    
    public static ResourceLocation getModelLocation(final Block bul, final String string) {
        final ResourceLocation vk3 = Registry.BLOCK.getKey(bul);
        return new ResourceLocation(vk3.getNamespace(), "block/" + vk3.getPath() + string);
    }
    
    public static ResourceLocation getModelLocation(final Block bul) {
        final ResourceLocation vk2 = Registry.BLOCK.getKey(bul);
        return new ResourceLocation(vk2.getNamespace(), "block/" + vk2.getPath());
    }
    
    public static ResourceLocation getModelLocation(final Item blu) {
        final ResourceLocation vk2 = Registry.ITEM.getKey(blu);
        return new ResourceLocation(vk2.getNamespace(), "item/" + vk2.getPath());
    }
    
    public static ResourceLocation getModelLocation(final Item blu, final String string) {
        final ResourceLocation vk3 = Registry.ITEM.getKey(blu);
        return new ResourceLocation(vk3.getNamespace(), "item/" + vk3.getPath() + string);
    }
}
