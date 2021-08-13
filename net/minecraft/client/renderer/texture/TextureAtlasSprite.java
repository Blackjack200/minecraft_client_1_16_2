package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.SpriteCoordinateExpander;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.CrashReportCategory;
import java.util.List;
import java.util.Iterator;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import com.google.common.collect.Lists;
import java.util.Arrays;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;

public class TextureAtlasSprite implements AutoCloseable {
    private final TextureAtlas atlas;
    private final Info info;
    private final AnimationMetadataSection metadata;
    protected final NativeImage[] mainImage;
    private final int[] framesX;
    private final int[] framesY;
    @Nullable
    private final InterpolationData interpolationData;
    private final int x;
    private final int y;
    private final float u0;
    private final float u1;
    private final float v0;
    private final float v1;
    private int frame;
    private int subFrame;
    
    protected TextureAtlasSprite(final TextureAtlas ejt, final Info a, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final NativeImage deq) {
        this.atlas = ejt;
        AnimationMetadataSection eku10 = a.metadata;
        final int integer8 = a.width;
        final int integer9 = a.height;
        this.x = integer6;
        this.y = integer7;
        this.u0 = integer6 / (float)integer4;
        this.u1 = (integer6 + integer8) / (float)integer4;
        this.v0 = integer7 / (float)integer5;
        this.v1 = (integer7 + integer9) / (float)integer5;
        final int integer10 = deq.getWidth() / eku10.getFrameWidth(integer8);
        final int integer11 = deq.getHeight() / eku10.getFrameHeight(integer9);
        if (eku10.getFrameCount() > 0) {
            final int integer12 = (int)eku10.getUniqueFrameIndices().stream().max(Integer::compareTo).get() + 1;
            this.framesX = new int[integer12];
            this.framesY = new int[integer12];
            Arrays.fill(this.framesX, -1);
            Arrays.fill(this.framesY, -1);
            for (final int integer13 : eku10.getUniqueFrameIndices()) {
                if (integer13 >= integer10 * integer11) {
                    throw new RuntimeException(new StringBuilder().append("invalid frameindex ").append(integer13).toString());
                }
                final int integer14 = integer13 / integer10;
                final int integer15 = integer13 % integer10;
                this.framesX[integer13] = integer15;
                this.framesY[integer13] = integer14;
            }
        }
        else {
            final List<AnimationFrame> list15 = (List<AnimationFrame>)Lists.newArrayList();
            final int integer16 = integer10 * integer11;
            this.framesX = new int[integer16];
            this.framesY = new int[integer16];
            for (int integer13 = 0; integer13 < integer11; ++integer13) {
                for (int integer14 = 0; integer14 < integer10; ++integer14) {
                    final int integer15 = integer13 * integer10 + integer14;
                    this.framesX[integer15] = integer14;
                    this.framesY[integer15] = integer13;
                    list15.add(new AnimationFrame(integer15, -1));
                }
            }
            eku10 = new AnimationMetadataSection(list15, integer8, integer9, eku10.getDefaultFrameTime(), eku10.isInterpolatedFrames());
        }
        this.info = new Info(a.name, integer8, integer9, eku10);
        this.metadata = eku10;
        try {
            try {
                this.mainImage = MipmapGenerator.generateMipLevels(deq, integer3);
            }
            catch (Throwable throwable15) {
                final CrashReport l16 = CrashReport.forThrowable(throwable15, "Generating mipmaps for frame");
                final CrashReportCategory m17 = l16.addCategory("Frame being iterated");
                m17.setDetail("First frame", (CrashReportDetail<String>)(() -> {
                    final StringBuilder stringBuilder2 = new StringBuilder();
                    if (stringBuilder2.length() > 0) {
                        stringBuilder2.append(", ");
                    }
                    stringBuilder2.append(deq.getWidth()).append("x").append(deq.getHeight());
                    return stringBuilder2.toString();
                }));
                throw new ReportedException(l16);
            }
        }
        catch (Throwable throwable15) {
            final CrashReport l16 = CrashReport.forThrowable(throwable15, "Applying mipmap");
            final CrashReportCategory m17 = l16.addCategory("Sprite being mipmapped");
            m17.setDetail("Sprite name", (CrashReportDetail<String>)(() -> this.getName().toString()));
            m17.setDetail("Sprite size", (CrashReportDetail<String>)(() -> new StringBuilder().append(this.getWidth()).append(" x ").append(this.getHeight()).toString()));
            m17.setDetail("Sprite frames", (CrashReportDetail<String>)(() -> new StringBuilder().append(this.getFrameCount()).append(" frames").toString()));
            m17.setDetail("Mipmap levels", integer3);
            throw new ReportedException(l16);
        }
        if (eku10.isInterpolatedFrames()) {
            this.interpolationData = new InterpolationData(a, integer3);
        }
        else {
            this.interpolationData = null;
        }
    }
    
    private void upload(final int integer) {
        final int integer2 = this.framesX[integer] * this.info.width;
        final int integer3 = this.framesY[integer] * this.info.height;
        this.upload(integer2, integer3, this.mainImage);
    }
    
    private void upload(final int integer1, final int integer2, final NativeImage[] arr) {
        for (int integer3 = 0; integer3 < this.mainImage.length; ++integer3) {
            arr[integer3].upload(integer3, this.x >> integer3, this.y >> integer3, integer1 >> integer3, integer2 >> integer3, this.info.width >> integer3, this.info.height >> integer3, this.mainImage.length > 1, false);
        }
    }
    
    public int getWidth() {
        return this.info.width;
    }
    
    public int getHeight() {
        return this.info.height;
    }
    
    public float getU0() {
        return this.u0;
    }
    
    public float getU1() {
        return this.u1;
    }
    
    public float getU(final double double1) {
        final float float4 = this.u1 - this.u0;
        return this.u0 + float4 * (float)double1 / 16.0f;
    }
    
    public float getV0() {
        return this.v0;
    }
    
    public float getV1() {
        return this.v1;
    }
    
    public float getV(final double double1) {
        final float float4 = this.v1 - this.v0;
        return this.v0 + float4 * (float)double1 / 16.0f;
    }
    
    public ResourceLocation getName() {
        return this.info.name;
    }
    
    public TextureAtlas atlas() {
        return this.atlas;
    }
    
    public int getFrameCount() {
        return this.framesX.length;
    }
    
    public void close() {
        for (final NativeImage deq5 : this.mainImage) {
            if (deq5 != null) {
                deq5.close();
            }
        }
        if (this.interpolationData != null) {
            this.interpolationData.close();
        }
    }
    
    public String toString() {
        final int integer2 = this.framesX.length;
        return new StringBuilder().append("TextureAtlasSprite{name='").append(this.info.name).append('\'').append(", frameCount=").append(integer2).append(", x=").append(this.x).append(", y=").append(this.y).append(", height=").append(this.info.height).append(", width=").append(this.info.width).append(", u0=").append(this.u0).append(", u1=").append(this.u1).append(", v0=").append(this.v0).append(", v1=").append(this.v1).append('}').toString();
    }
    
    public boolean isTransparent(final int integer1, final int integer2, final int integer3) {
        return (this.mainImage[0].getPixelRGBA(integer2 + this.framesX[integer1] * this.info.width, integer3 + this.framesY[integer1] * this.info.height) >> 24 & 0xFF) == 0x0;
    }
    
    public void uploadFirstFrame() {
        this.upload(0);
    }
    
    private float atlasSize() {
        final float float2 = this.info.width / (this.u1 - this.u0);
        final float float3 = this.info.height / (this.v1 - this.v0);
        return Math.max(float3, float2);
    }
    
    public float uvShrinkRatio() {
        return 4.0f / this.atlasSize();
    }
    
    public void cycleFrames() {
        ++this.subFrame;
        if (this.subFrame >= this.metadata.getFrameTime(this.frame)) {
            final int integer2 = this.metadata.getFrameIndex(this.frame);
            final int integer3 = (this.metadata.getFrameCount() == 0) ? this.getFrameCount() : this.metadata.getFrameCount();
            this.frame = (this.frame + 1) % integer3;
            this.subFrame = 0;
            final int integer4 = this.metadata.getFrameIndex(this.frame);
            if (integer2 != integer4 && integer4 >= 0 && integer4 < this.getFrameCount()) {
                this.upload(integer4);
            }
        }
        else if (this.interpolationData != null) {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> this.interpolationData.uploadInterpolatedFrame());
            }
            else {
                this.interpolationData.uploadInterpolatedFrame();
            }
        }
    }
    
    public boolean isAnimation() {
        return this.metadata.getFrameCount() > 1;
    }
    
    public VertexConsumer wrap(final VertexConsumer dfn) {
        return new SpriteCoordinateExpander(dfn, this);
    }
    
    public static final class Info {
        private final ResourceLocation name;
        private final int width;
        private final int height;
        private final AnimationMetadataSection metadata;
        
        public Info(final ResourceLocation vk, final int integer2, final int integer3, final AnimationMetadataSection eku) {
            this.name = vk;
            this.width = integer2;
            this.height = integer3;
            this.metadata = eku;
        }
        
        public ResourceLocation name() {
            return this.name;
        }
        
        public int width() {
            return this.width;
        }
        
        public int height() {
            return this.height;
        }
    }
    
    final class InterpolationData implements AutoCloseable {
        private final NativeImage[] activeFrame;
        
        private InterpolationData(final Info a, final int integer) {
            this.activeFrame = new NativeImage[integer + 1];
            for (int integer2 = 0; integer2 < this.activeFrame.length; ++integer2) {
                final int integer3 = a.width >> integer2;
                final int integer4 = a.height >> integer2;
                if (this.activeFrame[integer2] == null) {
                    this.activeFrame[integer2] = new NativeImage(integer3, integer4, false);
                }
            }
        }
        
        private void uploadInterpolatedFrame() {
            final double double2 = 1.0 - TextureAtlasSprite.this.subFrame / (double)TextureAtlasSprite.this.metadata.getFrameTime(TextureAtlasSprite.this.frame);
            final int integer4 = TextureAtlasSprite.this.metadata.getFrameIndex(TextureAtlasSprite.this.frame);
            final int integer5 = (TextureAtlasSprite.this.metadata.getFrameCount() == 0) ? TextureAtlasSprite.this.getFrameCount() : TextureAtlasSprite.this.metadata.getFrameCount();
            final int integer6 = TextureAtlasSprite.this.metadata.getFrameIndex((TextureAtlasSprite.this.frame + 1) % integer5);
            if (integer4 != integer6 && integer6 >= 0 && integer6 < TextureAtlasSprite.this.getFrameCount()) {
                for (int integer7 = 0; integer7 < this.activeFrame.length; ++integer7) {
                    final int integer8 = TextureAtlasSprite.this.info.width >> integer7;
                    for (int integer9 = TextureAtlasSprite.this.info.height >> integer7, integer10 = 0; integer10 < integer9; ++integer10) {
                        for (int integer11 = 0; integer11 < integer8; ++integer11) {
                            final int integer12 = this.getPixel(integer4, integer7, integer11, integer10);
                            final int integer13 = this.getPixel(integer6, integer7, integer11, integer10);
                            final int integer14 = this.mix(double2, integer12 >> 16 & 0xFF, integer13 >> 16 & 0xFF);
                            final int integer15 = this.mix(double2, integer12 >> 8 & 0xFF, integer13 >> 8 & 0xFF);
                            final int integer16 = this.mix(double2, integer12 & 0xFF, integer13 & 0xFF);
                            this.activeFrame[integer7].setPixelRGBA(integer11, integer10, (integer12 & 0xFF000000) | integer14 << 16 | integer15 << 8 | integer16);
                        }
                    }
                }
                TextureAtlasSprite.this.upload(0, 0, this.activeFrame);
            }
        }
        
        private int getPixel(final int integer1, final int integer2, final int integer3, final int integer4) {
            return TextureAtlasSprite.this.mainImage[integer2].getPixelRGBA(integer3 + (TextureAtlasSprite.this.framesX[integer1] * TextureAtlasSprite.this.info.width >> integer2), integer4 + (TextureAtlasSprite.this.framesY[integer1] * TextureAtlasSprite.this.info.height >> integer2));
        }
        
        private int mix(final double double1, final int integer2, final int integer3) {
            return (int)(double1 * integer2 + (1.0 - double1) * integer3);
        }
        
        public void close() {
            for (final NativeImage deq5 : this.activeFrame) {
                if (deq5 != null) {
                    deq5.close();
                }
            }
        }
    }
}
