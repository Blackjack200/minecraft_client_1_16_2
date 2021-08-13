package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.MobType;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Silverfish extends Monster {
    private SilverfishWakeUpFriendsGoal friendsGoal;
    
    public Silverfish(final EntityType<? extends Silverfish> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        this.friendsGoal = new SilverfishWakeUpFriendsGoal(this);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, this.friendsGoal);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new SilverfishMergeWithStoneGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    public double getMyRidingOffset() {
        return 0.1;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.13f;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 1.0);
    }
    
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SILVERFISH_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if ((aph instanceof EntityDamageSource || aph == DamageSource.MAGIC) && this.friendsGoal != null) {
            this.friendsGoal.notifyHurt();
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    public void tick() {
        this.yBodyRot = this.yRot;
        super.tick();
    }
    
    public void setYBodyRot(final float float1) {
        super.setYBodyRot(this.yRot = float1);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (InfestedBlock.isCompatibleHostBlock(brw.getBlockState(fx.below()))) {
            return 10.0f;
        }
        return super.getWalkTargetValue(fx, brw);
    }
    
    public static boolean checkSliverfishSpawnRules(final EntityType<Silverfish> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        if (Monster.checkAnyLightMonsterSpawnRules(aqb, brv, aqm, fx, random)) {
            final Player bft6 = brv.getNearestPlayer(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 5.0, true);
            return bft6 == null;
        }
        return false;
    }
    
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
    
    static class SilverfishWakeUpFriendsGoal extends Goal {
        private final Silverfish silverfish;
        private int lookForFriends;
        
        public SilverfishWakeUpFriendsGoal(final Silverfish bdu) {
            this.silverfish = bdu;
        }
        
        public void notifyHurt() {
            if (this.lookForFriends == 0) {
                this.lookForFriends = 20;
            }
        }
        
        @Override
        public boolean canUse() {
            return this.lookForFriends > 0;
        }
        
        @Override
        public void tick() {
            --this.lookForFriends;
            Label_0237: {
                if (this.lookForFriends <= 0) {
                    final Level bru2 = this.silverfish.level;
                    final Random random3 = this.silverfish.getRandom();
                    final BlockPos fx4 = this.silverfish.blockPosition();
                    for (int integer5 = 0; integer5 <= 5 && integer5 >= -5; integer5 = ((integer5 <= 0) ? 1 : 0) - integer5) {
                        for (int integer6 = 0; integer6 <= 10 && integer6 >= -10; integer6 = ((integer6 <= 0) ? 1 : 0) - integer6) {
                            for (int integer7 = 0; integer7 <= 10 && integer7 >= -10; integer7 = ((integer7 <= 0) ? 1 : 0) - integer7) {
                                final BlockPos fx5 = fx4.offset(integer6, integer5, integer7);
                                final BlockState cee9 = bru2.getBlockState(fx5);
                                final Block bul10 = cee9.getBlock();
                                if (bul10 instanceof InfestedBlock) {
                                    if (bru2.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                                        bru2.destroyBlock(fx5, true, this.silverfish);
                                    }
                                    else {
                                        bru2.setBlock(fx5, ((InfestedBlock)bul10).getHostBlock().defaultBlockState(), 3);
                                    }
                                    if (random3.nextBoolean()) {
                                        break Label_0237;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static class SilverfishMergeWithStoneGoal extends RandomStrollGoal {
        private Direction selectedDirection;
        private boolean doMerge;
        
        public SilverfishMergeWithStoneGoal(final Silverfish bdu) {
            super(bdu, 1.0, 10);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            }
            if (!this.mob.getNavigation().isDone()) {
                return false;
            }
            final Random random2 = this.mob.getRandom();
            if (this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && random2.nextInt(10) == 0) {
                this.selectedDirection = Direction.getRandom(random2);
                final BlockPos fx3 = new BlockPos(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()).relative(this.selectedDirection);
                final BlockState cee4 = this.mob.level.getBlockState(fx3);
                if (InfestedBlock.isCompatibleHostBlock(cee4)) {
                    return this.doMerge = true;
                }
            }
            this.doMerge = false;
            return super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.doMerge && super.canContinueToUse();
        }
        
        @Override
        public void start() {
            if (!this.doMerge) {
                super.start();
                return;
            }
            final LevelAccessor brv2 = this.mob.level;
            final BlockPos fx3 = new BlockPos(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()).relative(this.selectedDirection);
            final BlockState cee4 = brv2.getBlockState(fx3);
            if (InfestedBlock.isCompatibleHostBlock(cee4)) {
                brv2.setBlock(fx3, InfestedBlock.stateByHostBlock(cee4.getBlock()), 3);
                this.mob.spawnAnim();
                this.mob.remove();
            }
        }
    }
}
