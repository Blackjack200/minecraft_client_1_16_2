package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.core.Position;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.world.entity.npc.Villager;

public class UseBonemeal extends Behavior<Villager> {
    private long nextWorkCycleTime;
    private long lastBonemealingSession;
    private int timeWorkedSoFar;
    private Optional<BlockPos> cropPos;
    
    public UseBonemeal() {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.cropPos = (Optional<BlockPos>)Optional.empty();
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        if (bfg.tickCount % 10 != 0 || (this.lastBonemealingSession != 0L && this.lastBonemealingSession + 160L > bfg.tickCount)) {
            return false;
        }
        if (bfg.getInventory().countItem(Items.BONE_MEAL) <= 0) {
            return false;
        }
        this.cropPos = this.pickNextTarget(aag, bfg);
        return this.cropPos.isPresent();
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.timeWorkedSoFar < 80 && this.cropPos.isPresent();
    }
    
    private Optional<BlockPos> pickNextTarget(final ServerLevel aag, final Villager bfg) {
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        Optional<BlockPos> optional5 = (Optional<BlockPos>)Optional.empty();
        int integer6 = 0;
        for (int integer7 = -1; integer7 <= 1; ++integer7) {
            for (int integer8 = -1; integer8 <= 1; ++integer8) {
                for (int integer9 = -1; integer9 <= 1; ++integer9) {
                    a4.setWithOffset(bfg.blockPosition(), integer7, integer8, integer9);
                    if (this.validPos(a4, aag) && aag.random.nextInt(++integer6) == 0) {
                        optional5 = (Optional<BlockPos>)Optional.of(a4.immutable());
                    }
                }
            }
        }
        return optional5;
    }
    
    private boolean validPos(final BlockPos fx, final ServerLevel aag) {
        final BlockState cee4 = aag.getBlockState(fx);
        final Block bul5 = cee4.getBlock();
        return bul5 instanceof CropBlock && !((CropBlock)bul5).isMaxAge(cee4);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        this.setCurrentCropAsTarget(bfg);
        bfg.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
        this.nextWorkCycleTime = long3;
        this.timeWorkedSoFar = 0;
    }
    
    private void setCurrentCropAsTarget(final Villager bfg) {
        this.cropPos.ifPresent(fx -> {
            final BlockPosTracker aru3 = new BlockPosTracker(fx);
            bfg.getBrain().<PositionTracker>setMemory(MemoryModuleType.LOOK_TARGET, aru3);
            bfg.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(aru3, 0.5f, 1));
        });
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        bfg.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.lastBonemealingSession = bfg.tickCount;
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        final BlockPos fx6 = (BlockPos)this.cropPos.get();
        if (long3 < this.nextWorkCycleTime || !fx6.closerThan(bfg.position(), 1.0)) {
            return;
        }
        ItemStack bly7 = ItemStack.EMPTY;
        final SimpleContainer aox8 = bfg.getInventory();
        for (int integer9 = aox8.getContainerSize(), integer10 = 0; integer10 < integer9; ++integer10) {
            final ItemStack bly8 = aox8.getItem(integer10);
            if (bly8.getItem() == Items.BONE_MEAL) {
                bly7 = bly8;
                break;
            }
        }
        if (!bly7.isEmpty() && BoneMealItem.growCrop(bly7, aag, fx6)) {
            aag.levelEvent(2005, fx6, 0);
            this.cropPos = this.pickNextTarget(aag, bfg);
            this.setCurrentCropAsTarget(bfg);
            this.nextWorkCycleTime = long3 + 40L;
        }
        ++this.timeWorkedSoFar;
    }
}
