package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.vehicle.MinecartTNT;

public class TntMinecartRenderer extends MinecartRenderer<MinecartTNT> {
    public TntMinecartRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    protected void renderMinecartContents(final MinecartTNT bhs, final float float2, final BlockState cee, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final int integer2 = bhs.getFuse();
        if (integer2 > -1 && integer2 - float2 + 1.0f < 10.0f) {
            float float3 = 1.0f - (integer2 - float2 + 1.0f) / 10.0f;
            float3 = Mth.clamp(float3, 0.0f, 1.0f);
            float3 *= float3;
            float3 *= float3;
            final float float4 = 1.0f + float3 * 0.3f;
            dfj.scale(float4, float4, float4);
        }
        renderWhiteSolidBlock(cee, dfj, dzy, integer, integer2 > -1 && integer2 / 5 % 2 == 0);
    }
    
    public static void renderWhiteSolidBlock(final BlockState cee, final PoseStack dfj, final MultiBufferSource dzy, final int integer, final boolean boolean5) {
        int integer2;
        if (boolean5) {
            integer2 = OverlayTexture.pack(OverlayTexture.u(1.0f), 10);
        }
        else {
            integer2 = OverlayTexture.NO_OVERLAY;
        }
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(cee, dfj, dzy, integer, integer2);
    }
}
