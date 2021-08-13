package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.model.CatModel;
import net.minecraft.world.entity.animal.Cat;

public class CatRenderer extends MobRenderer<Cat, CatModel<Cat>> {
    public CatRenderer(final EntityRenderDispatcher eel) {
        super(eel, new CatModel(0.0f), 0.4f);
        this.addLayer(new CatCollarLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Cat azy) {
        return azy.getResourceLocation();
    }
    
    @Override
    protected void scale(final Cat azy, final PoseStack dfj, final float float3) {
        super.scale(azy, dfj, float3);
        dfj.scale(0.8f, 0.8f, 0.8f);
    }
    
    @Override
    protected void setupRotations(final Cat azy, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(azy, dfj, float3, float4, float5);
        final float float6 = azy.getLieDownAmount(float5);
        if (float6 > 0.0f) {
            dfj.translate(0.4f * float6, 0.15f * float6, 0.1f * float6);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.rotLerp(float6, 0.0f, 90.0f)));
            final BlockPos fx8 = azy.blockPosition();
            final List<Player> list9 = azy.level.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, new AABB(fx8).inflate(2.0, 2.0, 2.0));
            for (final Player bft11 : list9) {
                if (bft11.isSleeping()) {
                    dfj.translate(0.15f * float6, 0.0, 0.0);
                    break;
                }
            }
        }
    }
}
