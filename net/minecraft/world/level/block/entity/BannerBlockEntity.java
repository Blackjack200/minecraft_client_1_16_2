package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BannerBlock;
import com.google.common.collect.Lists;
import java.util.function.Supplier;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.DyeColor;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

public class BannerBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private Component name;
    @Nullable
    private DyeColor baseColor;
    @Nullable
    private ListTag itemPatterns;
    private boolean receivedData;
    @Nullable
    private List<Pair<BannerPattern, DyeColor>> patterns;
    
    public BannerBlockEntity() {
        super(BlockEntityType.BANNER);
        this.baseColor = DyeColor.WHITE;
    }
    
    public BannerBlockEntity(final DyeColor bku) {
        this();
        this.baseColor = bku;
    }
    
    @Nullable
    public static ListTag getItemPatterns(final ItemStack bly) {
        ListTag mj2 = null;
        final CompoundTag md3 = bly.getTagElement("BlockEntityTag");
        if (md3 != null && md3.contains("Patterns", 9)) {
            mj2 = md3.getList("Patterns", 10).copy();
        }
        return mj2;
    }
    
    public void fromItem(final ItemStack bly, final DyeColor bku) {
        this.itemPatterns = getItemPatterns(bly);
        this.baseColor = bku;
        this.patterns = null;
        this.receivedData = true;
        this.name = (bly.hasCustomHoverName() ? bly.getHoverName() : null);
    }
    
    @Override
    public Component getName() {
        if (this.name != null) {
            return this.name;
        }
        return new TranslatableComponent("block.minecraft.banner");
    }
    
    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }
    
    public void setCustomName(final Component nr) {
        this.name = nr;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (this.itemPatterns != null) {
            md.put("Patterns", (Tag)this.itemPatterns);
        }
        if (this.name != null) {
            md.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(md.getString("CustomName"));
        }
        if (this.hasLevel()) {
            this.baseColor = ((AbstractBannerBlock)this.getBlockState().getBlock()).getColor();
        }
        else {
            this.baseColor = null;
        }
        this.itemPatterns = md.getList("Patterns", 10);
        this.patterns = null;
        this.receivedData = true;
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 6, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public static int getPatternCount(final ItemStack bly) {
        final CompoundTag md2 = bly.getTagElement("BlockEntityTag");
        if (md2 != null && md2.contains("Patterns")) {
            return md2.getList("Patterns", 10).size();
        }
        return 0;
    }
    
    public List<Pair<BannerPattern, DyeColor>> getPatterns() {
        if (this.patterns == null && this.receivedData) {
            this.patterns = createPatterns(this.getBaseColor((Supplier<BlockState>)this::getBlockState), this.itemPatterns);
        }
        return this.patterns;
    }
    
    public static List<Pair<BannerPattern, DyeColor>> createPatterns(final DyeColor bku, @Nullable final ListTag mj) {
        final List<Pair<BannerPattern, DyeColor>> list3 = (List<Pair<BannerPattern, DyeColor>>)Lists.newArrayList();
        list3.add(Pair.of((Object)BannerPattern.BASE, (Object)bku));
        if (mj != null) {
            for (int integer4 = 0; integer4 < mj.size(); ++integer4) {
                final CompoundTag md5 = mj.getCompound(integer4);
                final BannerPattern cby6 = BannerPattern.byHash(md5.getString("Pattern"));
                if (cby6 != null) {
                    final int integer5 = md5.getInt("Color");
                    list3.add(Pair.of((Object)cby6, (Object)DyeColor.byId(integer5)));
                }
            }
        }
        return list3;
    }
    
    public static void removeLastPattern(final ItemStack bly) {
        final CompoundTag md2 = bly.getTagElement("BlockEntityTag");
        if (md2 == null || !md2.contains("Patterns", 9)) {
            return;
        }
        final ListTag mj3 = md2.getList("Patterns", 10);
        if (mj3.isEmpty()) {
            return;
        }
        mj3.remove(mj3.size() - 1);
        if (mj3.isEmpty()) {
            bly.removeTagKey("BlockEntityTag");
        }
    }
    
    public ItemStack getItem(final BlockState cee) {
        final ItemStack bly3 = new ItemStack(BannerBlock.byColor(this.getBaseColor((Supplier<BlockState>)(() -> cee))));
        if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            bly3.getOrCreateTagElement("BlockEntityTag").put("Patterns", (Tag)this.itemPatterns.copy());
        }
        if (this.name != null) {
            bly3.setHoverName(this.name);
        }
        return bly3;
    }
    
    public DyeColor getBaseColor(final Supplier<BlockState> supplier) {
        if (this.baseColor == null) {
            this.baseColor = ((AbstractBannerBlock)((BlockState)supplier.get()).getBlock()).getColor();
        }
        return this.baseColor;
    }
}
