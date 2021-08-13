package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.resources.ResourceLocation;

public class ElderGuardianRenderer extends GuardianRenderer {
    public static final ResourceLocation GUARDIAN_ELDER_LOCATION;
    
    public ElderGuardianRenderer(final EntityRenderDispatcher eel) {
        super(eel, 1.2f);
    }
    
    @Override
    protected void scale(final Guardian bdj, final PoseStack dfj, final float float3) {
        dfj.scale(ElderGuardian.ELDER_SIZE_SCALE, ElderGuardian.ELDER_SIZE_SCALE, ElderGuardian.ELDER_SIZE_SCALE);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Guardian bdj) {
        return ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION;
    }
    
    static {
        GUARDIAN_ELDER_LOCATION = new ResourceLocation("textures/entity/guardian_elder.png");
    }
}
