package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Cat;

public class CatSitOnBlockGoal extends MoveToBlockGoal {
    private final Cat cat;
    
    public CatSitOnBlockGoal(final Cat azy, final double double2) {
        super(azy, double2, 8);
        this.cat = azy;
    }
    
    @Override
    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse();
    }
    
    @Override
    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }
    
    @Override
    public void stop() {
        super.stop();
        this.cat.setInSittingPose(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        this.cat.setInSittingPose(this.isReachedTarget());
    }
    
    @Override
    protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
        if (!brw.isEmptyBlock(fx.above())) {
            return false;
        }
        final BlockState cee4 = brw.getBlockState(fx);
        if (cee4.is(Blocks.CHEST)) {
            return ChestBlockEntity.getOpenCount(brw, fx) < 1;
        }
        return (cee4.is(Blocks.FURNACE) && cee4.<Boolean>getValue((Property<Boolean>)FurnaceBlock.LIT)) || cee4.is(BlockTags.BEDS, (Predicate<BlockBehaviour.BlockStateBase>)(a -> (boolean)a.<BedPart>getOptionalValue(BedBlock.PART).map(ces -> ces != BedPart.HEAD).orElse(true)));
    }
}
