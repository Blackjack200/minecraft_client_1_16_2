package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.RecipeBook;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.OptionalInt;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import java.util.function.Predicate;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.util.Unit;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.core.Direction;
import net.minecraft.BlockUtil;
import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.ChatType;
import net.minecraft.world.scores.Team;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.scores.Score;
import java.util.function.Consumer;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.world.item.ComplexItem;
import java.util.Iterator;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.item.ServerItemCooldowns;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import net.minecraft.Util;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.SectionPos;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.server.PlayerAdvancements;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.entity.player.Player;

public class ServerPlayer extends Player implements ContainerListener {
    private static final Logger LOGGER;
    public ServerGamePacketListenerImpl connection;
    public final MinecraftServer server;
    public final ServerPlayerGameMode gameMode;
    private final List<Integer> entitiesToRemove;
    private final PlayerAdvancements advancements;
    private final ServerStatsCounter stats;
    private float lastRecordedHealthAndAbsorption;
    private int lastRecordedFoodLevel;
    private int lastRecordedAirLevel;
    private int lastRecordedArmor;
    private int lastRecordedLevel;
    private int lastRecordedExperience;
    private float lastSentHealth;
    private int lastSentFood;
    private boolean lastFoodSaturationZero;
    private int lastSentExp;
    private int spawnInvulnerableTime;
    private ChatVisiblity chatVisibility;
    private boolean canChatColor;
    private long lastActionTime;
    private Entity camera;
    private boolean isChangingDimension;
    private boolean seenCredits;
    private final ServerRecipeBook recipeBook;
    private Vec3 levitationStartPos;
    private int levitationStartTime;
    private boolean disconnected;
    @Nullable
    private Vec3 enteredNetherPosition;
    private SectionPos lastSectionPos;
    private ResourceKey<Level> respawnDimension;
    @Nullable
    private BlockPos respawnPosition;
    private boolean respawnForced;
    private float respawnAngle;
    private int containerCounter;
    public boolean ignoreSlotUpdateHack;
    public int latency;
    public boolean wonGame;
    
    public ServerPlayer(final MinecraftServer minecraftServer, final ServerLevel aag, final GameProfile gameProfile, final ServerPlayerGameMode aai) {
        super(aag, aag.getSharedSpawnPos(), aag.getSharedSpawnAngle(), gameProfile);
        this.entitiesToRemove = (List<Integer>)Lists.newLinkedList();
        this.lastRecordedHealthAndAbsorption = Float.MIN_VALUE;
        this.lastRecordedFoodLevel = Integer.MIN_VALUE;
        this.lastRecordedAirLevel = Integer.MIN_VALUE;
        this.lastRecordedArmor = Integer.MIN_VALUE;
        this.lastRecordedLevel = Integer.MIN_VALUE;
        this.lastRecordedExperience = Integer.MIN_VALUE;
        this.lastSentHealth = -1.0E8f;
        this.lastSentFood = -99999999;
        this.lastFoodSaturationZero = true;
        this.lastSentExp = -99999999;
        this.spawnInvulnerableTime = 60;
        this.canChatColor = true;
        this.lastActionTime = Util.getMillis();
        this.recipeBook = new ServerRecipeBook();
        this.lastSectionPos = SectionPos.of(0, 0, 0);
        this.respawnDimension = Level.OVERWORLD;
        aai.player = this;
        this.gameMode = aai;
        this.server = minecraftServer;
        this.stats = minecraftServer.getPlayerList().getPlayerStats(this);
        this.advancements = minecraftServer.getPlayerList().getPlayerAdvancements(this);
        this.maxUpStep = 1.0f;
        this.fudgeSpawnLocation(aag);
    }
    
    private void fudgeSpawnLocation(final ServerLevel aag) {
        final BlockPos fx3 = aag.getSharedSpawnPos();
        if (aag.dimensionType().hasSkyLight() && aag.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
            int integer4 = Math.max(0, this.server.getSpawnRadius(aag));
            final int integer5 = Mth.floor(aag.getWorldBorder().getDistanceToBorder(fx3.getX(), fx3.getZ()));
            if (integer5 < integer4) {
                integer4 = integer5;
            }
            if (integer5 <= 1) {
                integer4 = 1;
            }
            final long long6 = integer4 * 2 + 1;
            final long long7 = long6 * long6;
            final int integer6 = (long7 > 2147483647L) ? Integer.MAX_VALUE : ((int)long7);
            final int integer7 = this.getCoprime(integer6);
            final int integer8 = new Random().nextInt(integer6);
            for (int integer9 = 0; integer9 < integer6; ++integer9) {
                final int integer10 = (integer8 + integer7 * integer9) % integer6;
                final int integer11 = integer10 % (integer4 * 2 + 1);
                final int integer12 = integer10 / (integer4 * 2 + 1);
                final BlockPos fx4 = PlayerRespawnLogic.getOverworldRespawnPos(aag, fx3.getX() + integer11 - integer4, fx3.getZ() + integer12 - integer4, false);
                if (fx4 != null) {
                    this.moveTo(fx4, 0.0f, 0.0f);
                    if (aag.noCollision(this)) {
                        break;
                    }
                }
            }
        }
        else {
            this.moveTo(fx3, 0.0f, 0.0f);
            while (!aag.noCollision(this) && this.getY() < 255.0) {
                this.setPos(this.getX(), this.getY() + 1.0, this.getZ());
            }
        }
    }
    
    private int getCoprime(final int integer) {
        return (integer <= 16) ? (integer - 1) : 17;
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("playerGameType", 99)) {
            if (this.getServer().getForceGameType()) {
                this.gameMode.setGameModeForPlayer(this.getServer().getDefaultGameType(), GameType.NOT_SET);
            }
            else {
                this.gameMode.setGameModeForPlayer(GameType.byId(md.getInt("playerGameType")), md.contains("previousPlayerGameType", 3) ? GameType.byId(md.getInt("previousPlayerGameType")) : GameType.NOT_SET);
            }
        }
        if (md.contains("enteredNetherPosition", 10)) {
            final CompoundTag md2 = md.getCompound("enteredNetherPosition");
            this.enteredNetherPosition = new Vec3(md2.getDouble("x"), md2.getDouble("y"), md2.getDouble("z"));
        }
        this.seenCredits = md.getBoolean("seenCredits");
        if (md.contains("recipeBook", 10)) {
            this.recipeBook.fromNbt(md.getCompound("recipeBook"), this.server.getRecipeManager());
        }
        if (this.isSleeping()) {
            this.stopSleeping();
        }
        if (md.contains("SpawnX", 99) && md.contains("SpawnY", 99) && md.contains("SpawnZ", 99)) {
            this.respawnPosition = new BlockPos(md.getInt("SpawnX"), md.getInt("SpawnY"), md.getInt("SpawnZ"));
            this.respawnForced = md.getBoolean("SpawnForced");
            this.respawnAngle = md.getFloat("SpawnAngle");
            if (md.contains("SpawnDimension")) {
                this.respawnDimension = (ResourceKey<Level>)Level.RESOURCE_KEY_CODEC.parse((DynamicOps)NbtOps.INSTANCE, md.get("SpawnDimension")).resultOrPartial(ServerPlayer.LOGGER::error).orElse(Level.OVERWORLD);
            }
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("playerGameType", this.gameMode.getGameModeForPlayer().getId());
        md.putInt("previousPlayerGameType", this.gameMode.getPreviousGameModeForPlayer().getId());
        md.putBoolean("seenCredits", this.seenCredits);
        if (this.enteredNetherPosition != null) {
            final CompoundTag md2 = new CompoundTag();
            md2.putDouble("x", this.enteredNetherPosition.x);
            md2.putDouble("y", this.enteredNetherPosition.y);
            md2.putDouble("z", this.enteredNetherPosition.z);
            md.put("enteredNetherPosition", (Tag)md2);
        }
        final Entity apx3 = this.getRootVehicle();
        final Entity apx4 = this.getVehicle();
        if (apx4 != null && apx3 != this && apx3.hasOnePlayerPassenger()) {
            final CompoundTag md3 = new CompoundTag();
            final CompoundTag md4 = new CompoundTag();
            apx3.save(md4);
            md3.putUUID("Attach", apx4.getUUID());
            md3.put("Entity", (Tag)md4);
            md.put("RootVehicle", (Tag)md3);
        }
        md.put("recipeBook", (Tag)this.recipeBook.toNbt());
        md.putString("Dimension", this.level.dimension().location().toString());
        if (this.respawnPosition != null) {
            md.putInt("SpawnX", this.respawnPosition.getX());
            md.putInt("SpawnY", this.respawnPosition.getY());
            md.putInt("SpawnZ", this.respawnPosition.getZ());
            md.putBoolean("SpawnForced", this.respawnForced);
            md.putFloat("SpawnAngle", this.respawnAngle);
            ResourceLocation.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.respawnDimension.location()).resultOrPartial(ServerPlayer.LOGGER::error).ifPresent(mt -> md.put("SpawnDimension", mt));
        }
    }
    
    public void setExperiencePoints(final int integer) {
        final float float3 = (float)this.getXpNeededForNextLevel();
        final float float4 = (float3 - 1.0f) / float3;
        this.experienceProgress = Mth.clamp(integer / float3, 0.0f, float4);
        this.lastSentExp = -1;
    }
    
    public void setExperienceLevels(final int integer) {
        this.experienceLevel = integer;
        this.lastSentExp = -1;
    }
    
    @Override
    public void giveExperienceLevels(final int integer) {
        super.giveExperienceLevels(integer);
        this.lastSentExp = -1;
    }
    
    @Override
    public void onEnchantmentPerformed(final ItemStack bly, final int integer) {
        super.onEnchantmentPerformed(bly, integer);
        this.lastSentExp = -1;
    }
    
    public void initMenu() {
        this.containerMenu.addSlotListener(this);
    }
    
    @Override
    public void onEnterCombat() {
        super.onEnterCombat();
        this.connection.send(new ClientboundPlayerCombatPacket(this.getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTER_COMBAT));
    }
    
    @Override
    public void onLeaveCombat() {
        super.onLeaveCombat();
        this.connection.send(new ClientboundPlayerCombatPacket(this.getCombatTracker(), ClientboundPlayerCombatPacket.Event.END_COMBAT));
    }
    
    protected void onInsideBlock(final BlockState cee) {
        CriteriaTriggers.ENTER_BLOCK.trigger(this, cee);
    }
    
    @Override
    protected ItemCooldowns createItemCooldowns() {
        return new ServerItemCooldowns(this);
    }
    
    @Override
    public void tick() {
        this.gameMode.tick();
        --this.spawnInvulnerableTime;
        if (this.invulnerableTime > 0) {
            --this.invulnerableTime;
        }
        this.containerMenu.broadcastChanges();
        if (!this.level.isClientSide && !this.containerMenu.stillValid(this)) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }
        while (!this.entitiesToRemove.isEmpty()) {
            final int integer2 = Math.min(this.entitiesToRemove.size(), Integer.MAX_VALUE);
            final int[] arr3 = new int[integer2];
            final Iterator<Integer> iterator4 = (Iterator<Integer>)this.entitiesToRemove.iterator();
            int integer3 = 0;
            while (iterator4.hasNext() && integer3 < integer2) {
                arr3[integer3++] = (int)iterator4.next();
                iterator4.remove();
            }
            this.connection.send(new ClientboundRemoveEntitiesPacket(arr3));
        }
        final Entity apx2 = this.getCamera();
        if (apx2 != this) {
            if (apx2.isAlive()) {
                this.absMoveTo(apx2.getX(), apx2.getY(), apx2.getZ(), apx2.yRot, apx2.xRot);
                this.getLevel().getChunkSource().move(this);
                if (this.wantsToStopRiding()) {
                    this.setCamera(this);
                }
            }
            else {
                this.setCamera(this);
            }
        }
        CriteriaTriggers.TICK.trigger(this);
        if (this.levitationStartPos != null) {
            CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.tickCount - this.levitationStartTime);
        }
        this.advancements.flushDirty(this);
    }
    
    public void doTick() {
        try {
            if (!this.isSpectator() || this.level.hasChunkAt(this.blockPosition())) {
                super.tick();
            }
            for (int integer2 = 0; integer2 < this.inventory.getContainerSize(); ++integer2) {
                final ItemStack bly3 = this.inventory.getItem(integer2);
                if (bly3.getItem().isComplex()) {
                    final Packet<?> oj4 = ((ComplexItem)bly3.getItem()).getUpdatePacket(bly3, this.level, this);
                    if (oj4 != null) {
                        this.connection.send(oj4);
                    }
                }
            }
            if (this.getHealth() != this.lastSentHealth || this.lastSentFood != this.foodData.getFoodLevel() || this.foodData.getSaturationLevel() == 0.0f != this.lastFoodSaturationZero) {
                this.connection.send(new ClientboundSetHealthPacket(this.getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
                this.lastSentHealth = this.getHealth();
                this.lastSentFood = this.foodData.getFoodLevel();
                this.lastFoodSaturationZero = (this.foodData.getSaturationLevel() == 0.0f);
            }
            if (this.getHealth() + this.getAbsorptionAmount() != this.lastRecordedHealthAndAbsorption) {
                this.lastRecordedHealthAndAbsorption = this.getHealth() + this.getAbsorptionAmount();
                this.updateScoreForCriteria(ObjectiveCriteria.HEALTH, Mth.ceil(this.lastRecordedHealthAndAbsorption));
            }
            if (this.foodData.getFoodLevel() != this.lastRecordedFoodLevel) {
                this.lastRecordedFoodLevel = this.foodData.getFoodLevel();
                this.updateScoreForCriteria(ObjectiveCriteria.FOOD, Mth.ceil((float)this.lastRecordedFoodLevel));
            }
            if (this.getAirSupply() != this.lastRecordedAirLevel) {
                this.lastRecordedAirLevel = this.getAirSupply();
                this.updateScoreForCriteria(ObjectiveCriteria.AIR, Mth.ceil((float)this.lastRecordedAirLevel));
            }
            if (this.getArmorValue() != this.lastRecordedArmor) {
                this.lastRecordedArmor = this.getArmorValue();
                this.updateScoreForCriteria(ObjectiveCriteria.ARMOR, Mth.ceil((float)this.lastRecordedArmor));
            }
            if (this.totalExperience != this.lastRecordedExperience) {
                this.lastRecordedExperience = this.totalExperience;
                this.updateScoreForCriteria(ObjectiveCriteria.EXPERIENCE, Mth.ceil((float)this.lastRecordedExperience));
            }
            if (this.experienceLevel != this.lastRecordedLevel) {
                this.lastRecordedLevel = this.experienceLevel;
                this.updateScoreForCriteria(ObjectiveCriteria.LEVEL, Mth.ceil((float)this.lastRecordedLevel));
            }
            if (this.totalExperience != this.lastSentExp) {
                this.lastSentExp = this.totalExperience;
                this.connection.send(new ClientboundSetExperiencePacket(this.experienceProgress, this.totalExperience, this.experienceLevel));
            }
            if (this.tickCount % 20 == 0) {
                CriteriaTriggers.LOCATION.trigger(this);
            }
        }
        catch (Throwable throwable2) {
            final CrashReport l3 = CrashReport.forThrowable(throwable2, "Ticking player");
            final CrashReportCategory m4 = l3.addCategory("Player being ticked");
            this.fillCrashReportCategory(m4);
            throw new ReportedException(l3);
        }
    }
    
    private void updateScoreForCriteria(final ObjectiveCriteria ddn, final int integer) {
        this.getScoreboard().forAllObjectives(ddn, this.getScoreboardName(), (Consumer<Score>)(ddj -> ddj.setScore(integer)));
    }
    
    @Override
    public void die(final DamageSource aph) {
        final boolean boolean3 = this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
        if (boolean3) {
            final Component nr4 = this.getCombatTracker().getDeathMessage();
            this.connection.send(new ClientboundPlayerCombatPacket(this.getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED, nr4), (future -> {
                if (!future.isSuccess()) {
                    final int integer4 = 256;
                    final String string5 = nr4.getString(256);
                    final Component nr2 = new TranslatableComponent("death.attack.message_too_long", new Object[] { new TextComponent(string5).withStyle(ChatFormatting.YELLOW) });
                    final Component nr3 = new TranslatableComponent("death.attack.even_more_magic", new Object[] { this.getDisplayName() }).withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)nr2))));
                    this.connection.send(new ClientboundPlayerCombatPacket(this.getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED, nr3));
                }
            }));
            final Team ddm5 = this.getTeam();
            if (ddm5 == null || ddm5.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
                this.server.getPlayerList().broadcastMessage(nr4, ChatType.SYSTEM, Util.NIL_UUID);
            }
            else if (ddm5.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
                this.server.getPlayerList().broadcastToTeam(this, nr4);
            }
            else if (ddm5.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
                this.server.getPlayerList().broadcastToAllExceptTeam(this, nr4);
            }
        }
        else {
            this.connection.send(new ClientboundPlayerCombatPacket(this.getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED));
        }
        this.removeEntitiesOnShoulder();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            this.tellNeutralMobsThatIDied();
        }
        if (!this.isSpectator()) {
            this.dropAllDeathLoot(aph);
        }
        this.getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, this.getScoreboardName(), (Consumer<Score>)Score::increment);
        final LivingEntity aqj4 = this.getKillCredit();
        if (aqj4 != null) {
            this.awardStat(Stats.ENTITY_KILLED_BY.get(aqj4.getType()));
            aqj4.awardKillScore(this, this.deathScore, aph);
            this.createWitherRose(aqj4);
        }
        this.level.broadcastEntityEvent(this, (byte)3);
        this.awardStat(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.clearFire();
        this.setSharedFlag(0, false);
        this.getCombatTracker().recheckStatus();
    }
    
    private void tellNeutralMobsThatIDied() {
        final AABB dcf2 = new AABB(this.blockPosition()).inflate(32.0, 10.0, 32.0);
        this.level.<Entity>getLoadedEntitiesOfClass((java.lang.Class<? extends Entity>)Mob.class, dcf2).stream().filter(aqk -> aqk instanceof NeutralMob).forEach(aqk -> ((NeutralMob)aqk).playerDied(this));
    }
    
    public void awardKillScore(final Entity apx, final int integer, final DamageSource aph) {
        if (apx == this) {
            return;
        }
        super.awardKillScore(apx, integer, aph);
        this.increaseScore(integer);
        final String string5 = this.getScoreboardName();
        final String string6 = apx.getScoreboardName();
        this.getScoreboard().forAllObjectives(ObjectiveCriteria.KILL_COUNT_ALL, string5, (Consumer<Score>)Score::increment);
        if (apx instanceof Player) {
            this.awardStat(Stats.PLAYER_KILLS);
            this.getScoreboard().forAllObjectives(ObjectiveCriteria.KILL_COUNT_PLAYERS, string5, (Consumer<Score>)Score::increment);
        }
        else {
            this.awardStat(Stats.MOB_KILLS);
        }
        this.handleTeamKill(string5, string6, ObjectiveCriteria.TEAM_KILL);
        this.handleTeamKill(string6, string5, ObjectiveCriteria.KILLED_BY_TEAM);
        CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, apx, aph);
    }
    
    private void handleTeamKill(final String string1, final String string2, final ObjectiveCriteria[] arr) {
        final PlayerTeam ddi5 = this.getScoreboard().getPlayersTeam(string2);
        if (ddi5 != null) {
            final int integer6 = ddi5.getColor().getId();
            if (integer6 >= 0 && integer6 < arr.length) {
                this.getScoreboard().forAllObjectives(arr[integer6], string1, (Consumer<Score>)Score::increment);
            }
        }
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        final boolean boolean4 = this.server.isDedicatedServer() && this.isPvpAllowed() && "fall".equals(aph.msgId);
        if (!boolean4 && this.spawnInvulnerableTime > 0 && aph != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (aph instanceof EntityDamageSource) {
            final Entity apx5 = aph.getEntity();
            if (apx5 instanceof Player && !this.canHarmPlayer((Player)apx5)) {
                return false;
            }
            if (apx5 instanceof AbstractArrow) {
                final AbstractArrow bfx6 = (AbstractArrow)apx5;
                final Entity apx6 = bfx6.getOwner();
                if (apx6 instanceof Player && !this.canHarmPlayer((Player)apx6)) {
                    return false;
                }
            }
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    public boolean canHarmPlayer(final Player bft) {
        return this.isPvpAllowed() && super.canHarmPlayer(bft);
    }
    
    private boolean isPvpAllowed() {
        return this.server.isPvpAllowed();
    }
    
    @Nullable
    protected PortalInfo findDimensionEntryPoint(final ServerLevel aag) {
        final PortalInfo cxj3 = super.findDimensionEntryPoint(aag);
        if (cxj3 != null && this.level.dimension() == Level.OVERWORLD && aag.dimension() == Level.END) {
            final Vec3 dck4 = cxj3.pos.add(0.0, -1.0, 0.0);
            return new PortalInfo(dck4, Vec3.ZERO, 90.0f, 0.0f);
        }
        return cxj3;
    }
    
    @Nullable
    public Entity changeDimension(final ServerLevel aag) {
        this.isChangingDimension = true;
        final ServerLevel aag2 = this.getLevel();
        final ResourceKey<Level> vj4 = aag2.dimension();
        if (vj4 == Level.END && aag.dimension() == Level.OVERWORLD) {
            this.unRide();
            this.getLevel().removePlayerImmediately(this);
            if (!this.wonGame) {
                this.wonGame = true;
                this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, this.seenCredits ? 0.0f : 1.0f));
                this.seenCredits = true;
            }
            return this;
        }
        final LevelData cya5 = aag.getLevelData();
        this.connection.send(new ClientboundRespawnPacket(aag.dimensionType(), aag.dimension(), BiomeManager.obfuscateSeed(aag.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), aag.isDebug(), aag.isFlat(), true));
        this.connection.send(new ClientboundChangeDifficultyPacket(cya5.getDifficulty(), cya5.isDifficultyLocked()));
        final PlayerList acs6 = this.server.getPlayerList();
        acs6.sendPlayerPermissionLevel(this);
        aag2.removePlayerImmediately(this);
        this.removed = false;
        final PortalInfo cxj7 = this.findDimensionEntryPoint(aag);
        if (cxj7 != null) {
            aag2.getProfiler().push("moving");
            if (vj4 == Level.OVERWORLD && aag.dimension() == Level.NETHER) {
                this.enteredNetherPosition = this.position();
            }
            else if (aag.dimension() == Level.END) {
                this.createEndPlatform(aag, new BlockPos(cxj7.pos));
            }
            aag2.getProfiler().pop();
            aag2.getProfiler().push("placing");
            this.setLevel(aag);
            aag.addDuringPortalTeleport(this);
            this.setRot(cxj7.yRot, cxj7.xRot);
            this.moveTo(cxj7.pos.x, cxj7.pos.y, cxj7.pos.z);
            aag2.getProfiler().pop();
            this.triggerDimensionChangeTriggers(aag2);
            this.gameMode.setLevel(aag);
            this.connection.send(new ClientboundPlayerAbilitiesPacket(this.abilities));
            acs6.sendLevelInfo(this, aag);
            acs6.sendAllPlayerInfo(this);
            for (final MobEffectInstance apr9 : this.getActiveEffects()) {
                this.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), apr9));
            }
            this.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
            this.lastSentExp = -1;
            this.lastSentHealth = -1.0f;
            this.lastSentFood = -1;
        }
        return this;
    }
    
    private void createEndPlatform(final ServerLevel aag, final BlockPos fx) {
        final BlockPos.MutableBlockPos a4 = fx.mutable();
        for (int integer5 = -2; integer5 <= 2; ++integer5) {
            for (int integer6 = -2; integer6 <= 2; ++integer6) {
                for (int integer7 = -1; integer7 < 3; ++integer7) {
                    final BlockState cee8 = (integer7 == -1) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    aag.setBlockAndUpdate(a4.set(fx).move(integer6, integer7, integer5), cee8);
                }
            }
        }
    }
    
    protected Optional<BlockUtil.FoundRectangle> getExitPortal(final ServerLevel aag, final BlockPos fx, final boolean boolean3) {
        final Optional<BlockUtil.FoundRectangle> optional5 = super.getExitPortal(aag, fx, boolean3);
        if (optional5.isPresent()) {
            return optional5;
        }
        final Direction.Axis a6 = (Direction.Axis)this.level.getBlockState(this.portalEntrancePos).<Direction.Axis>getOptionalValue(NetherPortalBlock.AXIS).orElse(Direction.Axis.X);
        final Optional<BlockUtil.FoundRectangle> optional6 = aag.getPortalForcer().createPortal(fx, a6);
        if (!optional6.isPresent()) {
            ServerPlayer.LOGGER.error("Unable to create a portal, likely target out of worldborder");
        }
        return optional6;
    }
    
    private void triggerDimensionChangeTriggers(final ServerLevel aag) {
        final ResourceKey<Level> vj3 = aag.dimension();
        final ResourceKey<Level> vj4 = this.level.dimension();
        CriteriaTriggers.CHANGED_DIMENSION.trigger(this, vj3, vj4);
        if (vj3 == Level.NETHER && vj4 == Level.OVERWORLD && this.enteredNetherPosition != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(this, this.enteredNetherPosition);
        }
        if (vj4 != Level.NETHER) {
            this.enteredNetherPosition = null;
        }
    }
    
    public boolean broadcastToPlayer(final ServerPlayer aah) {
        if (aah.isSpectator()) {
            return this.getCamera() == this;
        }
        return !this.isSpectator() && super.broadcastToPlayer(aah);
    }
    
    private void broadcast(final BlockEntity ccg) {
        if (ccg != null) {
            final ClientboundBlockEntityDataPacket ow3 = ccg.getUpdatePacket();
            if (ow3 != null) {
                this.connection.send(ow3);
            }
        }
    }
    
    @Override
    public void take(final Entity apx, final int integer) {
        super.take(apx, integer);
        this.containerMenu.broadcastChanges();
    }
    
    @Override
    public Either<BedSleepingProblem, Unit> startSleepInBed(final BlockPos fx) {
        final Direction gc3 = this.level.getBlockState(fx).<Direction>getValue((Property<Direction>)HorizontalDirectionalBlock.FACING);
        if (this.isSleeping() || !this.isAlive()) {
            return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.OTHER_PROBLEM);
        }
        if (!this.level.dimensionType().natural()) {
            return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.NOT_POSSIBLE_HERE);
        }
        if (!this.bedInRange(fx, gc3)) {
            return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.TOO_FAR_AWAY);
        }
        if (this.bedBlocked(fx, gc3)) {
            return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.OBSTRUCTED);
        }
        this.setRespawnPosition(this.level.dimension(), fx, this.yRot, false, true);
        if (this.level.isDay()) {
            return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.NOT_POSSIBLE_NOW);
        }
        if (!this.isCreative()) {
            final double double4 = 8.0;
            final double double5 = 5.0;
            final Vec3 dck8 = Vec3.atBottomCenterOf(fx);
            final List<Monster> list9 = this.level.<Monster>getEntitiesOfClass((java.lang.Class<? extends Monster>)Monster.class, new AABB(dck8.x() - 8.0, dck8.y() - 5.0, dck8.z() - 8.0, dck8.x() + 8.0, dck8.y() + 5.0, dck8.z() + 8.0), (java.util.function.Predicate<? super Monster>)(bdn -> bdn.isPreventingPlayerRest(this)));
            if (!list9.isEmpty()) {
                return (Either<BedSleepingProblem, Unit>)Either.left(BedSleepingProblem.NOT_SAFE);
            }
        }
        final Either<BedSleepingProblem, Unit> either4 = (Either<BedSleepingProblem, Unit>)super.startSleepInBed(fx).ifRight(afu -> {
            this.awardStat(Stats.SLEEP_IN_BED);
            CriteriaTriggers.SLEPT_IN_BED.trigger(this);
        });
        ((ServerLevel)this.level).updateSleepingPlayerList();
        return either4;
    }
    
    @Override
    public void startSleeping(final BlockPos fx) {
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        super.startSleeping(fx);
    }
    
    private boolean bedInRange(final BlockPos fx, final Direction gc) {
        return this.isReachableBedBlock(fx) || this.isReachableBedBlock(fx.relative(gc.getOpposite()));
    }
    
    private boolean isReachableBedBlock(final BlockPos fx) {
        final Vec3 dck3 = Vec3.atBottomCenterOf(fx);
        return Math.abs(this.getX() - dck3.x()) <= 3.0 && Math.abs(this.getY() - dck3.y()) <= 2.0 && Math.abs(this.getZ() - dck3.z()) <= 3.0;
    }
    
    private boolean bedBlocked(final BlockPos fx, final Direction gc) {
        final BlockPos fx2 = fx.above();
        return !this.freeAt(fx2) || !this.freeAt(fx2.relative(gc.getOpposite()));
    }
    
    @Override
    public void stopSleepInBed(final boolean boolean1, final boolean boolean2) {
        if (this.isSleeping()) {
            this.getLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(this, 2));
        }
        super.stopSleepInBed(boolean1, boolean2);
        if (this.connection != null) {
            this.connection.teleport(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }
    }
    
    public boolean startRiding(final Entity apx, final boolean boolean2) {
        final Entity apx2 = this.getVehicle();
        if (!super.startRiding(apx, boolean2)) {
            return false;
        }
        final Entity apx3 = this.getVehicle();
        if (apx3 != apx2 && this.connection != null) {
            this.connection.teleport(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }
        return true;
    }
    
    @Override
    public void stopRiding() {
        final Entity apx2 = this.getVehicle();
        super.stopRiding();
        final Entity apx3 = this.getVehicle();
        if (apx3 != apx2 && this.connection != null) {
            this.connection.teleport(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }
    }
    
    @Override
    public boolean isInvulnerableTo(final DamageSource aph) {
        return super.isInvulnerableTo(aph) || this.isChangingDimension() || (this.abilities.invulnerable && aph == DamageSource.WITHER);
    }
    
    @Override
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
    }
    
    @Override
    protected void onChangedBlock(final BlockPos fx) {
        if (!this.isSpectator()) {
            super.onChangedBlock(fx);
        }
    }
    
    public void doCheckFallDamage(final double double1, final boolean boolean2) {
        final BlockPos fx5 = this.getOnPos();
        if (!this.level.hasChunkAt(fx5)) {
            return;
        }
        super.checkFallDamage(double1, boolean2, this.level.getBlockState(fx5), fx5);
    }
    
    @Override
    public void openTextEdit(final SignBlockEntity cdc) {
        cdc.setAllowedPlayerEditor(this);
        this.connection.send(new ClientboundOpenSignEditorPacket(cdc.getBlockPos()));
    }
    
    private void nextContainerCounter() {
        this.containerCounter = this.containerCounter % 100 + 1;
    }
    
    @Override
    public OptionalInt openMenu(@Nullable final MenuProvider aou) {
        if (aou == null) {
            return OptionalInt.empty();
        }
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }
        this.nextContainerCounter();
        final AbstractContainerMenu bhz3 = aou.createMenu(this.containerCounter, this.inventory, this);
        if (bhz3 == null) {
            if (this.isSpectator()) {
                this.displayClientMessage(new TranslatableComponent("container.spectatorCantOpen").withStyle(ChatFormatting.RED), true);
            }
            return OptionalInt.empty();
        }
        this.connection.send(new ClientboundOpenScreenPacket(bhz3.containerId, bhz3.getType(), aou.getDisplayName()));
        bhz3.addSlotListener(this);
        this.containerMenu = bhz3;
        return OptionalInt.of(this.containerCounter);
    }
    
    @Override
    public void sendMerchantOffers(final int integer1, final MerchantOffers bqt, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6) {
        this.connection.send(new ClientboundMerchantOffersPacket(integer1, bqt, integer3, integer4, boolean5, boolean6));
    }
    
    @Override
    public void openHorseInventory(final AbstractHorse bay, final Container aok) {
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }
        this.nextContainerCounter();
        this.connection.send(new ClientboundHorseScreenOpenPacket(this.containerCounter, aok.getContainerSize(), bay.getId()));
        (this.containerMenu = new HorseInventoryMenu(this.containerCounter, this.inventory, aok, bay)).addSlotListener(this);
    }
    
    @Override
    public void openItemGui(final ItemStack bly, final InteractionHand aoq) {
        final Item blu4 = bly.getItem();
        if (blu4 == Items.WRITTEN_BOOK) {
            if (WrittenBookItem.resolveBookComponents(bly, this.createCommandSourceStack(), this)) {
                this.containerMenu.broadcastChanges();
            }
            this.connection.send(new ClientboundOpenBookPacket(aoq));
        }
    }
    
    @Override
    public void openCommandBlock(final CommandBlockEntity ccl) {
        ccl.setSendToClient(true);
        this.broadcast(ccl);
    }
    
    @Override
    public void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly) {
        if (bhz.getSlot(integer) instanceof ResultSlot) {
            return;
        }
        if (bhz == this.inventoryMenu) {
            CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory, bly);
        }
        if (this.ignoreSlotUpdateHack) {
            return;
        }
        this.connection.send(new ClientboundContainerSetSlotPacket(bhz.containerId, integer, bly));
    }
    
    public void refreshContainer(final AbstractContainerMenu bhz) {
        this.refreshContainer(bhz, bhz.getItems());
    }
    
    @Override
    public void refreshContainer(final AbstractContainerMenu bhz, final NonNullList<ItemStack> gj) {
        this.connection.send(new ClientboundContainerSetContentPacket(bhz.containerId, gj));
        this.connection.send(new ClientboundContainerSetSlotPacket(-1, -1, this.inventory.getCarried()));
    }
    
    @Override
    public void setContainerData(final AbstractContainerMenu bhz, final int integer2, final int integer3) {
        this.connection.send(new ClientboundContainerSetDataPacket(bhz.containerId, integer2, integer3));
    }
    
    public void closeContainer() {
        this.connection.send(new ClientboundContainerClosePacket(this.containerMenu.containerId));
        this.doCloseContainer();
    }
    
    public void broadcastCarriedItem() {
        if (this.ignoreSlotUpdateHack) {
            return;
        }
        this.connection.send(new ClientboundContainerSetSlotPacket(-1, -1, this.inventory.getCarried()));
    }
    
    public void doCloseContainer() {
        this.containerMenu.removed(this);
        this.containerMenu = this.inventoryMenu;
    }
    
    public void setPlayerInput(final float float1, final float float2, final boolean boolean3, final boolean boolean4) {
        if (this.isPassenger()) {
            if (float1 >= -1.0f && float1 <= 1.0f) {
                this.xxa = float1;
            }
            if (float2 >= -1.0f && float2 <= 1.0f) {
                this.zza = float2;
            }
            this.jumping = boolean3;
            this.setShiftKeyDown(boolean4);
        }
    }
    
    @Override
    public void awardStat(final Stat<?> adv, final int integer) {
        this.stats.increment(this, adv, integer);
        this.getScoreboard().forAllObjectives(adv, this.getScoreboardName(), (Consumer<Score>)(ddj -> ddj.add(integer)));
    }
    
    @Override
    public void resetStat(final Stat<?> adv) {
        this.stats.setValue(this, adv, 0);
        this.getScoreboard().forAllObjectives(adv, this.getScoreboardName(), (Consumer<Score>)Score::reset);
    }
    
    @Override
    public int awardRecipes(final Collection<Recipe<?>> collection) {
        return this.recipeBook.addRecipes(collection, this);
    }
    
    @Override
    public void awardRecipesByKey(final ResourceLocation[] arr) {
        final List<Recipe<?>> list3 = (List<Recipe<?>>)Lists.newArrayList();
        for (final ResourceLocation vk7 : arr) {
            this.server.getRecipeManager().byKey(vk7).ifPresent(list3::add);
        }
        this.awardRecipes((Collection<Recipe<?>>)list3);
    }
    
    @Override
    public int resetRecipes(final Collection<Recipe<?>> collection) {
        return this.recipeBook.removeRecipes(collection, this);
    }
    
    @Override
    public void giveExperiencePoints(final int integer) {
        super.giveExperiencePoints(integer);
        this.lastSentExp = -1;
    }
    
    public void disconnect() {
        this.disconnected = true;
        this.ejectPassengers();
        if (this.isSleeping()) {
            this.stopSleepInBed(true, false);
        }
    }
    
    public boolean hasDisconnected() {
        return this.disconnected;
    }
    
    public void resetSentInfo() {
        this.lastSentHealth = -1.0E8f;
    }
    
    @Override
    public void displayClientMessage(final Component nr, final boolean boolean2) {
        this.connection.send(new ClientboundChatPacket(nr, boolean2 ? ChatType.GAME_INFO : ChatType.CHAT, Util.NIL_UUID));
    }
    
    @Override
    protected void completeUsingItem() {
        if (!this.useItem.isEmpty() && this.isUsingItem()) {
            this.connection.send(new ClientboundEntityEventPacket(this, (byte)9));
            super.completeUsingItem();
        }
    }
    
    @Override
    public void lookAt(final EntityAnchorArgument.Anchor a, final Vec3 dck) {
        super.lookAt(a, dck);
        this.connection.send(new ClientboundPlayerLookAtPacket(a, dck.x, dck.y, dck.z));
    }
    
    public void lookAt(final EntityAnchorArgument.Anchor a1, final Entity apx, final EntityAnchorArgument.Anchor a3) {
        final Vec3 dck5 = a3.apply(apx);
        super.lookAt(a1, dck5);
        this.connection.send(new ClientboundPlayerLookAtPacket(a1, apx, a3));
    }
    
    public void restoreFrom(final ServerPlayer aah, final boolean boolean2) {
        if (boolean2) {
            this.inventory.replaceWith(aah.inventory);
            this.setHealth(aah.getHealth());
            this.foodData = aah.foodData;
            this.experienceLevel = aah.experienceLevel;
            this.totalExperience = aah.totalExperience;
            this.experienceProgress = aah.experienceProgress;
            this.setScore(aah.getScore());
            this.portalEntrancePos = aah.portalEntrancePos;
        }
        else if (this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || aah.isSpectator()) {
            this.inventory.replaceWith(aah.inventory);
            this.experienceLevel = aah.experienceLevel;
            this.totalExperience = aah.totalExperience;
            this.experienceProgress = aah.experienceProgress;
            this.setScore(aah.getScore());
        }
        this.enchantmentSeed = aah.enchantmentSeed;
        this.enderChestInventory = aah.enderChestInventory;
        this.getEntityData().<Byte>set(ServerPlayer.DATA_PLAYER_MODE_CUSTOMISATION, (Byte)aah.getEntityData().<T>get((EntityDataAccessor<T>)ServerPlayer.DATA_PLAYER_MODE_CUSTOMISATION));
        this.lastSentExp = -1;
        this.lastSentHealth = -1.0f;
        this.lastSentFood = -1;
        this.recipeBook.copyOverData(aah.recipeBook);
        this.entitiesToRemove.addAll((Collection)aah.entitiesToRemove);
        this.seenCredits = aah.seenCredits;
        this.enteredNetherPosition = aah.enteredNetherPosition;
        this.setShoulderEntityLeft(aah.getShoulderEntityLeft());
        this.setShoulderEntityRight(aah.getShoulderEntityRight());
    }
    
    @Override
    protected void onEffectAdded(final MobEffectInstance apr) {
        super.onEffectAdded(apr);
        this.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), apr));
        if (apr.getEffect() == MobEffects.LEVITATION) {
            this.levitationStartTime = this.tickCount;
            this.levitationStartPos = this.position();
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    @Override
    protected void onEffectUpdated(final MobEffectInstance apr, final boolean boolean2) {
        super.onEffectUpdated(apr, boolean2);
        this.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), apr));
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    @Override
    protected void onEffectRemoved(final MobEffectInstance apr) {
        super.onEffectRemoved(apr);
        this.connection.send(new ClientboundRemoveMobEffectPacket(this.getId(), apr.getEffect()));
        if (apr.getEffect() == MobEffects.LEVITATION) {
            this.levitationStartPos = null;
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    public void teleportTo(final double double1, final double double2, final double double3) {
        this.connection.teleport(double1, double2, double3, this.yRot, this.xRot);
    }
    
    public void moveTo(final double double1, final double double2, final double double3) {
        this.teleportTo(double1, double2, double3);
        this.connection.resetPosition();
    }
    
    @Override
    public void crit(final Entity apx) {
        this.getLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(apx, 4));
    }
    
    @Override
    public void magicCrit(final Entity apx) {
        this.getLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(apx, 5));
    }
    
    @Override
    public void onUpdateAbilities() {
        if (this.connection == null) {
            return;
        }
        this.connection.send(new ClientboundPlayerAbilitiesPacket(this.abilities));
        this.updateInvisibilityStatus();
    }
    
    public ServerLevel getLevel() {
        return (ServerLevel)this.level;
    }
    
    @Override
    public void setGameMode(final GameType brr) {
        this.gameMode.setGameModeForPlayer(brr);
        this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, (float)brr.getId()));
        if (brr == GameType.SPECTATOR) {
            this.removeEntitiesOnShoulder();
            this.stopRiding();
        }
        else {
            this.setCamera(this);
        }
        this.onUpdateAbilities();
        this.updateEffectVisibility();
    }
    
    @Override
    public boolean isSpectator() {
        return this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
    }
    
    @Override
    public boolean isCreative() {
        return this.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
    }
    
    public void sendMessage(final Component nr, final UUID uUID) {
        this.sendMessage(nr, ChatType.SYSTEM, uUID);
    }
    
    public void sendMessage(final Component nr, final ChatType no, final UUID uUID) {
        this.connection.send(new ClientboundChatPacket(nr, no, uUID), (future -> {
            if (!future.isSuccess() && (no == ChatType.GAME_INFO || no == ChatType.SYSTEM)) {
                final int integer6 = 256;
                final String string7 = nr.getString(256);
                final Component nr2 = new TextComponent(string7).withStyle(ChatFormatting.YELLOW);
                this.connection.send(new ClientboundChatPacket(new TranslatableComponent("multiplayer.message_not_delivered", new Object[] { nr2 }).withStyle(ChatFormatting.RED), ChatType.SYSTEM, uUID));
            }
        }));
    }
    
    public String getIpAddress() {
        String string2 = this.connection.connection.getRemoteAddress().toString();
        string2 = string2.substring(string2.indexOf("/") + 1);
        string2 = string2.substring(0, string2.indexOf(":"));
        return string2;
    }
    
    public void updateOptions(final ServerboundClientInformationPacket sg) {
        this.chatVisibility = sg.getChatVisibility();
        this.canChatColor = sg.getChatColors();
        this.getEntityData().<Byte>set(ServerPlayer.DATA_PLAYER_MODE_CUSTOMISATION, (byte)sg.getModelCustomisation());
        this.getEntityData().<Byte>set(ServerPlayer.DATA_PLAYER_MAIN_HAND, (byte)((sg.getMainHand() != HumanoidArm.LEFT) ? 1 : 0));
    }
    
    public ChatVisiblity getChatVisibility() {
        return this.chatVisibility;
    }
    
    public void sendTexturePack(final String string1, final String string2) {
        this.connection.send(new ClientboundResourcePackPacket(string1, string2));
    }
    
    protected int getPermissionLevel() {
        return this.server.getProfilePermissions(this.getGameProfile());
    }
    
    public void resetLastActionTime() {
        this.lastActionTime = Util.getMillis();
    }
    
    public ServerStatsCounter getStats() {
        return this.stats;
    }
    
    public ServerRecipeBook getRecipeBook() {
        return this.recipeBook;
    }
    
    public void sendRemoveEntity(final Entity apx) {
        if (apx instanceof Player) {
            this.connection.send(new ClientboundRemoveEntitiesPacket(new int[] { apx.getId() }));
        }
        else {
            this.entitiesToRemove.add(apx.getId());
        }
    }
    
    public void cancelRemoveEntity(final Entity apx) {
        this.entitiesToRemove.remove(apx.getId());
    }
    
    @Override
    protected void updateInvisibilityStatus() {
        if (this.isSpectator()) {
            this.removeEffectParticles();
            this.setInvisible(true);
        }
        else {
            super.updateInvisibilityStatus();
        }
    }
    
    public Entity getCamera() {
        return (this.camera == null) ? this : this.camera;
    }
    
    public void setCamera(final Entity apx) {
        final Entity apx2 = this.getCamera();
        this.camera = ((apx == null) ? this : apx);
        if (apx2 != this.camera) {
            this.connection.send(new ClientboundSetCameraPacket(this.camera));
            this.teleportTo(this.camera.getX(), this.camera.getY(), this.camera.getZ());
        }
    }
    
    protected void processPortalCooldown() {
        if (!this.isChangingDimension) {
            super.processPortalCooldown();
        }
    }
    
    @Override
    public void attack(final Entity apx) {
        if (this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
            this.setCamera(apx);
        }
        else {
            super.attack(apx);
        }
    }
    
    public long getLastActionTime() {
        return this.lastActionTime;
    }
    
    @Nullable
    public Component getTabListDisplayName() {
        return null;
    }
    
    @Override
    public void swing(final InteractionHand aoq) {
        super.swing(aoq);
        this.resetAttackStrengthTicker();
    }
    
    public boolean isChangingDimension() {
        return this.isChangingDimension;
    }
    
    public void hasChangedDimension() {
        this.isChangingDimension = false;
    }
    
    public PlayerAdvancements getAdvancements() {
        return this.advancements;
    }
    
    public void teleportTo(final ServerLevel aag, final double double2, final double double3, final double double4, final float float5, final float float6) {
        this.setCamera(this);
        this.stopRiding();
        if (aag == this.level) {
            this.connection.teleport(double2, double3, double4, float5, float6);
        }
        else {
            final ServerLevel aag2 = this.getLevel();
            final LevelData cya12 = aag.getLevelData();
            this.connection.send(new ClientboundRespawnPacket(aag.dimensionType(), aag.dimension(), BiomeManager.obfuscateSeed(aag.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), aag.isDebug(), aag.isFlat(), true));
            this.connection.send(new ClientboundChangeDifficultyPacket(cya12.getDifficulty(), cya12.isDifficultyLocked()));
            this.server.getPlayerList().sendPlayerPermissionLevel(this);
            aag2.removePlayerImmediately(this);
            this.removed = false;
            this.moveTo(double2, double3, double4, float5, float6);
            this.setLevel(aag);
            aag.addDuringCommandTeleport(this);
            this.triggerDimensionChangeTriggers(aag2);
            this.connection.teleport(double2, double3, double4, float5, float6);
            this.gameMode.setLevel(aag);
            this.server.getPlayerList().sendLevelInfo(this, aag);
            this.server.getPlayerList().sendAllPlayerInfo(this);
        }
    }
    
    @Nullable
    public BlockPos getRespawnPosition() {
        return this.respawnPosition;
    }
    
    public float getRespawnAngle() {
        return this.respawnAngle;
    }
    
    public ResourceKey<Level> getRespawnDimension() {
        return this.respawnDimension;
    }
    
    public boolean isRespawnForced() {
        return this.respawnForced;
    }
    
    public void setRespawnPosition(final ResourceKey<Level> vj, @Nullable final BlockPos fx, final float float3, final boolean boolean4, final boolean boolean5) {
        if (fx != null) {
            final boolean boolean6 = fx.equals(this.respawnPosition) && vj.equals(this.respawnDimension);
            if (boolean5 && !boolean6) {
                this.sendMessage(new TranslatableComponent("block.minecraft.set_spawn"), Util.NIL_UUID);
            }
            this.respawnPosition = fx;
            this.respawnDimension = vj;
            this.respawnAngle = float3;
            this.respawnForced = boolean4;
        }
        else {
            this.respawnPosition = null;
            this.respawnDimension = Level.OVERWORLD;
            this.respawnAngle = 0.0f;
            this.respawnForced = false;
        }
    }
    
    public void trackChunk(final ChunkPos bra, final Packet<?> oj2, final Packet<?> oj3) {
        this.connection.send(oj3);
        this.connection.send(oj2);
    }
    
    public void untrackChunk(final ChunkPos bra) {
        if (this.isAlive()) {
            this.connection.send(new ClientboundForgetLevelChunkPacket(bra.x, bra.z));
        }
    }
    
    public SectionPos getLastSectionPos() {
        return this.lastSectionPos;
    }
    
    public void setLastSectionPos(final SectionPos gp) {
        this.lastSectionPos = gp;
    }
    
    @Override
    public void playNotifySound(final SoundEvent adn, final SoundSource adp, final float float3, final float float4) {
        this.connection.send(new ClientboundSoundPacket(adn, adp, this.getX(), this.getY(), this.getZ(), float3, float4));
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddPlayerPacket(this);
    }
    
    @Override
    public ItemEntity drop(final ItemStack bly, final boolean boolean2, final boolean boolean3) {
        final ItemEntity bcs5 = super.drop(bly, boolean2, boolean3);
        if (bcs5 == null) {
            return null;
        }
        this.level.addFreshEntity(bcs5);
        final ItemStack bly2 = bcs5.getItem();
        if (boolean3) {
            if (!bly2.isEmpty()) {
                this.awardStat(Stats.ITEM_DROPPED.get(bly2.getItem()), bly.getCount());
            }
            this.awardStat(Stats.DROP);
        }
        return bcs5;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
