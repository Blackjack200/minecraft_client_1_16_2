package net.minecraft.client.renderer;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class BlockEntityWithoutLevelRenderer {
    private static final ShulkerBoxBlockEntity[] SHULKER_BOXES;
    private static final ShulkerBoxBlockEntity DEFAULT_SHULKER_BOX;
    public static final BlockEntityWithoutLevelRenderer instance;
    private final ChestBlockEntity chest;
    private final ChestBlockEntity trappedChest;
    private final EnderChestBlockEntity enderChest;
    private final BannerBlockEntity banner;
    private final BedBlockEntity bed;
    private final ConduitBlockEntity conduit;
    private final ShieldModel shieldModel;
    private final TridentModel tridentModel;
    
    public BlockEntityWithoutLevelRenderer() {
        this.chest = new ChestBlockEntity();
        this.trappedChest = new TrappedChestBlockEntity();
        this.enderChest = new EnderChestBlockEntity();
        this.banner = new BannerBlockEntity();
        this.bed = new BedBlockEntity();
        this.conduit = new ConduitBlockEntity();
        this.shieldModel = new ShieldModel();
        this.tridentModel = new TridentModel();
    }
    
    public void renderByItem(final ItemStack bly, final ItemTransforms.TransformType b, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final Item blu8 = bly.getItem();
        if (!(blu8 instanceof BlockItem)) {
            if (blu8 == Items.SHIELD) {
                final boolean boolean9 = bly.getTagElement("BlockEntityTag") != null;
                dfj.pushPose();
                dfj.scale(1.0f, -1.0f, -1.0f);
                final Material elj10 = boolean9 ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
                final VertexConsumer dfn11 = elj10.sprite().wrap(ItemRenderer.getFoilBufferDirect(dzy, this.shieldModel.renderType(elj10.atlasLocation()), true, bly.hasFoil()));
                this.shieldModel.handle().render(dfj, dfn11, integer5, integer6, 1.0f, 1.0f, 1.0f, 1.0f);
                if (boolean9) {
                    final List<Pair<BannerPattern, DyeColor>> list12 = BannerBlockEntity.createPatterns(ShieldItem.getColor(bly), BannerBlockEntity.getItemPatterns(bly));
                    BannerRenderer.renderPatterns(dfj, dzy, integer5, integer6, this.shieldModel.plate(), elj10, false, list12, bly.hasFoil());
                }
                else {
                    this.shieldModel.plate().render(dfj, dfn11, integer5, integer6, 1.0f, 1.0f, 1.0f, 1.0f);
                }
                dfj.popPose();
            }
            else if (blu8 == Items.TRIDENT) {
                dfj.pushPose();
                dfj.scale(1.0f, -1.0f, -1.0f);
                final VertexConsumer dfn12 = ItemRenderer.getFoilBufferDirect(dzy, this.tridentModel.renderType(TridentModel.TEXTURE), false, bly.hasFoil());
                this.tridentModel.renderToBuffer(dfj, dfn12, integer5, integer6, 1.0f, 1.0f, 1.0f, 1.0f);
                dfj.popPose();
            }
            return;
        }
        final Block bul9 = ((BlockItem)blu8).getBlock();
        if (bul9 instanceof AbstractSkullBlock) {
            GameProfile gameProfile10 = null;
            if (bly.hasTag()) {
                final CompoundTag md11 = bly.getTag();
                if (md11.contains("SkullOwner", 10)) {
                    gameProfile10 = NbtUtils.readGameProfile(md11.getCompound("SkullOwner"));
                }
                else if (md11.contains("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)md11.getString("SkullOwner"))) {
                    gameProfile10 = new GameProfile((UUID)null, md11.getString("SkullOwner"));
                    gameProfile10 = SkullBlockEntity.updateGameprofile(gameProfile10);
                    md11.remove("SkullOwner");
                    md11.put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), gameProfile10));
                }
            }
            SkullBlockRenderer.renderSkull(null, 180.0f, ((AbstractSkullBlock)bul9).getType(), gameProfile10, 0.0f, dfj, dzy, integer5);
            return;
        }
        BlockEntity ccg10;
        if (bul9 instanceof AbstractBannerBlock) {
            this.banner.fromItem(bly, ((AbstractBannerBlock)bul9).getColor());
            ccg10 = this.banner;
        }
        else if (bul9 instanceof BedBlock) {
            this.bed.setColor(((BedBlock)bul9).getColor());
            ccg10 = this.bed;
        }
        else if (bul9 == Blocks.CONDUIT) {
            ccg10 = this.conduit;
        }
        else if (bul9 == Blocks.CHEST) {
            ccg10 = this.chest;
        }
        else if (bul9 == Blocks.ENDER_CHEST) {
            ccg10 = this.enderChest;
        }
        else if (bul9 == Blocks.TRAPPED_CHEST) {
            ccg10 = this.trappedChest;
        }
        else {
            if (!(bul9 instanceof ShulkerBoxBlock)) {
                return;
            }
            final DyeColor bku11 = ShulkerBoxBlock.getColorFromItem(blu8);
            if (bku11 == null) {
                ccg10 = BlockEntityWithoutLevelRenderer.DEFAULT_SHULKER_BOX;
            }
            else {
                ccg10 = BlockEntityWithoutLevelRenderer.SHULKER_BOXES[bku11.getId()];
            }
        }
        BlockEntityRenderDispatcher.instance.<BlockEntity>renderItem(ccg10, dfj, dzy, integer5, integer6);
    }
    
    static {
        SHULKER_BOXES = (ShulkerBoxBlockEntity[])Arrays.stream((Object[])DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(ShulkerBoxBlockEntity::new).toArray(ShulkerBoxBlockEntity[]::new);
        DEFAULT_SHULKER_BOX = new ShulkerBoxBlockEntity((DyeColor)null);
        instance = new BlockEntityWithoutLevelRenderer();
    }
}
