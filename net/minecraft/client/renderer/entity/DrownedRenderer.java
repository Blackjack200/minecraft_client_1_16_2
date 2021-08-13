package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.world.entity.monster.Drowned;

public class DrownedRenderer extends AbstractZombieRenderer<Drowned, DrownedModel<Drowned>> {
    private static final ResourceLocation DROWNED_LOCATION;
    
    public DrownedRenderer(final EntityRenderDispatcher eel) {
        super(eel, new DrownedModel(0.0f, 0.0f, 64, 64), new DrownedModel(0.5f, true), new DrownedModel(1.0f, true));
        this.addLayer((RenderLayer<Drowned, DrownedModel<Drowned>>)new DrownedOuterLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Zombie beg) {
        return DrownedRenderer.DROWNED_LOCATION;
    }
    
    @Override
    protected void setupRotations(final Drowned bdb, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bdb, dfj, float3, float4, float5);
        final float float6 = bdb.getSwimAmount(float5);
        if (float6 > 0.0f) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(float6, bdb.xRot, -10.0f - bdb.xRot)));
        }
    }
    
    static {
        DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");
    }
}
