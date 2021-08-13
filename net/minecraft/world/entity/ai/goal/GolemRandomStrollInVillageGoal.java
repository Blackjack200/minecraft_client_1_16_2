package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.util.RandomPos;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;

public class GolemRandomStrollInVillageGoal extends RandomStrollGoal {
    public GolemRandomStrollInVillageGoal(final PathfinderMob aqr, final double double2) {
        super(aqr, double2, 240, false);
    }
    
    @Nullable
    @Override
    protected Vec3 getPosition() {
        final float float3 = this.mob.level.random.nextFloat();
        if (this.mob.level.random.nextFloat() < 0.3f) {
            return this.getPositionTowardsAnywhere();
        }
        Vec3 dck2;
        if (float3 < 0.7f) {
            dck2 = this.getPositionTowardsVillagerWhoWantsGolem();
            if (dck2 == null) {
                dck2 = this.getPositionTowardsPoi();
            }
        }
        else {
            dck2 = this.getPositionTowardsPoi();
            if (dck2 == null) {
                dck2 = this.getPositionTowardsVillagerWhoWantsGolem();
            }
        }
        return (dck2 == null) ? this.getPositionTowardsAnywhere() : dck2;
    }
    
    @Nullable
    private Vec3 getPositionTowardsAnywhere() {
        return RandomPos.getLandPos(this.mob, 10, 7);
    }
    
    @Nullable
    private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final List<Villager> list3 = aag2.<Villager>getEntities(EntityType.VILLAGER, this.mob.getBoundingBox().inflate(32.0), (java.util.function.Predicate<? super Villager>)this::doesVillagerWantGolem);
        if (list3.isEmpty()) {
            return null;
        }
        final Villager bfg4 = (Villager)list3.get(this.mob.level.random.nextInt(list3.size()));
        final Vec3 dck5 = bfg4.position();
        return RandomPos.getLandPosTowards(this.mob, 10, 7, dck5);
    }
    
    @Nullable
    private Vec3 getPositionTowardsPoi() {
        final SectionPos gp2 = this.getRandomVillageSection();
        if (gp2 == null) {
            return null;
        }
        final BlockPos fx3 = this.getRandomPoiWithinSection(gp2);
        if (fx3 == null) {
            return null;
        }
        return RandomPos.getLandPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(fx3));
    }
    
    @Nullable
    private SectionPos getRandomVillageSection() {
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final List<SectionPos> list3 = (List<SectionPos>)SectionPos.cube(SectionPos.of(this.mob), 2).filter(gp -> aag2.sectionsToVillage(gp) == 0).collect(Collectors.toList());
        if (list3.isEmpty()) {
            return null;
        }
        return (SectionPos)list3.get(aag2.random.nextInt(list3.size()));
    }
    
    @Nullable
    private BlockPos getRandomPoiWithinSection(final SectionPos gp) {
        final ServerLevel aag3 = (ServerLevel)this.mob.level;
        final PoiManager azl4 = aag3.getPoiManager();
        final List<BlockPos> list5 = (List<BlockPos>)azl4.getInRange((Predicate<PoiType>)(azo -> true), gp.center(), 8, PoiManager.Occupancy.IS_OCCUPIED).map(PoiRecord::getPos).collect(Collectors.toList());
        if (list5.isEmpty()) {
            return null;
        }
        return (BlockPos)list5.get(aag3.random.nextInt(list5.size()));
    }
    
    private boolean doesVillagerWantGolem(final Villager bfg) {
        return bfg.wantsToSpawnGolem(this.mob.level.getGameTime());
    }
}
