package net.minecraft.client.renderer.texture;

import java.util.List;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.util.LazyLoadedValue;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public final class MissingTextureAtlasSprite extends TextureAtlasSprite {
    private static final ResourceLocation MISSING_TEXTURE_LOCATION;
    @Nullable
    private static DynamicTexture missingTexture;
    private static final LazyLoadedValue<NativeImage> MISSING_IMAGE_DATA;
    private static final Info INFO;
    
    private MissingTextureAtlasSprite(final TextureAtlas ejt, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        super(ejt, MissingTextureAtlasSprite.INFO, integer2, integer3, integer4, integer5, integer6, MissingTextureAtlasSprite.MISSING_IMAGE_DATA.get());
    }
    
    public static MissingTextureAtlasSprite newInstance(final TextureAtlas ejt, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return new MissingTextureAtlasSprite(ejt, integer2, integer3, integer4, integer5, integer6);
    }
    
    public static ResourceLocation getLocation() {
        return MissingTextureAtlasSprite.MISSING_TEXTURE_LOCATION;
    }
    
    public static Info info() {
        return MissingTextureAtlasSprite.INFO;
    }
    
    @Override
    public void close() {
        for (int integer2 = 1; integer2 < this.mainImage.length; ++integer2) {
            this.mainImage[integer2].close();
        }
    }
    
    public static DynamicTexture getTexture() {
        if (MissingTextureAtlasSprite.missingTexture == null) {
            MissingTextureAtlasSprite.missingTexture = new DynamicTexture(MissingTextureAtlasSprite.MISSING_IMAGE_DATA.get());
            Minecraft.getInstance().getTextureManager().register(MissingTextureAtlasSprite.MISSING_TEXTURE_LOCATION, MissingTextureAtlasSprite.missingTexture);
        }
        return MissingTextureAtlasSprite.missingTexture;
    }
    
    static {
        MISSING_TEXTURE_LOCATION = new ResourceLocation("missingno");
        MISSING_IMAGE_DATA = new LazyLoadedValue<NativeImage>((java.util.function.Supplier<NativeImage>)(() -> {
            final NativeImage deq1 = new NativeImage(16, 16, false);
            final int integer2 = -16777216;
            final int integer3 = -524040;
            for (int integer4 = 0; integer4 < 16; ++integer4) {
                for (int integer5 = 0; integer5 < 16; ++integer5) {
                    if (integer4 < 8 ^ integer5 < 8) {
                        deq1.setPixelRGBA(integer5, integer4, -524040);
                    }
                    else {
                        deq1.setPixelRGBA(integer5, integer4, -16777216);
                    }
                }
            }
            deq1.untrack();
            return deq1;
        }));
        INFO = new Info(MissingTextureAtlasSprite.MISSING_TEXTURE_LOCATION, 16, 16, new AnimationMetadataSection((List<AnimationFrame>)Lists.newArrayList((Object[])new AnimationFrame[] { new AnimationFrame(0, -1) }), 16, 16, 1, false));
    }
}
