package net.minecraft.world.entity.vehicle;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import java.util.List;
import net.minecraft.world.ContainerHelper;
import java.util.Iterator;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.Containers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Container;

public abstract class AbstractMinecartContainer extends AbstractMinecart implements Container, MenuProvider {
    private NonNullList<ItemStack> itemStacks;
    private boolean dropEquipment;
    @Nullable
    private ResourceLocation lootTable;
    private long lootTableSeed;
    
    protected AbstractMinecartContainer(final EntityType<?> aqb, final Level bru) {
        super(aqb, bru);
        this.itemStacks = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
        this.dropEquipment = true;
    }
    
    protected AbstractMinecartContainer(final EntityType<?> aqb, final double double2, final double double3, final double double4, final Level bru) {
        super(aqb, bru, double2, double3, double4);
        this.itemStacks = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
        this.dropEquipment = true;
    }
    
    @Override
    public void destroy(final DamageSource aph) {
        super.destroy(aph);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            Containers.dropContents(this.level, this, this);
            if (!this.level.isClientSide) {
                final Entity apx3 = aph.getDirectEntity();
                if (apx3 != null && apx3.getType() == EntityType.PLAYER) {
                    PiglinAi.angerNearbyPiglins((Player)apx3, true);
                }
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack bly3 : this.itemStacks) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(final int integer) {
        this.unpackLootTable(null);
        return this.itemStacks.get(integer);
    }
    
    @Override
    public ItemStack removeItem(final int integer1, final int integer2) {
        this.unpackLootTable(null);
        return ContainerHelper.removeItem((List<ItemStack>)this.itemStacks, integer1, integer2);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(final int integer) {
        this.unpackLootTable(null);
        final ItemStack bly3 = this.itemStacks.get(integer);
        if (bly3.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.itemStacks.set(integer, ItemStack.EMPTY);
        return bly3;
    }
    
    @Override
    public void setItem(final int integer, final ItemStack bly) {
        this.unpackLootTable(null);
        this.itemStacks.set(integer, bly);
        if (!bly.isEmpty() && bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (integer >= 0 && integer < this.getContainerSize()) {
            this.setItem(integer, bly);
            return true;
        }
        return false;
    }
    
    @Override
    public void setChanged() {
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return !this.removed && bft.distanceToSqr(this) <= 64.0;
    }
    
    @Nullable
    @Override
    public Entity changeDimension(final ServerLevel aag) {
        this.dropEquipment = false;
        return super.changeDimension(aag);
    }
    
    @Override
    public void remove() {
        if (!this.level.isClientSide && this.dropEquipment) {
            Containers.dropContents(this.level, this, this);
        }
        super.remove();
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.lootTable != null) {
            md.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                md.putLong("LootTableSeed", this.lootTableSeed);
            }
        }
        else {
            ContainerHelper.saveAllItems(md, this.itemStacks);
        }
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.itemStacks = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (md.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(md.getString("LootTable"));
            this.lootTableSeed = md.getLong("LootTableSeed");
        }
        else {
            ContainerHelper.loadAllItems(md, this.itemStacks);
        }
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        bft.openMenu(this);
        if (!bft.level.isClientSide) {
            PiglinAi.angerNearbyPiglins(bft, true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    protected void applyNaturalSlowdown() {
        float float2 = 0.98f;
        if (this.lootTable == null) {
            final int integer3 = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
            float2 += integer3 * 0.001f;
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(float2, 0.0, float2));
    }
    
    public void unpackLootTable(@Nullable final Player bft) {
        if (this.lootTable != null && this.level.getServer() != null) {
            final LootTable cyv3 = this.level.getServer().getLootTables().get(this.lootTable);
            if (bft instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)bft, this.lootTable);
            }
            this.lootTable = null;
            final LootContext.Builder a4 = new LootContext.Builder((ServerLevel)this.level).<Vec3>withParameter(LootContextParams.ORIGIN, this.position()).withOptionalRandomSeed(this.lootTableSeed);
            if (bft != null) {
                a4.withLuck(bft.getLuck()).<Entity>withParameter(LootContextParams.THIS_ENTITY, bft);
            }
            cyv3.fill(this, a4.create(LootContextParamSets.CHEST));
        }
    }
    
    public void clearContent() {
        this.unpackLootTable(null);
        this.itemStacks.clear();
    }
    
    public void setLootTable(final ResourceLocation vk, final long long2) {
        this.lootTable = vk;
        this.lootTableSeed = long2;
    }
    
    @Nullable
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
        if (this.lootTable == null || !bft.isSpectator()) {
            this.unpackLootTable(bfs.player);
            return this.createMenu(integer, bfs);
        }
        return null;
    }
    
    protected abstract AbstractContainerMenu createMenu(final int integer, final Inventory bfs);
}
