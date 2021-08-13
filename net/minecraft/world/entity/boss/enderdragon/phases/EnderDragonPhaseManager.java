package net.minecraft.world.entity.boss.enderdragon.phases;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.apache.logging.log4j.Logger;

public class EnderDragonPhaseManager {
    private static final Logger LOGGER;
    private final EnderDragon dragon;
    private final DragonPhaseInstance[] phases;
    private DragonPhaseInstance currentPhase;
    
    public EnderDragonPhaseManager(final EnderDragon bbo) {
        this.phases = new DragonPhaseInstance[EnderDragonPhase.getCount()];
        this.dragon = bbo;
        this.setPhase(EnderDragonPhase.HOVERING);
    }
    
    public void setPhase(final EnderDragonPhase<?> bce) {
        if (this.currentPhase != null && bce == this.currentPhase.getPhase()) {
            return;
        }
        if (this.currentPhase != null) {
            this.currentPhase.end();
        }
        this.currentPhase = this.<DragonPhaseInstance>getPhase(bce);
        if (!this.dragon.level.isClientSide) {
            this.dragon.getEntityData().<Integer>set(EnderDragon.DATA_PHASE, bce.getId());
        }
        EnderDragonPhaseManager.LOGGER.debug("Dragon is now in phase {} on the {}", bce, this.dragon.level.isClientSide ? "client" : "server");
        this.currentPhase.begin();
    }
    
    public DragonPhaseInstance getCurrentPhase() {
        return this.currentPhase;
    }
    
    public <T extends DragonPhaseInstance> T getPhase(final EnderDragonPhase<T> bce) {
        final int integer3 = bce.getId();
        if (this.phases[integer3] == null) {
            this.phases[integer3] = bce.createInstance(this.dragon);
        }
        return (T)this.phases[integer3];
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
