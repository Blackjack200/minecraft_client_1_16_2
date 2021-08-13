package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Cat;

public class CatLieOnBedGoal extends MoveToBlockGoal {
    private final Cat cat;
    
    public CatLieOnBedGoal(final Cat azy, final double double2, final int integer) {
        super(azy, double2, integer, 6);
        this.cat = azy;
        this.verticalSearchStart = -2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && !this.cat.isLying() && super.canUse();
    }
    
    @Override
    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }
    
    @Override
    protected int nextStartTick(final PathfinderMob aqr) {
        return 40;
    }
    
    @Override
    public void stop() {
        super.stop();
        this.cat.setLying(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        this.cat.setInSittingPose(false);
        if (!this.isReachedTarget()) {
            this.cat.setLying(false);
        }
        else if (!this.cat.isLying()) {
            this.cat.setLying(true);
        }
    }
    
    @Override
    protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
        return brw.isEmptyBlock(fx.above()) && brw.getBlockState(fx).getBlock().is(BlockTags.BEDS);
    }
}
