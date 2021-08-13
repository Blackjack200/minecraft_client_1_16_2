package net.minecraft.world.level.dimension.end;

import net.minecraft.world.entity.EntitySelector;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.ChunkStatus;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.util.Unit;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.TicketType;
import java.util.Iterator;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.predicate.BlockPredicate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import java.util.Collections;
import java.util.Random;
import java.util.Collection;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import net.minecraft.nbt.NbtUtils;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.core.BlockPos;
import java.util.UUID;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import org.apache.logging.log4j.Logger;

public class EndDragonFight {
    private static final Logger LOGGER;
    private static final Predicate<Entity> VALID_PLAYER;
    private final ServerBossEvent dragonEvent;
    private final ServerLevel level;
    private final List<Integer> gateways;
    private final BlockPattern exitPortalPattern;
    private int ticksSinceDragonSeen;
    private int crystalsAlive;
    private int ticksSinceCrystalsScanned;
    private int ticksSinceLastPlayerScan;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    private UUID dragonUUID;
    private boolean needsStateScanning;
    private BlockPos portalLocation;
    private DragonRespawnAnimation respawnStage;
    private int respawnTime;
    private List<EndCrystal> respawnCrystals;
    
    public EndDragonFight(final ServerLevel aag, final long long2, final CompoundTag md) {
        this.dragonEvent = (ServerBossEvent)new ServerBossEvent(new TranslatableComponent("entity.minecraft.ender_dragon"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS).setPlayBossMusic(true).setCreateWorldFog(true);
        this.gateways = (List<Integer>)Lists.newArrayList();
        this.needsStateScanning = true;
        this.level = aag;
        if (md.contains("DragonKilled", 99)) {
            if (md.hasUUID("Dragon")) {
                this.dragonUUID = md.getUUID("Dragon");
            }
            this.dragonKilled = md.getBoolean("DragonKilled");
            this.previouslyKilled = md.getBoolean("PreviouslyKilled");
            if (md.getBoolean("IsRespawning")) {
                this.respawnStage = DragonRespawnAnimation.START;
            }
            if (md.contains("ExitPortalLocation", 10)) {
                this.portalLocation = NbtUtils.readBlockPos(md.getCompound("ExitPortalLocation"));
            }
        }
        else {
            this.dragonKilled = true;
            this.previouslyKilled = true;
        }
        if (md.contains("Gateways", 9)) {
            final ListTag mj6 = md.getList("Gateways", 3);
            for (int integer7 = 0; integer7 < mj6.size(); ++integer7) {
                this.gateways.add(mj6.getInt(integer7));
            }
        }
        else {
            this.gateways.addAll((Collection)ContiguousSet.create(Range.closedOpen((Comparable)0, (Comparable)20), DiscreteDomain.integers()));
            Collections.shuffle((List)this.gateways, new Random(long2));
        }
        this.exitPortalPattern = BlockPatternBuilder.start().aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where('#', BlockInWorld.hasState((Predicate<BlockState>)BlockPredicate.forBlock(Blocks.BEDROCK))).build();
    }
    
    public CompoundTag saveData() {
        final CompoundTag md2 = new CompoundTag();
        if (this.dragonUUID != null) {
            md2.putUUID("Dragon", this.dragonUUID);
        }
        md2.putBoolean("DragonKilled", this.dragonKilled);
        md2.putBoolean("PreviouslyKilled", this.previouslyKilled);
        if (this.portalLocation != null) {
            md2.put("ExitPortalLocation", (Tag)NbtUtils.writeBlockPos(this.portalLocation));
        }
        final ListTag mj3 = new ListTag();
        for (final int integer5 : this.gateways) {
            mj3.add(IntTag.valueOf(integer5));
        }
        md2.put("Gateways", (Tag)mj3);
        return md2;
    }
    
    public void tick() {
        this.dragonEvent.setVisible(!this.dragonKilled);
        if (++this.ticksSinceLastPlayerScan >= 20) {
            this.updatePlayers();
            this.ticksSinceLastPlayerScan = 0;
        }
        if (!this.dragonEvent.getPlayers().isEmpty()) {
            this.level.getChunkSource().<Unit>addRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
            final boolean boolean2 = this.isArenaLoaded();
            if (this.needsStateScanning && boolean2) {
                this.scanState();
                this.needsStateScanning = false;
            }
            if (this.respawnStage != null) {
                if (this.respawnCrystals == null && boolean2) {
                    this.respawnStage = null;
                    this.tryRespawn();
                }
                this.respawnStage.tick(this.level, this, this.respawnCrystals, this.respawnTime++, this.portalLocation);
            }
            if (!this.dragonKilled) {
                if ((this.dragonUUID == null || ++this.ticksSinceDragonSeen >= 1200) && boolean2) {
                    this.findOrCreateDragon();
                    this.ticksSinceDragonSeen = 0;
                }
                if (++this.ticksSinceCrystalsScanned >= 100 && boolean2) {
                    this.updateCrystalCount();
                    this.ticksSinceCrystalsScanned = 0;
                }
            }
        }
        else {
            this.level.getChunkSource().<Unit>removeRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
        }
    }
    
    private void scanState() {
        EndDragonFight.LOGGER.info("Scanning for legacy world dragon fight...");
        final boolean boolean2 = this.hasActiveExitPortal();
        if (boolean2) {
            EndDragonFight.LOGGER.info("Found that the dragon has been killed in this world already.");
            this.previouslyKilled = true;
        }
        else {
            EndDragonFight.LOGGER.info("Found that the dragon has not yet been killed in this world.");
            this.previouslyKilled = false;
            if (this.findExitPortal() == null) {
                this.spawnExitPortal(false);
            }
        }
        final List<EnderDragon> list3 = this.level.getDragons();
        if (list3.isEmpty()) {
            this.dragonKilled = true;
        }
        else {
            final EnderDragon bbo4 = (EnderDragon)list3.get(0);
            this.dragonUUID = bbo4.getUUID();
            EndDragonFight.LOGGER.info("Found that there's a dragon still alive ({})", bbo4);
            this.dragonKilled = false;
            if (!boolean2) {
                EndDragonFight.LOGGER.info("But we didn't have a portal, let's remove it.");
                bbo4.remove();
                this.dragonUUID = null;
            }
        }
        if (!this.previouslyKilled && this.dragonKilled) {
            this.dragonKilled = false;
        }
    }
    
    private void findOrCreateDragon() {
        final List<EnderDragon> list2 = this.level.getDragons();
        if (list2.isEmpty()) {
            EndDragonFight.LOGGER.debug("Haven't seen the dragon, respawning it");
            this.createNewDragon();
        }
        else {
            EndDragonFight.LOGGER.debug("Haven't seen our dragon, but found another one to use.");
            this.dragonUUID = ((EnderDragon)list2.get(0)).getUUID();
        }
    }
    
    protected void setRespawnStage(final DragonRespawnAnimation chc) {
        if (this.respawnStage == null) {
            throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
        }
        this.respawnTime = 0;
        if (chc == DragonRespawnAnimation.END) {
            this.respawnStage = null;
            this.dragonKilled = false;
            final EnderDragon bbo3 = this.createNewDragon();
            for (final ServerPlayer aah5 : this.dragonEvent.getPlayers()) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(aah5, bbo3);
            }
        }
        else {
            this.respawnStage = chc;
        }
    }
    
    private boolean hasActiveExitPortal() {
        for (int integer2 = -8; integer2 <= 8; ++integer2) {
            for (int integer3 = -8; integer3 <= 8; ++integer3) {
                final LevelChunk cge4 = this.level.getChunk(integer2, integer3);
                for (final BlockEntity ccg6 : cge4.getBlockEntities().values()) {
                    if (ccg6 instanceof TheEndPortalBlockEntity) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Nullable
    private BlockPattern.BlockPatternMatch findExitPortal() {
        for (int integer2 = -8; integer2 <= 8; ++integer2) {
            for (int integer3 = -8; integer3 <= 8; ++integer3) {
                final LevelChunk cge4 = this.level.getChunk(integer2, integer3);
                for (final BlockEntity ccg6 : cge4.getBlockEntities().values()) {
                    if (ccg6 instanceof TheEndPortalBlockEntity) {
                        final BlockPattern.BlockPatternMatch b7 = this.exitPortalPattern.find(this.level, ccg6.getBlockPos());
                        if (b7 != null) {
                            final BlockPos fx8 = b7.getBlock(3, 3, 3).getPos();
                            if (this.portalLocation == null && fx8.getX() == 0 && fx8.getZ() == 0) {
                                this.portalLocation = fx8;
                            }
                            return b7;
                        }
                        continue;
                    }
                }
            }
        }
        int integer3;
        for (int integer2 = integer3 = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION).getY(); integer3 >= 0; --integer3) {
            final BlockPattern.BlockPatternMatch b8 = this.exitPortalPattern.find(this.level, new BlockPos(EndPodiumFeature.END_PODIUM_LOCATION.getX(), integer3, EndPodiumFeature.END_PODIUM_LOCATION.getZ()));
            if (b8 != null) {
                if (this.portalLocation == null) {
                    this.portalLocation = b8.getBlock(3, 3, 3).getPos();
                }
                return b8;
            }
        }
        return null;
    }
    
    private boolean isArenaLoaded() {
        for (int integer2 = -8; integer2 <= 8; ++integer2) {
            for (int integer3 = 8; integer3 <= 8; ++integer3) {
                final ChunkAccess cft4 = this.level.getChunk(integer2, integer3, ChunkStatus.FULL, false);
                if (!(cft4 instanceof LevelChunk)) {
                    return false;
                }
                final ChunkHolder.FullChunkStatus b5 = ((LevelChunk)cft4).getFullStatus();
                if (!b5.isOrAfter(ChunkHolder.FullChunkStatus.TICKING)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void updatePlayers() {
        final Set<ServerPlayer> set2 = (Set<ServerPlayer>)Sets.newHashSet();
        for (final ServerPlayer aah4 : this.level.getPlayers(EndDragonFight.VALID_PLAYER)) {
            this.dragonEvent.addPlayer(aah4);
            set2.add(aah4);
        }
        final Set<ServerPlayer> set3 = (Set<ServerPlayer>)Sets.newHashSet((Iterable)this.dragonEvent.getPlayers());
        set3.removeAll((Collection)set2);
        for (final ServerPlayer aah5 : set3) {
            this.dragonEvent.removePlayer(aah5);
        }
    }
    
    private void updateCrystalCount() {
        this.ticksSinceCrystalsScanned = 0;
        this.crystalsAlive = 0;
        for (final SpikeFeature.EndSpike a3 : SpikeFeature.getSpikesForLevel(this.level)) {
            this.crystalsAlive += this.level.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)EndCrystal.class, a3.getTopBoundingBox()).size();
        }
        EndDragonFight.LOGGER.debug("Found {} end crystals still alive", this.crystalsAlive);
    }
    
    public void setDragonKilled(final EnderDragon bbo) {
        if (bbo.getUUID().equals(this.dragonUUID)) {
            this.dragonEvent.setPercent(0.0f);
            this.dragonEvent.setVisible(false);
            this.spawnExitPortal(true);
            this.spawnNewGateway();
            if (!this.previouslyKilled) {
                this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.defaultBlockState());
            }
            this.previouslyKilled = true;
            this.dragonKilled = true;
        }
    }
    
    private void spawnNewGateway() {
        if (this.gateways.isEmpty()) {
            return;
        }
        final int integer2 = (int)this.gateways.remove(this.gateways.size() - 1);
        final int integer3 = Mth.floor(96.0 * Math.cos(2.0 * (-3.141592653589793 + 0.15707963267948966 * integer2)));
        final int integer4 = Mth.floor(96.0 * Math.sin(2.0 * (-3.141592653589793 + 0.15707963267948966 * integer2)));
        this.spawnNewGateway(new BlockPos(integer3, 75, integer4));
    }
    
    private void spawnNewGateway(final BlockPos fx) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/world/level/dimension/end/EndDragonFight.level:Lnet/minecraft/server/level/ServerLevel;
        //     4: sipush          3000
        //     7: aload_1         /* fx */
        //     8: iconst_0       
        //     9: invokevirtual   net/minecraft/server/level/ServerLevel.levelEvent:(ILnet/minecraft/core/BlockPos;I)V
        //    12: getstatic       net/minecraft/data/worldgen/Features.END_GATEWAY_DELAYED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    15: aload_0         /* this */
        //    16: getfield        net/minecraft/world/level/dimension/end/EndDragonFight.level:Lnet/minecraft/server/level/ServerLevel;
        //    19: aload_0         /* this */
        //    20: getfield        net/minecraft/world/level/dimension/end/EndDragonFight.level:Lnet/minecraft/server/level/ServerLevel;
        //    23: invokevirtual   net/minecraft/server/level/ServerLevel.getChunkSource:()Lnet/minecraft/server/level/ServerChunkCache;
        //    26: invokevirtual   net/minecraft/server/level/ServerChunkCache.getGenerator:()Lnet/minecraft/world/level/chunk/ChunkGenerator;
        //    29: new             Ljava/util/Random;
        //    32: dup            
        //    33: invokespecial   java/util/Random.<init>:()V
        //    36: aload_1         /* fx */
        //    37: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.place:(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/core/BlockPos;)Z
        //    40: pop            
        //    41: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  fx    
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void spawnExitPortal(final boolean boolean1) {
        final EndPodiumFeature cjh3 = new EndPodiumFeature(boolean1);
        if (this.portalLocation == null) {
            this.portalLocation = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION).below();
            while (this.level.getBlockState(this.portalLocation).is(Blocks.BEDROCK) && this.portalLocation.getY() > this.level.getSeaLevel()) {
                this.portalLocation = this.portalLocation.below();
            }
        }
        cjh3.configured(FeatureConfiguration.NONE).place(this.level, this.level.getChunkSource().getGenerator(), new Random(), this.portalLocation);
    }
    
    private EnderDragon createNewDragon() {
        this.level.getChunkAt(new BlockPos(0, 128, 0));
        final EnderDragon bbo2 = EntityType.ENDER_DRAGON.create(this.level);
        bbo2.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        bbo2.moveTo(0.0, 128.0, 0.0, this.level.random.nextFloat() * 360.0f, 0.0f);
        this.level.addFreshEntity(bbo2);
        this.dragonUUID = bbo2.getUUID();
        return bbo2;
    }
    
    public void updateDragon(final EnderDragon bbo) {
        if (bbo.getUUID().equals(this.dragonUUID)) {
            this.dragonEvent.setPercent(bbo.getHealth() / bbo.getMaxHealth());
            this.ticksSinceDragonSeen = 0;
            if (bbo.hasCustomName()) {
                this.dragonEvent.setName(bbo.getDisplayName());
            }
        }
    }
    
    public int getCrystalsAlive() {
        return this.crystalsAlive;
    }
    
    public void onCrystalDestroyed(final EndCrystal bbn, final DamageSource aph) {
        if (this.respawnStage != null && this.respawnCrystals.contains(bbn)) {
            EndDragonFight.LOGGER.debug("Aborting respawn sequence");
            this.respawnStage = null;
            this.respawnTime = 0;
            this.resetSpikeCrystals();
            this.spawnExitPortal(true);
        }
        else {
            this.updateCrystalCount();
            final Entity apx4 = this.level.getEntity(this.dragonUUID);
            if (apx4 instanceof EnderDragon) {
                ((EnderDragon)apx4).onCrystalDestroyed(bbn, bbn.blockPosition(), aph);
            }
        }
    }
    
    public boolean hasPreviouslyKilledDragon() {
        return this.previouslyKilled;
    }
    
    public void tryRespawn() {
        if (this.dragonKilled && this.respawnStage == null) {
            BlockPos fx2 = this.portalLocation;
            if (fx2 == null) {
                EndDragonFight.LOGGER.debug("Tried to respawn, but need to find the portal first.");
                final BlockPattern.BlockPatternMatch b3 = this.findExitPortal();
                if (b3 == null) {
                    EndDragonFight.LOGGER.debug("Couldn't find a portal, so we made one.");
                    this.spawnExitPortal(true);
                }
                else {
                    EndDragonFight.LOGGER.debug("Found the exit portal & temporarily using it.");
                }
                fx2 = this.portalLocation;
            }
            final List<EndCrystal> list3 = (List<EndCrystal>)Lists.newArrayList();
            final BlockPos fx3 = fx2.above(1);
            for (final Direction gc6 : Direction.Plane.HORIZONTAL) {
                final List<EndCrystal> list4 = this.level.<EndCrystal>getEntitiesOfClass((java.lang.Class<? extends EndCrystal>)EndCrystal.class, new AABB(fx3.relative(gc6, 2)));
                if (list4.isEmpty()) {
                    return;
                }
                list3.addAll((Collection)list4);
            }
            EndDragonFight.LOGGER.debug("Found all crystals, respawning dragon.");
            this.respawnDragon(list3);
        }
    }
    
    private void respawnDragon(final List<EndCrystal> list) {
        if (this.dragonKilled && this.respawnStage == null) {
            for (BlockPattern.BlockPatternMatch b3 = this.findExitPortal(); b3 != null; b3 = this.findExitPortal()) {
                for (int integer4 = 0; integer4 < this.exitPortalPattern.getWidth(); ++integer4) {
                    for (int integer5 = 0; integer5 < this.exitPortalPattern.getHeight(); ++integer5) {
                        for (int integer6 = 0; integer6 < this.exitPortalPattern.getDepth(); ++integer6) {
                            final BlockInWorld cei7 = b3.getBlock(integer4, integer5, integer6);
                            if (cei7.getState().is(Blocks.BEDROCK) || cei7.getState().is(Blocks.END_PORTAL)) {
                                this.level.setBlockAndUpdate(cei7.getPos(), Blocks.END_STONE.defaultBlockState());
                            }
                        }
                    }
                }
            }
            this.respawnStage = DragonRespawnAnimation.START;
            this.respawnTime = 0;
            this.spawnExitPortal(false);
            this.respawnCrystals = list;
        }
    }
    
    public void resetSpikeCrystals() {
        for (final SpikeFeature.EndSpike a3 : SpikeFeature.getSpikesForLevel(this.level)) {
            final List<EndCrystal> list4 = this.level.<EndCrystal>getEntitiesOfClass((java.lang.Class<? extends EndCrystal>)EndCrystal.class, a3.getTopBoundingBox());
            for (final EndCrystal bbn6 : list4) {
                bbn6.setInvulnerable(false);
                bbn6.setBeamTarget(null);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        VALID_PLAYER = EntitySelector.ENTITY_STILL_ALIVE.and((Predicate)EntitySelector.withinDistance(0.0, 128.0, 0.0, 192.0));
    }
}
