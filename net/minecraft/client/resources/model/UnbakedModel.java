package net.minecraft.client.resources.model;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.datafixers.util.Pair;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;

public interface UnbakedModel {
    Collection<ResourceLocation> getDependencies();
    
    Collection<Material> getMaterials(final Function<ResourceLocation, UnbakedModel> function, final Set<Pair<String, String>> set);
    
    @Nullable
    BakedModel bake(final ModelBakery elk, final Function<Material, TextureAtlasSprite> function, final ModelState eln, final ResourceLocation vk);
}
