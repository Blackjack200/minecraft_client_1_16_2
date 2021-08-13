package net.minecraft.client.gui.font;

import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import com.mojang.blaze3d.font.RawGlyph;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.AbstractTexture;

public class FontTexture extends AbstractTexture {
    private final ResourceLocation name;
    private final RenderType normalType;
    private final RenderType seeThroughType;
    private final boolean colored;
    private final Node root;
    
    public FontTexture(final ResourceLocation vk, final boolean boolean2) {
        this.name = vk;
        this.colored = boolean2;
        this.root = new Node(0, 0, 256, 256);
        TextureUtil.prepareImage(boolean2 ? NativeImage.InternalGlFormat.RGBA : NativeImage.InternalGlFormat.INTENSITY, this.getId(), 256, 256);
        this.normalType = RenderType.text(vk);
        this.seeThroughType = RenderType.textSeeThrough(vk);
    }
    
    @Override
    public void load(final ResourceManager acf) {
    }
    
    @Override
    public void close() {
        this.releaseId();
    }
    
    @Nullable
    public BakedGlyph add(final RawGlyph ddz) {
        if (ddz.isColored() != this.colored) {
            return null;
        }
        final Node a3 = this.root.insert(ddz);
        if (a3 != null) {
            this.bind();
            ddz.upload(a3.x, a3.y);
            final float float4 = 256.0f;
            final float float5 = 256.0f;
            final float float6 = 0.01f;
            return new BakedGlyph(this.normalType, this.seeThroughType, (a3.x + 0.01f) / 256.0f, (a3.x - 0.01f + ddz.getPixelWidth()) / 256.0f, (a3.y + 0.01f) / 256.0f, (a3.y - 0.01f + ddz.getPixelHeight()) / 256.0f, ddz.getLeft(), ddz.getRight(), ddz.getUp(), ddz.getDown());
        }
        return null;
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    static class Node {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private Node left;
        private Node right;
        private boolean occupied;
        
        private Node(final int integer1, final int integer2, final int integer3, final int integer4) {
            this.x = integer1;
            this.y = integer2;
            this.width = integer3;
            this.height = integer4;
        }
        
        @Nullable
        Node insert(final RawGlyph ddz) {
            if (this.left != null && this.right != null) {
                Node a3 = this.left.insert(ddz);
                if (a3 == null) {
                    a3 = this.right.insert(ddz);
                }
                return a3;
            }
            if (this.occupied) {
                return null;
            }
            final int integer3 = ddz.getPixelWidth();
            final int integer4 = ddz.getPixelHeight();
            if (integer3 > this.width || integer4 > this.height) {
                return null;
            }
            if (integer3 == this.width && integer4 == this.height) {
                this.occupied = true;
                return this;
            }
            final int integer5 = this.width - integer3;
            final int integer6 = this.height - integer4;
            if (integer5 > integer6) {
                this.left = new Node(this.x, this.y, integer3, this.height);
                this.right = new Node(this.x + integer3 + 1, this.y, this.width - integer3 - 1, this.height);
            }
            else {
                this.left = new Node(this.x, this.y, this.width, integer4);
                this.right = new Node(this.x, this.y + integer4 + 1, this.width, this.height - integer4 - 1);
            }
            return this.left.insert(ddz);
        }
    }
}
