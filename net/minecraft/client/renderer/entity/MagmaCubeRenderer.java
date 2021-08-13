package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.world.entity.monster.MagmaCube;

public class MagmaCubeRenderer extends MobRenderer<MagmaCube, LavaSlimeModel<MagmaCube>> {
    private static final ResourceLocation MAGMACUBE_LOCATION;
    
    public MagmaCubeRenderer(final EntityRenderDispatcher eel) {
        super(eel, new LavaSlimeModel(), 0.25f);
    }
    
    protected int getBlockLightLevel(final MagmaCube bdm, final BlockPos fx) {
        return 15;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final MagmaCube bdm) {
        return MagmaCubeRenderer.MAGMACUBE_LOCATION;
    }
    
    @Override
    protected void scale(final MagmaCube bdm, final PoseStack dfj, final float float3) {
        final int integer5 = bdm.getSize();
        final float float4 = Mth.lerp(float3, bdm.oSquish, bdm.squish) / (integer5 * 0.5f + 1.0f);
        final float float5 = 1.0f / (float4 + 1.0f);
        dfj.scale(float5 * integer5, 1.0f / float5 * integer5, float5 * integer5);
    }
    
    static {
        MAGMACUBE_LOCATION = new ResourceLocation("textures/entity/slime/magmacube.png");
    }
}
