package net.minecraft.world.damagesource;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

public class CombatTracker {
    private final List<CombatEntry> entries;
    private final LivingEntity mob;
    private int lastDamageTime;
    private int combatStartTime;
    private int combatEndTime;
    private boolean inCombat;
    private boolean takingDamage;
    private String nextLocation;
    
    public CombatTracker(final LivingEntity aqj) {
        this.entries = (List<CombatEntry>)Lists.newArrayList();
        this.mob = aqj;
    }
    
    public void prepareForDamage() {
        this.resetPreparedStatus();
        final Optional<BlockPos> optional2 = this.mob.getLastClimbablePos();
        if (optional2.isPresent()) {
            final BlockState cee3 = this.mob.level.getBlockState((BlockPos)optional2.get());
            if (cee3.is(Blocks.LADDER) || cee3.is(BlockTags.TRAPDOORS)) {
                this.nextLocation = "ladder";
            }
            else if (cee3.is(Blocks.VINE)) {
                this.nextLocation = "vines";
            }
            else if (cee3.is(Blocks.WEEPING_VINES) || cee3.is(Blocks.WEEPING_VINES_PLANT)) {
                this.nextLocation = "weeping_vines";
            }
            else if (cee3.is(Blocks.TWISTING_VINES) || cee3.is(Blocks.TWISTING_VINES_PLANT)) {
                this.nextLocation = "twisting_vines";
            }
            else if (cee3.is(Blocks.SCAFFOLDING)) {
                this.nextLocation = "scaffolding";
            }
            else {
                this.nextLocation = "other_climbable";
            }
        }
        else if (this.mob.isInWater()) {
            this.nextLocation = "water";
        }
    }
    
    public void recordDamage(final DamageSource aph, final float float2, final float float3) {
        this.recheckStatus();
        this.prepareForDamage();
        final CombatEntry ape5 = new CombatEntry(aph, this.mob.tickCount, float2, float3, this.nextLocation, this.mob.fallDistance);
        this.entries.add(ape5);
        this.lastDamageTime = this.mob.tickCount;
        this.takingDamage = true;
        if (ape5.isCombatRelated() && !this.inCombat && this.mob.isAlive()) {
            this.inCombat = true;
            this.combatStartTime = this.mob.tickCount;
            this.combatEndTime = this.combatStartTime;
            this.mob.onEnterCombat();
        }
    }
    
    public Component getDeathMessage() {
        if (this.entries.isEmpty()) {
            return new TranslatableComponent("death.attack.generic", new Object[] { this.mob.getDisplayName() });
        }
        final CombatEntry ape2 = this.getMostSignificantFall();
        final CombatEntry ape3 = (CombatEntry)this.entries.get(this.entries.size() - 1);
        final Component nr5 = ape3.getAttackerName();
        final Entity apx6 = ape3.getSource().getEntity();
        Component nr7;
        if (ape2 != null && ape3.getSource() == DamageSource.FALL) {
            final Component nr6 = ape2.getAttackerName();
            if (ape2.getSource() == DamageSource.FALL || ape2.getSource() == DamageSource.OUT_OF_WORLD) {
                nr7 = new TranslatableComponent("death.fell.accident." + this.getFallLocation(ape2), new Object[] { this.mob.getDisplayName() });
            }
            else if (nr6 != null && (nr5 == null || !nr6.equals(nr5))) {
                final Entity apx7 = ape2.getSource().getEntity();
                final ItemStack bly9 = (apx7 instanceof LivingEntity) ? ((LivingEntity)apx7).getMainHandItem() : ItemStack.EMPTY;
                if (!bly9.isEmpty() && bly9.hasCustomHoverName()) {
                    nr7 = new TranslatableComponent("death.fell.assist.item", new Object[] { this.mob.getDisplayName(), nr6, bly9.getDisplayName() });
                }
                else {
                    nr7 = new TranslatableComponent("death.fell.assist", new Object[] { this.mob.getDisplayName(), nr6 });
                }
            }
            else if (nr5 != null) {
                final ItemStack bly10 = (apx6 instanceof LivingEntity) ? ((LivingEntity)apx6).getMainHandItem() : ItemStack.EMPTY;
                if (!bly10.isEmpty() && bly10.hasCustomHoverName()) {
                    nr7 = new TranslatableComponent("death.fell.finish.item", new Object[] { this.mob.getDisplayName(), nr5, bly10.getDisplayName() });
                }
                else {
                    nr7 = new TranslatableComponent("death.fell.finish", new Object[] { this.mob.getDisplayName(), nr5 });
                }
            }
            else {
                nr7 = new TranslatableComponent("death.fell.killer", new Object[] { this.mob.getDisplayName() });
            }
        }
        else {
            nr7 = ape3.getSource().getLocalizedDeathMessage(this.mob);
        }
        return nr7;
    }
    
    @Nullable
    public LivingEntity getKiller() {
        LivingEntity aqj2 = null;
        Player bft3 = null;
        float float4 = 0.0f;
        float float5 = 0.0f;
        for (final CombatEntry ape7 : this.entries) {
            if (ape7.getSource().getEntity() instanceof Player && (bft3 == null || ape7.getDamage() > float5)) {
                float5 = ape7.getDamage();
                bft3 = (Player)ape7.getSource().getEntity();
            }
            if (ape7.getSource().getEntity() instanceof LivingEntity && (aqj2 == null || ape7.getDamage() > float4)) {
                float4 = ape7.getDamage();
                aqj2 = (LivingEntity)ape7.getSource().getEntity();
            }
        }
        if (bft3 != null && float5 >= float4 / 3.0f) {
            return bft3;
        }
        return aqj2;
    }
    
    @Nullable
    private CombatEntry getMostSignificantFall() {
        CombatEntry ape2 = null;
        CombatEntry ape3 = null;
        float float4 = 0.0f;
        float float5 = 0.0f;
        for (int integer6 = 0; integer6 < this.entries.size(); ++integer6) {
            final CombatEntry ape4 = (CombatEntry)this.entries.get(integer6);
            final CombatEntry ape5 = (integer6 > 0) ? ((CombatEntry)this.entries.get(integer6 - 1)) : null;
            if ((ape4.getSource() == DamageSource.FALL || ape4.getSource() == DamageSource.OUT_OF_WORLD) && ape4.getFallDistance() > 0.0f && (ape2 == null || ape4.getFallDistance() > float5)) {
                if (integer6 > 0) {
                    ape2 = ape5;
                }
                else {
                    ape2 = ape4;
                }
                float5 = ape4.getFallDistance();
            }
            if (ape4.getLocation() != null && (ape3 == null || ape4.getDamage() > float4)) {
                ape3 = ape4;
                float4 = ape4.getDamage();
            }
        }
        if (float5 > 5.0f && ape2 != null) {
            return ape2;
        }
        if (float4 > 5.0f && ape3 != null) {
            return ape3;
        }
        return null;
    }
    
    private String getFallLocation(final CombatEntry ape) {
        return (ape.getLocation() == null) ? "generic" : ape.getLocation();
    }
    
    public int getCombatDuration() {
        if (this.inCombat) {
            return this.mob.tickCount - this.combatStartTime;
        }
        return this.combatEndTime - this.combatStartTime;
    }
    
    private void resetPreparedStatus() {
        this.nextLocation = null;
    }
    
    public void recheckStatus() {
        final int integer2 = this.inCombat ? 300 : 100;
        if (this.takingDamage && (!this.mob.isAlive() || this.mob.tickCount - this.lastDamageTime > integer2)) {
            final boolean boolean3 = this.inCombat;
            this.takingDamage = false;
            this.inCombat = false;
            this.combatEndTime = this.mob.tickCount;
            if (boolean3) {
                this.mob.onLeaveCombat();
            }
            this.entries.clear();
        }
    }
    
    public LivingEntity getMob() {
        return this.mob;
    }
}
