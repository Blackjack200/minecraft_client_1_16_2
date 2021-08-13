package net.minecraft.world.level.block.entity;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.NonNullList;
import java.util.List;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.Container;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public abstract class RandomizableContainerBlockEntity extends BaseContainerBlockEntity {
    @Nullable
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    
    protected RandomizableContainerBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
    }
    
    public static void setLootTable(final BlockGetter bqz, final Random random, final BlockPos fx, final ResourceLocation vk) {
        final BlockEntity ccg5 = bqz.getBlockEntity(fx);
        if (ccg5 instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity)ccg5).setLootTable(vk, random.nextLong());
        }
    }
    
    protected boolean tryLoadLootTable(final CompoundTag md) {
        if (md.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(md.getString("LootTable"));
            this.lootTableSeed = md.getLong("LootTableSeed");
            return true;
        }
        return false;
    }
    
    protected boolean trySaveLootTable(final CompoundTag md) {
        if (this.lootTable == null) {
            return false;
        }
        md.putString("LootTable", this.lootTable.toString());
        if (this.lootTableSeed != 0L) {
            md.putLong("LootTableSeed", this.lootTableSeed);
        }
        return true;
    }
    
    public void unpackLootTable(@Nullable final Player bft) {
        if (this.lootTable != null && this.level.getServer() != null) {
            final LootTable cyv3 = this.level.getServer().getLootTables().get(this.lootTable);
            if (bft instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)bft, this.lootTable);
            }
            this.lootTable = null;
            final LootContext.Builder a4 = new LootContext.Builder((ServerLevel)this.level).<Vec3>withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (bft != null) {
                a4.withLuck(bft.getLuck()).<Entity>withParameter(LootContextParams.THIS_ENTITY, bft);
            }
            cyv3.fill(this, a4.create(LootContextParamSets.CHEST));
        }
    }
    
    public void setLootTable(final ResourceLocation vk, final long long2) {
        this.lootTable = vk;
        this.lootTableSeed = long2;
    }
    
    @Override
    public boolean isEmpty() {
        this.unpackLootTable(null);
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }
    
    @Override
    public ItemStack getItem(final int integer) {
        this.unpackLootTable(null);
        return this.getItems().get(integer);
    }
    
    @Override
    public ItemStack removeItem(final int integer1, final int integer2) {
        this.unpackLootTable(null);
        final ItemStack bly4 = ContainerHelper.removeItem((List<ItemStack>)this.getItems(), integer1, integer2);
        if (!bly4.isEmpty()) {
            this.setChanged();
        }
        return bly4;
    }
    
    @Override
    public ItemStack removeItemNoUpdate(final int integer) {
        this.unpackLootTable(null);
        return ContainerHelper.takeItem((List<ItemStack>)this.getItems(), integer);
    }
    
    @Override
    public void setItem(final int integer, final ItemStack bly) {
        this.unpackLootTable(null);
        this.getItems().set(integer, bly);
        if (bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.level.getBlockEntity(this.worldPosition) == this && bft.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }
    
    public void clearContent() {
        this.getItems().clear();
    }
    
    protected abstract NonNullList<ItemStack> getItems();
    
    protected abstract void setItems(final NonNullList<ItemStack> gj);
    
    @Override
    public boolean canOpen(final Player bft) {
        return super.canOpen(bft) && (this.lootTable == null || !bft.isSpectator());
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
        if (this.canOpen(bft)) {
            this.unpackLootTable(bfs.player);
            return this.createMenu(integer, bfs);
        }
        return null;
    }
}
