package net.minecraft.world.entity.ai.goal.target;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;

public class DefendVillageTargetGoal extends TargetGoal {
    private final IronGolem golem;
    private LivingEntity potentialTarget;
    private final TargetingConditions attackTargeting;
    
    public DefendVillageTargetGoal(final IronGolem baf) {
        super(baf, false, true);
        this.attackTargeting = new TargetingConditions().range(64.0);
        this.golem = baf;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.TARGET));
    }
    
    @Override
    public boolean canUse() {
        final AABB dcf2 = this.golem.getBoundingBox().inflate(10.0, 8.0, 10.0);
        final List<LivingEntity> list3 = this.golem.level.<LivingEntity>getNearbyEntities((java.lang.Class<? extends LivingEntity>)Villager.class, this.attackTargeting, (LivingEntity)this.golem, dcf2);
        final List<Player> list4 = this.golem.level.getNearbyPlayers(this.attackTargeting, this.golem, dcf2);
        for (final LivingEntity aqj6 : list3) {
            final Villager bfg7 = (Villager)aqj6;
            for (final Player bft9 : list4) {
                final int integer10 = bfg7.getPlayerReputation(bft9);
                if (integer10 <= -100) {
                    this.potentialTarget = bft9;
                }
            }
        }
        return this.potentialTarget != null && (!(this.potentialTarget instanceof Player) || (!this.potentialTarget.isSpectator() && !((Player)this.potentialTarget).isCreative()));
    }
    
    @Override
    public void start() {
        this.golem.setTarget(this.potentialTarget);
        super.start();
    }
}
