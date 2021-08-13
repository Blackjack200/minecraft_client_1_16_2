package net.minecraft.world.inventory;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import java.util.Random;
import net.minecraft.world.Container;

public class EnchantmentMenu extends AbstractContainerMenu {
    private final Container enchantSlots;
    private final ContainerLevelAccess access;
    private final Random random;
    private final DataSlot enchantmentSeed;
    public final int[] costs;
    public final int[] enchantClue;
    public final int[] levelClue;
    
    public EnchantmentMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public EnchantmentMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.ENCHANTMENT, integer);
        this.enchantSlots = new SimpleContainer(2) {
            @Override
            public void setChanged() {
                super.setChanged();
                EnchantmentMenu.this.slotsChanged(this);
            }
        };
        this.random = new Random();
        this.enchantmentSeed = DataSlot.standalone();
        this.costs = new int[3];
        this.enchantClue = new int[] { -1, -1, -1 };
        this.levelClue = new int[] { -1, -1, -1 };
        this.access = bij;
        this.addSlot(new Slot(this.enchantSlots, 0, 15, 47) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return true;
            }
            
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.enchantSlots, 1, 35, 47) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() == Items.LAPIS_LAZULI;
            }
        });
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
        this.addDataSlot(DataSlot.shared(this.costs, 0));
        this.addDataSlot(DataSlot.shared(this.costs, 1));
        this.addDataSlot(DataSlot.shared(this.costs, 2));
        this.addDataSlot(this.enchantmentSeed).set(bfs.player.getEnchantmentSeed());
        this.addDataSlot(DataSlot.shared(this.enchantClue, 0));
        this.addDataSlot(DataSlot.shared(this.enchantClue, 1));
        this.addDataSlot(DataSlot.shared(this.enchantClue, 2));
        this.addDataSlot(DataSlot.shared(this.levelClue, 0));
        this.addDataSlot(DataSlot.shared(this.levelClue, 1));
        this.addDataSlot(DataSlot.shared(this.levelClue, 2));
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        if (aok == this.enchantSlots) {
            final ItemStack bly3 = aok.getItem(0);
            if (bly3.isEmpty() || !bly3.isEnchantable()) {
                for (int integer4 = 0; integer4 < 3; ++integer4) {
                    this.costs[integer4] = 0;
                    this.enchantClue[integer4] = -1;
                    this.levelClue[integer4] = -1;
                }
            }
            else {
                this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                    int integer5 = 0;
                    for (int integer6 = -1; integer6 <= 1; ++integer6) {
                        for (int integer7 = -1; integer7 <= 1; ++integer7) {
                            if (integer6 != 0 || integer7 != 0) {
                                if (bru.isEmptyBlock(fx.offset(integer7, 0, integer6)) && bru.isEmptyBlock(fx.offset(integer7, 1, integer6))) {
                                    if (bru.getBlockState(fx.offset(integer7 * 2, 0, integer6 * 2)).is(Blocks.BOOKSHELF)) {
                                        ++integer5;
                                    }
                                    if (bru.getBlockState(fx.offset(integer7 * 2, 1, integer6 * 2)).is(Blocks.BOOKSHELF)) {
                                        ++integer5;
                                    }
                                    if (integer7 != 0 && integer6 != 0) {
                                        if (bru.getBlockState(fx.offset(integer7 * 2, 0, integer6)).is(Blocks.BOOKSHELF)) {
                                            ++integer5;
                                        }
                                        if (bru.getBlockState(fx.offset(integer7 * 2, 1, integer6)).is(Blocks.BOOKSHELF)) {
                                            ++integer5;
                                        }
                                        if (bru.getBlockState(fx.offset(integer7, 0, integer6 * 2)).is(Blocks.BOOKSHELF)) {
                                            ++integer5;
                                        }
                                        if (bru.getBlockState(fx.offset(integer7, 1, integer6 * 2)).is(Blocks.BOOKSHELF)) {
                                            ++integer5;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.random.setSeed((long)this.enchantmentSeed.get());
                    for (int integer6 = 0; integer6 < 3; ++integer6) {
                        this.costs[integer6] = EnchantmentHelper.getEnchantmentCost(this.random, integer6, integer5, bly3);
                        this.enchantClue[integer6] = -1;
                        this.levelClue[integer6] = -1;
                        if (this.costs[integer6] < integer6 + 1) {
                            this.costs[integer6] = 0;
                        }
                    }
                    for (int integer6 = 0; integer6 < 3; ++integer6) {
                        if (this.costs[integer6] > 0) {
                            final List<EnchantmentInstance> list7 = this.getEnchantmentList(bly3, integer6, this.costs[integer6]);
                            if (list7 != null && !list7.isEmpty()) {
                                final EnchantmentInstance bps8 = (EnchantmentInstance)list7.get(this.random.nextInt(list7.size()));
                                this.enchantClue[integer6] = Registry.ENCHANTMENT.getId(bps8.enchantment);
                                this.levelClue[integer6] = bps8.level;
                            }
                        }
                    }
                    this.broadcastChanges();
                }));
            }
        }
    }
    
    @Override
    public boolean clickMenuButton(final Player bft, final int integer) {
        final ItemStack bly4 = this.enchantSlots.getItem(0);
        final ItemStack bly5 = this.enchantSlots.getItem(1);
        final int integer2 = integer + 1;
        if ((bly5.isEmpty() || bly5.getCount() < integer2) && !bft.abilities.instabuild) {
            return false;
        }
        if (this.costs[integer] > 0 && !bly4.isEmpty() && ((bft.experienceLevel >= integer2 && bft.experienceLevel >= this.costs[integer]) || bft.abilities.instabuild)) {
            this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                ItemStack bly6 = bly4;
                final List<EnchantmentInstance> list10 = this.getEnchantmentList(bly6, integer, this.costs[integer]);
                if (!list10.isEmpty()) {
                    bft.onEnchantmentPerformed(bly6, integer2);
                    final boolean boolean11 = bly6.getItem() == Items.BOOK;
                    if (boolean11) {
                        bly6 = new ItemStack(Items.ENCHANTED_BOOK);
                        final CompoundTag md12 = bly4.getTag();
                        if (md12 != null) {
                            bly6.setTag(md12.copy());
                        }
                        this.enchantSlots.setItem(0, bly6);
                    }
                    for (int integer5 = 0; integer5 < list10.size(); ++integer5) {
                        final EnchantmentInstance bps13 = (EnchantmentInstance)list10.get(integer5);
                        if (boolean11) {
                            EnchantedBookItem.addEnchantment(bly6, bps13);
                        }
                        else {
                            bly6.enchant(bps13.enchantment, bps13.level);
                        }
                    }
                    if (!bft.abilities.instabuild) {
                        bly5.shrink(integer2);
                        if (bly5.isEmpty()) {
                            this.enchantSlots.setItem(1, ItemStack.EMPTY);
                        }
                    }
                    bft.awardStat(Stats.ENCHANT_ITEM);
                    if (bft instanceof ServerPlayer) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)bft, bly6, integer2);
                    }
                    this.enchantSlots.setChanged();
                    this.enchantmentSeed.set(bft.getEnchantmentSeed());
                    this.slotsChanged(this.enchantSlots);
                    bru.playSound(null, fx, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, bru.random.nextFloat() * 0.1f + 0.9f);
                }
            }));
            return true;
        }
        return false;
    }
    
    private List<EnchantmentInstance> getEnchantmentList(final ItemStack bly, final int integer2, final int integer3) {
        this.random.setSeed((long)(this.enchantmentSeed.get() + integer2));
        final List<EnchantmentInstance> list5 = EnchantmentHelper.selectEnchantment(this.random, bly, integer3, false);
        if (bly.getItem() == Items.BOOK && list5.size() > 1) {
            list5.remove(this.random.nextInt(list5.size()));
        }
        return list5;
    }
    
    public int getGoldCount() {
        final ItemStack bly2 = this.enchantSlots.getItem(1);
        if (bly2.isEmpty()) {
            return 0;
        }
        return bly2.getCount();
    }
    
    public int getEnchantmentSeed() {
        return this.enchantmentSeed.get();
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bft.level, this.enchantSlots)));
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.ENCHANTING_TABLE);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer == 0) {
                if (!this.moveItemStackTo(bly5, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer == 1) {
                if (!this.moveItemStackTo(bly5, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (bly5.getItem() == Items.LAPIS_LAZULI) {
                if (!this.moveItemStackTo(bly5, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (((Slot)this.slots.get(0)).hasItem() || !((Slot)this.slots.get(0)).mayPlace(bly5)) {
                    return ItemStack.EMPTY;
                }
                final ItemStack bly6 = bly5.copy();
                bly6.setCount(1);
                bly5.shrink(1);
                ((Slot)this.slots.get(0)).set(bly6);
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            else {
                bjo5.setChanged();
            }
            if (bly5.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly5);
        }
        return bly4;
    }
}
