package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class DragonSittingScanningPhase extends AbstractDragonSittingPhase {
    private static final TargetingConditions CHARGE_TARGETING;
    private final TargetingConditions scanTargeting;
    private int scanningTime;
    
    public DragonSittingScanningPhase(final EnderDragon bbo) {
        super(bbo);
        this.scanTargeting = new TargetingConditions().range(20.0).selector((Predicate<LivingEntity>)(aqj -> Math.abs(aqj.getY() - bbo.getY()) <= 10.0));
    }
    
    @Override
    public void doServerTick() {
        ++this.scanningTime;
        LivingEntity aqj2 = this.dragon.level.getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (aqj2 != null) {
            if (this.scanningTime > 25) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_ATTACKING);
            }
            else {
                final Vec3 dck3 = new Vec3(aqj2.getX() - this.dragon.getX(), 0.0, aqj2.getZ() - this.dragon.getZ()).normalize();
                final Vec3 dck4 = new Vec3(Mth.sin(this.dragon.yRot * 0.017453292f), 0.0, -Mth.cos(this.dragon.yRot * 0.017453292f)).normalize();
                final float float5 = (float)dck4.dot(dck3);
                final float float6 = (float)(Math.acos((double)float5) * 57.2957763671875) + 0.5f;
                if (float6 < 0.0f || float6 > 10.0f) {
                    final double double7 = aqj2.getX() - this.dragon.head.getX();
                    final double double8 = aqj2.getZ() - this.dragon.head.getZ();
                    final double double9 = Mth.clamp(Mth.wrapDegrees(180.0 - Mth.atan2(double7, double8) * 57.2957763671875 - this.dragon.yRot), -100.0, 100.0);
                    final EnderDragon dragon = this.dragon;
                    dragon.yRotA *= 0.8f;
                    final float float8;
                    float float7 = float8 = Mth.sqrt(double7 * double7 + double8 * double8) + 1.0f;
                    if (float7 > 40.0f) {
                        float7 = 40.0f;
                    }
                    final EnderDragon dragon2 = this.dragon;
                    dragon2.yRotA += (float)(double9 * (0.7f / float7 / float8));
                    final EnderDragon dragon3 = this.dragon;
                    dragon3.yRot += this.dragon.yRotA;
                }
            }
        }
        else if (this.scanningTime >= 100) {
            aqj2 = this.dragon.level.getNearestPlayer(DragonSittingScanningPhase.CHARGE_TARGETING, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            if (aqj2 != null) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
                this.dragon.getPhaseManager().<DragonChargePlayerPhase>getPhase(EnderDragonPhase.CHARGING_PLAYER).setTarget(new Vec3(aqj2.getX(), aqj2.getY(), aqj2.getZ()));
            }
        }
    }
    
    @Override
    public void begin() {
        this.scanningTime = 0;
    }
    
    public EnderDragonPhase<DragonSittingScanningPhase> getPhase() {
        return EnderDragonPhase.SITTING_SCANNING;
    }
    
    static {
        CHARGE_TARGETING = new TargetingConditions().range(150.0);
    }
}
