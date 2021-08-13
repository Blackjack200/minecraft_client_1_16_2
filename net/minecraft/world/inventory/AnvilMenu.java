package net.minecraft.world.inventory;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.block.AnvilBlock;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import org.apache.logging.log4j.Logger;

public class AnvilMenu extends ItemCombinerMenu {
    private static final Logger LOGGER;
    private int repairItemCountCost;
    private String itemName;
    private final DataSlot cost;
    
    public AnvilMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public AnvilMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.ANVIL, integer, bfs, bij);
        this.addDataSlot(this.cost = DataSlot.standalone());
    }
    
    @Override
    protected boolean isValidBlock(final BlockState cee) {
        return cee.is(BlockTags.ANVIL);
    }
    
    @Override
    protected boolean mayPickup(final Player bft, final boolean boolean2) {
        return (bft.abilities.instabuild || bft.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }
    
    @Override
    protected ItemStack onTake(final Player bft, final ItemStack bly) {
        if (!bft.abilities.instabuild) {
            bft.giveExperienceLevels(-this.cost.get());
        }
        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            final ItemStack bly2 = this.inputSlots.getItem(1);
            if (!bly2.isEmpty() && bly2.getCount() > this.repairItemCountCost) {
                bly2.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, bly2);
            }
            else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        }
        else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }
        this.cost.set(0);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
            final BlockState cee4 = bru.getBlockState(fx);
            if (!bft.abilities.instabuild && cee4.is(BlockTags.ANVIL) && bft.getRandom().nextFloat() < 0.12f) {
                final BlockState cee5 = AnvilBlock.damage(cee4);
                if (cee5 == null) {
                    bru.removeBlock(fx, false);
                    bru.levelEvent(1029, fx, 0);
                }
                else {
                    bru.setBlock(fx, cee5, 2);
                    bru.levelEvent(1030, fx, 0);
                }
            }
            else {
                bru.levelEvent(1030, fx, 0);
            }
        }));
        return bly;
    }
    
    @Override
    public void createResult() {
        final ItemStack bly2 = this.inputSlots.getItem(0);
        this.cost.set(1);
        int integer3 = 0;
        int integer4 = 0;
        int integer5 = 0;
        if (bly2.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }
        ItemStack bly3 = bly2.copy();
        final ItemStack bly4 = this.inputSlots.getItem(1);
        final Map<Enchantment, Integer> map8 = EnchantmentHelper.getEnchantments(bly3);
        integer4 += bly2.getBaseRepairCost() + (bly4.isEmpty() ? 0 : bly4.getBaseRepairCost());
        this.repairItemCountCost = 0;
        if (!bly4.isEmpty()) {
            final boolean boolean9 = bly4.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(bly4).isEmpty();
            if (bly3.isDamageableItem() && bly3.getItem().isValidRepairItem(bly2, bly4)) {
                int integer6 = Math.min(bly3.getDamageValue(), bly3.getMaxDamage() / 4);
                if (integer6 <= 0) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.cost.set(0);
                    return;
                }
                int integer7;
                for (integer7 = 0; integer6 > 0 && integer7 < bly4.getCount(); integer6 = Math.min(bly3.getDamageValue(), bly3.getMaxDamage() / 4), ++integer7) {
                    final int integer8 = bly3.getDamageValue() - integer6;
                    bly3.setDamageValue(integer8);
                    ++integer3;
                }
                this.repairItemCountCost = integer7;
            }
            else {
                if (!boolean9 && (bly3.getItem() != bly4.getItem() || !bly3.isDamageableItem())) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.cost.set(0);
                    return;
                }
                if (bly3.isDamageableItem() && !boolean9) {
                    final int integer6 = bly2.getMaxDamage() - bly2.getDamageValue();
                    final int integer7 = bly4.getMaxDamage() - bly4.getDamageValue();
                    final int integer8 = integer7 + bly3.getMaxDamage() * 12 / 100;
                    final int integer9 = integer6 + integer8;
                    int integer10 = bly3.getMaxDamage() - integer9;
                    if (integer10 < 0) {
                        integer10 = 0;
                    }
                    if (integer10 < bly3.getDamageValue()) {
                        bly3.setDamageValue(integer10);
                        integer3 += 2;
                    }
                }
                final Map<Enchantment, Integer> map9 = EnchantmentHelper.getEnchantments(bly4);
                boolean boolean10 = false;
                boolean boolean11 = false;
                for (final Enchantment bpp14 : map9.keySet()) {
                    if (bpp14 == null) {
                        continue;
                    }
                    final int integer11 = (int)map8.getOrDefault(bpp14, 0);
                    int integer12 = (int)map9.get(bpp14);
                    integer12 = ((integer11 == integer12) ? (integer12 + 1) : Math.max(integer12, integer11));
                    boolean boolean12 = bpp14.canEnchant(bly2);
                    if (this.player.abilities.instabuild || bly2.getItem() == Items.ENCHANTED_BOOK) {
                        boolean12 = true;
                    }
                    for (final Enchantment bpp15 : map8.keySet()) {
                        if (bpp15 != bpp14 && !bpp14.isCompatibleWith(bpp15)) {
                            boolean12 = false;
                            ++integer3;
                        }
                    }
                    if (!boolean12) {
                        boolean11 = true;
                    }
                    else {
                        boolean10 = true;
                        if (integer12 > bpp14.getMaxLevel()) {
                            integer12 = bpp14.getMaxLevel();
                        }
                        map8.put(bpp14, integer12);
                        int integer13 = 0;
                        switch (bpp14.getRarity()) {
                            case COMMON: {
                                integer13 = 1;
                                break;
                            }
                            case UNCOMMON: {
                                integer13 = 2;
                                break;
                            }
                            case RARE: {
                                integer13 = 4;
                                break;
                            }
                            case VERY_RARE: {
                                integer13 = 8;
                                break;
                            }
                        }
                        if (boolean9) {
                            integer13 = Math.max(1, integer13 / 2);
                        }
                        integer3 += integer13 * integer12;
                        if (bly2.getCount() <= 1) {
                            continue;
                        }
                        integer3 = 40;
                    }
                }
                if (boolean11 && !boolean10) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.cost.set(0);
                    return;
                }
            }
        }
        if (StringUtils.isBlank((CharSequence)this.itemName)) {
            if (bly2.hasCustomHoverName()) {
                integer5 = 1;
                integer3 += integer5;
                bly3.resetHoverName();
            }
        }
        else if (!this.itemName.equals(bly2.getHoverName().getString())) {
            integer5 = 1;
            integer3 += integer5;
            bly3.setHoverName(new TextComponent(this.itemName));
        }
        this.cost.set(integer4 + integer3);
        if (integer3 <= 0) {
            bly3 = ItemStack.EMPTY;
        }
        if (integer5 == integer3 && integer5 > 0 && this.cost.get() >= 40) {
            this.cost.set(39);
        }
        if (this.cost.get() >= 40 && !this.player.abilities.instabuild) {
            bly3 = ItemStack.EMPTY;
        }
        if (!bly3.isEmpty()) {
            int integer14 = bly3.getBaseRepairCost();
            if (!bly4.isEmpty() && integer14 < bly4.getBaseRepairCost()) {
                integer14 = bly4.getBaseRepairCost();
            }
            if (integer5 != integer3 || integer5 == 0) {
                integer14 = calculateIncreasedRepairCost(integer14);
            }
            bly3.setRepairCost(integer14);
            EnchantmentHelper.setEnchantments(map8, bly3);
        }
        this.resultSlots.setItem(0, bly3);
        this.broadcastChanges();
    }
    
    public static int calculateIncreasedRepairCost(final int integer) {
        return integer * 2 + 1;
    }
    
    public void setItemName(final String string) {
        this.itemName = string;
        if (this.getSlot(2).hasItem()) {
            final ItemStack bly3 = this.getSlot(2).getItem();
            if (StringUtils.isBlank((CharSequence)string)) {
                bly3.resetHoverName();
            }
            else {
                bly3.setHoverName(new TextComponent(this.itemName));
            }
        }
        this.createResult();
    }
    
    public int getCost() {
        return this.cost.get();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
