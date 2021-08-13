package net.minecraft.world.damagesource;

import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamageSource {
    public static final DamageSource IN_FIRE;
    public static final DamageSource LIGHTNING_BOLT;
    public static final DamageSource ON_FIRE;
    public static final DamageSource LAVA;
    public static final DamageSource HOT_FLOOR;
    public static final DamageSource IN_WALL;
    public static final DamageSource CRAMMING;
    public static final DamageSource DROWN;
    public static final DamageSource STARVE;
    public static final DamageSource CACTUS;
    public static final DamageSource FALL;
    public static final DamageSource FLY_INTO_WALL;
    public static final DamageSource OUT_OF_WORLD;
    public static final DamageSource GENERIC;
    public static final DamageSource MAGIC;
    public static final DamageSource WITHER;
    public static final DamageSource ANVIL;
    public static final DamageSource FALLING_BLOCK;
    public static final DamageSource DRAGON_BREATH;
    public static final DamageSource DRY_OUT;
    public static final DamageSource SWEET_BERRY_BUSH;
    private boolean bypassArmor;
    private boolean bypassInvul;
    private boolean bypassMagic;
    private float exhaustion;
    private boolean isFireSource;
    private boolean isProjectile;
    private boolean scalesWithDifficulty;
    private boolean isMagic;
    private boolean isExplosion;
    public final String msgId;
    
    public static DamageSource sting(final LivingEntity aqj) {
        return new EntityDamageSource("sting", (Entity)aqj);
    }
    
    public static DamageSource mobAttack(final LivingEntity aqj) {
        return new EntityDamageSource("mob", (Entity)aqj);
    }
    
    public static DamageSource indirectMobAttack(final Entity apx, final LivingEntity aqj) {
        return new IndirectEntityDamageSource("mob", apx, (Entity)aqj);
    }
    
    public static DamageSource playerAttack(final Player bft) {
        return new EntityDamageSource("player", (Entity)bft);
    }
    
    public static DamageSource arrow(final AbstractArrow bfx, @Nullable final Entity apx) {
        return new IndirectEntityDamageSource("arrow", (Entity)bfx, apx).setProjectile();
    }
    
    public static DamageSource trident(final Entity apx1, @Nullable final Entity apx2) {
        return new IndirectEntityDamageSource("trident", apx1, apx2).setProjectile();
    }
    
    public static DamageSource fireworks(final FireworkRocketEntity bge, @Nullable final Entity apx) {
        return new IndirectEntityDamageSource("fireworks", (Entity)bge, apx).setExplosion();
    }
    
    public static DamageSource fireball(final Fireball bgd, @Nullable final Entity apx) {
        if (apx == null) {
            return new IndirectEntityDamageSource("onFire", (Entity)bgd, (Entity)bgd).setIsFire().setProjectile();
        }
        return new IndirectEntityDamageSource("fireball", (Entity)bgd, apx).setIsFire().setProjectile();
    }
    
    public static DamageSource witherSkull(final WitherSkull bgw, final Entity apx) {
        return new IndirectEntityDamageSource("witherSkull", (Entity)bgw, apx).setProjectile();
    }
    
    public static DamageSource thrown(final Entity apx1, @Nullable final Entity apx2) {
        return new IndirectEntityDamageSource("thrown", apx1, apx2).setProjectile();
    }
    
    public static DamageSource indirectMagic(final Entity apx1, @Nullable final Entity apx2) {
        return new IndirectEntityDamageSource("indirectMagic", apx1, apx2).bypassArmor().setMagic();
    }
    
    public static DamageSource thorns(final Entity apx) {
        return new EntityDamageSource("thorns", apx).setThorns().setMagic();
    }
    
    public static DamageSource explosion(@Nullable final Explosion brm) {
        return explosion((brm != null) ? brm.getSourceMob() : null);
    }
    
    public static DamageSource explosion(@Nullable final LivingEntity aqj) {
        if (aqj != null) {
            return new EntityDamageSource("explosion.player", (Entity)aqj).setScalesWithDifficulty().setExplosion();
        }
        return new DamageSource("explosion").setScalesWithDifficulty().setExplosion();
    }
    
    public static DamageSource badRespawnPointExplosion() {
        return new BadRespawnPointDamage();
    }
    
    public String toString() {
        return "DamageSource (" + this.msgId + ")";
    }
    
    public boolean isProjectile() {
        return this.isProjectile;
    }
    
    public DamageSource setProjectile() {
        this.isProjectile = true;
        return this;
    }
    
    public boolean isExplosion() {
        return this.isExplosion;
    }
    
    public DamageSource setExplosion() {
        this.isExplosion = true;
        return this;
    }
    
    public boolean isBypassArmor() {
        return this.bypassArmor;
    }
    
    public float getFoodExhaustion() {
        return this.exhaustion;
    }
    
    public boolean isBypassInvul() {
        return this.bypassInvul;
    }
    
    public boolean isBypassMagic() {
        return this.bypassMagic;
    }
    
    protected DamageSource(final String string) {
        this.exhaustion = 0.1f;
        this.msgId = string;
    }
    
    @Nullable
    public Entity getDirectEntity() {
        return this.getEntity();
    }
    
    @Nullable
    public Entity getEntity() {
        return null;
    }
    
    protected DamageSource bypassArmor() {
        this.bypassArmor = true;
        this.exhaustion = 0.0f;
        return this;
    }
    
    protected DamageSource bypassInvul() {
        this.bypassInvul = true;
        return this;
    }
    
    protected DamageSource bypassMagic() {
        this.bypassMagic = true;
        this.exhaustion = 0.0f;
        return this;
    }
    
    protected DamageSource setIsFire() {
        this.isFireSource = true;
        return this;
    }
    
    public Component getLocalizedDeathMessage(final LivingEntity aqj) {
        final LivingEntity aqj2 = aqj.getKillCredit();
        final String string4 = "death.attack." + this.msgId;
        final String string5 = string4 + ".player";
        if (aqj2 != null) {
            return new TranslatableComponent(string5, new Object[] { aqj.getDisplayName(), aqj2.getDisplayName() });
        }
        return new TranslatableComponent(string4, new Object[] { aqj.getDisplayName() });
    }
    
    public boolean isFire() {
        return this.isFireSource;
    }
    
    public String getMsgId() {
        return this.msgId;
    }
    
    public DamageSource setScalesWithDifficulty() {
        this.scalesWithDifficulty = true;
        return this;
    }
    
    public boolean scalesWithDifficulty() {
        return this.scalesWithDifficulty;
    }
    
    public boolean isMagic() {
        return this.isMagic;
    }
    
    public DamageSource setMagic() {
        this.isMagic = true;
        return this;
    }
    
    public boolean isCreativePlayer() {
        final Entity apx2 = this.getEntity();
        return apx2 instanceof Player && ((Player)apx2).abilities.instabuild;
    }
    
    @Nullable
    public Vec3 getSourcePosition() {
        return null;
    }
    
    static {
        IN_FIRE = new DamageSource("inFire").bypassArmor().setIsFire();
        LIGHTNING_BOLT = new DamageSource("lightningBolt");
        ON_FIRE = new DamageSource("onFire").bypassArmor().setIsFire();
        LAVA = new DamageSource("lava").setIsFire();
        HOT_FLOOR = new DamageSource("hotFloor").setIsFire();
        IN_WALL = new DamageSource("inWall").bypassArmor();
        CRAMMING = new DamageSource("cramming").bypassArmor();
        DROWN = new DamageSource("drown").bypassArmor();
        STARVE = new DamageSource("starve").bypassArmor().bypassMagic();
        CACTUS = new DamageSource("cactus");
        FALL = new DamageSource("fall").bypassArmor();
        FLY_INTO_WALL = new DamageSource("flyIntoWall").bypassArmor();
        OUT_OF_WORLD = new DamageSource("outOfWorld").bypassArmor().bypassInvul();
        GENERIC = new DamageSource("generic").bypassArmor();
        MAGIC = new DamageSource("magic").bypassArmor().setMagic();
        WITHER = new DamageSource("wither").bypassArmor();
        ANVIL = new DamageSource("anvil");
        FALLING_BLOCK = new DamageSource("fallingBlock");
        DRAGON_BREATH = new DamageSource("dragonBreath").bypassArmor();
        DRY_OUT = new DamageSource("dryout");
        SWEET_BERRY_BUSH = new DamageSource("sweetBerryBush");
    }
}
