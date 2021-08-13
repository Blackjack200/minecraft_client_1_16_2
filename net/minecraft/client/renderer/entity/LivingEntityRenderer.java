package net.minecraft.client.renderer.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.scores.Team;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.ChatFormatting;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public abstract class LivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    private static final Logger LOGGER;
    protected M model;
    protected final List<RenderLayer<T, M>> layers;
    
    public LivingEntityRenderer(final EntityRenderDispatcher eel, final M dtu, final float float3) {
        super(eel);
        this.layers = (List<RenderLayer<T, M>>)Lists.newArrayList();
        this.model = dtu;
        this.shadowRadius = float3;
    }
    
    protected final boolean addLayer(final RenderLayer<T, M> eil) {
        return this.layers.add(eil);
    }
    
    @Override
    public M getModel() {
        return this.model;
    }
    
    @Override
    public void render(final T aqj, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        this.model.attackTime = this.getAttackAnim(aqj, float3);
        this.model.riding = aqj.isPassenger();
        this.model.young = aqj.isBaby();
        float float4 = Mth.rotLerp(float3, aqj.yBodyRotO, aqj.yBodyRot);
        final float float5 = Mth.rotLerp(float3, aqj.yHeadRotO, aqj.yHeadRot);
        float float6 = float5 - float4;
        if (aqj.isPassenger() && aqj.getVehicle() instanceof LivingEntity) {
            final LivingEntity aqj2 = (LivingEntity)aqj.getVehicle();
            float4 = Mth.rotLerp(float3, aqj2.yBodyRotO, aqj2.yBodyRot);
            float6 = float5 - float4;
            float float7 = Mth.wrapDegrees(float6);
            if (float7 < -85.0f) {
                float7 = -85.0f;
            }
            if (float7 >= 85.0f) {
                float7 = 85.0f;
            }
            float4 = float5 - float7;
            if (float7 * float7 > 2500.0f) {
                float4 += float7 * 0.2f;
            }
            float6 = float5 - float4;
        }
        final float float8 = Mth.lerp(float3, aqj.xRotO, aqj.xRot);
        if (aqj.getPose() == Pose.SLEEPING) {
            final Direction gc12 = aqj.getBedOrientation();
            if (gc12 != null) {
                final float float9 = aqj.getEyeHeight(Pose.STANDING) - 0.1f;
                dfj.translate(-gc12.getStepX() * float9, 0.0, -gc12.getStepZ() * float9);
            }
        }
        float float7 = this.getBob(aqj, float3);
        this.setupRotations(aqj, dfj, float7, float4, float3);
        dfj.scale(-1.0f, -1.0f, 1.0f);
        this.scale(aqj, dfj, float3);
        dfj.translate(0.0, -1.5010000467300415, 0.0);
        float float9 = 0.0f;
        float float10 = 0.0f;
        if (!aqj.isPassenger() && aqj.isAlive()) {
            float9 = Mth.lerp(float3, aqj.animationSpeedOld, aqj.animationSpeed);
            float10 = aqj.animationPosition - aqj.animationSpeed * (1.0f - float3);
            if (aqj.isBaby()) {
                float10 *= 3.0f;
            }
            if (float9 > 1.0f) {
                float9 = 1.0f;
            }
        }
        this.model.prepareMobModel(aqj, float10, float9, float3);
        this.model.setupAnim(aqj, float10, float9, float7, float6, float8);
        final Minecraft djw15 = Minecraft.getInstance();
        final boolean boolean16 = this.isBodyVisible(aqj);
        final boolean boolean17 = !boolean16 && !aqj.isInvisibleTo(djw15.player);
        final boolean boolean18 = djw15.shouldEntityAppearGlowing(aqj);
        final RenderType eag19 = this.getRenderType(aqj, boolean16, boolean17, boolean18);
        if (eag19 != null) {
            final VertexConsumer dfn20 = dzy.getBuffer(eag19);
            final int integer2 = getOverlayCoords(aqj, this.getWhiteOverlayProgress(aqj, float3));
            this.model.renderToBuffer(dfj, dfn20, integer, integer2, 1.0f, 1.0f, 1.0f, boolean17 ? 0.15f : 1.0f);
        }
        if (!aqj.isSpectator()) {
            for (final RenderLayer<T, M> eil21 : this.layers) {
                eil21.render(dfj, dzy, integer, aqj, float10, float9, float3, float7, float6, float8);
            }
        }
        dfj.popPose();
        super.render(aqj, float2, float3, dfj, dzy, integer);
    }
    
    @Nullable
    protected RenderType getRenderType(final T aqj, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        final ResourceLocation vk6 = this.getTextureLocation(aqj);
        if (boolean3) {
            return RenderType.itemEntityTranslucentCull(vk6);
        }
        if (boolean2) {
            return this.model.renderType(vk6);
        }
        if (boolean4) {
            return RenderType.outline(vk6);
        }
        return null;
    }
    
    public static int getOverlayCoords(final LivingEntity aqj, final float float2) {
        return OverlayTexture.pack(OverlayTexture.u(float2), OverlayTexture.v(aqj.hurtTime > 0 || aqj.deathTime > 0));
    }
    
    protected boolean isBodyVisible(final T aqj) {
        return !aqj.isInvisible();
    }
    
    private static float sleepDirectionToRotation(final Direction gc) {
        switch (gc) {
            case SOUTH: {
                return 90.0f;
            }
            case WEST: {
                return 0.0f;
            }
            case NORTH: {
                return 270.0f;
            }
            case EAST: {
                return 180.0f;
            }
            default: {
                return 0.0f;
            }
        }
    }
    
    protected boolean isShaking(final T aqj) {
        return false;
    }
    
    protected void setupRotations(final T aqj, final PoseStack dfj, final float float3, float float4, final float float5) {
        if (this.isShaking(aqj)) {
            float4 += (float)(Math.cos(aqj.tickCount * 3.25) * 3.141592653589793 * 0.4000000059604645);
        }
        final Pose aqu7 = aqj.getPose();
        if (aqu7 != Pose.SLEEPING) {
            dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float4));
        }
        if (aqj.deathTime > 0) {
            float float6 = (aqj.deathTime + float5 - 1.0f) / 20.0f * 1.6f;
            float6 = Mth.sqrt(float6);
            if (float6 > 1.0f) {
                float6 = 1.0f;
            }
            dfj.mulPose(Vector3f.ZP.rotationDegrees(float6 * this.getFlipDegrees(aqj)));
        }
        else if (aqj.isAutoSpinAttack()) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(-90.0f - aqj.xRot));
            dfj.mulPose(Vector3f.YP.rotationDegrees((aqj.tickCount + float5) * -75.0f));
        }
        else if (aqu7 == Pose.SLEEPING) {
            final Direction gc8 = aqj.getBedOrientation();
            final float float7 = (gc8 != null) ? sleepDirectionToRotation(gc8) : float4;
            dfj.mulPose(Vector3f.YP.rotationDegrees(float7));
            dfj.mulPose(Vector3f.ZP.rotationDegrees(this.getFlipDegrees(aqj)));
            dfj.mulPose(Vector3f.YP.rotationDegrees(270.0f));
        }
        else if (aqj.hasCustomName() || aqj instanceof Player) {
            final String string8 = ChatFormatting.stripFormatting(aqj.getName().getString());
            if (("Dinnerbone".equals(string8) || "Grumm".equals(string8)) && (!(aqj instanceof Player) || ((Player)aqj).isModelPartShown(PlayerModelPart.CAPE))) {
                dfj.translate(0.0, aqj.getBbHeight() + 0.1f, 0.0);
                dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            }
        }
    }
    
    protected float getAttackAnim(final T aqj, final float float2) {
        return aqj.getAttackAnim(float2);
    }
    
    protected float getBob(final T aqj, final float float2) {
        return aqj.tickCount + float2;
    }
    
    protected float getFlipDegrees(final T aqj) {
        return 90.0f;
    }
    
    protected float getWhiteOverlayProgress(final T aqj, final float float2) {
        return 0.0f;
    }
    
    protected void scale(final T aqj, final PoseStack dfj, final float float3) {
    }
    
    @Override
    protected boolean shouldShowName(final T aqj) {
        final double double3 = this.entityRenderDispatcher.distanceToSqr(aqj);
        final float float5 = aqj.isDiscrete() ? 32.0f : 64.0f;
        if (double3 >= float5 * float5) {
            return false;
        }
        final Minecraft djw6 = Minecraft.getInstance();
        final LocalPlayer dze7 = djw6.player;
        final boolean boolean8 = !aqj.isInvisibleTo(dze7);
        if (aqj != dze7) {
            final Team ddm9 = aqj.getTeam();
            final Team ddm10 = dze7.getTeam();
            if (ddm9 != null) {
                final Team.Visibility b11 = ddm9.getNameTagVisibility();
                switch (b11) {
                    case ALWAYS: {
                        return boolean8;
                    }
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return (ddm10 == null) ? boolean8 : (ddm9.isAlliedTo(ddm10) && (ddm9.canSeeFriendlyInvisibles() || boolean8));
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return (ddm10 == null) ? boolean8 : (!ddm9.isAlliedTo(ddm10) && boolean8);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.renderNames() && aqj != djw6.getCameraEntity() && boolean8 && !aqj.isVehicle();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
