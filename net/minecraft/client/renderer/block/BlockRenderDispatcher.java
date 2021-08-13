package net.minecraft.client.renderer.block;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReport;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.RenderShape;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.color.block.BlockColors;
import java.util.Random;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class BlockRenderDispatcher implements ResourceManagerReloadListener {
    private final BlockModelShaper blockModelShaper;
    private final ModelBlockRenderer modelRenderer;
    private final LiquidBlockRenderer liquidBlockRenderer;
    private final Random random;
    private final BlockColors blockColors;
    
    public BlockRenderDispatcher(final BlockModelShaper eao, final BlockColors dkl) {
        this.random = new Random();
        this.blockModelShaper = eao;
        this.blockColors = dkl;
        this.modelRenderer = new ModelBlockRenderer(this.blockColors);
        this.liquidBlockRenderer = new LiquidBlockRenderer();
    }
    
    public BlockModelShaper getBlockModelShaper() {
        return this.blockModelShaper;
    }
    
    public void renderBreakingTexture(final BlockState cee, final BlockPos fx, final BlockAndTintGetter bqx, final PoseStack dfj, final VertexConsumer dfn) {
        if (cee.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        final BakedModel elg7 = this.blockModelShaper.getBlockModel(cee);
        final long long8 = cee.getSeed(fx);
        this.modelRenderer.tesselateBlock(bqx, elg7, cee, fx, dfj, dfn, true, this.random, long8, OverlayTexture.NO_OVERLAY);
    }
    
    public boolean renderBatched(final BlockState cee, final BlockPos fx, final BlockAndTintGetter bqx, final PoseStack dfj, final VertexConsumer dfn, final boolean boolean6, final Random random) {
        try {
            final RenderShape bze9 = cee.getRenderShape();
            return bze9 == RenderShape.MODEL && this.modelRenderer.tesselateBlock(bqx, this.getBlockModel(cee), cee, fx, dfj, dfn, boolean6, random, cee.getSeed(fx), OverlayTexture.NO_OVERLAY);
        }
        catch (Throwable throwable9) {
            final CrashReport l10 = CrashReport.forThrowable(throwable9, "Tesselating block in world");
            final CrashReportCategory m11 = l10.addCategory("Block being tesselated");
            CrashReportCategory.populateBlockDetails(m11, fx, cee);
            throw new ReportedException(l10);
        }
    }
    
    public boolean renderLiquid(final BlockPos fx, final BlockAndTintGetter bqx, final VertexConsumer dfn, final FluidState cuu) {
        try {
            return this.liquidBlockRenderer.tesselate(bqx, fx, dfn, cuu);
        }
        catch (Throwable throwable6) {
            final CrashReport l7 = CrashReport.forThrowable(throwable6, "Tesselating liquid in world");
            final CrashReportCategory m8 = l7.addCategory("Block being tesselated");
            CrashReportCategory.populateBlockDetails(m8, fx, null);
            throw new ReportedException(l7);
        }
    }
    
    public ModelBlockRenderer getModelRenderer() {
        return this.modelRenderer;
    }
    
    public BakedModel getBlockModel(final BlockState cee) {
        return this.blockModelShaper.getBlockModel(cee);
    }
    
    public void renderSingleBlock(final BlockState cee, final PoseStack dfj, final MultiBufferSource dzy, final int integer4, final int integer5) {
        final RenderShape bze7 = cee.getRenderShape();
        if (bze7 == RenderShape.INVISIBLE) {
            return;
        }
        switch (bze7) {
            case MODEL: {
                final BakedModel elg8 = this.getBlockModel(cee);
                final int integer6 = this.blockColors.getColor(cee, null, null, 0);
                final float float10 = (integer6 >> 16 & 0xFF) / 255.0f;
                final float float11 = (integer6 >> 8 & 0xFF) / 255.0f;
                final float float12 = (integer6 & 0xFF) / 255.0f;
                this.modelRenderer.renderModel(dfj.last(), dzy.getBuffer(ItemBlockRenderTypes.getRenderType(cee, false)), cee, elg8, float10, float11, float12, integer4, integer5);
                break;
            }
            case ENTITYBLOCK_ANIMATED: {
                BlockEntityWithoutLevelRenderer.instance.renderByItem(new ItemStack(cee.getBlock()), ItemTransforms.TransformType.NONE, dfj, dzy, integer4, integer5);
                break;
            }
        }
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        this.liquidBlockRenderer.setupSprites();
    }
}
