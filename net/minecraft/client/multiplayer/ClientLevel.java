package net.minecraft.client.multiplayer;

import net.minecraft.world.Difficulty;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Cursor3D;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.ReportedException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagContainer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.EmptyTickList;
import net.minecraft.world.level.TickList;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReport;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.util.Mth;
import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.world.level.ColorResolver;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import java.util.Map;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.client.player.AbstractClientPlayer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.level.Level;

public class ClientLevel extends Level {
    private final Int2ObjectMap<Entity> entitiesById;
    private final ClientPacketListener connection;
    private final LevelRenderer levelRenderer;
    private final ClientLevelData clientLevelData;
    private final DimensionSpecialEffects effects;
    private final Minecraft minecraft;
    private final List<AbstractClientPlayer> players;
    private Scoreboard scoreboard;
    private final Map<String, MapItemSavedData> mapData;
    private int skyFlashTime;
    private final Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;
    private final ClientChunkCache chunkSource;
    
    public ClientLevel(final ClientPacketListener dwm, final ClientLevelData a, final ResourceKey<Level> vj, final DimensionType cha, final int integer, final Supplier<ProfilerFiller> supplier, final LevelRenderer dzw, final boolean boolean8, final long long9) {
        super(a, vj, cha, supplier, true, boolean8, long9);
        this.entitiesById = (Int2ObjectMap<Entity>)new Int2ObjectOpenHashMap();
        this.minecraft = Minecraft.getInstance();
        this.players = (List<AbstractClientPlayer>)Lists.newArrayList();
        this.scoreboard = new Scoreboard();
        this.mapData = (Map<String, MapItemSavedData>)Maps.newHashMap();
        this.tintCaches = Util.make(new Object2ObjectArrayMap(3), (java.util.function.Consumer<Object2ObjectArrayMap>)(object2ObjectArrayMap -> {
            object2ObjectArrayMap.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache());
            object2ObjectArrayMap.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache());
            object2ObjectArrayMap.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache());
        }));
        this.connection = dwm;
        this.chunkSource = new ClientChunkCache(this, integer);
        this.clientLevelData = a;
        this.levelRenderer = dzw;
        this.effects = DimensionSpecialEffects.forType(cha);
        this.setDefaultSpawnPos(new BlockPos(8, 64, 8), 0.0f);
        this.updateSkyBrightness();
        this.prepareWeather();
    }
    
    public DimensionSpecialEffects effects() {
        return this.effects;
    }
    
    public void tick(final BooleanSupplier booleanSupplier) {
        this.getWorldBorder().tick();
        this.tickTime();
        this.getProfiler().push("blocks");
        this.chunkSource.tick(booleanSupplier);
        this.getProfiler().pop();
    }
    
    private void tickTime() {
        this.setGameTime(this.levelData.getGameTime() + 1L);
        if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            this.setDayTime(this.levelData.getDayTime() + 1L);
        }
    }
    
    public void setGameTime(final long long1) {
        this.clientLevelData.setGameTime(long1);
    }
    
    public void setDayTime(long long1) {
        if (long1 < 0L) {
            long1 = -long1;
            this.getGameRules().<GameRules.BooleanValue>getRule(GameRules.RULE_DAYLIGHT).set(false, null);
        }
        else {
            this.getGameRules().<GameRules.BooleanValue>getRule(GameRules.RULE_DAYLIGHT).set(true, null);
        }
        this.clientLevelData.setDayTime(long1);
    }
    
    public Iterable<Entity> entitiesForRendering() {
        return (Iterable<Entity>)this.entitiesById.values();
    }
    
    public void tickEntities() {
        final ProfilerFiller ant2 = this.getProfiler();
        ant2.push("entities");
        final ObjectIterator<Int2ObjectMap.Entry<Entity>> objectIterator3 = (ObjectIterator<Int2ObjectMap.Entry<Entity>>)this.entitiesById.int2ObjectEntrySet().iterator();
        while (objectIterator3.hasNext()) {
            final Int2ObjectMap.Entry<Entity> entry4 = (Int2ObjectMap.Entry<Entity>)objectIterator3.next();
            final Entity apx5 = (Entity)entry4.getValue();
            if (apx5.isPassenger()) {
                continue;
            }
            ant2.push("tick");
            if (!apx5.removed) {
                this.guardEntityTick((Consumer<Entity>)this::tickNonPassenger, apx5);
            }
            ant2.pop();
            ant2.push("remove");
            if (apx5.removed) {
                objectIterator3.remove();
                this.onEntityRemoved(apx5);
            }
            ant2.pop();
        }
        this.tickBlockEntities();
        ant2.pop();
    }
    
    public void tickNonPassenger(final Entity apx) {
        if (!(apx instanceof Player) && !this.getChunkSource().isEntityTickingChunk(apx)) {
            this.updateChunkPos(apx);
            return;
        }
        apx.setPosAndOldPos(apx.getX(), apx.getY(), apx.getZ());
        apx.yRotO = apx.yRot;
        apx.xRotO = apx.xRot;
        if (apx.inChunk || apx.isSpectator()) {
            ++apx.tickCount;
            this.getProfiler().push((Supplier<String>)(() -> Registry.ENTITY_TYPE.getKey(apx.getType()).toString()));
            apx.tick();
            this.getProfiler().pop();
        }
        this.updateChunkPos(apx);
        if (apx.inChunk) {
            for (final Entity apx2 : apx.getPassengers()) {
                this.tickPassenger(apx, apx2);
            }
        }
    }
    
    public void tickPassenger(final Entity apx1, final Entity apx2) {
        if (apx2.removed || apx2.getVehicle() != apx1) {
            apx2.stopRiding();
            return;
        }
        if (!(apx2 instanceof Player) && !this.getChunkSource().isEntityTickingChunk(apx2)) {
            return;
        }
        apx2.setPosAndOldPos(apx2.getX(), apx2.getY(), apx2.getZ());
        apx2.yRotO = apx2.yRot;
        apx2.xRotO = apx2.xRot;
        if (apx2.inChunk) {
            ++apx2.tickCount;
            apx2.rideTick();
        }
        this.updateChunkPos(apx2);
        if (apx2.inChunk) {
            for (final Entity apx3 : apx2.getPassengers()) {
                this.tickPassenger(apx2, apx3);
            }
        }
    }
    
    private void updateChunkPos(final Entity apx) {
        if (!apx.checkAndResetUpdateChunkPos()) {
            return;
        }
        this.getProfiler().push("chunkCheck");
        final int integer3 = Mth.floor(apx.getX() / 16.0);
        final int integer4 = Mth.floor(apx.getY() / 16.0);
        final int integer5 = Mth.floor(apx.getZ() / 16.0);
        if (!apx.inChunk || apx.xChunk != integer3 || apx.yChunk != integer4 || apx.zChunk != integer5) {
            if (apx.inChunk && this.hasChunk(apx.xChunk, apx.zChunk)) {
                this.getChunk(apx.xChunk, apx.zChunk).removeEntity(apx, apx.yChunk);
            }
            if (apx.checkAndResetForcedChunkAdditionFlag() || this.hasChunk(integer3, integer5)) {
                this.getChunk(integer3, integer5).addEntity(apx);
            }
            else {
                if (apx.inChunk) {
                    ClientLevel.LOGGER.warn("Entity {} left loaded chunk area", apx);
                }
                apx.inChunk = false;
            }
        }
        this.getProfiler().pop();
    }
    
    public void unload(final LevelChunk cge) {
        this.blockEntitiesToUnload.addAll(cge.getBlockEntities().values());
        this.chunkSource.getLightEngine().enableLightSources(cge.getPos(), false);
    }
    
    public void onChunkLoaded(final int integer1, final int integer2) {
        this.tintCaches.forEach((colorResolver, dkm) -> dkm.invalidateForChunk(integer1, integer2));
    }
    
    public void clearTintCaches() {
        this.tintCaches.forEach((colorResolver, dkm) -> dkm.invalidateAll());
    }
    
    public boolean hasChunk(final int integer1, final int integer2) {
        return true;
    }
    
    public int getEntityCount() {
        return this.entitiesById.size();
    }
    
    public void addPlayer(final int integer, final AbstractClientPlayer dzb) {
        this.addEntity(integer, dzb);
        this.players.add(dzb);
    }
    
    public void putNonPlayerEntity(final int integer, final Entity apx) {
        this.addEntity(integer, apx);
    }
    
    private void addEntity(final int integer, final Entity apx) {
        this.removeEntity(integer);
        this.entitiesById.put(integer, apx);
        this.getChunkSource().getChunk(Mth.floor(apx.getX() / 16.0), Mth.floor(apx.getZ() / 16.0), ChunkStatus.FULL, true).addEntity(apx);
    }
    
    public void removeEntity(final int integer) {
        final Entity apx3 = (Entity)this.entitiesById.remove(integer);
        if (apx3 != null) {
            apx3.remove();
            this.onEntityRemoved(apx3);
        }
    }
    
    private void onEntityRemoved(final Entity apx) {
        apx.unRide();
        if (apx.inChunk) {
            this.getChunk(apx.xChunk, apx.zChunk).removeEntity(apx);
        }
        this.players.remove(apx);
    }
    
    public void reAddEntitiesToChunk(final LevelChunk cge) {
        for (final Int2ObjectMap.Entry<Entity> entry4 : this.entitiesById.int2ObjectEntrySet()) {
            final Entity apx5 = (Entity)entry4.getValue();
            final int integer6 = Mth.floor(apx5.getX() / 16.0);
            final int integer7 = Mth.floor(apx5.getZ() / 16.0);
            if (integer6 == cge.getPos().x && integer7 == cge.getPos().z) {
                cge.addEntity(apx5);
            }
        }
    }
    
    @Nullable
    @Override
    public Entity getEntity(final int integer) {
        return (Entity)this.entitiesById.get(integer);
    }
    
    public void setKnownState(final BlockPos fx, final BlockState cee) {
        this.setBlock(fx, cee, 19);
    }
    
    @Override
    public void disconnect() {
        this.connection.getConnection().disconnect(new TranslatableComponent("multiplayer.status.quitting"));
    }
    
    public void animateTick(final int integer1, final int integer2, final int integer3) {
        final int integer4 = 32;
        final Random random6 = new Random();
        boolean boolean7 = false;
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            for (final ItemStack bly9 : this.minecraft.player.getHandSlots()) {
                if (bly9.getItem() == Blocks.BARRIER.asItem()) {
                    boolean7 = true;
                    break;
                }
            }
        }
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        for (int integer5 = 0; integer5 < 667; ++integer5) {
            this.doAnimateTick(integer1, integer2, integer3, 16, random6, boolean7, a8);
            this.doAnimateTick(integer1, integer2, integer3, 32, random6, boolean7, a8);
        }
    }
    
    public void doAnimateTick(final int integer1, final int integer2, final int integer3, final int integer4, final Random random, final boolean boolean6, final BlockPos.MutableBlockPos a) {
        final int integer5 = integer1 + this.random.nextInt(integer4) - this.random.nextInt(integer4);
        final int integer6 = integer2 + this.random.nextInt(integer4) - this.random.nextInt(integer4);
        final int integer7 = integer3 + this.random.nextInt(integer4) - this.random.nextInt(integer4);
        a.set(integer5, integer6, integer7);
        final BlockState cee12 = this.getBlockState(a);
        cee12.getBlock().animateTick(cee12, this, a, random);
        final FluidState cuu13 = this.getFluidState(a);
        if (!cuu13.isEmpty()) {
            cuu13.animateTick(this, a, random);
            final ParticleOptions hf14 = cuu13.getDripParticle();
            if (hf14 != null && this.random.nextInt(10) == 0) {
                final boolean boolean7 = cee12.isFaceSturdy(this, a, Direction.DOWN);
                final BlockPos fx16 = a.below();
                this.trySpawnDripParticles(fx16, this.getBlockState(fx16), hf14, boolean7);
            }
        }
        if (boolean6 && cee12.is(Blocks.BARRIER)) {
            this.addParticle(ParticleTypes.BARRIER, integer5 + 0.5, integer6 + 0.5, integer7 + 0.5, 0.0, 0.0, 0.0);
        }
        if (!cee12.isCollisionShapeFullBlock(this, a)) {
            this.getBiome(a).getAmbientParticle().ifPresent(bsr -> {
                if (bsr.canSpawn(this.random)) {
                    this.addParticle(bsr.getOptions(), a.getX() + this.random.nextDouble(), a.getY() + this.random.nextDouble(), a.getZ() + this.random.nextDouble(), 0.0, 0.0, 0.0);
                }
            });
        }
    }
    
    private void trySpawnDripParticles(final BlockPos fx, final BlockState cee, final ParticleOptions hf, final boolean boolean4) {
        if (!cee.getFluidState().isEmpty()) {
            return;
        }
        final VoxelShape dde6 = cee.getCollisionShape(this, fx);
        final double double7 = dde6.max(Direction.Axis.Y);
        if (double7 < 1.0) {
            if (boolean4) {
                this.spawnFluidParticle(fx.getX(), fx.getX() + 1, fx.getZ(), fx.getZ() + 1, fx.getY() + 1 - 0.05, hf);
            }
        }
        else if (!cee.is(BlockTags.IMPERMEABLE)) {
            final double double8 = dde6.min(Direction.Axis.Y);
            if (double8 > 0.0) {
                this.spawnParticle(fx, hf, dde6, fx.getY() + double8 - 0.05);
            }
            else {
                final BlockPos fx2 = fx.below();
                final BlockState cee2 = this.getBlockState(fx2);
                final VoxelShape dde7 = cee2.getCollisionShape(this, fx2);
                final double double9 = dde7.max(Direction.Axis.Y);
                if (double9 < 1.0 && cee2.getFluidState().isEmpty()) {
                    this.spawnParticle(fx, hf, dde6, fx.getY() - 0.05);
                }
            }
        }
    }
    
    private void spawnParticle(final BlockPos fx, final ParticleOptions hf, final VoxelShape dde, final double double4) {
        this.spawnFluidParticle(fx.getX() + dde.min(Direction.Axis.X), fx.getX() + dde.max(Direction.Axis.X), fx.getZ() + dde.min(Direction.Axis.Z), fx.getZ() + dde.max(Direction.Axis.Z), double4, hf);
    }
    
    private void spawnFluidParticle(final double double1, final double double2, final double double3, final double double4, final double double5, final ParticleOptions hf) {
        this.addParticle(hf, Mth.lerp(this.random.nextDouble(), double1, double2), double5, Mth.lerp(this.random.nextDouble(), double3, double4), 0.0, 0.0, 0.0);
    }
    
    public void removeAllPendingEntityRemovals() {
        final ObjectIterator<Int2ObjectMap.Entry<Entity>> objectIterator2 = (ObjectIterator<Int2ObjectMap.Entry<Entity>>)this.entitiesById.int2ObjectEntrySet().iterator();
        while (objectIterator2.hasNext()) {
            final Int2ObjectMap.Entry<Entity> entry3 = (Int2ObjectMap.Entry<Entity>)objectIterator2.next();
            final Entity apx4 = (Entity)entry3.getValue();
            if (apx4.removed) {
                objectIterator2.remove();
                this.onEntityRemoved(apx4);
            }
        }
    }
    
    @Override
    public CrashReportCategory fillReportDetails(final CrashReport l) {
        final CrashReportCategory m3 = super.fillReportDetails(l);
        m3.setDetail("Server brand", (CrashReportDetail<String>)(() -> this.minecraft.player.getServerBrand()));
        m3.setDetail("Server type", (CrashReportDetail<String>)(() -> (this.minecraft.getSingleplayerServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server"));
        return m3;
    }
    
    @Override
    public void playSound(@Nullable final Player bft, final double double2, final double double3, final double double4, final SoundEvent adn, final SoundSource adp, final float float7, final float float8) {
        if (bft == this.minecraft.player) {
            this.playLocalSound(double2, double3, double4, adn, adp, float7, float8, false);
        }
    }
    
    @Override
    public void playSound(@Nullable final Player bft, final Entity apx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6) {
        if (bft == this.minecraft.player) {
            this.minecraft.getSoundManager().play(new EntityBoundSoundInstance(adn, adp, apx));
        }
    }
    
    public void playLocalSound(final BlockPos fx, final SoundEvent adn, final SoundSource adp, final float float4, final float float5, final boolean boolean6) {
        this.playLocalSound(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, adn, adp, float4, float5, boolean6);
    }
    
    @Override
    public void playLocalSound(final double double1, final double double2, final double double3, final SoundEvent adn, final SoundSource adp, final float float6, final float float7, final boolean boolean8) {
        final double double4 = this.minecraft.gameRenderer.getMainCamera().getPosition().distanceToSqr(double1, double2, double3);
        final SimpleSoundInstance emh15 = new SimpleSoundInstance(adn, adp, float6, float7, double1, double2, double3);
        if (boolean8 && double4 > 100.0) {
            final double double5 = Math.sqrt(double4) / 40.0;
            this.minecraft.getSoundManager().playDelayed(emh15, (int)(double5 * 20.0));
        }
        else {
            this.minecraft.getSoundManager().play(emh15);
        }
    }
    
    @Override
    public void createFireworks(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6, @Nullable final CompoundTag md) {
        this.minecraft.particleEngine.add(new FireworkParticles.Starter(this, double1, double2, double3, double4, double5, double6, this.minecraft.particleEngine, md));
    }
    
    @Override
    public void sendPacketToServer(final Packet<?> oj) {
        this.connection.send(oj);
    }
    
    @Override
    public RecipeManager getRecipeManager() {
        return this.connection.getRecipeManager();
    }
    
    public void setScoreboard(final Scoreboard ddk) {
        this.scoreboard = ddk;
    }
    
    public TickList<Block> getBlockTicks() {
        return EmptyTickList.empty();
    }
    
    public TickList<Fluid> getLiquidTicks() {
        return EmptyTickList.empty();
    }
    
    public ClientChunkCache getChunkSource() {
        return this.chunkSource;
    }
    
    @Nullable
    @Override
    public MapItemSavedData getMapData(final String string) {
        return (MapItemSavedData)this.mapData.get(string);
    }
    
    @Override
    public void setMapData(final MapItemSavedData cxu) {
        this.mapData.put(cxu.getId(), cxu);
    }
    
    @Override
    public int getFreeMapId() {
        return 0;
    }
    
    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    @Override
    public TagContainer getTagManager() {
        return this.connection.getTags();
    }
    
    public RegistryAccess registryAccess() {
        return this.connection.registryAccess();
    }
    
    @Override
    public void sendBlockUpdated(final BlockPos fx, final BlockState cee2, final BlockState cee3, final int integer) {
        this.levelRenderer.blockChanged(this, fx, cee2, cee3, integer);
    }
    
    @Override
    public void setBlocksDirty(final BlockPos fx, final BlockState cee2, final BlockState cee3) {
        this.levelRenderer.setBlockDirty(fx, cee2, cee3);
    }
    
    public void setSectionDirtyWithNeighbors(final int integer1, final int integer2, final int integer3) {
        this.levelRenderer.setSectionDirtyWithNeighbors(integer1, integer2, integer3);
    }
    
    @Override
    public void destroyBlockProgress(final int integer1, final BlockPos fx, final int integer3) {
        this.levelRenderer.destroyBlockProgress(integer1, fx, integer3);
    }
    
    @Override
    public void globalLevelEvent(final int integer1, final BlockPos fx, final int integer3) {
        this.levelRenderer.globalLevelEvent(integer1, fx, integer3);
    }
    
    public void levelEvent(@Nullable final Player bft, final int integer2, final BlockPos fx, final int integer4) {
        try {
            this.levelRenderer.levelEvent(bft, integer2, fx, integer4);
        }
        catch (Throwable throwable6) {
            final CrashReport l7 = CrashReport.forThrowable(throwable6, "Playing level event");
            final CrashReportCategory m8 = l7.addCategory("Level event being played");
            m8.setDetail("Block coordinates", CrashReportCategory.formatLocation(fx));
            m8.setDetail("Event source", bft);
            m8.setDetail("Event type", integer2);
            m8.setDetail("Event data", integer4);
            throw new ReportedException(l7);
        }
    }
    
    @Override
    public void addParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        this.levelRenderer.addParticle(hf, hf.getType().getOverrideLimiter(), double2, double3, double4, double5, double6, double7);
    }
    
    @Override
    public void addParticle(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
        this.levelRenderer.addParticle(hf, hf.getType().getOverrideLimiter() || boolean2, double3, double4, double5, double6, double7, double8);
    }
    
    @Override
    public void addAlwaysVisibleParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        this.levelRenderer.addParticle(hf, false, true, double2, double3, double4, double5, double6, double7);
    }
    
    @Override
    public void addAlwaysVisibleParticle(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
        this.levelRenderer.addParticle(hf, hf.getType().getOverrideLimiter() || boolean2, true, double3, double4, double5, double6, double7, double8);
    }
    
    public List<AbstractClientPlayer> players() {
        return this.players;
    }
    
    public Biome getUncachedNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return this.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOrThrow(Biomes.PLAINS);
    }
    
    public float getSkyDarken(final float float1) {
        final float float2 = this.getTimeOfDay(float1);
        float float3 = 1.0f - (Mth.cos(float2 * 6.2831855f) * 2.0f + 0.2f);
        float3 = Mth.clamp(float3, 0.0f, 1.0f);
        float3 = 1.0f - float3;
        float3 *= (float)(1.0 - this.getRainLevel(float1) * 5.0f / 16.0);
        float3 *= (float)(1.0 - this.getThunderLevel(float1) * 5.0f / 16.0);
        return float3 * 0.8f + 0.2f;
    }
    
    public Vec3 getSkyColor(final BlockPos fx, final float float2) {
        final float float3 = this.getTimeOfDay(float2);
        float float4 = Mth.cos(float3 * 6.2831855f) * 2.0f + 0.5f;
        float4 = Mth.clamp(float4, 0.0f, 1.0f);
        final Biome bss6 = this.getBiome(fx);
        final int integer7 = bss6.getSkyColor();
        float float5 = (integer7 >> 16 & 0xFF) / 255.0f;
        float float6 = (integer7 >> 8 & 0xFF) / 255.0f;
        float float7 = (integer7 & 0xFF) / 255.0f;
        float5 *= float4;
        float6 *= float4;
        float7 *= float4;
        final float float8 = this.getRainLevel(float2);
        if (float8 > 0.0f) {
            final float float9 = (float5 * 0.3f + float6 * 0.59f + float7 * 0.11f) * 0.6f;
            final float float10 = 1.0f - float8 * 0.75f;
            float5 = float5 * float10 + float9 * (1.0f - float10);
            float6 = float6 * float10 + float9 * (1.0f - float10);
            float7 = float7 * float10 + float9 * (1.0f - float10);
        }
        final float float9 = this.getThunderLevel(float2);
        if (float9 > 0.0f) {
            final float float10 = (float5 * 0.3f + float6 * 0.59f + float7 * 0.11f) * 0.2f;
            final float float11 = 1.0f - float9 * 0.75f;
            float5 = float5 * float11 + float10 * (1.0f - float11);
            float6 = float6 * float11 + float10 * (1.0f - float11);
            float7 = float7 * float11 + float10 * (1.0f - float11);
        }
        if (this.skyFlashTime > 0) {
            float float10 = this.skyFlashTime - float2;
            if (float10 > 1.0f) {
                float10 = 1.0f;
            }
            float10 *= 0.45f;
            float5 = float5 * (1.0f - float10) + 0.8f * float10;
            float6 = float6 * (1.0f - float10) + 0.8f * float10;
            float7 = float7 * (1.0f - float10) + 1.0f * float10;
        }
        return new Vec3(float5, float6, float7);
    }
    
    public Vec3 getCloudColor(final float float1) {
        final float float2 = this.getTimeOfDay(float1);
        float float3 = Mth.cos(float2 * 6.2831855f) * 2.0f + 0.5f;
        float3 = Mth.clamp(float3, 0.0f, 1.0f);
        float float4 = 1.0f;
        float float5 = 1.0f;
        float float6 = 1.0f;
        final float float7 = this.getRainLevel(float1);
        if (float7 > 0.0f) {
            final float float8 = (float4 * 0.3f + float5 * 0.59f + float6 * 0.11f) * 0.6f;
            final float float9 = 1.0f - float7 * 0.95f;
            float4 = float4 * float9 + float8 * (1.0f - float9);
            float5 = float5 * float9 + float8 * (1.0f - float9);
            float6 = float6 * float9 + float8 * (1.0f - float9);
        }
        float4 *= float3 * 0.9f + 0.1f;
        float5 *= float3 * 0.9f + 0.1f;
        float6 *= float3 * 0.85f + 0.15f;
        final float float8 = this.getThunderLevel(float1);
        if (float8 > 0.0f) {
            final float float9 = (float4 * 0.3f + float5 * 0.59f + float6 * 0.11f) * 0.2f;
            final float float10 = 1.0f - float8 * 0.95f;
            float4 = float4 * float10 + float9 * (1.0f - float10);
            float5 = float5 * float10 + float9 * (1.0f - float10);
            float6 = float6 * float10 + float9 * (1.0f - float10);
        }
        return new Vec3(float4, float5, float6);
    }
    
    public float getStarBrightness(final float float1) {
        final float float2 = this.getTimeOfDay(float1);
        float float3 = 1.0f - (Mth.cos(float2 * 6.2831855f) * 2.0f + 0.25f);
        float3 = Mth.clamp(float3, 0.0f, 1.0f);
        return float3 * float3 * 0.5f;
    }
    
    public int getSkyFlashTime() {
        return this.skyFlashTime;
    }
    
    @Override
    public void setSkyFlashTime(final int integer) {
        this.skyFlashTime = integer;
    }
    
    public float getShade(final Direction gc, final boolean boolean2) {
        final boolean boolean3 = this.effects().constantAmbientLight();
        if (!boolean2) {
            return boolean3 ? 0.9f : 1.0f;
        }
        switch (gc) {
            case DOWN: {
                return boolean3 ? 0.9f : 0.5f;
            }
            case UP: {
                return boolean3 ? 0.9f : 1.0f;
            }
            case NORTH:
            case SOUTH: {
                return 0.8f;
            }
            case WEST:
            case EAST: {
                return 0.6f;
            }
            default: {
                return 1.0f;
            }
        }
    }
    
    public int getBlockTint(final BlockPos fx, final ColorResolver colorResolver) {
        final BlockTintCache dkm4 = (BlockTintCache)this.tintCaches.get(colorResolver);
        return dkm4.getColor(fx, () -> this.calculateBlockTint(fx, colorResolver));
    }
    
    public int calculateBlockTint(final BlockPos fx, final ColorResolver colorResolver) {
        final int integer4 = Minecraft.getInstance().options.biomeBlendRadius;
        if (integer4 == 0) {
            return colorResolver.getColor(this.getBiome(fx), fx.getX(), fx.getZ());
        }
        final int integer5 = (integer4 * 2 + 1) * (integer4 * 2 + 1);
        int integer6 = 0;
        int integer7 = 0;
        int integer8 = 0;
        final Cursor3D ga9 = new Cursor3D(fx.getX() - integer4, fx.getY(), fx.getZ() - integer4, fx.getX() + integer4, fx.getY(), fx.getZ() + integer4);
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        while (ga9.advance()) {
            a10.set(ga9.nextX(), ga9.nextY(), ga9.nextZ());
            final int integer9 = colorResolver.getColor(this.getBiome(a10), a10.getX(), a10.getZ());
            integer6 += (integer9 & 0xFF0000) >> 16;
            integer7 += (integer9 & 0xFF00) >> 8;
            integer8 += (integer9 & 0xFF);
        }
        return (integer6 / integer5 & 0xFF) << 16 | (integer7 / integer5 & 0xFF) << 8 | (integer8 / integer5 & 0xFF);
    }
    
    public BlockPos getSharedSpawnPos() {
        BlockPos fx2 = new BlockPos(this.levelData.getXSpawn(), this.levelData.getYSpawn(), this.levelData.getZSpawn());
        if (!this.getWorldBorder().isWithinBounds(fx2)) {
            fx2 = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return fx2;
    }
    
    public float getSharedSpawnAngle() {
        return this.levelData.getSpawnAngle();
    }
    
    public void setDefaultSpawnPos(final BlockPos fx, final float float2) {
        this.levelData.setSpawn(fx, float2);
    }
    
    public String toString() {
        return "ClientLevel";
    }
    
    @Override
    public ClientLevelData getLevelData() {
        return this.clientLevelData;
    }
    
    public static class ClientLevelData implements WritableLevelData {
        private final boolean hardcore;
        private final GameRules gameRules;
        private final boolean isFlat;
        private int xSpawn;
        private int ySpawn;
        private int zSpawn;
        private float spawnAngle;
        private long gameTime;
        private long dayTime;
        private boolean raining;
        private Difficulty difficulty;
        private boolean difficultyLocked;
        
        public ClientLevelData(final Difficulty aoo, final boolean boolean2, final boolean boolean3) {
            this.difficulty = aoo;
            this.hardcore = boolean2;
            this.isFlat = boolean3;
            this.gameRules = new GameRules();
        }
        
        public int getXSpawn() {
            return this.xSpawn;
        }
        
        public int getYSpawn() {
            return this.ySpawn;
        }
        
        public int getZSpawn() {
            return this.zSpawn;
        }
        
        public float getSpawnAngle() {
            return this.spawnAngle;
        }
        
        public long getGameTime() {
            return this.gameTime;
        }
        
        public long getDayTime() {
            return this.dayTime;
        }
        
        public void setXSpawn(final int integer) {
            this.xSpawn = integer;
        }
        
        public void setYSpawn(final int integer) {
            this.ySpawn = integer;
        }
        
        public void setZSpawn(final int integer) {
            this.zSpawn = integer;
        }
        
        public void setSpawnAngle(final float float1) {
            this.spawnAngle = float1;
        }
        
        public void setGameTime(final long long1) {
            this.gameTime = long1;
        }
        
        public void setDayTime(final long long1) {
            this.dayTime = long1;
        }
        
        public void setSpawn(final BlockPos fx, final float float2) {
            this.xSpawn = fx.getX();
            this.ySpawn = fx.getY();
            this.zSpawn = fx.getZ();
            this.spawnAngle = float2;
        }
        
        public boolean isThundering() {
            return false;
        }
        
        public boolean isRaining() {
            return this.raining;
        }
        
        public void setRaining(final boolean boolean1) {
            this.raining = boolean1;
        }
        
        public boolean isHardcore() {
            return this.hardcore;
        }
        
        public GameRules getGameRules() {
            return this.gameRules;
        }
        
        public Difficulty getDifficulty() {
            return this.difficulty;
        }
        
        public boolean isDifficultyLocked() {
            return this.difficultyLocked;
        }
        
        public void fillCrashReportCategory(final CrashReportCategory m) {
            super.fillCrashReportCategory(m);
        }
        
        public void setDifficulty(final Difficulty aoo) {
            this.difficulty = aoo;
        }
        
        public void setDifficultyLocked(final boolean boolean1) {
            this.difficultyLocked = boolean1;
        }
        
        public double getHorizonHeight() {
            if (this.isFlat) {
                return 0.0;
            }
            return 63.0;
        }
        
        public double getClearColorScale() {
            if (this.isFlat) {
                return 1.0;
            }
            return 0.03125;
        }
    }
}
