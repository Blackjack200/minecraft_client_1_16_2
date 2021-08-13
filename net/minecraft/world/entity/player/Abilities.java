package net.minecraft.world.entity.player;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;

public class Abilities {
    public boolean invulnerable;
    public boolean flying;
    public boolean mayfly;
    public boolean instabuild;
    public boolean mayBuild;
    private float flyingSpeed;
    private float walkingSpeed;
    
    public Abilities() {
        this.mayBuild = true;
        this.flyingSpeed = 0.05f;
        this.walkingSpeed = 0.1f;
    }
    
    public void addSaveData(final CompoundTag md) {
        final CompoundTag md2 = new CompoundTag();
        md2.putBoolean("invulnerable", this.invulnerable);
        md2.putBoolean("flying", this.flying);
        md2.putBoolean("mayfly", this.mayfly);
        md2.putBoolean("instabuild", this.instabuild);
        md2.putBoolean("mayBuild", this.mayBuild);
        md2.putFloat("flySpeed", this.flyingSpeed);
        md2.putFloat("walkSpeed", this.walkingSpeed);
        md.put("abilities", (Tag)md2);
    }
    
    public void loadSaveData(final CompoundTag md) {
        if (md.contains("abilities", 10)) {
            final CompoundTag md2 = md.getCompound("abilities");
            this.invulnerable = md2.getBoolean("invulnerable");
            this.flying = md2.getBoolean("flying");
            this.mayfly = md2.getBoolean("mayfly");
            this.instabuild = md2.getBoolean("instabuild");
            if (md2.contains("flySpeed", 99)) {
                this.flyingSpeed = md2.getFloat("flySpeed");
                this.walkingSpeed = md2.getFloat("walkSpeed");
            }
            if (md2.contains("mayBuild", 1)) {
                this.mayBuild = md2.getBoolean("mayBuild");
            }
        }
    }
    
    public float getFlyingSpeed() {
        return this.flyingSpeed;
    }
    
    public void setFlyingSpeed(final float float1) {
        this.flyingSpeed = float1;
    }
    
    public float getWalkingSpeed() {
        return this.walkingSpeed;
    }
    
    public void setWalkingSpeed(final float float1) {
        this.walkingSpeed = float1;
    }
}
