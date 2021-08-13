package net.minecraft.world.level.block.entity;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.sounds.SoundEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class ChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity, TickableBlockEntity {
    private NonNullList<ItemStack> items;
    protected float openness;
    protected float oOpenness;
    protected int openCount;
    private int tickInterval;
    
    protected ChestBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    }
    
    public ChestBlockEntity() {
        this(BlockEntityType.CHEST);
    }
    
    public int getContainerSize() {
        return 27;
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.chest");
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
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (!this.trySaveLootTable(md)) {
            ContainerHelper.saveAllItems(md, this.items);
        }
        return md;
    }
    
    @Override
    public void tick() {
        final int integer2 = this.worldPosition.getX();
        final int integer3 = this.worldPosition.getY();
        final int integer4 = this.worldPosition.getZ();
        ++this.tickInterval;
        this.openCount = getOpenCount(this.level, this, this.tickInterval, integer2, integer3, integer4, this.openCount);
        this.oOpenness = this.openness;
        final float float5 = 0.1f;
        if (this.openCount > 0 && this.openness == 0.0f) {
            this.playSound(SoundEvents.CHEST_OPEN);
        }
        if ((this.openCount == 0 && this.openness > 0.0f) || (this.openCount > 0 && this.openness < 1.0f)) {
            final float float6 = this.openness;
            if (this.openCount > 0) {
                this.openness += 0.1f;
            }
            else {
                this.openness -= 0.1f;
            }
            if (this.openness > 1.0f) {
                this.openness = 1.0f;
            }
            final float float7 = 0.5f;
            if (this.openness < 0.5f && float6 >= 0.5f) {
                this.playSound(SoundEvents.CHEST_CLOSE);
            }
            if (this.openness < 0.0f) {
                this.openness = 0.0f;
            }
        }
    }
    
    public static int getOpenCount(final Level bru, final BaseContainerBlockEntity cca, final int integer3, final int integer4, final int integer5, final int integer6, int integer7) {
        if (!bru.isClientSide && integer7 != 0 && (integer3 + integer4 + integer5 + integer6) % 200 == 0) {
            integer7 = getOpenCount(bru, cca, integer4, integer5, integer6);
        }
        return integer7;
    }
    
    public static int getOpenCount(final Level bru, final BaseContainerBlockEntity cca, final int integer3, final int integer4, final int integer5) {
        int integer6 = 0;
        final float float7 = 5.0f;
        final List<Player> list8 = bru.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, new AABB(integer3 - 5.0f, integer4 - 5.0f, integer5 - 5.0f, integer3 + 1 + 5.0f, integer4 + 1 + 5.0f, integer5 + 1 + 5.0f));
        for (final Player bft10 : list8) {
            if (bft10.containerMenu instanceof ChestMenu) {
                final Container aok11 = ((ChestMenu)bft10.containerMenu).getContainer();
                if (aok11 != cca && (!(aok11 instanceof CompoundContainer) || !((CompoundContainer)aok11).contains(cca))) {
                    continue;
                }
                ++integer6;
            }
        }
        return integer6;
    }
    
    private void playSound(final SoundEvent adn) {
        final ChestType cew3 = this.getBlockState().<ChestType>getValue(ChestBlock.TYPE);
        if (cew3 == ChestType.LEFT) {
            return;
        }
        double double4 = this.worldPosition.getX() + 0.5;
        final double double5 = this.worldPosition.getY() + 0.5;
        double double6 = this.worldPosition.getZ() + 0.5;
        if (cew3 == ChestType.RIGHT) {
            final Direction gc10 = ChestBlock.getConnectedDirection(this.getBlockState());
            double4 += gc10.getStepX() * 0.5;
            double6 += gc10.getStepZ() * 0.5;
        }
        this.level.playSound(null, double4, double5, double6, adn, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }
    
    public boolean triggerEvent(final int integer1, final int integer2) {
        if (integer1 == 1) {
            this.openCount = integer2;
            return true;
        }
        return super.triggerEvent(integer1, integer2);
    }
    
    public void startOpen(final Player bft) {
        if (!bft.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            ++this.openCount;
            this.signalOpenCount();
        }
    }
    
    public void stopOpen(final Player bft) {
        if (!bft.isSpectator()) {
            --this.openCount;
            this.signalOpenCount();
        }
    }
    
    protected void signalOpenCount() {
        final Block bul2 = this.getBlockState().getBlock();
        if (bul2 instanceof ChestBlock) {
            this.level.blockEvent(this.worldPosition, bul2, 1, this.openCount);
            this.level.updateNeighborsAt(this.worldPosition, bul2);
        }
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
    public float getOpenNess(final float float1) {
        return Mth.lerp(float1, this.oOpenness, this.openness);
    }
    
    public static int getOpenCount(final BlockGetter bqz, final BlockPos fx) {
        final BlockState cee3 = bqz.getBlockState(fx);
        if (cee3.getBlock().isEntityBlock()) {
            final BlockEntity ccg4 = bqz.getBlockEntity(fx);
            if (ccg4 instanceof ChestBlockEntity) {
                return ((ChestBlockEntity)ccg4).openCount;
            }
        }
        return 0;
    }
    
    public static void swapContents(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
        final NonNullList<ItemStack> gj3 = cck1.getItems();
        cck1.setItems(cck2.getItems());
        cck2.setItems(gj3);
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return ChestMenu.threeRows(integer, bfs, this);
    }
}
