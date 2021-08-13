package net.minecraft.world.inventory;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import java.util.Iterator;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class GrindstoneMenu extends AbstractContainerMenu {
    private final Container resultSlots;
    private final Container repairSlots;
    private final ContainerLevelAccess access;
    
    public GrindstoneMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public GrindstoneMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.GRINDSTONE, integer);
        this.resultSlots = new ResultContainer();
        this.repairSlots = new SimpleContainer(2) {
            @Override
            public void setChanged() {
                super.setChanged();
                GrindstoneMenu.this.slotsChanged(this);
            }
        };
        this.access = bij;
        this.addSlot(new Slot(this.repairSlots, 0, 49, 19) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.isDamageableItem() || bly.getItem() == Items.ENCHANTED_BOOK || bly.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.repairSlots, 1, 49, 40) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.isDamageableItem() || bly.getItem() == Items.ENCHANTED_BOOK || bly.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.resultSlots, 2, 129, 34) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return false;
            }
            
            @Override
            public ItemStack onTake(final Player bft, final ItemStack bly) {
                bij.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                    int integer4 = this.getExperienceAmount(bru);
                    while (integer4 > 0) {
                        final int integer5 = ExperienceOrb.getExperienceValue(integer4);
                        integer4 -= integer5;
                        bru.addFreshEntity(new ExperienceOrb(bru, fx.getX(), fx.getY() + 0.5, fx.getZ() + 0.5, integer5));
                    }
                    bru.levelEvent(1042, fx, 0);
                }));
                GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
                GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
                return bly;
            }
            
            private int getExperienceAmount(final Level bru) {
                int integer3 = 0;
                integer3 += this.getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
                integer3 += this.getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
                if (integer3 > 0) {
                    final int integer4 = (int)Math.ceil(integer3 / 2.0);
                    return integer4 + bru.random.nextInt(integer4);
                }
                return 0;
            }
            
            private int getExperienceFromItem(final ItemStack bly) {
                int integer3 = 0;
                final Map<Enchantment, Integer> map4 = EnchantmentHelper.getEnchantments(bly);
                for (final Map.Entry<Enchantment, Integer> entry6 : map4.entrySet()) {
                    final Enchantment bpp7 = (Enchantment)entry6.getKey();
                    final Integer integer4 = (Integer)entry6.getValue();
                    if (!bpp7.isCurse()) {
                        integer3 += bpp7.getMinCost(integer4);
                    }
                }
                return integer3;
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
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        super.slotsChanged(aok);
        if (aok == this.repairSlots) {
            this.createResult();
        }
    }
    
    private void createResult() {
        final ItemStack bly2 = this.repairSlots.getItem(0);
        final ItemStack bly3 = this.repairSlots.getItem(1);
        final boolean boolean4 = !bly2.isEmpty() || !bly3.isEmpty();
        final boolean boolean5 = !bly2.isEmpty() && !bly3.isEmpty();
        if (boolean4) {
            final boolean boolean6 = (!bly2.isEmpty() && bly2.getItem() != Items.ENCHANTED_BOOK && !bly2.isEnchanted()) || (!bly3.isEmpty() && bly3.getItem() != Items.ENCHANTED_BOOK && !bly3.isEnchanted());
            if (bly2.getCount() > 1 || bly3.getCount() > 1 || (!boolean5 && boolean6)) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }
            int integer8 = 1;
            int integer12;
            ItemStack bly4;
            if (boolean5) {
                if (bly2.getItem() != bly3.getItem()) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }
                final Item blu10 = bly2.getItem();
                final int integer9 = blu10.getMaxDamage() - bly2.getDamageValue();
                final int integer10 = blu10.getMaxDamage() - bly3.getDamageValue();
                final int integer11 = integer9 + integer10 + blu10.getMaxDamage() * 5 / 100;
                integer12 = Math.max(blu10.getMaxDamage() - integer11, 0);
                bly4 = this.mergeEnchants(bly2, bly3);
                if (!bly4.isDamageableItem()) {
                    if (!ItemStack.matches(bly2, bly3)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }
                    integer8 = 2;
                }
            }
            else {
                final boolean boolean7 = !bly2.isEmpty();
                integer12 = (boolean7 ? bly2.getDamageValue() : bly3.getDamageValue());
                bly4 = (boolean7 ? bly2 : bly3);
            }
            this.resultSlots.setItem(0, this.removeNonCurses(bly4, integer12, integer8));
        }
        else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }
    
    private ItemStack mergeEnchants(final ItemStack bly1, final ItemStack bly2) {
        final ItemStack bly3 = bly1.copy();
        final Map<Enchantment, Integer> map5 = EnchantmentHelper.getEnchantments(bly2);
        for (final Map.Entry<Enchantment, Integer> entry7 : map5.entrySet()) {
            final Enchantment bpp8 = (Enchantment)entry7.getKey();
            if (!bpp8.isCurse() || EnchantmentHelper.getItemEnchantmentLevel(bpp8, bly3) == 0) {
                bly3.enchant(bpp8, (int)entry7.getValue());
            }
        }
        return bly3;
    }
    
    private ItemStack removeNonCurses(final ItemStack bly, final int integer2, final int integer3) {
        ItemStack bly2 = bly.copy();
        bly2.removeTagKey("Enchantments");
        bly2.removeTagKey("StoredEnchantments");
        if (integer2 > 0) {
            bly2.setDamageValue(integer2);
        }
        else {
            bly2.removeTagKey("Damage");
        }
        bly2.setCount(integer3);
        final Map<Enchantment, Integer> map6 = (Map<Enchantment, Integer>)EnchantmentHelper.getEnchantments(bly).entrySet().stream().filter(entry -> ((Enchantment)entry.getKey()).isCurse()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        EnchantmentHelper.setEnchantments(map6, bly2);
        bly2.setRepairCost(0);
        if (bly2.getItem() == Items.ENCHANTED_BOOK && map6.size() == 0) {
            bly2 = new ItemStack(Items.BOOK);
            if (bly.hasCustomHoverName()) {
                bly2.setHoverName(bly.getHoverName());
            }
        }
        for (int integer4 = 0; integer4 < map6.size(); ++integer4) {
            bly2.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(bly2.getBaseRepairCost()));
        }
        return bly2;
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bru, this.repairSlots)));
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.GRINDSTONE);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            final ItemStack bly6 = this.repairSlots.getItem(0);
            final ItemStack bly7 = this.repairSlots.getItem(1);
            if (integer == 2) {
                if (!this.moveItemStackTo(bly5, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer == 0 || integer == 1) {
                if (!this.moveItemStackTo(bly5, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (bly6.isEmpty() || bly7.isEmpty()) {
                if (!this.moveItemStackTo(bly5, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 3 && integer < 30) {
                if (!this.moveItemStackTo(bly5, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 30 && integer < 39 && !this.moveItemStackTo(bly5, 3, 30, false)) {
                return ItemStack.EMPTY;
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
