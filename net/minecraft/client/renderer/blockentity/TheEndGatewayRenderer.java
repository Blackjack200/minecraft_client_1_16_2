package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;

public class TheEndGatewayRenderer extends TheEndPortalRenderer<TheEndGatewayBlockEntity> {
    private static final ResourceLocation BEAM_LOCATION;
    
    public TheEndGatewayRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final TheEndGatewayBlockEntity cdh, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        if (cdh.isSpawning() || cdh.isCoolingDown()) {
            float float3 = cdh.isSpawning() ? cdh.getSpawnPercent(float2) : cdh.getCooldownPercent(float2);
            final double double9 = cdh.isSpawning() ? 256.0 : 50.0;
            float3 = Mth.sin(float3 * 3.1415927f);
            final int integer7 = Mth.floor(float3 * double9);
            final float[] arr12 = cdh.isSpawning() ? DyeColor.MAGENTA.getTextureDiffuseColors() : DyeColor.PURPLE.getTextureDiffuseColors();
            final long long13 = cdh.getLevel().getGameTime();
            BeaconRenderer.renderBeaconBeam(dfj, dzy, TheEndGatewayRenderer.BEAM_LOCATION, float2, float3, long13, 0, integer7, arr12, 0.15f, 0.175f);
            BeaconRenderer.renderBeaconBeam(dfj, dzy, TheEndGatewayRenderer.BEAM_LOCATION, float2, float3, long13, 0, -integer7, arr12, 0.15f, 0.175f);
        }
        super.render(cdh, float2, dfj, dzy, integer5, integer6);
    }
    
    @Override
    protected int getPasses(final double double1) {
        return super.getPasses(double1) + 1;
    }
    
    @Override
    protected float getOffset() {
        return 1.0f;
    }
    
    static {
        BEAM_LOCATION = new ResourceLocation("textures/entity/end_gateway_beam.png");
    }
}
