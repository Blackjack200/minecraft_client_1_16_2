package net.minecraft.client.renderer.entity.player;

import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

public class PlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerRenderer(final EntityRenderDispatcher eel) {
        this(eel, false);
    }
    
    public PlayerRenderer(final EntityRenderDispatcher eel, final boolean boolean2) {
        super(eel, new PlayerModel(0.0f, boolean2), 0.5f);
        this.addLayer((RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new HumanoidModel(0.5f), new HumanoidModel(1.0f)));
        this.addLayer(new ItemInHandLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(this));
        this.addLayer(new ArrowLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(this));
        this.addLayer(new Deadmau5EarsLayer(this));
        this.addLayer(new CapeLayer(this));
        this.addLayer(new CustomHeadLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(this));
        this.addLayer(new ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(this));
        this.addLayer((RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)new ParrotOnShoulderLayer((RenderLayerParent<Player, PlayerModel<Player>>)this));
        this.addLayer((RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)new SpinAttackEffectLayer((RenderLayerParent<LivingEntity, PlayerModel<LivingEntity>>)this));
        this.addLayer(new BeeStingerLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(this));
    }
    
    @Override
    public void render(final AbstractClientPlayer dzb, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        this.setModelProperties(dzb);
        super.render(dzb, float2, float3, dfj, dzy, integer);
    }
    
    public Vec3 getRenderOffset(final AbstractClientPlayer dzb, final float float2) {
        if (dzb.isCrouching()) {
            return new Vec3(0.0, -0.125, 0.0);
        }
        return super.getRenderOffset((T)dzb, float2);
    }
    
    private void setModelProperties(final AbstractClientPlayer dzb) {
        final PlayerModel<AbstractClientPlayer> duv3 = ((LivingEntityRenderer<T, PlayerModel<AbstractClientPlayer>>)this).getModel();
        if (dzb.isSpectator()) {
            duv3.setAllVisible(false);
            duv3.head.visible = true;
            duv3.hat.visible = true;
        }
        else {
            duv3.setAllVisible(true);
            duv3.hat.visible = dzb.isModelPartShown(PlayerModelPart.HAT);
            duv3.jacket.visible = dzb.isModelPartShown(PlayerModelPart.JACKET);
            duv3.leftPants.visible = dzb.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            duv3.rightPants.visible = dzb.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            duv3.leftSleeve.visible = dzb.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            duv3.rightSleeve.visible = dzb.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            duv3.crouching = dzb.isCrouching();
            final HumanoidModel.ArmPose a4 = getArmPose(dzb, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose a5 = getArmPose(dzb, InteractionHand.OFF_HAND);
            if (a4.isTwoHanded()) {
                a5 = (dzb.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM);
            }
            if (dzb.getMainArm() == HumanoidArm.RIGHT) {
                duv3.rightArmPose = a4;
                duv3.leftArmPose = a5;
            }
            else {
                duv3.rightArmPose = a5;
                duv3.leftArmPose = a4;
            }
        }
    }
    
    private static HumanoidModel.ArmPose getArmPose(final AbstractClientPlayer dzb, final InteractionHand aoq) {
        final ItemStack bly3 = dzb.getItemInHand(aoq);
        if (bly3.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        }
        if (dzb.getUsedItemHand() == aoq && dzb.getUseItemRemainingTicks() > 0) {
            final UseAnim bnk4 = bly3.getUseAnimation();
            if (bnk4 == UseAnim.BLOCK) {
                return HumanoidModel.ArmPose.BLOCK;
            }
            if (bnk4 == UseAnim.BOW) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
            if (bnk4 == UseAnim.SPEAR) {
                return HumanoidModel.ArmPose.THROW_SPEAR;
            }
            if (bnk4 == UseAnim.CROSSBOW && aoq == dzb.getUsedItemHand()) {
                return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            }
        }
        else if (!dzb.swinging && bly3.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(bly3)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return HumanoidModel.ArmPose.ITEM;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AbstractClientPlayer dzb) {
        return dzb.getSkinTextureLocation();
    }
    
    @Override
    protected void scale(final AbstractClientPlayer dzb, final PoseStack dfj, final float float3) {
        final float float4 = 0.9375f;
        dfj.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    protected void renderNameTag(final AbstractClientPlayer dzb, final Component nr, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final double double7 = this.entityRenderDispatcher.distanceToSqr(dzb);
        dfj.pushPose();
        if (double7 < 100.0) {
            final Scoreboard ddk9 = dzb.getScoreboard();
            final Objective ddh10 = ddk9.getDisplayObjective(2);
            if (ddh10 != null) {
                final Score ddj11 = ddk9.getOrCreatePlayerScore(dzb.getScoreboardName(), ddh10);
                super.renderNameTag((T)dzb, new TextComponent(Integer.toString(ddj11.getScore())).append(" ").append(ddh10.getDisplayName()), dfj, dzy, integer);
                final double double8 = 0.0;
                this.getFont().getClass();
                dfj.translate(double8, 9.0f * 1.15f * 0.025f, 0.0);
            }
        }
        super.renderNameTag((T)dzb, nr, dfj, dzy, integer);
        dfj.popPose();
    }
    
    public void renderRightHand(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final AbstractClientPlayer dzb) {
        this.renderHand(dfj, dzy, integer, dzb, ((PlayerModel)this.model).rightArm, ((PlayerModel)this.model).rightSleeve);
    }
    
    public void renderLeftHand(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final AbstractClientPlayer dzb) {
        this.renderHand(dfj, dzy, integer, dzb, ((PlayerModel)this.model).leftArm, ((PlayerModel)this.model).leftSleeve);
    }
    
    private void renderHand(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final AbstractClientPlayer dzb, final ModelPart dwf5, final ModelPart dwf6) {
        final PlayerModel<AbstractClientPlayer> duv8 = ((LivingEntityRenderer<T, PlayerModel<AbstractClientPlayer>>)this).getModel();
        this.setModelProperties(dzb);
        duv8.attackTime = 0.0f;
        duv8.crouching = false;
        duv8.setupAnim(dzb, duv8.swimAmount = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        dwf5.xRot = 0.0f;
        dwf5.render(dfj, dzy.getBuffer(RenderType.entitySolid(dzb.getSkinTextureLocation())), integer, OverlayTexture.NO_OVERLAY);
        dwf6.xRot = 0.0f;
        dwf6.render(dfj, dzy.getBuffer(RenderType.entityTranslucent(dzb.getSkinTextureLocation())), integer, OverlayTexture.NO_OVERLAY);
    }
    
    @Override
    protected void setupRotations(final AbstractClientPlayer dzb, final PoseStack dfj, final float float3, final float float4, final float float5) {
        final float float6 = dzb.getSwimAmount(float5);
        if (dzb.isFallFlying()) {
            super.setupRotations(dzb, dfj, float3, float4, float5);
            final float float7 = dzb.getFallFlyingTicks() + float5;
            final float float8 = Mth.clamp(float7 * float7 / 100.0f, 0.0f, 1.0f);
            if (!dzb.isAutoSpinAttack()) {
                dfj.mulPose(Vector3f.XP.rotationDegrees(float8 * (-90.0f - dzb.xRot)));
            }
            final Vec3 dck10 = dzb.getViewVector(float5);
            final Vec3 dck11 = dzb.getDeltaMovement();
            final double double12 = Entity.getHorizontalDistanceSqr(dck11);
            final double double13 = Entity.getHorizontalDistanceSqr(dck10);
            if (double12 > 0.0 && double13 > 0.0) {
                final double double14 = (dck11.x * dck10.x + dck11.z * dck10.z) / Math.sqrt(double12 * double13);
                final double double15 = dck11.x * dck10.z - dck11.z * dck10.x;
                dfj.mulPose(Vector3f.YP.rotation((float)(Math.signum(double15) * Math.acos(double14))));
            }
        }
        else if (float6 > 0.0f) {
            super.setupRotations(dzb, dfj, float3, float4, float5);
            final float float7 = dzb.isInWater() ? (-90.0f - dzb.xRot) : -90.0f;
            final float float8 = Mth.lerp(float6, 0.0f, float7);
            dfj.mulPose(Vector3f.XP.rotationDegrees(float8));
            if (dzb.isVisuallySwimming()) {
                dfj.translate(0.0, -1.0, 0.30000001192092896);
            }
        }
        else {
            super.setupRotations(dzb, dfj, float3, float4, float5);
        }
    }
}
