package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.world.entity.monster.Phantom;

public class PhantomRenderer extends MobRenderer<Phantom, PhantomModel<Phantom>> {
    private static final ResourceLocation PHANTOM_LOCATION;
    
    public PhantomRenderer(final EntityRenderDispatcher eel) {
        super(eel, new PhantomModel(), 0.75f);
        this.addLayer((RenderLayer<Phantom, PhantomModel<Phantom>>)new PhantomEyesLayer((RenderLayerParent<Entity, PhantomModel<Entity>>)this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Phantom bdp) {
        return PhantomRenderer.PHANTOM_LOCATION;
    }
    
    @Override
    protected void scale(final Phantom bdp, final PoseStack dfj, final float float3) {
        final int integer5 = bdp.getPhantomSize();
        final float float4 = 1.0f + 0.15f * integer5;
        dfj.scale(float4, float4, float4);
        dfj.translate(0.0, 1.3125, 0.1875);
    }
    
    @Override
    protected void setupRotations(final Phantom bdp, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bdp, dfj, float3, float4, float5);
        dfj.mulPose(Vector3f.XP.rotationDegrees(bdp.xRot));
    }
    
    static {
        PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");
    }
}
