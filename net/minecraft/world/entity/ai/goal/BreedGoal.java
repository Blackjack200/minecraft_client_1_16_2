package net.minecraft.world.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class BreedGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING;
    protected final Animal animal;
    private final Class<? extends Animal> partnerClass;
    protected final Level level;
    protected Animal partner;
    private int loveTime;
    private final double speedModifier;
    
    public BreedGoal(final Animal azw, final double double2) {
        this(azw, double2, azw.getClass());
    }
    
    public BreedGoal(final Animal azw, final double double2, final Class<? extends Animal> class3) {
        this.animal = azw;
        this.level = azw.level;
        this.partnerClass = class3;
        this.speedModifier = double2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        }
        this.partner = this.getFreePartner();
        return this.partner != null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
    }
    
    @Override
    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }
    
    @Override
    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0f, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= 60 && this.animal.distanceToSqr(this.partner) < 9.0) {
            this.breed();
        }
    }
    
    @Nullable
    private Animal getFreePartner() {
        final List<Animal> list2 = this.level.<Animal>getNearbyEntities(this.partnerClass, BreedGoal.PARTNER_TARGETING, (LivingEntity)this.animal, this.animal.getBoundingBox().inflate(8.0));
        double double3 = Double.MAX_VALUE;
        Animal azw5 = null;
        for (final Animal azw6 : list2) {
            if (this.animal.canMate(azw6) && this.animal.distanceToSqr(azw6) < double3) {
                azw5 = azw6;
                double3 = this.animal.distanceToSqr(azw6);
            }
        }
        return azw5;
    }
    
    protected void breed() {
        this.animal.spawnChildFromBreeding((ServerLevel)this.level, this.partner);
    }
    
    static {
        PARTNER_TARGETING = new TargetingConditions().range(8.0).allowInvulnerable().allowSameTeam().allowUnseeable();
    }
}
