package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class BarrelBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private int openCount;
    
    private BarrelBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    }
    
    public BarrelBlockEntity() {
        this(BlockEntityType.BARREL);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (!this.trySaveLootTable(md)) {
            ContainerHelper.saveAllItems(md, this.items);
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.items = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(md)) {
            ContainerHelper.loadAllItems(md, this.items);
        }
    }
    
    @Override
    public int getContainerSize() {
        return 27;
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    protected void setItems(final NonNullList<ItemStack> gj) {
        this.items = gj;
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.barrel");
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return ChestMenu.threeRows(integer, bfs, this);
    }
    
    @Override
    public void startOpen(final Player bft) {
        if (!bft.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            ++this.openCount;
            final BlockState cee3 = this.getBlockState();
            final boolean boolean4 = cee3.<Boolean>getValue((Property<Boolean>)BarrelBlock.OPEN);
            if (!boolean4) {
                this.playSound(cee3, SoundEvents.BARREL_OPEN);
                this.updateBlockState(cee3, true);
            }
            this.scheduleRecheck();
        }
    }
    
    private void scheduleRecheck() {
        this.level.getBlockTicks().scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 5);
    }
    
    public void recheckOpen() {
        final int integer2 = this.worldPosition.getX();
        final int integer3 = this.worldPosition.getY();
        final int integer4 = this.worldPosition.getZ();
        this.openCount = ChestBlockEntity.getOpenCount(this.level, this, integer2, integer3, integer4);
        if (this.openCount > 0) {
            this.scheduleRecheck();
        }
        else {
            final BlockState cee5 = this.getBlockState();
            if (!cee5.is(Blocks.BARREL)) {
                this.setRemoved();
                return;
            }
            final boolean boolean6 = cee5.<Boolean>getValue((Property<Boolean>)BarrelBlock.OPEN);
            if (boolean6) {
                this.playSound(cee5, SoundEvents.BARREL_CLOSE);
                this.updateBlockState(cee5, false);
            }
        }
    }
    
    @Override
    public void stopOpen(final Player bft) {
        if (!bft.isSpectator()) {
            --this.openCount;
        }
    }
    
    private void updateBlockState(final BlockState cee, final boolean boolean2) {
        this.level.setBlock(this.getBlockPos(), ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)BarrelBlock.OPEN, boolean2), 3);
    }
    
    private void playSound(final BlockState cee, final SoundEvent adn) {
        final Vec3i gr4 = cee.<Direction>getValue((Property<Direction>)BarrelBlock.FACING).getNormal();
        final double double5 = this.worldPosition.getX() + 0.5 + gr4.getX() / 2.0;
        final double double6 = this.worldPosition.getY() + 0.5 + gr4.getY() / 2.0;
        final double double7 = this.worldPosition.getZ() + 0.5 + gr4.getZ() / 2.0;
        this.level.playSound(null, double5, double6, double7, adn, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }
}
