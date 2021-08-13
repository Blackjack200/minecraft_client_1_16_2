package net.minecraft.client.gui;

import net.minecraft.network.chat.TextColor;
import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.locale.Language;
import java.util.List;
import java.util.Iterator;
import com.mojang.math.Transformation;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import net.minecraft.network.chat.Style;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.ArabicShaping;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.Random;
import com.mojang.math.Vector3f;

public class Font {
    private static final Vector3f SHADOW_OFFSET;
    public final int lineHeight = 9;
    public final Random random;
    private final Function<ResourceLocation, FontSet> fonts;
    private final StringSplitter splitter;
    
    public Font(final Function<ResourceLocation, FontSet> function) {
        this.random = new Random();
        this.fonts = function;
        this.splitter = new StringSplitter((integer, ob) -> this.getFontSet(ob.getFont()).getGlyphInfo(integer).getAdvance(ob.isBold()));
    }
    
    private FontSet getFontSet(final ResourceLocation vk) {
        return (FontSet)this.fonts.apply(vk);
    }
    
    public int drawShadow(final PoseStack dfj, final String string, final float float3, final float float4, final int integer) {
        return this.drawInternal(string, float3, float4, integer, dfj.last().pose(), true, this.isBidirectional());
    }
    
    public int drawShadow(final PoseStack dfj, final String string, final float float3, final float float4, final int integer, final boolean boolean6) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(string, float3, float4, integer, dfj.last().pose(), true, boolean6);
    }
    
    public int draw(final PoseStack dfj, final String string, final float float3, final float float4, final int integer) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(string, float3, float4, integer, dfj.last().pose(), false, this.isBidirectional());
    }
    
    public int drawShadow(final PoseStack dfj, final FormattedCharSequence aex, final float float3, final float float4, final int integer) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(aex, float3, float4, integer, dfj.last().pose(), true);
    }
    
    public int drawShadow(final PoseStack dfj, final Component nr, final float float3, final float float4, final int integer) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(nr.getVisualOrderText(), float3, float4, integer, dfj.last().pose(), true);
    }
    
    public int draw(final PoseStack dfj, final FormattedCharSequence aex, final float float3, final float float4, final int integer) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(aex, float3, float4, integer, dfj.last().pose(), false);
    }
    
    public int draw(final PoseStack dfj, final Component nr, final float float3, final float float4, final int integer) {
        RenderSystem.enableAlphaTest();
        return this.drawInternal(nr.getVisualOrderText(), float3, float4, integer, dfj.last().pose(), false);
    }
    
    public String bidirectionalShaping(final String string) {
        try {
            final Bidi bidi3 = new Bidi(new ArabicShaping(8).shape(string), 127);
            bidi3.setReorderingMode(0);
            return bidi3.writeReordered(2);
        }
        catch (ArabicShapingException ex) {
            return string;
        }
    }
    
    private int drawInternal(final String string, final float float2, final float float3, final int integer, final Matrix4f b, final boolean boolean6, final boolean boolean7) {
        if (string == null) {
            return 0;
        }
        final MultiBufferSource.BufferSource a9 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        final int integer2 = this.drawInBatch(string, float2, float3, integer, boolean6, b, a9, false, 0, 15728880, boolean7);
        a9.endBatch();
        return integer2;
    }
    
    private int drawInternal(final FormattedCharSequence aex, final float float2, final float float3, final int integer, final Matrix4f b, final boolean boolean6) {
        final MultiBufferSource.BufferSource a8 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        final int integer2 = this.drawInBatch(aex, float2, float3, integer, boolean6, b, a8, false, 0, 15728880);
        a8.endBatch();
        return integer2;
    }
    
    public int drawInBatch(final String string, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        return this.drawInBatch(string, float2, float3, integer4, boolean5, b, dzy, boolean8, integer9, integer10, this.isBidirectional());
    }
    
    public int drawInBatch(final String string, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10, final boolean boolean11) {
        return this.drawInternal(string, float2, float3, integer4, boolean5, b, dzy, boolean8, integer9, integer10, boolean11);
    }
    
    public int drawInBatch(final Component nr, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        return this.drawInBatch(nr.getVisualOrderText(), float2, float3, integer4, boolean5, b, dzy, boolean8, integer9, integer10);
    }
    
    public int drawInBatch(final FormattedCharSequence aex, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        return this.drawInternal(aex, float2, float3, integer4, boolean5, b, dzy, boolean8, integer9, integer10);
    }
    
    private static int adjustColor(final int integer) {
        if ((integer & 0xFC000000) == 0x0) {
            return integer | 0xFF000000;
        }
        return integer;
    }
    
    private int drawInternal(String string, float float2, final float float3, int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10, final boolean boolean11) {
        if (boolean11) {
            string = this.bidirectionalShaping(string);
        }
        integer4 = adjustColor(integer4);
        final Matrix4f b2 = b.copy();
        if (boolean5) {
            this.renderText(string, float2, float3, integer4, true, b, dzy, boolean8, integer9, integer10);
            b2.translate(Font.SHADOW_OFFSET);
        }
        float2 = this.renderText(string, float2, float3, integer4, false, b2, dzy, boolean8, integer9, integer10);
        return (int)float2 + (boolean5 ? 1 : 0);
    }
    
    private int drawInternal(final FormattedCharSequence aex, float float2, final float float3, int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        integer4 = adjustColor(integer4);
        final Matrix4f b2 = b.copy();
        if (boolean5) {
            this.renderText(aex, float2, float3, integer4, true, b, dzy, boolean8, integer9, integer10);
            b2.translate(Font.SHADOW_OFFSET);
        }
        float2 = this.renderText(aex, float2, float3, integer4, false, b2, dzy, boolean8, integer9, integer10);
        return (int)float2 + (boolean5 ? 1 : 0);
    }
    
    private float renderText(final String string, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        final StringRenderOutput a12 = new StringRenderOutput(dzy, float2, float3, integer4, boolean5, b, boolean8, integer10);
        StringDecomposer.iterateFormatted(string, Style.EMPTY, a12);
        return a12.finish(integer9, float2);
    }
    
    private float renderText(final FormattedCharSequence aex, final float float2, final float float3, final int integer4, final boolean boolean5, final Matrix4f b, final MultiBufferSource dzy, final boolean boolean8, final int integer9, final int integer10) {
        final StringRenderOutput a12 = new StringRenderOutput(dzy, float2, float3, integer4, boolean5, b, boolean8, integer10);
        aex.accept(a12);
        return a12.finish(integer9, float2);
    }
    
    private void renderChar(final BakedGlyph dmw, final boolean boolean2, final boolean boolean3, final float float4, final float float5, final float float6, final Matrix4f b, final VertexConsumer dfn, final float float9, final float float10, final float float11, final float float12, final int integer) {
        dmw.render(boolean3, float5, float6, b, dfn, float9, float10, float11, float12, integer);
        if (boolean2) {
            dmw.render(boolean3, float5 + float4, float6, b, dfn, float9, float10, float11, float12, integer);
        }
    }
    
    public int width(final String string) {
        return Mth.ceil(this.splitter.stringWidth(string));
    }
    
    public int width(final FormattedText nu) {
        return Mth.ceil(this.splitter.stringWidth(nu));
    }
    
    public int width(final FormattedCharSequence aex) {
        return Mth.ceil(this.splitter.stringWidth(aex));
    }
    
    public String plainSubstrByWidth(final String string, final int integer, final boolean boolean3) {
        return boolean3 ? this.splitter.plainTailByWidth(string, integer, Style.EMPTY) : this.splitter.plainHeadByWidth(string, integer, Style.EMPTY);
    }
    
    public String plainSubstrByWidth(final String string, final int integer) {
        return this.splitter.plainHeadByWidth(string, integer, Style.EMPTY);
    }
    
    public FormattedText substrByWidth(final FormattedText nu, final int integer) {
        return this.splitter.headByWidth(nu, integer, Style.EMPTY);
    }
    
    public void drawWordWrap(final FormattedText nu, final int integer2, int integer3, final int integer4, final int integer5) {
        final Matrix4f b7 = Transformation.identity().getMatrix();
        for (final FormattedCharSequence aex9 : this.split(nu, integer4)) {
            this.drawInternal(aex9, (float)integer2, (float)integer3, integer5, b7, false);
            integer3 += 9;
        }
    }
    
    public int wordWrapHeight(final String string, final int integer) {
        return 9 * this.splitter.splitLines(string, integer, Style.EMPTY).size();
    }
    
    public List<FormattedCharSequence> split(final FormattedText nu, final int integer) {
        return Language.getInstance().getVisualOrder(this.splitter.splitLines(nu, integer, Style.EMPTY));
    }
    
    public boolean isBidirectional() {
        return Language.getInstance().isDefaultRightToLeft();
    }
    
    public StringSplitter getSplitter() {
        return this.splitter;
    }
    
    static {
        SHADOW_OFFSET = new Vector3f(0.0f, 0.0f, 0.03f);
    }
    
    class StringRenderOutput implements FormattedCharSink {
        final MultiBufferSource bufferSource;
        private final boolean dropShadow;
        private final float dimFactor;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final Matrix4f pose;
        private final boolean seeThrough;
        private final int packedLightCoords;
        private float x;
        private float y;
        @Nullable
        private List<BakedGlyph.Effect> effects;
        
        private void addEffect(final BakedGlyph.Effect a) {
            if (this.effects == null) {
                this.effects = (List<BakedGlyph.Effect>)Lists.newArrayList();
            }
            this.effects.add(a);
        }
        
        public StringRenderOutput(final MultiBufferSource dzy, final float float3, final float float4, final int integer5, final boolean boolean6, final Matrix4f b, final boolean boolean8, final int integer9) {
            this.bufferSource = dzy;
            this.x = float3;
            this.y = float4;
            this.dropShadow = boolean6;
            this.dimFactor = (boolean6 ? 0.25f : 1.0f);
            this.r = (integer5 >> 16 & 0xFF) / 255.0f * this.dimFactor;
            this.g = (integer5 >> 8 & 0xFF) / 255.0f * this.dimFactor;
            this.b = (integer5 & 0xFF) / 255.0f * this.dimFactor;
            this.a = (integer5 >> 24 & 0xFF) / 255.0f;
            this.pose = b;
            this.seeThrough = boolean8;
            this.packedLightCoords = integer9;
        }
        
        public boolean accept(final int integer1, final Style ob, final int integer3) {
            final FontSet dmt5 = Font.this.getFontSet(ob.getFont());
            final GlyphInfo ddx6 = dmt5.getGlyphInfo(integer3);
            final BakedGlyph dmw7 = (ob.isObfuscated() && integer3 != 32) ? dmt5.getRandomGlyph(ddx6) : dmt5.getGlyph(integer3);
            final boolean boolean8 = ob.isBold();
            final float float12 = this.a;
            final TextColor od13 = ob.getColor();
            float float13;
            float float14;
            float float15;
            if (od13 != null) {
                final int integer4 = od13.getValue();
                float13 = (integer4 >> 16 & 0xFF) / 255.0f * this.dimFactor;
                float14 = (integer4 >> 8 & 0xFF) / 255.0f * this.dimFactor;
                float15 = (integer4 & 0xFF) / 255.0f * this.dimFactor;
            }
            else {
                float13 = this.r;
                float14 = this.g;
                float15 = this.b;
            }
            if (!(dmw7 instanceof EmptyGlyph)) {
                final float float16 = boolean8 ? ddx6.getBoldOffset() : 0.0f;
                final float float17 = this.dropShadow ? ddx6.getShadowOffset() : 0.0f;
                final VertexConsumer dfn16 = this.bufferSource.getBuffer(dmw7.renderType(this.seeThrough));
                Font.this.renderChar(dmw7, boolean8, ob.isItalic(), float16, this.x + float17, this.y + float17, this.pose, dfn16, float13, float14, float15, float12, this.packedLightCoords);
            }
            final float float16 = ddx6.getAdvance(boolean8);
            final float float17 = this.dropShadow ? 1.0f : 0.0f;
            if (ob.isStrikethrough()) {
                this.addEffect(new BakedGlyph.Effect(this.x + float17 - 1.0f, this.y + float17 + 4.5f, this.x + float17 + float16, this.y + float17 + 4.5f - 1.0f, 0.01f, float13, float14, float15, float12));
            }
            if (ob.isUnderlined()) {
                this.addEffect(new BakedGlyph.Effect(this.x + float17 - 1.0f, this.y + float17 + 9.0f, this.x + float17 + float16, this.y + float17 + 9.0f - 1.0f, 0.01f, float13, float14, float15, float12));
            }
            this.x += float16;
            return true;
        }
        
        public float finish(final int integer, final float float2) {
            if (integer != 0) {
                final float float3 = (integer >> 24 & 0xFF) / 255.0f;
                final float float4 = (integer >> 16 & 0xFF) / 255.0f;
                final float float5 = (integer >> 8 & 0xFF) / 255.0f;
                final float float6 = (integer & 0xFF) / 255.0f;
                this.addEffect(new BakedGlyph.Effect(float2 - 1.0f, this.y + 9.0f, this.x + 1.0f, this.y - 1.0f, 0.01f, float4, float5, float6, float3));
            }
            if (this.effects != null) {
                final BakedGlyph dmw4 = Font.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
                final VertexConsumer dfn5 = this.bufferSource.getBuffer(dmw4.renderType(this.seeThrough));
                for (final BakedGlyph.Effect a7 : this.effects) {
                    dmw4.renderEffect(a7, this.pose, dfn5, this.packedLightCoords);
                }
            }
            return this.x;
        }
    }
}
