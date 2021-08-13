package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import java.util.List;

public class WorkAtComposter extends WorkAtPoi {
    private static final List<Item> COMPOSTABLE_ITEMS;
    
    @Override
    protected void useWorkstation(final ServerLevel aag, final Villager bfg) {
        final Optional<GlobalPos> optional4 = bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE);
        if (!optional4.isPresent()) {
            return;
        }
        final GlobalPos gf5 = (GlobalPos)optional4.get();
        final BlockState cee6 = aag.getBlockState(gf5.pos());
        if (cee6.is(Blocks.COMPOSTER)) {
            this.makeBread(bfg);
            this.compostItems(aag, bfg, gf5, cee6);
        }
    }
    
    private void compostItems(final ServerLevel aag, final Villager bfg, final GlobalPos gf, BlockState cee) {
        final BlockPos fx6 = gf.pos();
        if (cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL) == 8) {
            cee = ComposterBlock.extractProduce(cee, aag, fx6);
        }
        int integer7 = 20;
        final int integer8 = 10;
        final int[] arr9 = new int[WorkAtComposter.COMPOSTABLE_ITEMS.size()];
        final SimpleContainer aox10 = bfg.getInventory();
        final int integer9 = aox10.getContainerSize();
        BlockState cee2 = cee;
        for (int integer10 = integer9 - 1; integer10 >= 0 && integer7 > 0; --integer10) {
            final ItemStack bly14 = aox10.getItem(integer10);
            final int integer11 = WorkAtComposter.COMPOSTABLE_ITEMS.indexOf(bly14.getItem());
            if (integer11 != -1) {
                final int integer12 = bly14.getCount();
                final int integer13 = arr9[integer11] + integer12;
                arr9[integer11] = integer13;
                final int integer14 = Math.min(Math.min(integer13 - 10, integer7), integer12);
                if (integer14 > 0) {
                    integer7 -= integer14;
                    for (int integer15 = 0; integer15 < integer14; ++integer15) {
                        cee2 = ComposterBlock.insertItem(cee2, aag, bly14, fx6);
                        if (cee2.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL) == 7) {
                            this.spawnComposterFillEffects(aag, cee, fx6, cee2);
                            return;
                        }
                    }
                }
            }
        }
        this.spawnComposterFillEffects(aag, cee, fx6, cee2);
    }
    
    private void spawnComposterFillEffects(final ServerLevel aag, final BlockState cee2, final BlockPos fx, final BlockState cee4) {
        aag.levelEvent(1500, fx, (cee4 != cee2) ? 1 : 0);
    }
    
    private void makeBread(final Villager bfg) {
        final SimpleContainer aox3 = bfg.getInventory();
        if (aox3.countItem(Items.BREAD) > 36) {
            return;
        }
        final int integer4 = aox3.countItem(Items.WHEAT);
        final int integer5 = 3;
        final int integer6 = 3;
        final int integer7 = Math.min(3, integer4 / 3);
        if (integer7 == 0) {
            return;
        }
        final int integer8 = integer7 * 3;
        aox3.removeItemType(Items.WHEAT, integer8);
        final ItemStack bly9 = aox3.addItem(new ItemStack(Items.BREAD, integer7));
        if (!bly9.isEmpty()) {
            bfg.spawnAtLocation(bly9, 0.5f);
        }
    }
    
    static {
        COMPOSTABLE_ITEMS = (List)ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
    }
}
