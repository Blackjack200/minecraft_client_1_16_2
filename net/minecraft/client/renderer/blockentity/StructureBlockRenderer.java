package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.StructureBlockEntity;

public class StructureBlockRenderer extends BlockEntityRenderer<StructureBlockEntity> {
    public StructureBlockRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final StructureBlockEntity cdg, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        if (!Minecraft.getInstance().player.canUseGameMasterBlocks() && !Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        final BlockPos fx8 = cdg.getStructurePos();
        final BlockPos fx9 = cdg.getStructureSize();
        if (fx9.getX() < 1 || fx9.getY() < 1 || fx9.getZ() < 1) {
            return;
        }
        if (cdg.getMode() != StructureMode.SAVE && cdg.getMode() != StructureMode.LOAD) {
            return;
        }
        final double double10 = fx8.getX();
        final double double11 = fx8.getZ();
        final double double12 = fx8.getY();
        final double double13 = double12 + fx9.getY();
        double double14 = 0.0;
        double double15 = 0.0;
        switch (cdg.getMirror()) {
            case LEFT_RIGHT: {
                double14 = fx9.getX();
                double15 = -fx9.getZ();
                break;
            }
            case FRONT_BACK: {
                double14 = -fx9.getX();
                double15 = fx9.getZ();
                break;
            }
            default: {
                double14 = fx9.getX();
                double15 = fx9.getZ();
                break;
            }
        }
        double double16 = 0.0;
        double double17 = 0.0;
        double double18 = 0.0;
        double double19 = 0.0;
        switch (cdg.getRotation()) {
            case CLOCKWISE_90: {
                double16 = ((double15 < 0.0) ? double10 : (double10 + 1.0));
                double17 = ((double14 < 0.0) ? (double11 + 1.0) : double11);
                double18 = double16 - double15;
                double19 = double17 + double14;
                break;
            }
            case CLOCKWISE_180: {
                double16 = ((double14 < 0.0) ? double10 : (double10 + 1.0));
                double17 = ((double15 < 0.0) ? double11 : (double11 + 1.0));
                double18 = double16 - double14;
                double19 = double17 - double15;
                break;
            }
            case COUNTERCLOCKWISE_90: {
                double16 = ((double15 < 0.0) ? (double10 + 1.0) : double10);
                double17 = ((double14 < 0.0) ? double11 : (double11 + 1.0));
                double18 = double16 + double15;
                double19 = double17 - double14;
                break;
            }
            default: {
                double16 = ((double14 < 0.0) ? (double10 + 1.0) : double10);
                double17 = ((double15 < 0.0) ? (double11 + 1.0) : double11);
                double18 = double16 + double14;
                double19 = double17 + double15;
                break;
            }
        }
        final float float3 = 1.0f;
        final float float4 = 0.9f;
        final float float5 = 0.5f;
        final VertexConsumer dfn33 = dzy.getBuffer(RenderType.lines());
        if (cdg.getMode() == StructureMode.SAVE || cdg.getShowBoundingBox()) {
            LevelRenderer.renderLineBox(dfj, dfn33, double16, double12, double17, double18, double13, double19, 0.9f, 0.9f, 0.9f, 1.0f, 0.5f, 0.5f, 0.5f);
        }
        if (cdg.getMode() == StructureMode.SAVE && cdg.getShowAir()) {
            this.renderInvisibleBlocks(cdg, dfn33, fx8, true, dfj);
            this.renderInvisibleBlocks(cdg, dfn33, fx8, false, dfj);
        }
    }
    
    private void renderInvisibleBlocks(final StructureBlockEntity cdg, final VertexConsumer dfn, final BlockPos fx, final boolean boolean4, final PoseStack dfj) {
        final BlockGetter bqz7 = cdg.getLevel();
        final BlockPos fx2 = cdg.getBlockPos();
        final BlockPos fx3 = fx2.offset(fx);
        for (final BlockPos fx4 : BlockPos.betweenClosed(fx3, fx3.offset(cdg.getStructureSize()).offset(-1, -1, -1))) {
            final BlockState cee12 = bqz7.getBlockState(fx4);
            final boolean boolean5 = cee12.isAir();
            final boolean boolean6 = cee12.is(Blocks.STRUCTURE_VOID);
            if (boolean5 || boolean6) {
                final float float15 = boolean5 ? 0.05f : 0.0f;
                final double double16 = fx4.getX() - fx2.getX() + 0.45f - float15;
                final double double17 = fx4.getY() - fx2.getY() + 0.45f - float15;
                final double double18 = fx4.getZ() - fx2.getZ() + 0.45f - float15;
                final double double19 = fx4.getX() - fx2.getX() + 0.55f + float15;
                final double double20 = fx4.getY() - fx2.getY() + 0.55f + float15;
                final double double21 = fx4.getZ() - fx2.getZ() + 0.55f + float15;
                if (boolean4) {
                    LevelRenderer.renderLineBox(dfj, dfn, double16, double17, double18, double19, double20, double21, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f);
                }
                else if (boolean5) {
                    LevelRenderer.renderLineBox(dfj, dfn, double16, double17, double18, double19, double20, double21, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, 1.0f);
                }
                else {
                    LevelRenderer.renderLineBox(dfj, dfn, double16, double17, double18, double19, double20, double21, 1.0f, 0.25f, 0.25f, 1.0f, 1.0f, 0.25f, 0.25f);
                }
            }
        }
    }
    
    @Override
    public boolean shouldRenderOffScreen(final StructureBlockEntity cdg) {
        return true;
    }
}
