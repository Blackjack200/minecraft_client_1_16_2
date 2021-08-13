package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.resources.ResourceLocation;

public class HuskRenderer extends ZombieRenderer {
    private static final ResourceLocation HUSK_LOCATION;
    
    public HuskRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    protected void scale(final Zombie beg, final PoseStack dfj, final float float3) {
        final float float4 = 1.0625f;
        dfj.scale(1.0625f, 1.0625f, 1.0625f);
        super.scale(beg, dfj, float3);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Zombie beg) {
        return HuskRenderer.HUSK_LOCATION;
    }
    
    static {
        HUSK_LOCATION = new ResourceLocation("textures/entity/zombie/husk.png");
    }
}
