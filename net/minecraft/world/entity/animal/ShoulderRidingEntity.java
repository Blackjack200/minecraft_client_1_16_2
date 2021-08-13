package net.minecraft.world.entity.animal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;

public abstract class ShoulderRidingEntity extends TamableAnimal {
    private int rideCooldownCounter;
    
    protected ShoulderRidingEntity(final EntityType<? extends ShoulderRidingEntity> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public boolean setEntityOnShoulder(final ServerPlayer aah) {
        final CompoundTag md3 = new CompoundTag();
        md3.putString("id", this.getEncodeId());
        this.saveWithoutId(md3);
        if (aah.setEntityOnShoulder(md3)) {
            this.remove();
            return true;
        }
        return false;
    }
    
    @Override
    public void tick() {
        ++this.rideCooldownCounter;
        super.tick();
    }
    
    public boolean canSitOnShoulder() {
        return this.rideCooldownCounter > 100;
    }
}
