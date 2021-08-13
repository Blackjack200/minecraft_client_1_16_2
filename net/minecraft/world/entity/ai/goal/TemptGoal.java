package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import java.util.EnumSet;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class TemptGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING;
    protected final PathfinderMob mob;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    protected Player player;
    private int calmDown;
    private boolean isRunning;
    private final Ingredient items;
    private final boolean canScare;
    
    public TemptGoal(final PathfinderMob aqr, final double double2, final Ingredient bok, final boolean boolean4) {
        this(aqr, double2, boolean4, bok);
    }
    
    public TemptGoal(final PathfinderMob aqr, final double double2, final boolean boolean3, final Ingredient bok) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.items = bok;
        this.canScare = boolean3;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        if (!(aqr.getNavigation() instanceof GroundPathNavigation) && !(aqr.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }
    
    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        }
        this.player = this.mob.level.getNearestPlayer(TemptGoal.TEMP_TARGETING, this.mob);
        return this.player != null && (this.shouldFollowItem(this.player.getMainHandItem()) || this.shouldFollowItem(this.player.getOffhandItem()));
    }
    
    protected boolean shouldFollowItem(final ItemStack bly) {
        return this.items.test(bly);
    }
    
    @Override
    public boolean canContinueToUse() {
        if (this.canScare()) {
            if (this.mob.distanceToSqr(this.player) < 36.0) {
                if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002) {
                    return false;
                }
                if (Math.abs(this.player.xRot - this.pRotX) > 5.0 || Math.abs(this.player.yRot - this.pRotY) > 5.0) {
                    return false;
                }
            }
            else {
                this.px = this.player.getX();
                this.py = this.player.getY();
                this.pz = this.player.getZ();
            }
            this.pRotX = this.player.xRot;
            this.pRotY = this.player.yRot;
        }
        return this.canUse();
    }
    
    protected boolean canScare() {
        return this.canScare;
    }
    
    @Override
    public void start() {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
        this.isRunning = true;
    }
    
    @Override
    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
        this.calmDown = 100;
        this.isRunning = false;
    }
    
    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 6.25) {
            this.mob.getNavigation().stop();
        }
        else {
            this.mob.getNavigation().moveTo(this.player, this.speedModifier);
        }
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    static {
        TEMP_TARGETING = new TargetingConditions().range(10.0).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
    }
}
