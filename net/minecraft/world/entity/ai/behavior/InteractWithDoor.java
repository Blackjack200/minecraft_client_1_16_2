package net.minecraft.world.entity.ai.behavior;

import java.util.HashSet;
import com.google.common.collect.Sets;
import net.minecraft.core.Position;
import java.util.List;
import java.util.Iterator;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.GlobalPos;
import java.util.Set;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.Objects;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.entity.LivingEntity;

public class InteractWithDoor extends Behavior<LivingEntity> {
    @Nullable
    private Node lastCheckedNode;
    private int remainingCooldown;
    
    public InteractWithDoor() {
        super((Map)ImmutableMap.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT, MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final Path cxa4 = (Path)aqj.getBrain().<Path>getMemory(MemoryModuleType.PATH).get();
        if (cxa4.notStarted() || cxa4.isDone()) {
            return false;
        }
        if (!Objects.equals(this.lastCheckedNode, cxa4.getNextNode())) {
            this.remainingCooldown = 20;
            return true;
        }
        if (this.remainingCooldown > 0) {
            --this.remainingCooldown;
        }
        return this.remainingCooldown == 0;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Path cxa6 = (Path)aqj.getBrain().<Path>getMemory(MemoryModuleType.PATH).get();
        this.lastCheckedNode = cxa6.getNextNode();
        final Node cwy7 = cxa6.getPreviousNode();
        final Node cwy8 = cxa6.getNextNode();
        final BlockPos fx9 = cwy7.asBlockPos();
        final BlockState cee10 = aag.getBlockState(fx9);
        if (cee10.is(BlockTags.WOODEN_DOORS)) {
            final DoorBlock bvy11 = (DoorBlock)cee10.getBlock();
            if (!bvy11.isOpen(cee10)) {
                bvy11.setOpen(aag, cee10, fx9, true);
            }
            this.rememberDoorToClose(aag, aqj, fx9);
        }
        final BlockPos fx10 = cwy8.asBlockPos();
        final BlockState cee11 = aag.getBlockState(fx10);
        if (cee11.is(BlockTags.WOODEN_DOORS)) {
            final DoorBlock bvy12 = (DoorBlock)cee11.getBlock();
            if (!bvy12.isOpen(cee11)) {
                bvy12.setOpen(aag, cee11, fx10, true);
                this.rememberDoorToClose(aag, aqj, fx10);
            }
        }
        closeDoorsThatIHaveOpenedOrPassedThrough(aag, aqj, cwy7, cwy8);
    }
    
    public static void closeDoorsThatIHaveOpenedOrPassedThrough(final ServerLevel aag, final LivingEntity aqj, @Nullable final Node cwy3, @Nullable final Node cwy4) {
        final Brain<?> arc5 = aqj.getBrain();
        if (arc5.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
            final Iterator<GlobalPos> iterator6 = (Iterator<GlobalPos>)((Set)arc5.<Set<GlobalPos>>getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).iterator();
            while (iterator6.hasNext()) {
                final GlobalPos gf7 = (GlobalPos)iterator6.next();
                final BlockPos fx8 = gf7.pos();
                if (cwy3 != null && cwy3.asBlockPos().equals(fx8)) {
                    continue;
                }
                if (cwy4 != null && cwy4.asBlockPos().equals(fx8)) {
                    continue;
                }
                if (isDoorTooFarAway(aag, aqj, gf7)) {
                    iterator6.remove();
                }
                else {
                    final BlockState cee9 = aag.getBlockState(fx8);
                    if (!cee9.is(BlockTags.WOODEN_DOORS)) {
                        iterator6.remove();
                    }
                    else {
                        final DoorBlock bvy10 = (DoorBlock)cee9.getBlock();
                        if (!bvy10.isOpen(cee9)) {
                            iterator6.remove();
                        }
                        else if (areOtherMobsComingThroughDoor(aag, aqj, fx8)) {
                            iterator6.remove();
                        }
                        else {
                            bvy10.setOpen(aag, cee9, fx8, false);
                            iterator6.remove();
                        }
                    }
                }
            }
        }
    }
    
    private static boolean areOtherMobsComingThroughDoor(final ServerLevel aag, final LivingEntity aqj, final BlockPos fx) {
        final Brain<?> arc4 = aqj.getBrain();
        return arc4.hasMemoryValue(MemoryModuleType.LIVING_ENTITIES) && ((List)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.LIVING_ENTITIES).get()).stream().filter(aqj2 -> aqj2.getType() == aqj.getType()).filter(aqj -> fx.closerThan(aqj.position(), 2.0)).anyMatch(aqj -> isMobComingThroughDoor(aag, aqj, fx));
    }
    
    private static boolean isMobComingThroughDoor(final ServerLevel aag, final LivingEntity aqj, final BlockPos fx) {
        if (!aqj.getBrain().hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        }
        final Path cxa4 = (Path)aqj.getBrain().<Path>getMemory(MemoryModuleType.PATH).get();
        if (cxa4.isDone()) {
            return false;
        }
        final Node cwy5 = cxa4.getPreviousNode();
        if (cwy5 == null) {
            return false;
        }
        final Node cwy6 = cxa4.getNextNode();
        return fx.equals(cwy5.asBlockPos()) || fx.equals(cwy6.asBlockPos());
    }
    
    private static boolean isDoorTooFarAway(final ServerLevel aag, final LivingEntity aqj, final GlobalPos gf) {
        return gf.dimension() != aag.dimension() || !gf.pos().closerThan(aqj.position(), 2.0);
    }
    
    private void rememberDoorToClose(final ServerLevel aag, final LivingEntity aqj, final BlockPos fx) {
        final Brain<?> arc5 = aqj.getBrain();
        final GlobalPos gf6 = GlobalPos.of(aag.dimension(), fx);
        if (arc5.<Set<GlobalPos>>getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
            ((Set)arc5.<Set<GlobalPos>>getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).add(gf6);
        }
        else {
            arc5.<HashSet>setMemory((MemoryModuleType<HashSet>)MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet((Object[])new GlobalPos[] { gf6 }));
        }
    }
}
