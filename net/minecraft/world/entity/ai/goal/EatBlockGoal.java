package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import java.util.EnumSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class EatBlockGoal extends Goal {
    private static final Predicate<BlockState> IS_TALL_GRASS;
    private final Mob mob;
    private final Level level;
    private int eatAnimationTick;
    
    public EatBlockGoal(final Mob aqk) {
        this.mob = aqk;
        this.level = aqk.level;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK, (Enum)Flag.JUMP));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            return false;
        }
        final BlockPos fx2 = this.mob.blockPosition();
        return EatBlockGoal.IS_TALL_GRASS.test(this.level.getBlockState(fx2)) || this.level.getBlockState(fx2.below()).is(Blocks.GRASS_BLOCK);
    }
    
    @Override
    public void start() {
        this.eatAnimationTick = 40;
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }
    
    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }
    
    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }
    
    @Override
    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick != 4) {
            return;
        }
        final BlockPos fx2 = this.mob.blockPosition();
        if (EatBlockGoal.IS_TALL_GRASS.test(this.level.getBlockState(fx2))) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                this.level.destroyBlock(fx2, false);
            }
            this.mob.ate();
        }
        else {
            final BlockPos fx3 = fx2.below();
            if (this.level.getBlockState(fx3).is(Blocks.GRASS_BLOCK)) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.levelEvent(2001, fx3, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                    this.level.setBlock(fx3, Blocks.DIRT.defaultBlockState(), 2);
                }
                this.mob.ate();
            }
        }
    }
    
    static {
        IS_TALL_GRASS = (Predicate)BlockStatePredicate.forBlock(Blocks.GRASS);
    }
}
