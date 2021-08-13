package net.minecraft.world.effect;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.ComparisonChain;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class MobEffectInstance implements Comparable<MobEffectInstance> {
    private static final Logger LOGGER;
    private final MobEffect effect;
    private int duration;
    private int amplifier;
    private boolean splash;
    private boolean ambient;
    private boolean noCounter;
    private boolean visible;
    private boolean showIcon;
    @Nullable
    private MobEffectInstance hiddenEffect;
    
    public MobEffectInstance(final MobEffect app) {
        this(app, 0, 0);
    }
    
    public MobEffectInstance(final MobEffect app, final int integer) {
        this(app, integer, 0);
    }
    
    public MobEffectInstance(final MobEffect app, final int integer2, final int integer3) {
        this(app, integer2, integer3, false, true);
    }
    
    public MobEffectInstance(final MobEffect app, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5) {
        this(app, integer2, integer3, boolean4, boolean5, boolean5);
    }
    
    public MobEffectInstance(final MobEffect app, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        this(app, integer2, integer3, boolean4, boolean5, boolean6, null);
    }
    
    public MobEffectInstance(final MobEffect app, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5, final boolean boolean6, @Nullable final MobEffectInstance apr) {
        this.effect = app;
        this.duration = integer2;
        this.amplifier = integer3;
        this.ambient = boolean4;
        this.visible = boolean5;
        this.showIcon = boolean6;
        this.hiddenEffect = apr;
    }
    
    public MobEffectInstance(final MobEffectInstance apr) {
        this.effect = apr.effect;
        this.setDetailsFrom(apr);
    }
    
    void setDetailsFrom(final MobEffectInstance apr) {
        this.duration = apr.duration;
        this.amplifier = apr.amplifier;
        this.ambient = apr.ambient;
        this.visible = apr.visible;
        this.showIcon = apr.showIcon;
    }
    
    public boolean update(final MobEffectInstance apr) {
        if (this.effect != apr.effect) {
            MobEffectInstance.LOGGER.warn("This method should only be called for matching effects!");
        }
        boolean boolean3 = false;
        if (apr.amplifier > this.amplifier) {
            if (apr.duration < this.duration) {
                final MobEffectInstance apr2 = this.hiddenEffect;
                this.hiddenEffect = new MobEffectInstance(this);
                this.hiddenEffect.hiddenEffect = apr2;
            }
            this.amplifier = apr.amplifier;
            this.duration = apr.duration;
            boolean3 = true;
        }
        else if (apr.duration > this.duration) {
            if (apr.amplifier == this.amplifier) {
                this.duration = apr.duration;
                boolean3 = true;
            }
            else if (this.hiddenEffect == null) {
                this.hiddenEffect = new MobEffectInstance(apr);
            }
            else {
                this.hiddenEffect.update(apr);
            }
        }
        if ((!apr.ambient && this.ambient) || boolean3) {
            this.ambient = apr.ambient;
            boolean3 = true;
        }
        if (apr.visible != this.visible) {
            this.visible = apr.visible;
            boolean3 = true;
        }
        if (apr.showIcon != this.showIcon) {
            this.showIcon = apr.showIcon;
            boolean3 = true;
        }
        return boolean3;
    }
    
    public MobEffect getEffect() {
        return this.effect;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public int getAmplifier() {
        return this.amplifier;
    }
    
    public boolean isAmbient() {
        return this.ambient;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public boolean showIcon() {
        return this.showIcon;
    }
    
    public boolean tick(final LivingEntity aqj, final Runnable runnable) {
        if (this.duration > 0) {
            if (this.effect.isDurationEffectTick(this.duration, this.amplifier)) {
                this.applyEffect(aqj);
            }
            this.tickDownDuration();
            if (this.duration == 0 && this.hiddenEffect != null) {
                this.setDetailsFrom(this.hiddenEffect);
                this.hiddenEffect = this.hiddenEffect.hiddenEffect;
                runnable.run();
            }
        }
        return this.duration > 0;
    }
    
    private int tickDownDuration() {
        if (this.hiddenEffect != null) {
            this.hiddenEffect.tickDownDuration();
        }
        return --this.duration;
    }
    
    public void applyEffect(final LivingEntity aqj) {
        if (this.duration > 0) {
            this.effect.applyEffectTick(aqj, this.amplifier);
        }
    }
    
    public String getDescriptionId() {
        return this.effect.getDescriptionId();
    }
    
    public String toString() {
        String string2;
        if (this.amplifier > 0) {
            string2 = this.getDescriptionId() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
        }
        else {
            string2 = this.getDescriptionId() + ", Duration: " + this.duration;
        }
        if (this.splash) {
            string2 += ", Splash: true";
        }
        if (!this.visible) {
            string2 += ", Particles: false";
        }
        if (!this.showIcon) {
            string2 += ", Show Icon: false";
        }
        return string2;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof MobEffectInstance) {
            final MobEffectInstance apr3 = (MobEffectInstance)object;
            return this.duration == apr3.duration && this.amplifier == apr3.amplifier && this.splash == apr3.splash && this.ambient == apr3.ambient && this.effect.equals(apr3.effect);
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = this.effect.hashCode();
        integer2 = 31 * integer2 + this.duration;
        integer2 = 31 * integer2 + this.amplifier;
        integer2 = 31 * integer2 + (this.splash ? 1 : 0);
        integer2 = 31 * integer2 + (this.ambient ? 1 : 0);
        return integer2;
    }
    
    public CompoundTag save(final CompoundTag md) {
        md.putByte("Id", (byte)MobEffect.getId(this.getEffect()));
        this.writeDetailsTo(md);
        return md;
    }
    
    private void writeDetailsTo(final CompoundTag md) {
        md.putByte("Amplifier", (byte)this.getAmplifier());
        md.putInt("Duration", this.getDuration());
        md.putBoolean("Ambient", this.isAmbient());
        md.putBoolean("ShowParticles", this.isVisible());
        md.putBoolean("ShowIcon", this.showIcon());
        if (this.hiddenEffect != null) {
            final CompoundTag md2 = new CompoundTag();
            this.hiddenEffect.save(md2);
            md.put("HiddenEffect", (Tag)md2);
        }
    }
    
    public static MobEffectInstance load(final CompoundTag md) {
        final int integer2 = md.getByte("Id");
        final MobEffect app3 = MobEffect.byId(integer2);
        if (app3 == null) {
            return null;
        }
        return loadSpecifiedEffect(app3, md);
    }
    
    private static MobEffectInstance loadSpecifiedEffect(final MobEffect app, final CompoundTag md) {
        final int integer3 = md.getByte("Amplifier");
        final int integer4 = md.getInt("Duration");
        final boolean boolean5 = md.getBoolean("Ambient");
        boolean boolean6 = true;
        if (md.contains("ShowParticles", 1)) {
            boolean6 = md.getBoolean("ShowParticles");
        }
        boolean boolean7 = boolean6;
        if (md.contains("ShowIcon", 1)) {
            boolean7 = md.getBoolean("ShowIcon");
        }
        MobEffectInstance apr8 = null;
        if (md.contains("HiddenEffect", 10)) {
            apr8 = loadSpecifiedEffect(app, md.getCompound("HiddenEffect"));
        }
        return new MobEffectInstance(app, integer4, (integer3 < 0) ? 0 : integer3, boolean5, boolean6, boolean7, apr8);
    }
    
    public void setNoCounter(final boolean boolean1) {
        this.noCounter = boolean1;
    }
    
    public boolean isNoCounter() {
        return this.noCounter;
    }
    
    public int compareTo(final MobEffectInstance apr) {
        final int integer3 = 32147;
        if ((this.getDuration() > 32147 && apr.getDuration() > 32147) || (this.isAmbient() && apr.isAmbient())) {
            return ComparisonChain.start().compare(Boolean.valueOf(this.isAmbient()), Boolean.valueOf(apr.isAmbient())).compare(this.getEffect().getColor(), apr.getEffect().getColor()).result();
        }
        return ComparisonChain.start().compare(Boolean.valueOf(this.isAmbient()), Boolean.valueOf(apr.isAmbient())).compare(this.getDuration(), apr.getDuration()).compare(this.getEffect().getColor(), apr.getEffect().getColor()).result();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
