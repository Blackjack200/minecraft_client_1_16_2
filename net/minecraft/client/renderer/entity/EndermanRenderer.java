package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.world.entity.monster.EnderMan;

public class EndermanRenderer extends MobRenderer<EnderMan, EndermanModel<EnderMan>> {
    private static final ResourceLocation ENDERMAN_LOCATION;
    private final Random random;
    
    public EndermanRenderer(final EntityRenderDispatcher eel) {
        super(eel, new EndermanModel(0.0f), 0.5f);
        this.random = new Random();
        this.addLayer((RenderLayer<EnderMan, EndermanModel<EnderMan>>)new EnderEyesLayer((RenderLayerParent<LivingEntity, EndermanModel<LivingEntity>>)this));
        this.addLayer(new CarriedBlockLayer(this));
    }
    
    @Override
    public void render(final EnderMan bdd, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final BlockState cee8 = bdd.getCarriedBlock();
        final EndermanModel<EnderMan> dts9 = ((LivingEntityRenderer<T, EndermanModel<EnderMan>>)this).getModel();
        dts9.carrying = (cee8 != null);
        dts9.creepy = bdd.isCreepy();
        super.render(bdd, float2, float3, dfj, dzy, integer);
    }
    
    public Vec3 getRenderOffset(final EnderMan bdd, final float float2) {
        if (bdd.isCreepy()) {
            final double double4 = 0.02;
            return new Vec3(this.random.nextGaussian() * 0.02, 0.0, this.random.nextGaussian() * 0.02);
        }
        return super.getRenderOffset((T)bdd, float2);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final EnderMan bdd) {
        return EndermanRenderer.ENDERMAN_LOCATION;
    }
    
    static {
        ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
    }
}
