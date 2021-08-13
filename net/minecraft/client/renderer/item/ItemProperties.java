package net.minecraft.client.renderer.item;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import java.util.Optional;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class ItemProperties {
    private static final Map<ResourceLocation, ItemPropertyFunction> GENERIC_PROPERTIES;
    private static final ResourceLocation DAMAGED;
    private static final ResourceLocation DAMAGE;
    private static final ItemPropertyFunction PROPERTY_DAMAGED;
    private static final ItemPropertyFunction PROPERTY_DAMAGE;
    private static final Map<Item, Map<ResourceLocation, ItemPropertyFunction>> PROPERTIES;
    
    private static ItemPropertyFunction registerGeneric(final ResourceLocation vk, final ItemPropertyFunction ejf) {
        ItemProperties.GENERIC_PROPERTIES.put(vk, ejf);
        return ejf;
    }
    
    private static void register(final Item blu, final ResourceLocation vk, final ItemPropertyFunction ejf) {
        ((Map)ItemProperties.PROPERTIES.computeIfAbsent(blu, blu -> Maps.newHashMap())).put(vk, ejf);
    }
    
    @Nullable
    public static ItemPropertyFunction getProperty(final Item blu, final ResourceLocation vk) {
        if (blu.getMaxDamage() > 0) {
            if (ItemProperties.DAMAGE.equals(vk)) {
                return ItemProperties.PROPERTY_DAMAGE;
            }
            if (ItemProperties.DAMAGED.equals(vk)) {
                return ItemProperties.PROPERTY_DAMAGED;
            }
        }
        final ItemPropertyFunction ejf3 = (ItemPropertyFunction)ItemProperties.GENERIC_PROPERTIES.get(vk);
        if (ejf3 != null) {
            return ejf3;
        }
        final Map<ResourceLocation, ItemPropertyFunction> map4 = (Map<ResourceLocation, ItemPropertyFunction>)ItemProperties.PROPERTIES.get(blu);
        if (map4 == null) {
            return null;
        }
        return (ItemPropertyFunction)map4.get(vk);
    }
    
    static {
        GENERIC_PROPERTIES = (Map)Maps.newHashMap();
        DAMAGED = new ResourceLocation("damaged");
        DAMAGE = new ResourceLocation("damage");
        PROPERTY_DAMAGED = ((bly, dwl, aqj) -> bly.isDamaged() ? 1.0f : 0.0f);
        PROPERTY_DAMAGE = ((bly, dwl, aqj) -> Mth.clamp(bly.getDamageValue() / (float)bly.getMaxDamage(), 0.0f, 1.0f));
        PROPERTIES = (Map)Maps.newHashMap();
        registerGeneric(new ResourceLocation("lefthanded"), (bly, dwl, aqj) -> (aqj == null || aqj.getMainArm() == HumanoidArm.RIGHT) ? 0.0f : 1.0f);
        registerGeneric(new ResourceLocation("cooldown"), (bly, dwl, aqj) -> (aqj instanceof Player) ? aqj.getCooldowns().getCooldownPercent(bly.getItem(), 0.0f) : 0.0f);
        registerGeneric(new ResourceLocation("custom_model_data"), (bly, dwl, aqj) -> bly.hasTag() ? ((float)bly.getTag().getInt("CustomModelData")) : 0.0f);
        register(Items.BOW, new ResourceLocation("pull"), (bly, dwl, aqj) -> {
            if (aqj == null) {
                return 0.0f;
            }
            else if (aqj.getUseItem() != bly) {
                return 0.0f;
            }
            else {
                return (bly.getUseDuration() - aqj.getUseItemRemainingTicks()) / 20.0f;
            }
        });
        register(Items.BOW, new ResourceLocation("pulling"), (bly, dwl, aqj) -> (aqj != null && aqj.isUsingItem() && aqj.getUseItem() == bly) ? 1.0f : 0.0f);
        register(Items.CLOCK, new ResourceLocation("time"), new ItemPropertyFunction() {
            private double rotation;
            private double rota;
            private long lastUpdateTick;
            
            public float call(final ItemStack bly, @Nullable ClientLevel dwl, @Nullable final LivingEntity aqj) {
                final Entity apx5 = (aqj != null) ? aqj : bly.getEntityRepresentation();
                if (apx5 == null) {
                    return 0.0f;
                }
                if (dwl == null && apx5.level instanceof ClientLevel) {
                    dwl = (ClientLevel)apx5.level;
                }
                if (dwl == null) {
                    return 0.0f;
                }
                double double6;
                if (dwl.dimensionType().natural()) {
                    double6 = dwl.getTimeOfDay(1.0f);
                }
                else {
                    double6 = Math.random();
                }
                double6 = this.wobble(dwl, double6);
                return (float)double6;
            }
            
            private double wobble(final Level bru, final double double2) {
                if (bru.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = bru.getGameTime();
                    double double3 = double2 - this.rotation;
                    double3 = Mth.positiveModulo(double3 + 0.5, 1.0) - 0.5;
                    this.rota += double3 * 0.1;
                    this.rota *= 0.9;
                    this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }
        });
        register(Items.COMPASS, new ResourceLocation("angle"), new ItemPropertyFunction() {
            private final CompassWobble wobble = new CompassWobble();
            private final CompassWobble wobbleRandom = new CompassWobble();
            
            public float call(final ItemStack bly, @Nullable ClientLevel dwl, @Nullable final LivingEntity aqj) {
                final Entity apx5 = (aqj != null) ? aqj : bly.getEntityRepresentation();
                if (apx5 == null) {
                    return 0.0f;
                }
                if (dwl == null && apx5.level instanceof ClientLevel) {
                    dwl = (ClientLevel)apx5.level;
                }
                final BlockPos fx6 = CompassItem.isLodestoneCompass(bly) ? this.getLodestonePosition(dwl, bly.getOrCreateTag()) : this.getSpawnPosition(dwl);
                final long long7 = dwl.getGameTime();
                if (fx6 == null || apx5.position().distanceToSqr(fx6.getX() + 0.5, apx5.position().y(), fx6.getZ() + 0.5) < 9.999999747378752E-6) {
                    if (this.wobbleRandom.shouldUpdate(long7)) {
                        this.wobbleRandom.update(long7, Math.random());
                    }
                    final double double9 = this.wobbleRandom.rotation + bly.hashCode() / 2.14748365E9f;
                    return Mth.positiveModulo((float)double9, 1.0f);
                }
                final boolean boolean9 = aqj instanceof Player && ((Player)aqj).isLocalPlayer();
                double double10 = 0.0;
                if (boolean9) {
                    double10 = aqj.yRot;
                }
                else if (apx5 instanceof ItemFrame) {
                    double10 = this.getFrameRotation((ItemFrame)apx5);
                }
                else if (apx5 instanceof ItemEntity) {
                    double10 = 180.0f - ((ItemEntity)apx5).getSpin(0.5f) / 6.2831855f * 360.0f;
                }
                else if (aqj != null) {
                    double10 = aqj.yBodyRot;
                }
                double10 = Mth.positiveModulo(double10 / 360.0, 1.0);
                final double double11 = this.getAngleTo(Vec3.atCenterOf(fx6), apx5) / 6.2831854820251465;
                double double12;
                if (boolean9) {
                    if (this.wobble.shouldUpdate(long7)) {
                        this.wobble.update(long7, 0.5 - (double10 - 0.25));
                    }
                    double12 = double11 + this.wobble.rotation;
                }
                else {
                    double12 = 0.5 - (double10 - 0.25 - double11);
                }
                return Mth.positiveModulo((float)double12, 1.0f);
            }
            
            @Nullable
            private BlockPos getSpawnPosition(final ClientLevel dwl) {
                return dwl.dimensionType().natural() ? dwl.getSharedSpawnPos() : null;
            }
            
            @Nullable
            private BlockPos getLodestonePosition(final Level bru, final CompoundTag md) {
                final boolean boolean4 = md.contains("LodestonePos");
                final boolean boolean5 = md.contains("LodestoneDimension");
                if (boolean4 && boolean5) {
                    final Optional<ResourceKey<Level>> optional6 = CompassItem.getLodestoneDimension(md);
                    if (optional6.isPresent() && bru.dimension() == optional6.get()) {
                        return NbtUtils.readBlockPos(md.getCompound("LodestonePos"));
                    }
                }
                return null;
            }
            
            private double getFrameRotation(final ItemFrame bcm) {
                final Direction gc3 = bcm.getDirection();
                final int integer4 = gc3.getAxis().isVertical() ? (90 * gc3.getAxisDirection().getStep()) : 0;
                return Mth.wrapDegrees(180 + gc3.get2DDataValue() * 90 + bcm.getRotation() * 45 + integer4);
            }
            
            private double getAngleTo(final Vec3 dck, final Entity apx) {
                return Math.atan2(dck.z() - apx.getZ(), dck.x() - apx.getX());
            }
        });
        register(Items.CROSSBOW, new ResourceLocation("pull"), (bly, dwl, aqj) -> {
            if (aqj == null) {
                return 0.0f;
            }
            else if (CrossbowItem.isCharged(bly)) {
                return 0.0f;
            }
            else {
                return (bly.getUseDuration() - aqj.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(bly);
            }
        });
        register(Items.CROSSBOW, new ResourceLocation("pulling"), (bly, dwl, aqj) -> (aqj != null && aqj.isUsingItem() && aqj.getUseItem() == bly && !CrossbowItem.isCharged(bly)) ? 1.0f : 0.0f);
        register(Items.CROSSBOW, new ResourceLocation("charged"), (bly, dwl, aqj) -> (aqj != null && CrossbowItem.isCharged(bly)) ? 1.0f : 0.0f);
        register(Items.CROSSBOW, new ResourceLocation("firework"), (bly, dwl, aqj) -> (aqj != null && CrossbowItem.isCharged(bly) && CrossbowItem.containsChargedProjectile(bly, Items.FIREWORK_ROCKET)) ? 1.0f : 0.0f);
        register(Items.ELYTRA, new ResourceLocation("broken"), (bly, dwl, aqj) -> ElytraItem.isFlyEnabled(bly) ? 0.0f : 1.0f);
        boolean boolean4;
        boolean boolean5;
        register(Items.FISHING_ROD, new ResourceLocation("cast"), (bly, dwl, aqj) -> {
            if (aqj == null) {
                return 0.0f;
            }
            else {
                boolean4 = (aqj.getMainHandItem() == bly);
                boolean5 = (aqj.getOffhandItem() == bly);
                if (aqj.getMainHandItem().getItem() instanceof FishingRodItem) {
                    boolean5 = false;
                }
                return ((boolean4 || boolean5) && aqj instanceof Player && aqj.fishing != null) ? 1.0f : 0.0f;
            }
        });
        register(Items.SHIELD, new ResourceLocation("blocking"), (bly, dwl, aqj) -> (aqj != null && aqj.isUsingItem() && aqj.getUseItem() == bly) ? 1.0f : 0.0f);
        register(Items.TRIDENT, new ResourceLocation("throwing"), (bly, dwl, aqj) -> (aqj != null && aqj.isUsingItem() && aqj.getUseItem() == bly) ? 1.0f : 0.0f);
    }
    
    static class CompassWobble {
        private double rotation;
        private double deltaRotation;
        private long lastUpdateTick;
        
        private CompassWobble() {
        }
        
        private boolean shouldUpdate(final long long1) {
            return this.lastUpdateTick != long1;
        }
        
        private void update(final long long1, final double double2) {
            this.lastUpdateTick = long1;
            double double3 = double2 - this.rotation;
            double3 = Mth.positiveModulo(double3 + 0.5, 1.0) - 0.5;
            this.deltaRotation += double3 * 0.1;
            this.deltaRotation *= 0.8;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0);
        }
    }
}
