package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.Position;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.Difficulty;
import java.util.function.Predicate;

public class BreakDoorGoal extends DoorInteractGoal {
    private final Predicate<Difficulty> validDifficulties;
    protected int breakTime;
    protected int lastBreakProgress;
    protected int doorBreakTime;
    
    public BreakDoorGoal(final Mob aqk, final Predicate<Difficulty> predicate) {
        super(aqk);
        this.lastBreakProgress = -1;
        this.doorBreakTime = -1;
        this.validDifficulties = predicate;
    }
    
    public BreakDoorGoal(final Mob aqk, final int integer, final Predicate<Difficulty> predicate) {
        this(aqk, predicate);
        this.doorBreakTime = integer;
    }
    
    protected int getDoorBreakTime() {
        return Math.max(240, this.doorBreakTime);
    }
    
    @Override
    public boolean canUse() {
        return super.canUse() && this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.isValidDifficulty(this.mob.level.getDifficulty()) && !this.isOpen();
    }
    
    @Override
    public void start() {
        super.start();
        this.breakTime = 0;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.breakTime <= this.getDoorBreakTime() && !this.isOpen() && this.doorPos.closerThan(this.mob.position(), 2.0) && this.isValidDifficulty(this.mob.level.getDifficulty());
    }
    
    @Override
    public void stop() {
        super.stop();
        this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.mob.getRandom().nextInt(20) == 0) {
            this.mob.level.levelEvent(1019, this.doorPos, 0);
            if (!this.mob.swinging) {
                this.mob.swing(this.mob.getUsedItemHand());
            }
        }
        ++this.breakTime;
        final int integer2 = (int)(this.breakTime / (float)this.getDoorBreakTime() * 10.0f);
        if (integer2 != this.lastBreakProgress) {
            this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, integer2);
            this.lastBreakProgress = integer2;
        }
        if (this.breakTime == this.getDoorBreakTime() && this.isValidDifficulty(this.mob.level.getDifficulty())) {
            this.mob.level.removeBlock(this.doorPos, false);
            this.mob.level.levelEvent(1021, this.doorPos, 0);
            this.mob.level.levelEvent(2001, this.doorPos, Block.getId(this.mob.level.getBlockState(this.doorPos)));
        }
    }
    
    private boolean isValidDifficulty(final Difficulty aoo) {
        return this.validDifficulties.test(aoo);
    }
}
