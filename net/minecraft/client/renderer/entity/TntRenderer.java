package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.item.PrimedTnt;

public class TntRenderer extends EntityRenderer<PrimedTnt> {
    public TntRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.shadowRadius = 0.5f;
    }
    
    @Override
    public void render(final PrimedTnt bct, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.translate(0.0, 0.5, 0.0);
        if (bct.getLife() - float3 + 1.0f < 10.0f) {
            float float4 = 1.0f - (bct.getLife() - float3 + 1.0f) / 10.0f;
            float4 = Mth.clamp(float4, 0.0f, 1.0f);
            float4 *= float4;
            float4 *= float4;
            final float float5 = 1.0f + float4 * 0.3f;
            dfj.scale(float5, float5, float5);
        }
        dfj.mulPose(Vector3f.YP.rotationDegrees(-90.0f));
        dfj.translate(-0.5, -0.5, 0.5);
        dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        TntMinecartRenderer.renderWhiteSolidBlock(Blocks.TNT.defaultBlockState(), dfj, dzy, integer, bct.getLife() / 5 % 2 == 0);
        dfj.popPose();
        super.render(bct, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final PrimedTnt bct) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
