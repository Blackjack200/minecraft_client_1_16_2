package net.minecraft.world.entity.boss.enderdragon.phases;

import java.util.Arrays;
import java.lang.reflect.Constructor;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class EnderDragonPhase<T extends DragonPhaseInstance> {
    private static EnderDragonPhase<?>[] phases;
    public static final EnderDragonPhase<DragonHoldingPatternPhase> HOLDING_PATTERN;
    public static final EnderDragonPhase<DragonStrafePlayerPhase> STRAFE_PLAYER;
    public static final EnderDragonPhase<DragonLandingApproachPhase> LANDING_APPROACH;
    public static final EnderDragonPhase<DragonLandingPhase> LANDING;
    public static final EnderDragonPhase<DragonTakeoffPhase> TAKEOFF;
    public static final EnderDragonPhase<DragonSittingFlamingPhase> SITTING_FLAMING;
    public static final EnderDragonPhase<DragonSittingScanningPhase> SITTING_SCANNING;
    public static final EnderDragonPhase<DragonSittingAttackingPhase> SITTING_ATTACKING;
    public static final EnderDragonPhase<DragonChargePlayerPhase> CHARGING_PLAYER;
    public static final EnderDragonPhase<DragonDeathPhase> DYING;
    public static final EnderDragonPhase<DragonHoverPhase> HOVERING;
    private final Class<? extends DragonPhaseInstance> instanceClass;
    private final int id;
    private final String name;
    
    private EnderDragonPhase(final int integer, final Class<? extends DragonPhaseInstance> class2, final String string) {
        this.id = integer;
        this.instanceClass = class2;
        this.name = string;
    }
    
    public DragonPhaseInstance createInstance(final EnderDragon bbo) {
        try {
            final Constructor<? extends DragonPhaseInstance> constructor3 = this.getConstructor();
            return (DragonPhaseInstance)constructor3.newInstance(new Object[] { bbo });
        }
        catch (Exception exception3) {
            throw new Error((Throwable)exception3);
        }
    }
    
    protected Constructor<? extends DragonPhaseInstance> getConstructor() throws NoSuchMethodException {
        return this.instanceClass.getConstructor(new Class[] { EnderDragon.class });
    }
    
    public int getId() {
        return this.id;
    }
    
    public String toString() {
        return this.name + " (#" + this.id + ")";
    }
    
    public static EnderDragonPhase<?> getById(final int integer) {
        if (integer < 0 || integer >= EnderDragonPhase.phases.length) {
            return EnderDragonPhase.HOLDING_PATTERN;
        }
        return EnderDragonPhase.phases[integer];
    }
    
    public static int getCount() {
        return EnderDragonPhase.phases.length;
    }
    
    private static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(final Class<T> class1, final String string) {
        final EnderDragonPhase<T> bce3 = new EnderDragonPhase<T>(EnderDragonPhase.phases.length, class1, string);
        EnderDragonPhase.phases = Arrays.copyOf((Object[])EnderDragonPhase.phases, EnderDragonPhase.phases.length + 1);
        return (EnderDragonPhase<T>)(EnderDragonPhase.phases[bce3.getId()] = bce3);
    }
    
    static {
        EnderDragonPhase.phases = new EnderDragonPhase[0];
        HOLDING_PATTERN = EnderDragonPhase.<DragonHoldingPatternPhase>create(DragonHoldingPatternPhase.class, "HoldingPattern");
        STRAFE_PLAYER = EnderDragonPhase.<DragonStrafePlayerPhase>create(DragonStrafePlayerPhase.class, "StrafePlayer");
        LANDING_APPROACH = EnderDragonPhase.<DragonLandingApproachPhase>create(DragonLandingApproachPhase.class, "LandingApproach");
        LANDING = EnderDragonPhase.<DragonLandingPhase>create(DragonLandingPhase.class, "Landing");
        TAKEOFF = EnderDragonPhase.<DragonTakeoffPhase>create(DragonTakeoffPhase.class, "Takeoff");
        SITTING_FLAMING = EnderDragonPhase.<DragonSittingFlamingPhase>create(DragonSittingFlamingPhase.class, "SittingFlaming");
        SITTING_SCANNING = EnderDragonPhase.<DragonSittingScanningPhase>create(DragonSittingScanningPhase.class, "SittingScanning");
        SITTING_ATTACKING = EnderDragonPhase.<DragonSittingAttackingPhase>create(DragonSittingAttackingPhase.class, "SittingAttacking");
        CHARGING_PLAYER = EnderDragonPhase.<DragonChargePlayerPhase>create(DragonChargePlayerPhase.class, "ChargingPlayer");
        DYING = EnderDragonPhase.<DragonDeathPhase>create(DragonDeathPhase.class, "Dying");
        HOVERING = EnderDragonPhase.<DragonHoverPhase>create(DragonHoverPhase.class, "Hover");
    }
}
