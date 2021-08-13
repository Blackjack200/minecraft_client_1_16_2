package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Position;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.Lists;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;

public class HarvestFarmland extends Behavior<Villager> {
    @Nullable
    private BlockPos aboveFarmlandPos;
    private long nextOkStartTime;
    private int timeWorkedSoFar;
    private final List<BlockPos> validFarmlandAroundVillager;
    
    public HarvestFarmland() {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
        this.validFarmlandAroundVillager = (List<BlockPos>)Lists.newArrayList();
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        if (!aag.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }
        if (bfg.getVillagerData().getProfession() != VillagerProfession.FARMER) {
            return false;
        }
        final BlockPos.MutableBlockPos a4 = bfg.blockPosition().mutable();
        this.validFarmlandAroundVillager.clear();
        for (int integer5 = -1; integer5 <= 1; ++integer5) {
            for (int integer6 = -1; integer6 <= 1; ++integer6) {
                for (int integer7 = -1; integer7 <= 1; ++integer7) {
                    a4.set(bfg.getX() + integer5, bfg.getY() + integer6, bfg.getZ() + integer7);
                    if (this.validPos(a4, aag)) {
                        this.validFarmlandAroundVillager.add(new BlockPos(a4));
                    }
                }
            }
        }
        this.aboveFarmlandPos = this.getValidFarmland(aag);
        return this.aboveFarmlandPos != null;
    }
    
    @Nullable
    private BlockPos getValidFarmland(final ServerLevel aag) {
        return this.validFarmlandAroundVillager.isEmpty() ? null : ((BlockPos)this.validFarmlandAroundVillager.get(aag.getRandom().nextInt(this.validFarmlandAroundVillager.size())));
    }
    
    private boolean validPos(final BlockPos fx, final ServerLevel aag) {
        final BlockState cee4 = aag.getBlockState(fx);
        final Block bul5 = cee4.getBlock();
        final Block bul6 = aag.getBlockState(fx.below()).getBlock();
        return (bul5 instanceof CropBlock && ((CropBlock)bul5).isMaxAge(cee4)) || (cee4.isAir() && bul6 instanceof FarmBlock);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        if (long3 > this.nextOkStartTime && this.aboveFarmlandPos != null) {
            bfg.getBrain().<BlockPosTracker>setMemory((MemoryModuleType<BlockPosTracker>)MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
            bfg.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5f, 1));
        }
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        bfg.getBrain().<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
        bfg.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        this.timeWorkedSoFar = 0;
        this.nextOkStartTime = long3 + 40L;
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        if (this.aboveFarmlandPos != null && !this.aboveFarmlandPos.closerThan(bfg.position(), 1.0)) {
            return;
        }
        if (this.aboveFarmlandPos != null && long3 > this.nextOkStartTime) {
            final BlockState cee6 = aag.getBlockState(this.aboveFarmlandPos);
            final Block bul7 = cee6.getBlock();
            final Block bul8 = aag.getBlockState(this.aboveFarmlandPos.below()).getBlock();
            if (bul7 instanceof CropBlock && ((CropBlock)bul7).isMaxAge(cee6)) {
                aag.destroyBlock(this.aboveFarmlandPos, true, bfg);
            }
            if (cee6.isAir() && bul8 instanceof FarmBlock && bfg.hasFarmSeeds()) {
                final SimpleContainer aox9 = bfg.getInventory();
                int integer10 = 0;
                while (integer10 < aox9.getContainerSize()) {
                    final ItemStack bly11 = aox9.getItem(integer10);
                    boolean boolean12 = false;
                    if (!bly11.isEmpty()) {
                        if (bly11.getItem() == Items.WHEAT_SEEDS) {
                            aag.setBlock(this.aboveFarmlandPos, Blocks.WHEAT.defaultBlockState(), 3);
                            boolean12 = true;
                        }
                        else if (bly11.getItem() == Items.POTATO) {
                            aag.setBlock(this.aboveFarmlandPos, Blocks.POTATOES.defaultBlockState(), 3);
                            boolean12 = true;
                        }
                        else if (bly11.getItem() == Items.CARROT) {
                            aag.setBlock(this.aboveFarmlandPos, Blocks.CARROTS.defaultBlockState(), 3);
                            boolean12 = true;
                        }
                        else if (bly11.getItem() == Items.BEETROOT_SEEDS) {
                            aag.setBlock(this.aboveFarmlandPos, Blocks.BEETROOTS.defaultBlockState(), 3);
                            boolean12 = true;
                        }
                    }
                    if (boolean12) {
                        aag.playSound(null, this.aboveFarmlandPos.getX(), this.aboveFarmlandPos.getY(), this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
                        bly11.shrink(1);
                        if (bly11.isEmpty()) {
                            aox9.setItem(integer10, ItemStack.EMPTY);
                            break;
                        }
                        break;
                    }
                    else {
                        ++integer10;
                    }
                }
            }
            if (bul7 instanceof CropBlock && !((CropBlock)bul7).isMaxAge(cee6)) {
                this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
                this.aboveFarmlandPos = this.getValidFarmland(aag);
                if (this.aboveFarmlandPos != null) {
                    this.nextOkStartTime = long3 + 20L;
                    bfg.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5f, 1));
                    bfg.getBrain().<BlockPosTracker>setMemory((MemoryModuleType<BlockPosTracker>)MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
                }
            }
        }
        ++this.timeWorkedSoFar;
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.timeWorkedSoFar < 200;
    }
}
