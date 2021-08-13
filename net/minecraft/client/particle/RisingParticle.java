package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public abstract class RisingParticle extends TextureSheetParticle {
    protected RisingParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        this.xd = this.xd * 0.009999999776482582 + double5;
        this.yd = this.yd * 0.009999999776482582 + double6;
        this.zd = this.zd * 0.009999999776482582 + double7;
        this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05f;
        this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05f;
        this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05f;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9599999785423279;
        this.yd *= 0.9599999785423279;
        this.zd *= 0.9599999785423279;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
}
