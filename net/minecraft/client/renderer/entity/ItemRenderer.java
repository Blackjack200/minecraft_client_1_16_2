package net.minecraft.client.renderer.entity;

import com.google.common.collect.Sets;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Items;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import java.util.Random;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.resources.model.BakedModel;
import java.util.Iterator;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.world.item.Item;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ItemRenderer implements ResourceManagerReloadListener {
    public static final ResourceLocation ENCHANT_GLINT_LOCATION;
    private static final Set<Item> IGNORED;
    public float blitOffset;
    private final ItemModelShaper itemModelShaper;
    private final TextureManager textureManager;
    private final ItemColors itemColors;
    
    public ItemRenderer(final TextureManager ejv, final ModelManager ell, final ItemColors dkp) {
        this.textureManager = ejv;
        this.itemModelShaper = new ItemModelShaper(ell);
        for (final Item blu6 : Registry.ITEM) {
            if (!ItemRenderer.IGNORED.contains(blu6)) {
                this.itemModelShaper.register(blu6, new ModelResourceLocation(Registry.ITEM.getKey(blu6), "inventory"));
            }
        }
        this.itemColors = dkp;
    }
    
    public ItemModelShaper getItemModelShaper() {
        return this.itemModelShaper;
    }
    
    private void renderModelLists(final BakedModel elg, final ItemStack bly, final int integer3, final int integer4, final PoseStack dfj, final VertexConsumer dfn) {
        final Random random8 = new Random();
        final long long9 = 42L;
        for (final Direction gc14 : Direction.values()) {
            random8.setSeed(42L);
            this.renderQuadList(dfj, dfn, elg.getQuads(null, gc14, random8), bly, integer3, integer4);
        }
        random8.setSeed(42L);
        this.renderQuadList(dfj, dfn, elg.getQuads(null, null, random8), bly, integer3, integer4);
    }
    
    public void render(final ItemStack bly, final ItemTransforms.TransformType b, final boolean boolean3, final PoseStack dfj, final MultiBufferSource dzy, final int integer6, final int integer7, BakedModel elg) {
        if (bly.isEmpty()) {
            return;
        }
        dfj.pushPose();
        final boolean boolean4 = b == ItemTransforms.TransformType.GUI || b == ItemTransforms.TransformType.GROUND || b == ItemTransforms.TransformType.FIXED;
        if (bly.getItem() == Items.TRIDENT && boolean4) {
            elg = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
        }
        elg.getTransforms().getTransform(b).apply(boolean3, dfj);
        dfj.translate(-0.5, -0.5, -0.5);
        if (elg.isCustomRenderer() || (bly.getItem() == Items.TRIDENT && !boolean4)) {
            BlockEntityWithoutLevelRenderer.instance.renderByItem(bly, b, dfj, dzy, integer6, integer7);
        }
        else {
            boolean boolean5;
            if (b != ItemTransforms.TransformType.GUI && !b.firstPerson() && bly.getItem() instanceof BlockItem) {
                final Block bul12 = ((BlockItem)bly.getItem()).getBlock();
                boolean5 = (!(bul12 instanceof HalfTransparentBlock) && !(bul12 instanceof StainedGlassPaneBlock));
            }
            else {
                boolean5 = true;
            }
            final RenderType eag12 = ItemBlockRenderTypes.getRenderType(bly, boolean5);
            VertexConsumer dfn13;
            if (bly.getItem() == Items.COMPASS && bly.hasFoil()) {
                dfj.pushPose();
                final PoseStack.Pose a14 = dfj.last();
                if (b == ItemTransforms.TransformType.GUI) {
                    a14.pose().multiply(0.5f);
                }
                else if (b.firstPerson()) {
                    a14.pose().multiply(0.75f);
                }
                if (boolean5) {
                    dfn13 = getCompassFoilBufferDirect(dzy, eag12, a14);
                }
                else {
                    dfn13 = getCompassFoilBuffer(dzy, eag12, a14);
                }
                dfj.popPose();
            }
            else if (boolean5) {
                dfn13 = getFoilBufferDirect(dzy, eag12, true, bly.hasFoil());
            }
            else {
                dfn13 = getFoilBuffer(dzy, eag12, true, bly.hasFoil());
            }
            this.renderModelLists(elg, bly, integer6, integer7, dfj, dfn13);
        }
        dfj.popPose();
    }
    
    public static VertexConsumer getArmorFoilBuffer(final MultiBufferSource dzy, final RenderType eag, final boolean boolean3, final boolean boolean4) {
        if (boolean4) {
            return VertexMultiConsumer.create(dzy.getBuffer(boolean3 ? RenderType.armorGlint() : RenderType.armorEntityGlint()), dzy.getBuffer(eag));
        }
        return dzy.getBuffer(eag);
    }
    
    public static VertexConsumer getCompassFoilBuffer(final MultiBufferSource dzy, final RenderType eag, final PoseStack.Pose a) {
        return VertexMultiConsumer.create(new SheetedDecalTextureGenerator(dzy.getBuffer(RenderType.glint()), a.pose(), a.normal()), dzy.getBuffer(eag));
    }
    
    public static VertexConsumer getCompassFoilBufferDirect(final MultiBufferSource dzy, final RenderType eag, final PoseStack.Pose a) {
        return VertexMultiConsumer.create(new SheetedDecalTextureGenerator(dzy.getBuffer(RenderType.glintDirect()), a.pose(), a.normal()), dzy.getBuffer(eag));
    }
    
    public static VertexConsumer getFoilBuffer(final MultiBufferSource dzy, final RenderType eag, final boolean boolean3, final boolean boolean4) {
        if (!boolean4) {
            return dzy.getBuffer(eag);
        }
        if (Minecraft.useShaderTransparency() && eag == Sheets.translucentItemSheet()) {
            return VertexMultiConsumer.create(dzy.getBuffer(RenderType.glintTranslucent()), dzy.getBuffer(eag));
        }
        return VertexMultiConsumer.create(dzy.getBuffer(boolean3 ? RenderType.glint() : RenderType.entityGlint()), dzy.getBuffer(eag));
    }
    
    public static VertexConsumer getFoilBufferDirect(final MultiBufferSource dzy, final RenderType eag, final boolean boolean3, final boolean boolean4) {
        if (boolean4) {
            return VertexMultiConsumer.create(dzy.getBuffer(boolean3 ? RenderType.glintDirect() : RenderType.entityGlintDirect()), dzy.getBuffer(eag));
        }
        return dzy.getBuffer(eag);
    }
    
    private void renderQuadList(final PoseStack dfj, final VertexConsumer dfn, final List<BakedQuad> list, final ItemStack bly, final int integer5, final int integer6) {
        final boolean boolean8 = !bly.isEmpty();
        final PoseStack.Pose a9 = dfj.last();
        for (final BakedQuad eas11 : list) {
            int integer7 = -1;
            if (boolean8 && eas11.isTinted()) {
                integer7 = this.itemColors.getColor(bly, eas11.getTintIndex());
            }
            final float float13 = (integer7 >> 16 & 0xFF) / 255.0f;
            final float float14 = (integer7 >> 8 & 0xFF) / 255.0f;
            final float float15 = (integer7 & 0xFF) / 255.0f;
            dfn.putBulkData(a9, eas11, float13, float14, float15, integer5, integer6);
        }
    }
    
    public BakedModel getModel(final ItemStack bly, @Nullable final Level bru, @Nullable final LivingEntity aqj) {
        final Item blu6 = bly.getItem();
        BakedModel elg5;
        if (blu6 == Items.TRIDENT) {
            elg5 = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:trident_in_hand#inventory"));
        }
        else {
            elg5 = this.itemModelShaper.getItemModel(bly);
        }
        final ClientLevel dwl7 = (bru instanceof ClientLevel) ? ((ClientLevel)bru) : null;
        final BakedModel elg6 = elg5.getOverrides().resolve(elg5, bly, dwl7, aqj);
        return (elg6 == null) ? this.itemModelShaper.getModelManager().getMissingModel() : elg6;
    }
    
    public void renderStatic(final ItemStack bly, final ItemTransforms.TransformType b, final int integer3, final int integer4, final PoseStack dfj, final MultiBufferSource dzy) {
        this.renderStatic(null, bly, b, false, dfj, dzy, null, integer3, integer4);
    }
    
    public void renderStatic(@Nullable final LivingEntity aqj, final ItemStack bly, final ItemTransforms.TransformType b, final boolean boolean4, final PoseStack dfj, final MultiBufferSource dzy, @Nullable final Level bru, final int integer8, final int integer9) {
        if (bly.isEmpty()) {
            return;
        }
        final BakedModel elg11 = this.getModel(bly, bru, aqj);
        this.render(bly, b, boolean4, dfj, dzy, integer8, integer9, elg11);
    }
    
    public void renderGuiItem(final ItemStack bly, final int integer2, final int integer3) {
        this.renderGuiItem(bly, integer2, integer3, this.getModel(bly, null, null));
    }
    
    protected void renderGuiItem(final ItemStack bly, final int integer2, final int integer3, final BakedModel elg) {
        RenderSystem.pushMatrix();
        this.textureManager.bind(TextureAtlas.LOCATION_BLOCKS);
        this.textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.translatef((float)integer2, (float)integer3, 100.0f + this.blitOffset);
        RenderSystem.translatef(8.0f, 8.0f, 0.0f);
        RenderSystem.scalef(1.0f, -1.0f, 1.0f);
        RenderSystem.scalef(16.0f, 16.0f, 16.0f);
        final PoseStack dfj6 = new PoseStack();
        final MultiBufferSource.BufferSource a7 = Minecraft.getInstance().renderBuffers().bufferSource();
        final boolean boolean8 = !elg.usesBlockLight();
        if (boolean8) {
            Lighting.setupForFlatItems();
        }
        this.render(bly, ItemTransforms.TransformType.GUI, false, dfj6, a7, 15728880, OverlayTexture.NO_OVERLAY, elg);
        a7.endBatch();
        RenderSystem.enableDepthTest();
        if (boolean8) {
            Lighting.setupFor3DItems();
        }
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }
    
    public void renderAndDecorateItem(final ItemStack bly, final int integer2, final int integer3) {
        this.tryRenderGuiItem(Minecraft.getInstance().player, bly, integer2, integer3);
    }
    
    public void renderAndDecorateFakeItem(final ItemStack bly, final int integer2, final int integer3) {
        this.tryRenderGuiItem(null, bly, integer2, integer3);
    }
    
    public void renderAndDecorateItem(final LivingEntity aqj, final ItemStack bly, final int integer3, final int integer4) {
        this.tryRenderGuiItem(aqj, bly, integer3, integer4);
    }
    
    private void tryRenderGuiItem(@Nullable final LivingEntity aqj, final ItemStack bly, final int integer3, final int integer4) {
        if (bly.isEmpty()) {
            return;
        }
        this.blitOffset += 50.0f;
        try {
            this.renderGuiItem(bly, integer3, integer4, this.getModel(bly, null, aqj));
        }
        catch (Throwable throwable6) {
            final CrashReport l7 = CrashReport.forThrowable(throwable6, "Rendering item");
            final CrashReportCategory m8 = l7.addCategory("Item being rendered");
            m8.setDetail("Item Type", (CrashReportDetail<String>)(() -> String.valueOf(bly.getItem())));
            m8.setDetail("Item Damage", (CrashReportDetail<String>)(() -> String.valueOf(bly.getDamageValue())));
            m8.setDetail("Item NBT", (CrashReportDetail<String>)(() -> String.valueOf(bly.getTag())));
            m8.setDetail("Item Foil", (CrashReportDetail<String>)(() -> String.valueOf(bly.hasFoil())));
            throw new ReportedException(l7);
        }
        this.blitOffset -= 50.0f;
    }
    
    public void renderGuiItemDecorations(final Font dkr, final ItemStack bly, final int integer3, final int integer4) {
        this.renderGuiItemDecorations(dkr, bly, integer3, integer4, null);
    }
    
    public void renderGuiItemDecorations(final Font dkr, final ItemStack bly, final int integer3, final int integer4, @Nullable final String string) {
        if (bly.isEmpty()) {
            return;
        }
        final PoseStack dfj7 = new PoseStack();
        if (bly.getCount() != 1 || string != null) {
            final String string2 = (string == null) ? String.valueOf(bly.getCount()) : string;
            dfj7.translate(0.0, 0.0, this.blitOffset + 200.0f);
            final MultiBufferSource.BufferSource a9 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            dkr.drawInBatch(string2, (float)(integer3 + 19 - 2 - dkr.width(string2)), (float)(integer4 + 6 + 3), 16777215, true, dfj7.last().pose(), a9, false, 0, 15728880);
            a9.endBatch();
        }
        if (bly.isDamaged()) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
            final Tesselator dfl8 = Tesselator.getInstance();
            final BufferBuilder dfe9 = dfl8.getBuilder();
            final float float10 = (float)bly.getDamageValue();
            final float float11 = (float)bly.getMaxDamage();
            final float float12 = Math.max(0.0f, (float11 - float10) / float11);
            final int integer5 = Math.round(13.0f - float10 * 13.0f / float11);
            final int integer6 = Mth.hsvToRgb(float12 / 3.0f, 1.0f, 1.0f);
            this.fillRect(dfe9, integer3 + 2, integer4 + 13, 13, 2, 0, 0, 0, 255);
            this.fillRect(dfe9, integer3 + 2, integer4 + 13, integer5, 1, integer6 >> 16 & 0xFF, integer6 >> 8 & 0xFF, integer6 & 0xFF, 255);
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
        final LocalPlayer dze8 = Minecraft.getInstance().player;
        final float float13 = (dze8 == null) ? 0.0f : dze8.getCooldowns().getCooldownPercent(bly.getItem(), Minecraft.getInstance().getFrameTime());
        if (float13 > 0.0f) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            final Tesselator dfl9 = Tesselator.getInstance();
            final BufferBuilder dfe10 = dfl9.getBuilder();
            this.fillRect(dfe10, integer3, integer4 + Mth.floor(16.0f * (1.0f - float13)), 16, Mth.ceil(16.0f * float13), 255, 255, 255, 127);
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
    }
    
    private void fillRect(final BufferBuilder dfe, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        dfe.begin(7, DefaultVertexFormat.POSITION_COLOR);
        dfe.vertex(integer2 + 0, integer3 + 0, 0.0).color(integer6, integer7, integer8, integer9).endVertex();
        dfe.vertex(integer2 + 0, integer3 + integer5, 0.0).color(integer6, integer7, integer8, integer9).endVertex();
        dfe.vertex(integer2 + integer4, integer3 + integer5, 0.0).color(integer6, integer7, integer8, integer9).endVertex();
        dfe.vertex(integer2 + integer4, integer3 + 0, 0.0).color(integer6, integer7, integer8, integer9).endVertex();
        Tesselator.getInstance().end();
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        this.itemModelShaper.rebuildCache();
    }
    
    static {
        ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        IGNORED = (Set)Sets.newHashSet((Object[])new Item[] { Items.AIR });
    }
}
