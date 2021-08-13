package net.minecraft.world.phys;

import net.minecraft.world.entity.Entity;

public class EntityHitResult extends HitResult {
    private final Entity entity;
    
    public EntityHitResult(final Entity apx) {
        this(apx, apx.position());
    }
    
    public EntityHitResult(final Entity apx, final Vec3 dck) {
        super(dck);
        this.entity = apx;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    @Override
    public Type getType() {
        return Type.ENTITY;
    }
}
