package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.Random;
import net.minecraft.world.entity.LivingEntity;

public abstract class Sensor<E extends LivingEntity> {
    private static final Random RANDOM;
    private static final TargetingConditions TARGET_CONDITIONS;
    private static final TargetingConditions TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING;
    private final int scanRate;
    private long timeToTick;
    
    public Sensor(final int integer) {
        this.scanRate = integer;
        this.timeToTick = Sensor.RANDOM.nextInt(integer);
    }
    
    public Sensor() {
        this(20);
    }
    
    public final void tick(final ServerLevel aag, final E aqj) {
        final long timeToTick = this.timeToTick - 1L;
        this.timeToTick = timeToTick;
        if (timeToTick <= 0L) {
            this.timeToTick = this.scanRate;
            this.doTick(aag, aqj);
        }
    }
    
    protected abstract void doTick(final ServerLevel aag, final E aqj);
    
    public abstract Set<MemoryModuleType<?>> requires();
    
    protected static boolean isEntityTargetable(final LivingEntity aqj1, final LivingEntity aqj2) {
        if (aqj1.getBrain().<LivingEntity>isMemoryValue(MemoryModuleType.ATTACK_TARGET, aqj2)) {
            return Sensor.TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(aqj1, aqj2);
        }
        return Sensor.TARGET_CONDITIONS.test(aqj1, aqj2);
    }
    
    static {
        RANDOM = new Random();
        TARGET_CONDITIONS = new TargetingConditions().range(16.0).allowSameTeam().allowNonAttackable();
        TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = new TargetingConditions().range(16.0).allowSameTeam().allowNonAttackable().ignoreInvisibilityTesting();
    }
}
