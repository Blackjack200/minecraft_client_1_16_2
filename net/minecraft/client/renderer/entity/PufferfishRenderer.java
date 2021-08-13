package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PufferfishBigModel;
import net.minecraft.client.model.PufferfishMidModel;
import net.minecraft.client.model.PufferfishSmallModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.animal.Pufferfish;

public class PufferfishRenderer extends MobRenderer<Pufferfish, EntityModel<Pufferfish>> {
    private static final ResourceLocation PUFFER_LOCATION;
    private int puffStateO;
    private final PufferfishSmallModel<Pufferfish> small;
    private final PufferfishMidModel<Pufferfish> mid;
    private final PufferfishBigModel<Pufferfish> big;
    
    public PufferfishRenderer(final EntityRenderDispatcher eel) {
        super(eel, new PufferfishBigModel(), 0.2f);
        this.small = new PufferfishSmallModel<Pufferfish>();
        this.mid = new PufferfishMidModel<Pufferfish>();
        this.big = new PufferfishBigModel<Pufferfish>();
        this.puffStateO = 3;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Pufferfish bam) {
        return PufferfishRenderer.PUFFER_LOCATION;
    }
    
    @Override
    public void render(final Pufferfish bam, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final int integer2 = bam.getPuffState();
        if (integer2 != this.puffStateO) {
            if (integer2 == 0) {
                this.model = (M)this.small;
            }
            else if (integer2 == 1) {
                this.model = (M)this.mid;
            }
            else {
                this.model = (M)this.big;
            }
        }
        this.puffStateO = integer2;
        this.shadowRadius = 0.1f + 0.1f * integer2;
        super.render(bam, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    protected void setupRotations(final Pufferfish bam, final PoseStack dfj, final float float3, final float float4, final float float5) {
        dfj.translate(0.0, Mth.cos(float3 * 0.05f) * 0.08f, 0.0);
        super.setupRotations(bam, dfj, float3, float4, float5);
    }
    
    static {
        PUFFER_LOCATION = new ResourceLocation("textures/entity/fish/pufferfish.png");
    }
}
