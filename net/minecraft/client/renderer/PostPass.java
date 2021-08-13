package net.minecraft.client.renderer;

import java.util.Iterator;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import com.google.common.collect.Lists;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.math.Matrix4f;
import java.util.function.IntSupplier;
import java.util.List;
import com.mojang.blaze3d.pipeline.RenderTarget;

public class PostPass implements AutoCloseable {
    private final EffectInstance effect;
    public final RenderTarget inTarget;
    public final RenderTarget outTarget;
    private final List<IntSupplier> auxAssets;
    private final List<String> auxNames;
    private final List<Integer> auxWidths;
    private final List<Integer> auxHeights;
    private Matrix4f shaderOrthoMatrix;
    
    public PostPass(final ResourceManager acf, final String string, final RenderTarget ded3, final RenderTarget ded4) throws IOException {
        this.auxAssets = (List<IntSupplier>)Lists.newArrayList();
        this.auxNames = (List<String>)Lists.newArrayList();
        this.auxWidths = (List<Integer>)Lists.newArrayList();
        this.auxHeights = (List<Integer>)Lists.newArrayList();
        this.effect = new EffectInstance(acf, string);
        this.inTarget = ded3;
        this.outTarget = ded4;
    }
    
    public void close() {
        this.effect.close();
    }
    
    public void addAuxAsset(final String string, final IntSupplier intSupplier, final int integer3, final int integer4) {
        this.auxNames.add(this.auxNames.size(), string);
        this.auxAssets.add(this.auxAssets.size(), intSupplier);
        this.auxWidths.add(this.auxWidths.size(), integer3);
        this.auxHeights.add(this.auxHeights.size(), integer4);
    }
    
    public void setOrthoMatrix(final Matrix4f b) {
        this.shaderOrthoMatrix = b;
    }
    
    public void process(final float float1) {
        this.inTarget.unbindWrite();
        final float float2 = (float)this.outTarget.width;
        final float float3 = (float)this.outTarget.height;
        RenderSystem.viewport(0, 0, (int)float2, (int)float3);
        this.effect.setSampler("DiffuseSampler", this.inTarget::getColorTextureId);
        for (int integer5 = 0; integer5 < this.auxAssets.size(); ++integer5) {
            this.effect.setSampler((String)this.auxNames.get(integer5), (IntSupplier)this.auxAssets.get(integer5));
            this.effect.safeGetUniform(new StringBuilder().append("AuxSize").append(integer5).toString()).set((float)(int)this.auxWidths.get(integer5), (float)(int)this.auxHeights.get(integer5));
        }
        this.effect.safeGetUniform("ProjMat").set(this.shaderOrthoMatrix);
        this.effect.safeGetUniform("InSize").set((float)this.inTarget.width, (float)this.inTarget.height);
        this.effect.safeGetUniform("OutSize").set(float2, float3);
        this.effect.safeGetUniform("Time").set(float1);
        final Minecraft djw5 = Minecraft.getInstance();
        this.effect.safeGetUniform("ScreenSize").set((float)djw5.getWindow().getWidth(), (float)djw5.getWindow().getHeight());
        this.effect.apply();
        this.outTarget.clear(Minecraft.ON_OSX);
        this.outTarget.bindWrite(false);
        RenderSystem.depthFunc(519);
        final BufferBuilder dfe6 = Tesselator.getInstance().getBuilder();
        dfe6.begin(7, DefaultVertexFormat.POSITION_COLOR);
        dfe6.vertex(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        dfe6.vertex(float2, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
        dfe6.vertex(float2, float3, 500.0).color(255, 255, 255, 255).endVertex();
        dfe6.vertex(0.0, float3, 500.0).color(255, 255, 255, 255).endVertex();
        dfe6.end();
        BufferUploader.end(dfe6);
        RenderSystem.depthFunc(515);
        this.effect.clear();
        this.outTarget.unbindWrite();
        this.inTarget.unbindRead();
        for (final Object object8 : this.auxAssets) {
            if (object8 instanceof RenderTarget) {
                ((RenderTarget)object8).unbindRead();
            }
        }
    }
    
    public EffectInstance getEffect() {
        return this.effect;
    }
}
