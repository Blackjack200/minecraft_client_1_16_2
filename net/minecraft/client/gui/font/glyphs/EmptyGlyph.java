package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EmptyGlyph extends BakedGlyph {
    public EmptyGlyph() {
        super(RenderType.text(new ResourceLocation("")), RenderType.textSeeThrough(new ResourceLocation("")), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final boolean boolean1, final float float2, final float float3, final Matrix4f b, final VertexConsumer dfn, final float float6, final float float7, final float float8, final float float9, final int integer) {
    }
}
