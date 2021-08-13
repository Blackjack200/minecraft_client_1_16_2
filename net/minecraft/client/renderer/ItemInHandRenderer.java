package net.minecraft.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import com.google.common.base.MoreObjects;
import net.minecraft.world.InteractionHand;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.client.player.AbstractClientPlayer;
import com.mojang.math.Vector3f;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.Minecraft;

public class ItemInHandRenderer {
    private static final RenderType MAP_BACKGROUND;
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD;
    private final Minecraft minecraft;
    private ItemStack mainHandItem;
    private ItemStack offHandItem;
    private float mainHandHeight;
    private float oMainHandHeight;
    private float offHandHeight;
    private float oOffHandHeight;
    private final EntityRenderDispatcher entityRenderDispatcher;
    private final ItemRenderer itemRenderer;
    
    public ItemInHandRenderer(final Minecraft djw) {
        this.mainHandItem = ItemStack.EMPTY;
        this.offHandItem = ItemStack.EMPTY;
        this.minecraft = djw;
        this.entityRenderDispatcher = djw.getEntityRenderDispatcher();
        this.itemRenderer = djw.getItemRenderer();
    }
    
    public void renderItem(final LivingEntity aqj, final ItemStack bly, final ItemTransforms.TransformType b, final boolean boolean4, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (bly.isEmpty()) {
            return;
        }
        this.itemRenderer.renderStatic(aqj, bly, b, boolean4, dfj, dzy, aqj.level, integer, OverlayTexture.NO_OVERLAY);
    }
    
    private float calculateMapTilt(final float float1) {
        float float2 = 1.0f - float1 / 45.0f + 0.1f;
        float2 = Mth.clamp(float2, 0.0f, 1.0f);
        float2 = -Mth.cos(float2 * 3.1415927f) * 0.5f + 0.5f;
        return float2;
    }
    
    private void renderMapHand(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final HumanoidArm aqf) {
        this.minecraft.getTextureManager().bind(this.minecraft.player.getSkinTextureLocation());
        final PlayerRenderer ejc6 = (PlayerRenderer)this.entityRenderDispatcher.<LocalPlayer>getRenderer(this.minecraft.player);
        dfj.pushPose();
        final float float7 = (aqf == HumanoidArm.RIGHT) ? 1.0f : -1.0f;
        dfj.mulPose(Vector3f.YP.rotationDegrees(92.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(45.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(float7 * -41.0f));
        dfj.translate(float7 * 0.3f, -1.100000023841858, 0.44999998807907104);
        if (aqf == HumanoidArm.RIGHT) {
            ejc6.renderRightHand(dfj, dzy, integer, this.minecraft.player);
        }
        else {
            ejc6.renderLeftHand(dfj, dzy, integer, this.minecraft.player);
        }
        dfj.popPose();
    }
    
    private void renderOneHandedMap(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final float float4, final HumanoidArm aqf, final float float6, final ItemStack bly) {
        final float float7 = (aqf == HumanoidArm.RIGHT) ? 1.0f : -1.0f;
        dfj.translate(float7 * 0.125f, -0.125, 0.0);
        if (!this.minecraft.player.isInvisible()) {
            dfj.pushPose();
            dfj.mulPose(Vector3f.ZP.rotationDegrees(float7 * 10.0f));
            this.renderPlayerArm(dfj, dzy, integer, float4, float6, aqf);
            dfj.popPose();
        }
        dfj.pushPose();
        dfj.translate(float7 * 0.51f, -0.08f + float4 * -1.2f, -0.75);
        final float float8 = Mth.sqrt(float6);
        final float float9 = Mth.sin(float8 * 3.1415927f);
        final float float10 = -0.5f * float9;
        final float float11 = 0.4f * Mth.sin(float8 * 6.2831855f);
        final float float12 = -0.3f * Mth.sin(float6 * 3.1415927f);
        dfj.translate(float7 * float10, float11 - 0.3f * float9, float12);
        dfj.mulPose(Vector3f.XP.rotationDegrees(float9 * -45.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float7 * float9 * -30.0f));
        this.renderMap(dfj, dzy, integer, bly);
        dfj.popPose();
    }
    
    private void renderTwoHandedMap(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final float float4, final float float5, final float float6) {
        final float float7 = Mth.sqrt(float6);
        final float float8 = -0.2f * Mth.sin(float6 * 3.1415927f);
        final float float9 = -0.4f * Mth.sin(float7 * 3.1415927f);
        dfj.translate(0.0, -float8 / 2.0f, float9);
        final float float10 = this.calculateMapTilt(float4);
        dfj.translate(0.0, 0.04f + float5 * -1.2f + float10 * -0.5f, -0.7200000286102295);
        dfj.mulPose(Vector3f.XP.rotationDegrees(float10 * -85.0f));
        if (!this.minecraft.player.isInvisible()) {
            dfj.pushPose();
            dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f));
            this.renderMapHand(dfj, dzy, integer, HumanoidArm.RIGHT);
            this.renderMapHand(dfj, dzy, integer, HumanoidArm.LEFT);
            dfj.popPose();
        }
        final float float11 = Mth.sin(float7 * 3.1415927f);
        dfj.mulPose(Vector3f.XP.rotationDegrees(float11 * 20.0f));
        dfj.scale(2.0f, 2.0f, 2.0f);
        this.renderMap(dfj, dzy, integer, this.mainHandItem);
    }
    
    private void renderMap(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final ItemStack bly) {
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        dfj.scale(0.38f, 0.38f, 0.38f);
        dfj.translate(-0.5, -0.5, 0.0);
        dfj.scale(0.0078125f, 0.0078125f, 0.0078125f);
        final MapItemSavedData cxu6 = MapItem.getOrCreateSavedData(bly, this.minecraft.level);
        final VertexConsumer dfn7 = dzy.getBuffer((cxu6 == null) ? ItemInHandRenderer.MAP_BACKGROUND : ItemInHandRenderer.MAP_BACKGROUND_CHECKERBOARD);
        final Matrix4f b8 = dfj.last().pose();
        dfn7.vertex(b8, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).uv2(integer).endVertex();
        dfn7.vertex(b8, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).uv2(integer).endVertex();
        dfn7.vertex(b8, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).uv2(integer).endVertex();
        dfn7.vertex(b8, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).uv2(integer).endVertex();
        if (cxu6 != null) {
            this.minecraft.gameRenderer.getMapRenderer().render(dfj, dzy, cxu6, false, integer);
        }
    }
    
    private void renderPlayerArm(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final float float4, final float float5, final HumanoidArm aqf) {
        final boolean boolean8 = aqf != HumanoidArm.LEFT;
        final float float6 = boolean8 ? 1.0f : -1.0f;
        final float float7 = Mth.sqrt(float5);
        final float float8 = -0.3f * Mth.sin(float7 * 3.1415927f);
        final float float9 = 0.4f * Mth.sin(float7 * 6.2831855f);
        final float float10 = -0.4f * Mth.sin(float5 * 3.1415927f);
        dfj.translate(float6 * (float8 + 0.64000005f), float9 - 0.6f + float4 * -0.6f, float10 - 0.71999997f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6 * 45.0f));
        final float float11 = Mth.sin(float5 * float5 * 3.1415927f);
        final float float12 = Mth.sin(float7 * 3.1415927f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6 * float12 * 70.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(float6 * float11 * -20.0f));
        final AbstractClientPlayer dzb16 = this.minecraft.player;
        this.minecraft.getTextureManager().bind(dzb16.getSkinTextureLocation());
        dfj.translate(float6 * -1.0f, 3.5999999046325684, 3.5);
        dfj.mulPose(Vector3f.ZP.rotationDegrees(float6 * 120.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(200.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6 * -135.0f));
        dfj.translate(float6 * 5.6f, 0.0, 0.0);
        final PlayerRenderer ejc17 = (PlayerRenderer)this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer(dzb16);
        if (boolean8) {
            ejc17.renderRightHand(dfj, dzy, integer, dzb16);
        }
        else {
            ejc17.renderLeftHand(dfj, dzy, integer, dzb16);
        }
    }
    
    private void applyEatTransform(final PoseStack dfj, final float float2, final HumanoidArm aqf, final ItemStack bly) {
        final float float3 = this.minecraft.player.getUseItemRemainingTicks() - float2 + 1.0f;
        final float float4 = float3 / bly.getUseDuration();
        if (float4 < 0.8f) {
            final float float5 = Mth.abs(Mth.cos(float3 / 4.0f * 3.1415927f) * 0.1f);
            dfj.translate(0.0, float5, 0.0);
        }
        final float float5 = 1.0f - (float)Math.pow((double)float4, 27.0);
        final int integer9 = (aqf == HumanoidArm.RIGHT) ? 1 : -1;
        dfj.translate(float5 * 0.6f * integer9, float5 * -0.5f, float5 * 0.0f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(integer9 * float5 * 90.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(float5 * 10.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(integer9 * float5 * 30.0f));
    }
    
    private void applyItemArmAttackTransform(final PoseStack dfj, final HumanoidArm aqf, final float float3) {
        final int integer5 = (aqf == HumanoidArm.RIGHT) ? 1 : -1;
        final float float4 = Mth.sin(float3 * float3 * 3.1415927f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(integer5 * (45.0f + float4 * -20.0f)));
        final float float5 = Mth.sin(Mth.sqrt(float3) * 3.1415927f);
        dfj.mulPose(Vector3f.ZP.rotationDegrees(integer5 * float5 * -20.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(float5 * -80.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(integer5 * -45.0f));
    }
    
    private void applyItemArmTransform(final PoseStack dfj, final HumanoidArm aqf, final float float3) {
        final int integer5 = (aqf == HumanoidArm.RIGHT) ? 1 : -1;
        dfj.translate(integer5 * 0.56f, -0.52f + float3 * -0.6f, -0.7200000286102295);
    }
    
    public void renderHandsWithItems(final float float1, final PoseStack dfj, final MultiBufferSource.BufferSource a, final LocalPlayer dze, final int integer) {
        final float float2 = dze.getAttackAnim(float1);
        final InteractionHand aoq8 = (InteractionHand)MoreObjects.firstNonNull(dze.swingingArm, InteractionHand.MAIN_HAND);
        final float float3 = Mth.lerp(float1, dze.xRotO, dze.xRot);
        boolean boolean10 = true;
        boolean boolean11 = true;
        if (dze.isUsingItem()) {
            final ItemStack bly12 = dze.getUseItem();
            if (bly12.getItem() == Items.BOW || bly12.getItem() == Items.CROSSBOW) {
                boolean10 = (dze.getUsedItemHand() == InteractionHand.MAIN_HAND);
                boolean11 = !boolean10;
            }
            final InteractionHand aoq9 = dze.getUsedItemHand();
            if (aoq9 == InteractionHand.MAIN_HAND) {
                final ItemStack bly13 = dze.getOffhandItem();
                if (bly13.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(bly13)) {
                    boolean11 = false;
                }
            }
        }
        else {
            final ItemStack bly12 = dze.getMainHandItem();
            final ItemStack bly14 = dze.getOffhandItem();
            if (bly12.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(bly12)) {
                boolean11 = !boolean10;
            }
            if (bly14.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(bly14)) {
                boolean10 = !bly12.isEmpty();
                boolean11 = !boolean10;
            }
        }
        final float float4 = Mth.lerp(float1, dze.xBobO, dze.xBob);
        final float float5 = Mth.lerp(float1, dze.yBobO, dze.yBob);
        dfj.mulPose(Vector3f.XP.rotationDegrees((dze.getViewXRot(float1) - float4) * 0.1f));
        dfj.mulPose(Vector3f.YP.rotationDegrees((dze.getViewYRot(float1) - float5) * 0.1f));
        if (boolean10) {
            final float float6 = (aoq8 == InteractionHand.MAIN_HAND) ? float2 : 0.0f;
            final float float7 = 1.0f - Mth.lerp(float1, this.oMainHandHeight, this.mainHandHeight);
            this.renderArmWithItem(dze, float1, float3, InteractionHand.MAIN_HAND, float6, this.mainHandItem, float7, dfj, a, integer);
        }
        if (boolean11) {
            final float float6 = (aoq8 == InteractionHand.OFF_HAND) ? float2 : 0.0f;
            final float float7 = 1.0f - Mth.lerp(float1, this.oOffHandHeight, this.offHandHeight);
            this.renderArmWithItem(dze, float1, float3, InteractionHand.OFF_HAND, float6, this.offHandItem, float7, dfj, a, integer);
        }
        a.endBatch();
    }
    
    private void renderArmWithItem(final AbstractClientPlayer dzb, final float float2, final float float3, final InteractionHand aoq, final float float5, final ItemStack bly, final float float7, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final boolean boolean12 = aoq == InteractionHand.MAIN_HAND;
        final HumanoidArm aqf13 = boolean12 ? dzb.getMainArm() : dzb.getMainArm().getOpposite();
        dfj.pushPose();
        if (bly.isEmpty()) {
            if (boolean12 && !dzb.isInvisible()) {
                this.renderPlayerArm(dfj, dzy, integer, float7, float5, aqf13);
            }
        }
        else if (bly.getItem() == Items.FILLED_MAP) {
            if (boolean12 && this.offHandItem.isEmpty()) {
                this.renderTwoHandedMap(dfj, dzy, integer, float3, float7, float5);
            }
            else {
                this.renderOneHandedMap(dfj, dzy, integer, float7, aqf13, float5, bly);
            }
        }
        else if (bly.getItem() == Items.CROSSBOW) {
            final boolean boolean13 = CrossbowItem.isCharged(bly);
            final boolean boolean14 = aqf13 == HumanoidArm.RIGHT;
            final int integer2 = boolean14 ? 1 : -1;
            if (dzb.isUsingItem() && dzb.getUseItemRemainingTicks() > 0 && dzb.getUsedItemHand() == aoq) {
                this.applyItemArmTransform(dfj, aqf13, float7);
                dfj.translate(integer2 * -0.4785682f, -0.0943870022892952, 0.05731530860066414);
                dfj.mulPose(Vector3f.XP.rotationDegrees(-11.935f));
                dfj.mulPose(Vector3f.YP.rotationDegrees(integer2 * 65.3f));
                dfj.mulPose(Vector3f.ZP.rotationDegrees(integer2 * -9.785f));
                final float float8 = bly.getUseDuration() - (this.minecraft.player.getUseItemRemainingTicks() - float2 + 1.0f);
                float float9 = float8 / CrossbowItem.getChargeDuration(bly);
                if (float9 > 1.0f) {
                    float9 = 1.0f;
                }
                if (float9 > 0.1f) {
                    final float float10 = Mth.sin((float8 - 0.1f) * 1.3f);
                    final float float11 = float9 - 0.1f;
                    final float float12 = float10 * float11;
                    dfj.translate(float12 * 0.0f, float12 * 0.004f, float12 * 0.0f);
                }
                dfj.translate(float9 * 0.0f, float9 * 0.0f, float9 * 0.04f);
                dfj.scale(1.0f, 1.0f, 1.0f + float9 * 0.2f);
                dfj.mulPose(Vector3f.YN.rotationDegrees(integer2 * 45.0f));
            }
            else {
                final float float8 = -0.4f * Mth.sin(Mth.sqrt(float5) * 3.1415927f);
                final float float9 = 0.2f * Mth.sin(Mth.sqrt(float5) * 6.2831855f);
                final float float10 = -0.2f * Mth.sin(float5 * 3.1415927f);
                dfj.translate(integer2 * float8, float9, float10);
                this.applyItemArmTransform(dfj, aqf13, float7);
                this.applyItemArmAttackTransform(dfj, aqf13, float5);
                if (boolean13 && float5 < 0.001f) {
                    dfj.translate(integer2 * -0.641864f, 0.0, 0.0);
                    dfj.mulPose(Vector3f.YP.rotationDegrees(integer2 * 10.0f));
                }
            }
            this.renderItem(dzb, bly, boolean14 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !boolean14, dfj, dzy, integer);
        }
        else {
            final boolean boolean13 = aqf13 == HumanoidArm.RIGHT;
            if (dzb.isUsingItem() && dzb.getUseItemRemainingTicks() > 0 && dzb.getUsedItemHand() == aoq) {
                final int integer3 = boolean13 ? 1 : -1;
                switch (bly.getUseAnimation()) {
                    case NONE: {
                        this.applyItemArmTransform(dfj, aqf13, float7);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.applyEatTransform(dfj, float2, aqf13, bly);
                        this.applyItemArmTransform(dfj, aqf13, float7);
                        break;
                    }
                    case BLOCK: {
                        this.applyItemArmTransform(dfj, aqf13, float7);
                        break;
                    }
                    case BOW: {
                        this.applyItemArmTransform(dfj, aqf13, float7);
                        dfj.translate(integer3 * -0.2785682f, 0.18344387412071228, 0.15731531381607056);
                        dfj.mulPose(Vector3f.XP.rotationDegrees(-13.935f));
                        dfj.mulPose(Vector3f.YP.rotationDegrees(integer3 * 35.3f));
                        dfj.mulPose(Vector3f.ZP.rotationDegrees(integer3 * -9.785f));
                        final float float13 = bly.getUseDuration() - (this.minecraft.player.getUseItemRemainingTicks() - float2 + 1.0f);
                        float float8 = float13 / 20.0f;
                        float8 = (float8 * float8 + float8 * 2.0f) / 3.0f;
                        if (float8 > 1.0f) {
                            float8 = 1.0f;
                        }
                        if (float8 > 0.1f) {
                            final float float9 = Mth.sin((float13 - 0.1f) * 1.3f);
                            final float float10 = float8 - 0.1f;
                            final float float11 = float9 * float10;
                            dfj.translate(float11 * 0.0f, float11 * 0.004f, float11 * 0.0f);
                        }
                        dfj.translate(float8 * 0.0f, float8 * 0.0f, float8 * 0.04f);
                        dfj.scale(1.0f, 1.0f, 1.0f + float8 * 0.2f);
                        dfj.mulPose(Vector3f.YN.rotationDegrees(integer3 * 45.0f));
                        break;
                    }
                    case SPEAR: {
                        this.applyItemArmTransform(dfj, aqf13, float7);
                        dfj.translate(integer3 * -0.5f, 0.699999988079071, 0.10000000149011612);
                        dfj.mulPose(Vector3f.XP.rotationDegrees(-55.0f));
                        dfj.mulPose(Vector3f.YP.rotationDegrees(integer3 * 35.3f));
                        dfj.mulPose(Vector3f.ZP.rotationDegrees(integer3 * -9.785f));
                        final float float13 = bly.getUseDuration() - (this.minecraft.player.getUseItemRemainingTicks() - float2 + 1.0f);
                        float float8 = float13 / 10.0f;
                        if (float8 > 1.0f) {
                            float8 = 1.0f;
                        }
                        if (float8 > 0.1f) {
                            final float float9 = Mth.sin((float13 - 0.1f) * 1.3f);
                            final float float10 = float8 - 0.1f;
                            final float float11 = float9 * float10;
                            dfj.translate(float11 * 0.0f, float11 * 0.004f, float11 * 0.0f);
                        }
                        dfj.translate(0.0, 0.0, float8 * 0.2f);
                        dfj.scale(1.0f, 1.0f, 1.0f + float8 * 0.2f);
                        dfj.mulPose(Vector3f.YN.rotationDegrees(integer3 * 45.0f));
                        break;
                    }
                }
            }
            else if (dzb.isAutoSpinAttack()) {
                this.applyItemArmTransform(dfj, aqf13, float7);
                final int integer3 = boolean13 ? 1 : -1;
                dfj.translate(integer3 * -0.4f, 0.800000011920929, 0.30000001192092896);
                dfj.mulPose(Vector3f.YP.rotationDegrees(integer3 * 65.0f));
                dfj.mulPose(Vector3f.ZP.rotationDegrees(integer3 * -85.0f));
            }
            else {
                final float float14 = -0.4f * Mth.sin(Mth.sqrt(float5) * 3.1415927f);
                final float float13 = 0.2f * Mth.sin(Mth.sqrt(float5) * 6.2831855f);
                final float float8 = -0.2f * Mth.sin(float5 * 3.1415927f);
                final int integer4 = boolean13 ? 1 : -1;
                dfj.translate(integer4 * float14, float13, float8);
                this.applyItemArmTransform(dfj, aqf13, float7);
                this.applyItemArmAttackTransform(dfj, aqf13, float5);
            }
            this.renderItem(dzb, bly, boolean13 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !boolean13, dfj, dzy, integer);
        }
        dfj.popPose();
    }
    
    public void tick() {
        this.oMainHandHeight = this.mainHandHeight;
        this.oOffHandHeight = this.offHandHeight;
        final LocalPlayer dze2 = this.minecraft.player;
        final ItemStack bly3 = dze2.getMainHandItem();
        final ItemStack bly4 = dze2.getOffhandItem();
        if (ItemStack.matches(this.mainHandItem, bly3)) {
            this.mainHandItem = bly3;
        }
        if (ItemStack.matches(this.offHandItem, bly4)) {
            this.offHandItem = bly4;
        }
        if (dze2.isHandsBusy()) {
            this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4f, 0.0f, 1.0f);
            this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4f, 0.0f, 1.0f);
        }
        else {
            final float float5 = dze2.getAttackStrengthScale(1.0f);
            this.mainHandHeight += Mth.clamp(((this.mainHandItem == bly3) ? (float5 * float5 * float5) : 0.0f) - this.mainHandHeight, -0.4f, 0.4f);
            this.offHandHeight += Mth.clamp((float)((this.offHandItem == bly4) ? 1 : 0) - this.offHandHeight, -0.4f, 0.4f);
        }
        if (this.mainHandHeight < 0.1f) {
            this.mainHandItem = bly3;
        }
        if (this.offHandHeight < 0.1f) {
            this.offHandItem = bly4;
        }
    }
    
    public void itemUsed(final InteractionHand aoq) {
        if (aoq == InteractionHand.MAIN_HAND) {
            this.mainHandHeight = 0.0f;
        }
        else {
            this.offHandHeight = 0.0f;
        }
    }
    
    static {
        MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
        MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));
    }
}
