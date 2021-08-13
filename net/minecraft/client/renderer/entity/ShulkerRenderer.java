package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Vec3i;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.ShulkerHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.world.entity.monster.Shulker;

public class ShulkerRenderer extends MobRenderer<Shulker, ShulkerModel<Shulker>> {
    public static final ResourceLocation DEFAULT_TEXTURE_LOCATION;
    public static final ResourceLocation[] TEXTURE_LOCATION;
    
    public ShulkerRenderer(final EntityRenderDispatcher eel) {
        super(eel, new ShulkerModel(), 0.0f);
        this.addLayer(new ShulkerHeadLayer(this));
    }
    
    public Vec3 getRenderOffset(final Shulker bdt, final float float2) {
        final int integer4 = bdt.getClientSideTeleportInterpolation();
        if (integer4 > 0 && bdt.hasValidInterpolationPositions()) {
            final BlockPos fx5 = bdt.getAttachPosition();
            final BlockPos fx6 = bdt.getOldAttachPosition();
            double double7 = (integer4 - float2) / 6.0;
            double7 *= double7;
            final double double8 = (fx5.getX() - fx6.getX()) * double7;
            final double double9 = (fx5.getY() - fx6.getY()) * double7;
            final double double10 = (fx5.getZ() - fx6.getZ()) * double7;
            return new Vec3(-double8, -double9, -double10);
        }
        return super.getRenderOffset((T)bdt, float2);
    }
    
    @Override
    public boolean shouldRender(final Shulker bdt, final Frustum ecr, final double double3, final double double4, final double double5) {
        if (super.shouldRender(bdt, ecr, double3, double4, double5)) {
            return true;
        }
        if (bdt.getClientSideTeleportInterpolation() > 0 && bdt.hasValidInterpolationPositions()) {
            final Vec3 dck10 = Vec3.atLowerCornerOf(bdt.getAttachPosition());
            final Vec3 dck11 = Vec3.atLowerCornerOf(bdt.getOldAttachPosition());
            if (ecr.isVisible(new AABB(dck11.x, dck11.y, dck11.z, dck10.x, dck10.y, dck10.z))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Shulker bdt) {
        if (bdt.getColor() == null) {
            return ShulkerRenderer.DEFAULT_TEXTURE_LOCATION;
        }
        return ShulkerRenderer.TEXTURE_LOCATION[bdt.getColor().getId()];
    }
    
    @Override
    protected void setupRotations(final Shulker bdt, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bdt, dfj, float3, float4 + 180.0f, float5);
        dfj.translate(0.0, 0.5, 0.0);
        dfj.mulPose(bdt.getAttachFace().getOpposite().getRotation());
        dfj.translate(0.0, -0.5, 0.0);
    }
    
    static {
        DEFAULT_TEXTURE_LOCATION = new ResourceLocation("textures/" + Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION.texture().getPath() + ".png");
        TEXTURE_LOCATION = (ResourceLocation[])Sheets.SHULKER_TEXTURE_LOCATION.stream().map(elj -> new ResourceLocation("textures/" + elj.texture().getPath() + ".png")).toArray(ResourceLocation[]::new);
    }
}
