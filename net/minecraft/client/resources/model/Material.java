package net.minecraft.client.resources.model;

import java.util.Objects;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class Material {
    private final ResourceLocation atlasLocation;
    private final ResourceLocation texture;
    @Nullable
    private RenderType renderType;
    
    public Material(final ResourceLocation vk1, final ResourceLocation vk2) {
        this.atlasLocation = vk1;
        this.texture = vk2;
    }
    
    public ResourceLocation atlasLocation() {
        return this.atlasLocation;
    }
    
    public ResourceLocation texture() {
        return this.texture;
    }
    
    public TextureAtlasSprite sprite() {
        return (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(this.atlasLocation()).apply(this.texture());
    }
    
    public RenderType renderType(final Function<ResourceLocation, RenderType> function) {
        if (this.renderType == null) {
            this.renderType = (RenderType)function.apply(this.atlasLocation);
        }
        return this.renderType;
    }
    
    public VertexConsumer buffer(final MultiBufferSource dzy, final Function<ResourceLocation, RenderType> function) {
        return this.sprite().wrap(dzy.getBuffer(this.renderType(function)));
    }
    
    public VertexConsumer buffer(final MultiBufferSource dzy, final Function<ResourceLocation, RenderType> function, final boolean boolean3) {
        return this.sprite().wrap(ItemRenderer.getFoilBufferDirect(dzy, this.renderType(function), true, boolean3));
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Material elj3 = (Material)object;
        return this.atlasLocation.equals(elj3.atlasLocation) && this.texture.equals(elj3.texture);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.atlasLocation, this.texture });
    }
    
    public String toString() {
        return new StringBuilder().append("Material{atlasLocation=").append(this.atlasLocation).append(", texture=").append(this.texture).append('}').toString();
    }
}
