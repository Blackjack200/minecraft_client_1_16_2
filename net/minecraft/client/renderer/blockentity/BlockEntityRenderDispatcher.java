package net.minecraft.client.renderer.blockentity;

import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import com.google.common.collect.Maps;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Camera;
import net.minecraft.world.level.Level;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Map;

public class BlockEntityRenderDispatcher {
    private final Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers;
    public static final BlockEntityRenderDispatcher instance;
    private final BufferBuilder singleRenderBuffer;
    private Font font;
    public TextureManager textureManager;
    public Level level;
    public Camera camera;
    public HitResult cameraHitResult;
    
    private BlockEntityRenderDispatcher() {
        this.renderers = (Map<BlockEntityType<?>, BlockEntityRenderer<?>>)Maps.newHashMap();
        this.singleRenderBuffer = new BufferBuilder(256);
        this.<SignBlockEntity>register(BlockEntityType.SIGN, new SignRenderer(this));
        this.<SpawnerBlockEntity>register(BlockEntityType.MOB_SPAWNER, new SpawnerRenderer(this));
        this.<PistonMovingBlockEntity>register(BlockEntityType.PISTON, new PistonHeadRenderer(this));
        this.<BlockEntity>register((BlockEntityType<BlockEntity>)BlockEntityType.CHEST, new ChestRenderer<BlockEntity>(this));
        this.<BlockEntity>register((BlockEntityType<BlockEntity>)BlockEntityType.ENDER_CHEST, new ChestRenderer<BlockEntity>(this));
        this.<BlockEntity>register((BlockEntityType<BlockEntity>)BlockEntityType.TRAPPED_CHEST, new ChestRenderer<BlockEntity>(this));
        this.<EnchantmentTableBlockEntity>register(BlockEntityType.ENCHANTING_TABLE, new EnchantTableRenderer(this));
        this.<LecternBlockEntity>register(BlockEntityType.LECTERN, new LecternRenderer(this));
        this.<BlockEntity>register((BlockEntityType<BlockEntity>)BlockEntityType.END_PORTAL, new TheEndPortalRenderer<BlockEntity>(this));
        this.<TheEndGatewayBlockEntity>register(BlockEntityType.END_GATEWAY, new TheEndGatewayRenderer(this));
        this.<BeaconBlockEntity>register(BlockEntityType.BEACON, new BeaconRenderer(this));
        this.<SkullBlockEntity>register(BlockEntityType.SKULL, new SkullBlockRenderer(this));
        this.<BannerBlockEntity>register(BlockEntityType.BANNER, new BannerRenderer(this));
        this.<StructureBlockEntity>register(BlockEntityType.STRUCTURE_BLOCK, new StructureBlockRenderer(this));
        this.<ShulkerBoxBlockEntity>register(BlockEntityType.SHULKER_BOX, new ShulkerBoxRenderer(new ShulkerModel<>(), this));
        this.<BedBlockEntity>register(BlockEntityType.BED, new BedRenderer(this));
        this.<ConduitBlockEntity>register(BlockEntityType.CONDUIT, new ConduitRenderer(this));
        this.<BellBlockEntity>register(BlockEntityType.BELL, new BellRenderer(this));
        this.<CampfireBlockEntity>register(BlockEntityType.CAMPFIRE, new CampfireRenderer(this));
    }
    
    private <E extends BlockEntity> void register(final BlockEntityType<E> cch, final BlockEntityRenderer<E> ebw) {
        this.renderers.put(cch, ebw);
    }
    
    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> getRenderer(final E ccg) {
        return (BlockEntityRenderer<E>)this.renderers.get(ccg.getType());
    }
    
    public void prepare(final Level bru, final TextureManager ejv, final Font dkr, final Camera djh, final HitResult dci) {
        if (this.level != bru) {
            this.setLevel(bru);
        }
        this.textureManager = ejv;
        this.camera = djh;
        this.font = dkr;
        this.cameraHitResult = dci;
    }
    
    public <E extends BlockEntity> void render(final E ccg, final float float2, final PoseStack dfj, final MultiBufferSource dzy) {
        if (!Vec3.atCenterOf(ccg.getBlockPos()).closerThan(this.camera.getPosition(), ccg.getViewDistance())) {
            return;
        }
        final BlockEntityRenderer<E> ebw6 = this.<E>getRenderer(ccg);
        if (ebw6 == null) {
            return;
        }
        if (!ccg.hasLevel() || !ccg.getType().isValid(ccg.getBlockState().getBlock())) {
            return;
        }
        tryRender(ccg, () -> BlockEntityRenderDispatcher.<BlockEntity>setupAndRender(ebw6, ccg, float2, dfj, dzy));
    }
    
    private static <T extends BlockEntity> void setupAndRender(final BlockEntityRenderer<T> ebw, final T ccg, final float float3, final PoseStack dfj, final MultiBufferSource dzy) {
        final Level bru7 = ccg.getLevel();
        int integer6;
        if (bru7 != null) {
            integer6 = LevelRenderer.getLightColor(bru7, ccg.getBlockPos());
        }
        else {
            integer6 = 15728880;
        }
        ebw.render(ccg, float3, dfj, dzy, integer6, OverlayTexture.NO_OVERLAY);
    }
    
    public <E extends BlockEntity> boolean renderItem(final E ccg, final PoseStack dfj, final MultiBufferSource dzy, final int integer4, final int integer5) {
        final BlockEntityRenderer<E> ebw7 = this.<E>getRenderer(ccg);
        if (ebw7 == null) {
            return true;
        }
        tryRender(ccg, () -> ebw7.render(ccg, 0.0f, dfj, dzy, integer4, integer5));
        return false;
    }
    
    private static void tryRender(final BlockEntity ccg, final Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable throwable3) {
            final CrashReport l4 = CrashReport.forThrowable(throwable3, "Rendering Block Entity");
            final CrashReportCategory m5 = l4.addCategory("Block Entity Details");
            ccg.fillCrashReportCategory(m5);
            throw new ReportedException(l4);
        }
    }
    
    public void setLevel(@Nullable final Level bru) {
        this.level = bru;
        if (bru == null) {
            this.camera = null;
        }
    }
    
    public Font getFont() {
        return this.font;
    }
    
    static {
        instance = new BlockEntityRenderDispatcher();
    }
}
