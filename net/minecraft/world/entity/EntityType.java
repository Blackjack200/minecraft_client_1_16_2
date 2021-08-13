package net.minecraft.world.entity;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.nbt.ListTag;
import java.util.function.Function;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Consumer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.core.Direction;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.apache.logging.log4j.Logger;

public class EntityType<T extends Entity> {
    private static final Logger LOGGER;
    public static final EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD;
    public static final EntityType<ArmorStand> ARMOR_STAND;
    public static final EntityType<Arrow> ARROW;
    public static final EntityType<Bat> BAT;
    public static final EntityType<Bee> BEE;
    public static final EntityType<Blaze> BLAZE;
    public static final EntityType<Boat> BOAT;
    public static final EntityType<Cat> CAT;
    public static final EntityType<CaveSpider> CAVE_SPIDER;
    public static final EntityType<Chicken> CHICKEN;
    public static final EntityType<Cod> COD;
    public static final EntityType<Cow> COW;
    public static final EntityType<Creeper> CREEPER;
    public static final EntityType<Dolphin> DOLPHIN;
    public static final EntityType<Donkey> DONKEY;
    public static final EntityType<DragonFireball> DRAGON_FIREBALL;
    public static final EntityType<Drowned> DROWNED;
    public static final EntityType<ElderGuardian> ELDER_GUARDIAN;
    public static final EntityType<EndCrystal> END_CRYSTAL;
    public static final EntityType<EnderDragon> ENDER_DRAGON;
    public static final EntityType<EnderMan> ENDERMAN;
    public static final EntityType<Endermite> ENDERMITE;
    public static final EntityType<Evoker> EVOKER;
    public static final EntityType<EvokerFangs> EVOKER_FANGS;
    public static final EntityType<ExperienceOrb> EXPERIENCE_ORB;
    public static final EntityType<EyeOfEnder> EYE_OF_ENDER;
    public static final EntityType<FallingBlockEntity> FALLING_BLOCK;
    public static final EntityType<FireworkRocketEntity> FIREWORK_ROCKET;
    public static final EntityType<Fox> FOX;
    public static final EntityType<Ghast> GHAST;
    public static final EntityType<Giant> GIANT;
    public static final EntityType<Guardian> GUARDIAN;
    public static final EntityType<Hoglin> HOGLIN;
    public static final EntityType<Horse> HORSE;
    public static final EntityType<Husk> HUSK;
    public static final EntityType<Illusioner> ILLUSIONER;
    public static final EntityType<IronGolem> IRON_GOLEM;
    public static final EntityType<ItemEntity> ITEM;
    public static final EntityType<ItemFrame> ITEM_FRAME;
    public static final EntityType<LargeFireball> FIREBALL;
    public static final EntityType<LeashFenceKnotEntity> LEASH_KNOT;
    public static final EntityType<LightningBolt> LIGHTNING_BOLT;
    public static final EntityType<Llama> LLAMA;
    public static final EntityType<LlamaSpit> LLAMA_SPIT;
    public static final EntityType<MagmaCube> MAGMA_CUBE;
    public static final EntityType<Minecart> MINECART;
    public static final EntityType<MinecartChest> CHEST_MINECART;
    public static final EntityType<MinecartCommandBlock> COMMAND_BLOCK_MINECART;
    public static final EntityType<MinecartFurnace> FURNACE_MINECART;
    public static final EntityType<MinecartHopper> HOPPER_MINECART;
    public static final EntityType<MinecartSpawner> SPAWNER_MINECART;
    public static final EntityType<MinecartTNT> TNT_MINECART;
    public static final EntityType<Mule> MULE;
    public static final EntityType<MushroomCow> MOOSHROOM;
    public static final EntityType<Ocelot> OCELOT;
    public static final EntityType<Painting> PAINTING;
    public static final EntityType<Panda> PANDA;
    public static final EntityType<Parrot> PARROT;
    public static final EntityType<Phantom> PHANTOM;
    public static final EntityType<Pig> PIG;
    public static final EntityType<Piglin> PIGLIN;
    public static final EntityType<PiglinBrute> PIGLIN_BRUTE;
    public static final EntityType<Pillager> PILLAGER;
    public static final EntityType<PolarBear> POLAR_BEAR;
    public static final EntityType<PrimedTnt> TNT;
    public static final EntityType<Pufferfish> PUFFERFISH;
    public static final EntityType<Rabbit> RABBIT;
    public static final EntityType<Ravager> RAVAGER;
    public static final EntityType<Salmon> SALMON;
    public static final EntityType<Sheep> SHEEP;
    public static final EntityType<Shulker> SHULKER;
    public static final EntityType<ShulkerBullet> SHULKER_BULLET;
    public static final EntityType<Silverfish> SILVERFISH;
    public static final EntityType<Skeleton> SKELETON;
    public static final EntityType<SkeletonHorse> SKELETON_HORSE;
    public static final EntityType<Slime> SLIME;
    public static final EntityType<SmallFireball> SMALL_FIREBALL;
    public static final EntityType<SnowGolem> SNOW_GOLEM;
    public static final EntityType<Snowball> SNOWBALL;
    public static final EntityType<SpectralArrow> SPECTRAL_ARROW;
    public static final EntityType<Spider> SPIDER;
    public static final EntityType<Squid> SQUID;
    public static final EntityType<Stray> STRAY;
    public static final EntityType<Strider> STRIDER;
    public static final EntityType<ThrownEgg> EGG;
    public static final EntityType<ThrownEnderpearl> ENDER_PEARL;
    public static final EntityType<ThrownExperienceBottle> EXPERIENCE_BOTTLE;
    public static final EntityType<ThrownPotion> POTION;
    public static final EntityType<ThrownTrident> TRIDENT;
    public static final EntityType<TraderLlama> TRADER_LLAMA;
    public static final EntityType<TropicalFish> TROPICAL_FISH;
    public static final EntityType<Turtle> TURTLE;
    public static final EntityType<Vex> VEX;
    public static final EntityType<Villager> VILLAGER;
    public static final EntityType<Vindicator> VINDICATOR;
    public static final EntityType<WanderingTrader> WANDERING_TRADER;
    public static final EntityType<Witch> WITCH;
    public static final EntityType<WitherBoss> WITHER;
    public static final EntityType<WitherSkeleton> WITHER_SKELETON;
    public static final EntityType<WitherSkull> WITHER_SKULL;
    public static final EntityType<Wolf> WOLF;
    public static final EntityType<Zoglin> ZOGLIN;
    public static final EntityType<Zombie> ZOMBIE;
    public static final EntityType<ZombieHorse> ZOMBIE_HORSE;
    public static final EntityType<ZombieVillager> ZOMBIE_VILLAGER;
    public static final EntityType<ZombifiedPiglin> ZOMBIFIED_PIGLIN;
    public static final EntityType<Player> PLAYER;
    public static final EntityType<FishingHook> FISHING_BOBBER;
    private final EntityFactory<T> factory;
    private final MobCategory category;
    private final ImmutableSet<Block> immuneTo;
    private final boolean serialize;
    private final boolean summon;
    private final boolean fireImmune;
    private final boolean canSpawnFarFromPlayer;
    private final int clientTrackingRange;
    private final int updateInterval;
    @Nullable
    private String descriptionId;
    @Nullable
    private Component description;
    @Nullable
    private ResourceLocation lootTable;
    private final EntityDimensions dimensions;
    
    private static <T extends Entity> EntityType<T> register(final String string, final Builder<T> a) {
        return Registry.<EntityType<T>>register(Registry.ENTITY_TYPE, string, a.build(string));
    }
    
    public static ResourceLocation getKey(final EntityType<?> aqb) {
        return Registry.ENTITY_TYPE.getKey(aqb);
    }
    
    public static Optional<EntityType<?>> byString(final String string) {
        return Registry.ENTITY_TYPE.getOptional(ResourceLocation.tryParse(string));
    }
    
    public EntityType(final EntityFactory<T> b, final MobCategory aql, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6, final ImmutableSet<Block> immutableSet, final EntityDimensions apy, final int integer9, final int integer10) {
        this.factory = b;
        this.category = aql;
        this.canSpawnFarFromPlayer = boolean6;
        this.serialize = boolean3;
        this.summon = boolean4;
        this.fireImmune = boolean5;
        this.immuneTo = immutableSet;
        this.dimensions = apy;
        this.clientTrackingRange = integer9;
        this.updateInterval = integer10;
    }
    
    @Nullable
    public Entity spawn(final ServerLevel aag, @Nullable final ItemStack bly, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean6, final boolean boolean7) {
        return this.spawn(aag, (bly == null) ? null : bly.getTag(), (bly != null && bly.hasCustomHoverName()) ? bly.getHoverName() : null, bft, fx, aqm, boolean6, boolean7);
    }
    
    @Nullable
    public T spawn(final ServerLevel aag, @Nullable final CompoundTag md, @Nullable final Component nr, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean7, final boolean boolean8) {
        final T apx10 = this.create(aag, md, nr, bft, fx, aqm, boolean7, boolean8);
        if (apx10 != null) {
            aag.addFreshEntityWithPassengers(apx10);
        }
        return apx10;
    }
    
    @Nullable
    public T create(final ServerLevel aag, @Nullable final CompoundTag md, @Nullable final Component nr, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean7, final boolean boolean8) {
        final T apx10 = this.create(aag);
        if (apx10 == null) {
            return null;
        }
        double double11;
        if (boolean7) {
            apx10.setPos(fx.getX() + 0.5, fx.getY() + 1, fx.getZ() + 0.5);
            double11 = getYOffset(aag, fx, boolean8, apx10.getBoundingBox());
        }
        else {
            double11 = 0.0;
        }
        apx10.moveTo(fx.getX() + 0.5, fx.getY() + double11, fx.getZ() + 0.5, Mth.wrapDegrees(aag.random.nextFloat() * 360.0f), 0.0f);
        if (apx10 instanceof Mob) {
            final Mob aqk13 = (Mob)apx10;
            aqk13.yHeadRot = aqk13.yRot;
            aqk13.yBodyRot = aqk13.yRot;
            aqk13.finalizeSpawn(aag, aag.getCurrentDifficultyAt(aqk13.blockPosition()), aqm, null, md);
            aqk13.playAmbientSound();
        }
        if (nr != null && apx10 instanceof LivingEntity) {
            apx10.setCustomName(nr);
        }
        updateCustomEntityTag(aag, bft, apx10, md);
        return apx10;
    }
    
    protected static double getYOffset(final LevelReader brw, final BlockPos fx, final boolean boolean3, final AABB dcf) {
        AABB dcf2 = new AABB(fx);
        if (boolean3) {
            dcf2 = dcf2.expandTowards(0.0, -1.0, 0.0);
        }
        final Stream<VoxelShape> stream6 = brw.getCollisions(null, dcf2, (Predicate<Entity>)(apx -> true));
        return 1.0 + Shapes.collide(Direction.Axis.Y, dcf, stream6, boolean3 ? -2.0 : -1.0);
    }
    
    public static void updateCustomEntityTag(final Level bru, @Nullable final Player bft, @Nullable final Entity apx, @Nullable final CompoundTag md) {
        if (md == null || !md.contains("EntityTag", 10)) {
            return;
        }
        final MinecraftServer minecraftServer5 = bru.getServer();
        if (minecraftServer5 == null || apx == null) {
            return;
        }
        if (!bru.isClientSide && apx.onlyOpCanSetNbt() && (bft == null || !minecraftServer5.getPlayerList().isOp(bft.getGameProfile()))) {
            return;
        }
        final CompoundTag md2 = apx.saveWithoutId(new CompoundTag());
        final UUID uUID7 = apx.getUUID();
        md2.merge(md.getCompound("EntityTag"));
        apx.setUUID(uUID7);
        apx.load(md2);
    }
    
    public boolean canSerialize() {
        return this.serialize;
    }
    
    public boolean canSummon() {
        return this.summon;
    }
    
    public boolean fireImmune() {
        return this.fireImmune;
    }
    
    public boolean canSpawnFarFromPlayer() {
        return this.canSpawnFarFromPlayer;
    }
    
    public MobCategory getCategory() {
        return this.category;
    }
    
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("entity", Registry.ENTITY_TYPE.getKey(this));
        }
        return this.descriptionId;
    }
    
    public Component getDescription() {
        if (this.description == null) {
            this.description = new TranslatableComponent(this.getDescriptionId());
        }
        return this.description;
    }
    
    public String toString() {
        return this.getDescriptionId();
    }
    
    public ResourceLocation getDefaultLootTable() {
        if (this.lootTable == null) {
            final ResourceLocation vk2 = Registry.ENTITY_TYPE.getKey(this);
            this.lootTable = new ResourceLocation(vk2.getNamespace(), "entities/" + vk2.getPath());
        }
        return this.lootTable;
    }
    
    public float getWidth() {
        return this.dimensions.width;
    }
    
    public float getHeight() {
        return this.dimensions.height;
    }
    
    @Nullable
    public T create(final Level bru) {
        return this.factory.create(this, bru);
    }
    
    @Nullable
    public static Entity create(final int integer, final Level bru) {
        return create(bru, Registry.ENTITY_TYPE.byId(integer));
    }
    
    public static Optional<Entity> create(final CompoundTag md, final Level bru) {
        return Util.<Entity>ifElse((java.util.Optional<Entity>)by(md).map(aqb -> aqb.create(bru)), (java.util.function.Consumer<Entity>)(apx -> apx.load(md)), () -> EntityType.LOGGER.warn("Skipping Entity with id {}", md.getString("id")));
    }
    
    @Nullable
    private static Entity create(final Level bru, @Nullable final EntityType<?> aqb) {
        return (Entity)((aqb == null) ? null : aqb.create(bru));
    }
    
    public AABB getAABB(final double double1, final double double2, final double double3) {
        final float float8 = this.getWidth() / 2.0f;
        return new AABB(double1 - float8, double2, double3 - float8, double1 + float8, double2 + this.getHeight(), double3 + float8);
    }
    
    public boolean isBlockDangerous(final BlockState cee) {
        return !this.immuneTo.contains(cee.getBlock()) && ((!this.fireImmune && (cee.is(BlockTags.FIRE) || cee.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(cee) || cee.is(Blocks.LAVA))) || cee.is(Blocks.WITHER_ROSE) || cee.is(Blocks.SWEET_BERRY_BUSH) || cee.is(Blocks.CACTUS));
    }
    
    public EntityDimensions getDimensions() {
        return this.dimensions;
    }
    
    public static Optional<EntityType<?>> by(final CompoundTag md) {
        return Registry.ENTITY_TYPE.getOptional(new ResourceLocation(md.getString("id")));
    }
    
    @Nullable
    public static Entity loadEntityRecursive(final CompoundTag md, final Level bru, final Function<Entity, Entity> function) {
        return (Entity)loadStaticEntity(md, bru).map((Function)function).map(apx -> {
            if (md.contains("Passengers", 9)) {
                final ListTag mj5 = md.getList("Passengers", 10);
                for (int integer6 = 0; integer6 < mj5.size(); ++integer6) {
                    final Entity apx2 = loadEntityRecursive(mj5.getCompound(integer6), bru, function);
                    if (apx2 != null) {
                        apx2.startRiding(apx, true);
                    }
                }
            }
            return apx;
        }).orElse(null);
    }
    
    private static Optional<Entity> loadStaticEntity(final CompoundTag md, final Level bru) {
        try {
            return create(md, bru);
        }
        catch (RuntimeException runtimeException3) {
            EntityType.LOGGER.warn("Exception loading entity: ", (Throwable)runtimeException3);
            return (Optional<Entity>)Optional.empty();
        }
    }
    
    public int clientTrackingRange() {
        return this.clientTrackingRange;
    }
    
    public int updateInterval() {
        return this.updateInterval;
    }
    
    public boolean trackDeltas() {
        return this != EntityType.PLAYER && this != EntityType.LLAMA_SPIT && this != EntityType.WITHER && this != EntityType.BAT && this != EntityType.ITEM_FRAME && this != EntityType.LEASH_KNOT && this != EntityType.PAINTING && this != EntityType.END_CRYSTAL && this != EntityType.EVOKER_FANGS;
    }
    
    public boolean is(final Tag<EntityType<?>> aej) {
        return aej.contains(this);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: putstatic       net/minecraft/world/entity/EntityType.LOGGER:Lorg/apache/logging/log4j/Logger;
        //     6: ldc_w           "area_effect_cloud"
        //     9: invokedynamic   BootstrapMethod #5, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //    14: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //    17: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //    20: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //    23: ldc_w           6.0
        //    26: ldc_w           0.5
        //    29: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //    32: bipush          10
        //    34: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //    37: ldc_w           2147483647
        //    40: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //    43: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //    46: putstatic       net/minecraft/world/entity/EntityType.AREA_EFFECT_CLOUD:Lnet/minecraft/world/entity/EntityType;
        //    49: ldc_w           "armor_stand"
        //    52: invokedynamic   BootstrapMethod #6, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //    57: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //    60: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //    63: ldc_w           0.5
        //    66: ldc_w           1.975
        //    69: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //    72: bipush          10
        //    74: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //    77: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //    80: putstatic       net/minecraft/world/entity/EntityType.ARMOR_STAND:Lnet/minecraft/world/entity/EntityType;
        //    83: ldc_w           "arrow"
        //    86: invokedynamic   BootstrapMethod #7, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //    91: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //    94: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //    97: ldc_w           0.5
        //   100: ldc_w           0.5
        //   103: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   106: iconst_4       
        //   107: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   110: bipush          20
        //   112: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   115: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   118: putstatic       net/minecraft/world/entity/EntityType.ARROW:Lnet/minecraft/world/entity/EntityType;
        //   121: ldc_w           "bat"
        //   124: invokedynamic   BootstrapMethod #8, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   129: getstatic       net/minecraft/world/entity/MobCategory.AMBIENT:Lnet/minecraft/world/entity/MobCategory;
        //   132: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   135: ldc_w           0.5
        //   138: ldc_w           0.9
        //   141: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   144: iconst_5       
        //   145: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   148: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   151: putstatic       net/minecraft/world/entity/EntityType.BAT:Lnet/minecraft/world/entity/EntityType;
        //   154: ldc_w           "bee"
        //   157: invokedynamic   BootstrapMethod #9, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   162: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   165: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   168: ldc_w           0.7
        //   171: ldc_w           0.6
        //   174: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   177: bipush          8
        //   179: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   182: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   185: putstatic       net/minecraft/world/entity/EntityType.BEE:Lnet/minecraft/world/entity/EntityType;
        //   188: ldc_w           "blaze"
        //   191: invokedynamic   BootstrapMethod #10, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   196: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   199: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   202: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //   205: ldc_w           0.6
        //   208: ldc_w           1.8
        //   211: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   214: bipush          8
        //   216: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   219: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   222: putstatic       net/minecraft/world/entity/EntityType.BLAZE:Lnet/minecraft/world/entity/EntityType;
        //   225: ldc_w           "boat"
        //   228: invokedynamic   BootstrapMethod #11, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   233: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   236: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   239: ldc_w           1.375
        //   242: ldc_w           0.5625
        //   245: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   248: bipush          10
        //   250: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   253: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   256: putstatic       net/minecraft/world/entity/EntityType.BOAT:Lnet/minecraft/world/entity/EntityType;
        //   259: ldc_w           "cat"
        //   262: invokedynamic   BootstrapMethod #12, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   267: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   270: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   273: ldc_w           0.6
        //   276: ldc_w           0.7
        //   279: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   282: bipush          8
        //   284: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   287: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   290: putstatic       net/minecraft/world/entity/EntityType.CAT:Lnet/minecraft/world/entity/EntityType;
        //   293: ldc_w           "cave_spider"
        //   296: invokedynamic   BootstrapMethod #13, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   301: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   304: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   307: ldc_w           0.7
        //   310: ldc_w           0.5
        //   313: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   316: bipush          8
        //   318: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   321: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   324: putstatic       net/minecraft/world/entity/EntityType.CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;
        //   327: ldc_w           "chicken"
        //   330: invokedynamic   BootstrapMethod #14, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   335: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   338: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   341: ldc_w           0.4
        //   344: ldc_w           0.7
        //   347: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   350: bipush          10
        //   352: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   355: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   358: putstatic       net/minecraft/world/entity/EntityType.CHICKEN:Lnet/minecraft/world/entity/EntityType;
        //   361: ldc_w           "cod"
        //   364: invokedynamic   BootstrapMethod #15, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   369: getstatic       net/minecraft/world/entity/MobCategory.WATER_AMBIENT:Lnet/minecraft/world/entity/MobCategory;
        //   372: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   375: ldc_w           0.5
        //   378: ldc_w           0.3
        //   381: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   384: iconst_4       
        //   385: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   388: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   391: putstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        //   394: ldc_w           "cow"
        //   397: invokedynamic   BootstrapMethod #16, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   402: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   405: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   408: ldc_w           0.9
        //   411: ldc_w           1.4
        //   414: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   417: bipush          10
        //   419: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   422: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   425: putstatic       net/minecraft/world/entity/EntityType.COW:Lnet/minecraft/world/entity/EntityType;
        //   428: ldc_w           "creeper"
        //   431: invokedynamic   BootstrapMethod #17, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   436: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   439: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   442: ldc_w           0.6
        //   445: ldc_w           1.7
        //   448: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   451: bipush          8
        //   453: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   456: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   459: putstatic       net/minecraft/world/entity/EntityType.CREEPER:Lnet/minecraft/world/entity/EntityType;
        //   462: ldc_w           "dolphin"
        //   465: invokedynamic   BootstrapMethod #18, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   470: getstatic       net/minecraft/world/entity/MobCategory.WATER_CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   473: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   476: ldc_w           0.9
        //   479: ldc_w           0.6
        //   482: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   485: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   488: putstatic       net/minecraft/world/entity/EntityType.DOLPHIN:Lnet/minecraft/world/entity/EntityType;
        //   491: ldc_w           "donkey"
        //   494: invokedynamic   BootstrapMethod #19, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   499: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //   502: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   505: ldc_w           1.3964844
        //   508: ldc_w           1.5
        //   511: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   514: bipush          10
        //   516: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   519: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   522: putstatic       net/minecraft/world/entity/EntityType.DONKEY:Lnet/minecraft/world/entity/EntityType;
        //   525: ldc_w           "dragon_fireball"
        //   528: invokedynamic   BootstrapMethod #20, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   533: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   536: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   539: fconst_1       
        //   540: fconst_1       
        //   541: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   544: iconst_4       
        //   545: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   548: bipush          10
        //   550: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   553: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   556: putstatic       net/minecraft/world/entity/EntityType.DRAGON_FIREBALL:Lnet/minecraft/world/entity/EntityType;
        //   559: ldc_w           "drowned"
        //   562: invokedynamic   BootstrapMethod #21, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   567: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   570: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   573: ldc_w           0.6
        //   576: ldc_w           1.95
        //   579: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   582: bipush          8
        //   584: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   587: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   590: putstatic       net/minecraft/world/entity/EntityType.DROWNED:Lnet/minecraft/world/entity/EntityType;
        //   593: ldc_w           "elder_guardian"
        //   596: invokedynamic   BootstrapMethod #22, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   601: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   604: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   607: ldc_w           1.9975
        //   610: ldc_w           1.9975
        //   613: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   616: bipush          10
        //   618: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   621: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   624: putstatic       net/minecraft/world/entity/EntityType.ELDER_GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //   627: ldc_w           "end_crystal"
        //   630: invokedynamic   BootstrapMethod #23, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   635: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   638: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   641: fconst_2       
        //   642: fconst_2       
        //   643: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   646: bipush          16
        //   648: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   651: ldc_w           2147483647
        //   654: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   657: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   660: putstatic       net/minecraft/world/entity/EntityType.END_CRYSTAL:Lnet/minecraft/world/entity/EntityType;
        //   663: ldc_w           "ender_dragon"
        //   666: invokedynamic   BootstrapMethod #24, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   671: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   674: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   677: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //   680: ldc_w           16.0
        //   683: ldc_w           8.0
        //   686: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   689: bipush          10
        //   691: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   694: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   697: putstatic       net/minecraft/world/entity/EntityType.ENDER_DRAGON:Lnet/minecraft/world/entity/EntityType;
        //   700: ldc_w           "enderman"
        //   703: invokedynamic   BootstrapMethod #25, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   708: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   711: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   714: ldc_w           0.6
        //   717: ldc_w           2.9
        //   720: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   723: bipush          8
        //   725: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   728: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   731: putstatic       net/minecraft/world/entity/EntityType.ENDERMAN:Lnet/minecraft/world/entity/EntityType;
        //   734: ldc_w           "endermite"
        //   737: invokedynamic   BootstrapMethod #26, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   742: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   745: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   748: ldc_w           0.4
        //   751: ldc_w           0.3
        //   754: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   757: bipush          8
        //   759: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   762: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   765: putstatic       net/minecraft/world/entity/EntityType.ENDERMITE:Lnet/minecraft/world/entity/EntityType;
        //   768: ldc_w           "evoker"
        //   771: invokedynamic   BootstrapMethod #27, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   776: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //   779: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   782: ldc_w           0.6
        //   785: ldc_w           1.95
        //   788: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   791: bipush          8
        //   793: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   796: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   799: putstatic       net/minecraft/world/entity/EntityType.EVOKER:Lnet/minecraft/world/entity/EntityType;
        //   802: ldc_w           "evoker_fangs"
        //   805: invokedynamic   BootstrapMethod #28, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   810: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   813: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   816: ldc_w           0.5
        //   819: ldc_w           0.8
        //   822: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   825: bipush          6
        //   827: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   830: iconst_2       
        //   831: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   834: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   837: putstatic       net/minecraft/world/entity/EntityType.EVOKER_FANGS:Lnet/minecraft/world/entity/EntityType;
        //   840: ldc_w           "experience_orb"
        //   843: invokedynamic   BootstrapMethod #29, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   848: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   851: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   854: ldc_w           0.5
        //   857: ldc_w           0.5
        //   860: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   863: bipush          6
        //   865: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   868: bipush          20
        //   870: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   873: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   876: putstatic       net/minecraft/world/entity/EntityType.EXPERIENCE_ORB:Lnet/minecraft/world/entity/EntityType;
        //   879: ldc_w           "eye_of_ender"
        //   882: invokedynamic   BootstrapMethod #30, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   887: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   890: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   893: ldc_w           0.25
        //   896: ldc_w           0.25
        //   899: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   902: iconst_4       
        //   903: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   906: iconst_4       
        //   907: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   910: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   913: putstatic       net/minecraft/world/entity/EntityType.EYE_OF_ENDER:Lnet/minecraft/world/entity/EntityType;
        //   916: ldc_w           "falling_block"
        //   919: invokedynamic   BootstrapMethod #31, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   924: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   927: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   930: ldc_w           0.98
        //   933: ldc_w           0.98
        //   936: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   939: bipush          10
        //   941: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   944: bipush          20
        //   946: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   949: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   952: putstatic       net/minecraft/world/entity/EntityType.FALLING_BLOCK:Lnet/minecraft/world/entity/EntityType;
        //   955: ldc_w           "firework_rocket"
        //   958: invokedynamic   BootstrapMethod #32, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //   963: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //   966: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //   969: ldc_w           0.25
        //   972: ldc_w           0.25
        //   975: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //   978: iconst_4       
        //   979: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   982: bipush          10
        //   984: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //   987: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //   990: putstatic       net/minecraft/world/entity/EntityType.FIREWORK_ROCKET:Lnet/minecraft/world/entity/EntityType;
        //   993: ldc_w           "fox"
        //   996: invokedynamic   BootstrapMethod #33, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1001: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1004: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1007: ldc_w           0.6
        //  1010: ldc_w           0.7
        //  1013: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1016: bipush          8
        //  1018: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1021: iconst_1       
        //  1022: anewarray       Lnet/minecraft/world/level/block/Block;
        //  1025: dup            
        //  1026: iconst_0       
        //  1027: getstatic       net/minecraft/world/level/block/Blocks.SWEET_BERRY_BUSH:Lnet/minecraft/world/level/block/Block;
        //  1030: aastore        
        //  1031: invokevirtual   net/minecraft/world/entity/EntityType$Builder.immuneTo:([Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1034: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1037: putstatic       net/minecraft/world/entity/EntityType.FOX:Lnet/minecraft/world/entity/EntityType;
        //  1040: ldc_w           "ghast"
        //  1043: invokedynamic   BootstrapMethod #34, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1048: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1051: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1054: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  1057: ldc_w           4.0
        //  1060: ldc_w           4.0
        //  1063: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1066: bipush          10
        //  1068: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1071: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1074: putstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //  1077: ldc_w           "giant"
        //  1080: invokedynamic   BootstrapMethod #35, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1085: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1088: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1091: ldc_w           3.6
        //  1094: ldc_w           12.0
        //  1097: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1100: bipush          10
        //  1102: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1105: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1108: putstatic       net/minecraft/world/entity/EntityType.GIANT:Lnet/minecraft/world/entity/EntityType;
        //  1111: ldc_w           "guardian"
        //  1114: invokedynamic   BootstrapMethod #36, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1119: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1122: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1125: ldc_w           0.85
        //  1128: ldc_w           0.85
        //  1131: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1134: bipush          8
        //  1136: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1139: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1142: putstatic       net/minecraft/world/entity/EntityType.GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //  1145: ldc_w           "hoglin"
        //  1148: invokedynamic   BootstrapMethod #37, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1153: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1156: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1159: ldc_w           1.3964844
        //  1162: ldc_w           1.4
        //  1165: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1168: bipush          8
        //  1170: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1173: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1176: putstatic       net/minecraft/world/entity/EntityType.HOGLIN:Lnet/minecraft/world/entity/EntityType;
        //  1179: ldc_w           "horse"
        //  1182: invokedynamic   BootstrapMethod #38, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1187: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1190: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1193: ldc_w           1.3964844
        //  1196: ldc_w           1.6
        //  1199: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1202: bipush          10
        //  1204: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1207: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1210: putstatic       net/minecraft/world/entity/EntityType.HORSE:Lnet/minecraft/world/entity/EntityType;
        //  1213: ldc_w           "husk"
        //  1216: invokedynamic   BootstrapMethod #39, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1221: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1224: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1227: ldc_w           0.6
        //  1230: ldc_w           1.95
        //  1233: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1236: bipush          8
        //  1238: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1241: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1244: putstatic       net/minecraft/world/entity/EntityType.HUSK:Lnet/minecraft/world/entity/EntityType;
        //  1247: ldc_w           "illusioner"
        //  1250: invokedynamic   BootstrapMethod #40, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1255: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1258: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1261: ldc_w           0.6
        //  1264: ldc_w           1.95
        //  1267: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1270: bipush          8
        //  1272: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1275: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1278: putstatic       net/minecraft/world/entity/EntityType.ILLUSIONER:Lnet/minecraft/world/entity/EntityType;
        //  1281: ldc_w           "iron_golem"
        //  1284: invokedynamic   BootstrapMethod #41, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1289: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1292: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1295: ldc_w           1.4
        //  1298: ldc_w           2.7
        //  1301: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1304: bipush          10
        //  1306: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1309: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1312: putstatic       net/minecraft/world/entity/EntityType.IRON_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //  1315: ldc_w           "item"
        //  1318: invokedynamic   BootstrapMethod #42, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1323: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1326: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1329: ldc_w           0.25
        //  1332: ldc_w           0.25
        //  1335: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1338: bipush          6
        //  1340: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1343: bipush          20
        //  1345: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1348: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1351: putstatic       net/minecraft/world/entity/EntityType.ITEM:Lnet/minecraft/world/entity/EntityType;
        //  1354: ldc_w           "item_frame"
        //  1357: invokedynamic   BootstrapMethod #43, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1362: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1365: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1368: ldc_w           0.5
        //  1371: ldc_w           0.5
        //  1374: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1377: bipush          10
        //  1379: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1382: ldc_w           2147483647
        //  1385: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1388: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1391: putstatic       net/minecraft/world/entity/EntityType.ITEM_FRAME:Lnet/minecraft/world/entity/EntityType;
        //  1394: ldc_w           "fireball"
        //  1397: invokedynamic   BootstrapMethod #44, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1402: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1405: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1408: fconst_1       
        //  1409: fconst_1       
        //  1410: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1413: iconst_4       
        //  1414: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1417: bipush          10
        //  1419: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1422: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1425: putstatic       net/minecraft/world/entity/EntityType.FIREBALL:Lnet/minecraft/world/entity/EntityType;
        //  1428: ldc_w           "leash_knot"
        //  1431: invokedynamic   BootstrapMethod #45, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1436: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1439: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1442: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSave:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  1445: ldc_w           0.5
        //  1448: ldc_w           0.5
        //  1451: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1454: bipush          10
        //  1456: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1459: ldc_w           2147483647
        //  1462: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1465: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1468: putstatic       net/minecraft/world/entity/EntityType.LEASH_KNOT:Lnet/minecraft/world/entity/EntityType;
        //  1471: ldc_w           "lightning_bolt"
        //  1474: invokedynamic   BootstrapMethod #46, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1479: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1482: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1485: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSave:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  1488: fconst_0       
        //  1489: fconst_0       
        //  1490: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1493: bipush          16
        //  1495: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1498: ldc_w           2147483647
        //  1501: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1504: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1507: putstatic       net/minecraft/world/entity/EntityType.LIGHTNING_BOLT:Lnet/minecraft/world/entity/EntityType;
        //  1510: ldc_w           "llama"
        //  1513: invokedynamic   BootstrapMethod #47, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1518: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1521: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1524: ldc_w           0.9
        //  1527: ldc_w           1.87
        //  1530: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1533: bipush          10
        //  1535: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1538: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1541: putstatic       net/minecraft/world/entity/EntityType.LLAMA:Lnet/minecraft/world/entity/EntityType;
        //  1544: ldc_w           "llama_spit"
        //  1547: invokedynamic   BootstrapMethod #48, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1552: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1555: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1558: ldc_w           0.25
        //  1561: ldc_w           0.25
        //  1564: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1567: iconst_4       
        //  1568: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1571: bipush          10
        //  1573: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1576: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1579: putstatic       net/minecraft/world/entity/EntityType.LLAMA_SPIT:Lnet/minecraft/world/entity/EntityType;
        //  1582: ldc_w           "magma_cube"
        //  1585: invokedynamic   BootstrapMethod #49, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1590: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  1593: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1596: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  1599: ldc_w           2.04
        //  1602: ldc_w           2.04
        //  1605: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1608: bipush          8
        //  1610: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1613: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1616: putstatic       net/minecraft/world/entity/EntityType.MAGMA_CUBE:Lnet/minecraft/world/entity/EntityType;
        //  1619: ldc_w           "minecart"
        //  1622: invokedynamic   BootstrapMethod #50, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1627: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1630: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1633: ldc_w           0.98
        //  1636: ldc_w           0.7
        //  1639: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1642: bipush          8
        //  1644: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1647: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1650: putstatic       net/minecraft/world/entity/EntityType.MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1653: ldc_w           "chest_minecart"
        //  1656: invokedynamic   BootstrapMethod #51, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1661: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1664: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1667: ldc_w           0.98
        //  1670: ldc_w           0.7
        //  1673: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1676: bipush          8
        //  1678: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1681: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1684: putstatic       net/minecraft/world/entity/EntityType.CHEST_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1687: ldc_w           "command_block_minecart"
        //  1690: invokedynamic   BootstrapMethod #52, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1695: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1698: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1701: ldc_w           0.98
        //  1704: ldc_w           0.7
        //  1707: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1710: bipush          8
        //  1712: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1715: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1718: putstatic       net/minecraft/world/entity/EntityType.COMMAND_BLOCK_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1721: ldc_w           "furnace_minecart"
        //  1724: invokedynamic   BootstrapMethod #53, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1729: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1732: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1735: ldc_w           0.98
        //  1738: ldc_w           0.7
        //  1741: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1744: bipush          8
        //  1746: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1749: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1752: putstatic       net/minecraft/world/entity/EntityType.FURNACE_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1755: ldc_w           "hopper_minecart"
        //  1758: invokedynamic   BootstrapMethod #54, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1763: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1766: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1769: ldc_w           0.98
        //  1772: ldc_w           0.7
        //  1775: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1778: bipush          8
        //  1780: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1783: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1786: putstatic       net/minecraft/world/entity/EntityType.HOPPER_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1789: ldc_w           "spawner_minecart"
        //  1792: invokedynamic   BootstrapMethod #55, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1797: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1800: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1803: ldc_w           0.98
        //  1806: ldc_w           0.7
        //  1809: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1812: bipush          8
        //  1814: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1817: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1820: putstatic       net/minecraft/world/entity/EntityType.SPAWNER_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1823: ldc_w           "tnt_minecart"
        //  1826: invokedynamic   BootstrapMethod #56, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1831: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1834: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1837: ldc_w           0.98
        //  1840: ldc_w           0.7
        //  1843: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1846: bipush          8
        //  1848: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1851: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1854: putstatic       net/minecraft/world/entity/EntityType.TNT_MINECART:Lnet/minecraft/world/entity/EntityType;
        //  1857: ldc_w           "mule"
        //  1860: invokedynamic   BootstrapMethod #57, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1865: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1868: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1871: ldc_w           1.3964844
        //  1874: ldc_w           1.6
        //  1877: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1880: bipush          8
        //  1882: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1885: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1888: putstatic       net/minecraft/world/entity/EntityType.MULE:Lnet/minecraft/world/entity/EntityType;
        //  1891: ldc_w           "mooshroom"
        //  1894: invokedynamic   BootstrapMethod #58, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1899: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1902: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1905: ldc_w           0.9
        //  1908: ldc_w           1.4
        //  1911: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1914: bipush          10
        //  1916: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1919: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1922: putstatic       net/minecraft/world/entity/EntityType.MOOSHROOM:Lnet/minecraft/world/entity/EntityType;
        //  1925: ldc_w           "ocelot"
        //  1928: invokedynamic   BootstrapMethod #59, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1933: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  1936: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1939: ldc_w           0.6
        //  1942: ldc_w           0.7
        //  1945: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1948: bipush          10
        //  1950: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1953: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1956: putstatic       net/minecraft/world/entity/EntityType.OCELOT:Lnet/minecraft/world/entity/EntityType;
        //  1959: ldc_w           "painting"
        //  1962: invokedynamic   BootstrapMethod #60, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  1967: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  1970: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1973: ldc_w           0.5
        //  1976: ldc_w           0.5
        //  1979: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1982: bipush          10
        //  1984: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1987: ldc_w           2147483647
        //  1990: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  1993: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  1996: putstatic       net/minecraft/world/entity/EntityType.PAINTING:Lnet/minecraft/world/entity/EntityType;
        //  1999: ldc_w           "panda"
        //  2002: invokedynamic   BootstrapMethod #61, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2007: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2010: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2013: ldc_w           1.3
        //  2016: ldc_w           1.25
        //  2019: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2022: bipush          10
        //  2024: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2027: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2030: putstatic       net/minecraft/world/entity/EntityType.PANDA:Lnet/minecraft/world/entity/EntityType;
        //  2033: ldc_w           "parrot"
        //  2036: invokedynamic   BootstrapMethod #62, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2041: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2044: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2047: ldc_w           0.5
        //  2050: ldc_w           0.9
        //  2053: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2056: bipush          8
        //  2058: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2061: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2064: putstatic       net/minecraft/world/entity/EntityType.PARROT:Lnet/minecraft/world/entity/EntityType;
        //  2067: ldc_w           "phantom"
        //  2070: invokedynamic   BootstrapMethod #63, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2075: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2078: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2081: ldc_w           0.9
        //  2084: ldc_w           0.5
        //  2087: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2090: bipush          8
        //  2092: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2095: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2098: putstatic       net/minecraft/world/entity/EntityType.PHANTOM:Lnet/minecraft/world/entity/EntityType;
        //  2101: ldc_w           "pig"
        //  2104: invokedynamic   BootstrapMethod #64, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2109: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2112: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2115: ldc_w           0.9
        //  2118: ldc_w           0.9
        //  2121: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2124: bipush          10
        //  2126: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2129: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2132: putstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        //  2135: ldc_w           "piglin"
        //  2138: invokedynamic   BootstrapMethod #65, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2143: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2146: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2149: ldc_w           0.6
        //  2152: ldc_w           1.95
        //  2155: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2158: bipush          8
        //  2160: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2163: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2166: putstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  2169: ldc_w           "piglin_brute"
        //  2172: invokedynamic   BootstrapMethod #66, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2177: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2180: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2183: ldc_w           0.6
        //  2186: ldc_w           1.95
        //  2189: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2192: bipush          8
        //  2194: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2197: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2200: putstatic       net/minecraft/world/entity/EntityType.PIGLIN_BRUTE:Lnet/minecraft/world/entity/EntityType;
        //  2203: ldc_w           "pillager"
        //  2206: invokedynamic   BootstrapMethod #67, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2211: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2214: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2217: invokevirtual   net/minecraft/world/entity/EntityType$Builder.canSpawnFarFromPlayer:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  2220: ldc_w           0.6
        //  2223: ldc_w           1.95
        //  2226: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2229: bipush          8
        //  2231: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2234: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2237: putstatic       net/minecraft/world/entity/EntityType.PILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  2240: ldc_w           "polar_bear"
        //  2243: invokedynamic   BootstrapMethod #68, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2248: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2251: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2254: ldc_w           1.4
        //  2257: ldc_w           1.4
        //  2260: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2263: bipush          10
        //  2265: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2268: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2271: putstatic       net/minecraft/world/entity/EntityType.POLAR_BEAR:Lnet/minecraft/world/entity/EntityType;
        //  2274: ldc_w           "tnt"
        //  2277: invokedynamic   BootstrapMethod #69, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2282: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2285: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2288: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  2291: ldc_w           0.98
        //  2294: ldc_w           0.98
        //  2297: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2300: bipush          10
        //  2302: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2305: bipush          10
        //  2307: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2310: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2313: putstatic       net/minecraft/world/entity/EntityType.TNT:Lnet/minecraft/world/entity/EntityType;
        //  2316: ldc_w           "pufferfish"
        //  2319: invokedynamic   BootstrapMethod #70, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2324: getstatic       net/minecraft/world/entity/MobCategory.WATER_AMBIENT:Lnet/minecraft/world/entity/MobCategory;
        //  2327: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2330: ldc_w           0.7
        //  2333: ldc_w           0.7
        //  2336: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2339: iconst_4       
        //  2340: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2343: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2346: putstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        //  2349: ldc_w           "rabbit"
        //  2352: invokedynamic   BootstrapMethod #71, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2357: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2360: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2363: ldc_w           0.4
        //  2366: ldc_w           0.5
        //  2369: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2372: bipush          8
        //  2374: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2377: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2380: putstatic       net/minecraft/world/entity/EntityType.RABBIT:Lnet/minecraft/world/entity/EntityType;
        //  2383: ldc_w           "ravager"
        //  2386: invokedynamic   BootstrapMethod #72, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2391: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2394: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2397: ldc_w           1.95
        //  2400: ldc_w           2.2
        //  2403: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2406: bipush          10
        //  2408: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2411: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2414: putstatic       net/minecraft/world/entity/EntityType.RAVAGER:Lnet/minecraft/world/entity/EntityType;
        //  2417: ldc_w           "salmon"
        //  2420: invokedynamic   BootstrapMethod #73, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2425: getstatic       net/minecraft/world/entity/MobCategory.WATER_AMBIENT:Lnet/minecraft/world/entity/MobCategory;
        //  2428: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2431: ldc_w           0.7
        //  2434: ldc_w           0.4
        //  2437: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2440: iconst_4       
        //  2441: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2444: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2447: putstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        //  2450: ldc_w           "sheep"
        //  2453: invokedynamic   BootstrapMethod #74, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2458: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2461: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2464: ldc_w           0.9
        //  2467: ldc_w           1.3
        //  2470: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2473: bipush          10
        //  2475: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2478: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2481: putstatic       net/minecraft/world/entity/EntityType.SHEEP:Lnet/minecraft/world/entity/EntityType;
        //  2484: ldc_w           "shulker"
        //  2487: invokedynamic   BootstrapMethod #75, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2492: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2495: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2498: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  2501: invokevirtual   net/minecraft/world/entity/EntityType$Builder.canSpawnFarFromPlayer:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  2504: fconst_1       
        //  2505: fconst_1       
        //  2506: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2509: bipush          10
        //  2511: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2514: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2517: putstatic       net/minecraft/world/entity/EntityType.SHULKER:Lnet/minecraft/world/entity/EntityType;
        //  2520: ldc_w           "shulker_bullet"
        //  2523: invokedynamic   BootstrapMethod #76, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2528: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2531: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2534: ldc_w           0.3125
        //  2537: ldc_w           0.3125
        //  2540: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2543: bipush          8
        //  2545: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2548: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2551: putstatic       net/minecraft/world/entity/EntityType.SHULKER_BULLET:Lnet/minecraft/world/entity/EntityType;
        //  2554: ldc_w           "silverfish"
        //  2557: invokedynamic   BootstrapMethod #77, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2562: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2565: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2568: ldc_w           0.4
        //  2571: ldc_w           0.3
        //  2574: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2577: bipush          8
        //  2579: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2582: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2585: putstatic       net/minecraft/world/entity/EntityType.SILVERFISH:Lnet/minecraft/world/entity/EntityType;
        //  2588: ldc_w           "skeleton"
        //  2591: invokedynamic   BootstrapMethod #78, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2596: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2599: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2602: ldc_w           0.6
        //  2605: ldc_w           1.99
        //  2608: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2611: bipush          8
        //  2613: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2616: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2619: putstatic       net/minecraft/world/entity/EntityType.SKELETON:Lnet/minecraft/world/entity/EntityType;
        //  2622: ldc_w           "skeleton_horse"
        //  2625: invokedynamic   BootstrapMethod #79, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2630: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2633: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2636: ldc_w           1.3964844
        //  2639: ldc_w           1.6
        //  2642: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2645: bipush          10
        //  2647: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2650: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2653: putstatic       net/minecraft/world/entity/EntityType.SKELETON_HORSE:Lnet/minecraft/world/entity/EntityType;
        //  2656: ldc_w           "slime"
        //  2659: invokedynamic   BootstrapMethod #80, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2664: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2667: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2670: ldc_w           2.04
        //  2673: ldc_w           2.04
        //  2676: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2679: bipush          10
        //  2681: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2684: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2687: putstatic       net/minecraft/world/entity/EntityType.SLIME:Lnet/minecraft/world/entity/EntityType;
        //  2690: ldc_w           "small_fireball"
        //  2693: invokedynamic   BootstrapMethod #81, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2698: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2701: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2704: ldc_w           0.3125
        //  2707: ldc_w           0.3125
        //  2710: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2713: iconst_4       
        //  2714: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2717: bipush          10
        //  2719: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2722: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2725: putstatic       net/minecraft/world/entity/EntityType.SMALL_FIREBALL:Lnet/minecraft/world/entity/EntityType;
        //  2728: ldc_w           "snow_golem"
        //  2731: invokedynamic   BootstrapMethod #82, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2736: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2739: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2742: ldc_w           0.7
        //  2745: ldc_w           1.9
        //  2748: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2751: bipush          8
        //  2753: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2756: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2759: putstatic       net/minecraft/world/entity/EntityType.SNOW_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //  2762: ldc_w           "snowball"
        //  2765: invokedynamic   BootstrapMethod #83, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2770: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2773: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2776: ldc_w           0.25
        //  2779: ldc_w           0.25
        //  2782: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2785: iconst_4       
        //  2786: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2789: bipush          10
        //  2791: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2794: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2797: putstatic       net/minecraft/world/entity/EntityType.SNOWBALL:Lnet/minecraft/world/entity/EntityType;
        //  2800: ldc_w           "spectral_arrow"
        //  2803: invokedynamic   BootstrapMethod #84, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2808: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2811: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2814: ldc_w           0.5
        //  2817: ldc_w           0.5
        //  2820: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2823: iconst_4       
        //  2824: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2827: bipush          20
        //  2829: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2832: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2835: putstatic       net/minecraft/world/entity/EntityType.SPECTRAL_ARROW:Lnet/minecraft/world/entity/EntityType;
        //  2838: ldc_w           "spider"
        //  2841: invokedynamic   BootstrapMethod #85, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2846: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2849: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2852: ldc_w           1.4
        //  2855: ldc_w           0.9
        //  2858: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2861: bipush          8
        //  2863: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2866: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2869: putstatic       net/minecraft/world/entity/EntityType.SPIDER:Lnet/minecraft/world/entity/EntityType;
        //  2872: ldc_w           "squid"
        //  2875: invokedynamic   BootstrapMethod #86, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2880: getstatic       net/minecraft/world/entity/MobCategory.WATER_CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2883: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2886: ldc_w           0.8
        //  2889: ldc_w           0.8
        //  2892: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2895: bipush          8
        //  2897: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2900: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2903: putstatic       net/minecraft/world/entity/EntityType.SQUID:Lnet/minecraft/world/entity/EntityType;
        //  2906: ldc_w           "stray"
        //  2909: invokedynamic   BootstrapMethod #87, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2914: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  2917: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2920: ldc_w           0.6
        //  2923: ldc_w           1.99
        //  2926: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2929: bipush          8
        //  2931: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2934: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2937: putstatic       net/minecraft/world/entity/EntityType.STRAY:Lnet/minecraft/world/entity/EntityType;
        //  2940: ldc_w           "strider"
        //  2943: invokedynamic   BootstrapMethod #88, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2948: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  2951: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2954: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  2957: ldc_w           0.9
        //  2960: ldc_w           1.7
        //  2963: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2966: bipush          10
        //  2968: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2971: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  2974: putstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        //  2977: ldc_w           "egg"
        //  2980: invokedynamic   BootstrapMethod #89, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  2985: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  2988: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  2991: ldc_w           0.25
        //  2994: ldc_w           0.25
        //  2997: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3000: iconst_4       
        //  3001: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3004: bipush          10
        //  3006: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3009: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3012: putstatic       net/minecraft/world/entity/EntityType.EGG:Lnet/minecraft/world/entity/EntityType;
        //  3015: ldc_w           "ender_pearl"
        //  3018: invokedynamic   BootstrapMethod #90, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3023: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3026: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3029: ldc_w           0.25
        //  3032: ldc_w           0.25
        //  3035: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3038: iconst_4       
        //  3039: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3042: bipush          10
        //  3044: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3047: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3050: putstatic       net/minecraft/world/entity/EntityType.ENDER_PEARL:Lnet/minecraft/world/entity/EntityType;
        //  3053: ldc_w           "experience_bottle"
        //  3056: invokedynamic   BootstrapMethod #91, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3061: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3064: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3067: ldc_w           0.25
        //  3070: ldc_w           0.25
        //  3073: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3076: iconst_4       
        //  3077: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3080: bipush          10
        //  3082: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3085: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3088: putstatic       net/minecraft/world/entity/EntityType.EXPERIENCE_BOTTLE:Lnet/minecraft/world/entity/EntityType;
        //  3091: ldc_w           "potion"
        //  3094: invokedynamic   BootstrapMethod #92, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3099: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3102: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3105: ldc_w           0.25
        //  3108: ldc_w           0.25
        //  3111: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3114: iconst_4       
        //  3115: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3118: bipush          10
        //  3120: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3123: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3126: putstatic       net/minecraft/world/entity/EntityType.POTION:Lnet/minecraft/world/entity/EntityType;
        //  3129: ldc_w           "trident"
        //  3132: invokedynamic   BootstrapMethod #93, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3137: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3140: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3143: ldc_w           0.5
        //  3146: ldc_w           0.5
        //  3149: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3152: iconst_4       
        //  3153: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3156: bipush          20
        //  3158: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3161: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3164: putstatic       net/minecraft/world/entity/EntityType.TRIDENT:Lnet/minecraft/world/entity/EntityType;
        //  3167: ldc_w           "trader_llama"
        //  3170: invokedynamic   BootstrapMethod #94, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3175: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  3178: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3181: ldc_w           0.9
        //  3184: ldc_w           1.87
        //  3187: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3190: bipush          10
        //  3192: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3195: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3198: putstatic       net/minecraft/world/entity/EntityType.TRADER_LLAMA:Lnet/minecraft/world/entity/EntityType;
        //  3201: ldc_w           "tropical_fish"
        //  3204: invokedynamic   BootstrapMethod #95, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3209: getstatic       net/minecraft/world/entity/MobCategory.WATER_AMBIENT:Lnet/minecraft/world/entity/MobCategory;
        //  3212: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3215: ldc_w           0.5
        //  3218: ldc_w           0.4
        //  3221: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3224: iconst_4       
        //  3225: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3228: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3231: putstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        //  3234: ldc_w           "turtle"
        //  3237: invokedynamic   BootstrapMethod #96, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3242: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  3245: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3248: ldc_w           1.2
        //  3251: ldc_w           0.4
        //  3254: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3257: bipush          10
        //  3259: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3262: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3265: putstatic       net/minecraft/world/entity/EntityType.TURTLE:Lnet/minecraft/world/entity/EntityType;
        //  3268: ldc_w           "vex"
        //  3271: invokedynamic   BootstrapMethod #97, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3276: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3279: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3282: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3285: ldc_w           0.4
        //  3288: ldc_w           0.8
        //  3291: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3294: bipush          8
        //  3296: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3299: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3302: putstatic       net/minecraft/world/entity/EntityType.VEX:Lnet/minecraft/world/entity/EntityType;
        //  3305: ldc_w           "villager"
        //  3308: invokedynamic   BootstrapMethod #98, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3313: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3316: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3319: ldc_w           0.6
        //  3322: ldc_w           1.95
        //  3325: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3328: bipush          10
        //  3330: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3333: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3336: putstatic       net/minecraft/world/entity/EntityType.VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  3339: ldc_w           "vindicator"
        //  3342: invokedynamic   BootstrapMethod #99, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3347: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3350: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3353: ldc_w           0.6
        //  3356: ldc_w           1.95
        //  3359: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3362: bipush          8
        //  3364: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3367: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3370: putstatic       net/minecraft/world/entity/EntityType.VINDICATOR:Lnet/minecraft/world/entity/EntityType;
        //  3373: ldc_w           "wandering_trader"
        //  3376: invokedynamic   BootstrapMethod #100, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3381: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  3384: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3387: ldc_w           0.6
        //  3390: ldc_w           1.95
        //  3393: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3396: bipush          10
        //  3398: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3401: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3404: putstatic       net/minecraft/world/entity/EntityType.WANDERING_TRADER:Lnet/minecraft/world/entity/EntityType;
        //  3407: ldc_w           "witch"
        //  3410: invokedynamic   BootstrapMethod #101, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3415: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3418: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3421: ldc_w           0.6
        //  3424: ldc_w           1.95
        //  3427: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3430: bipush          8
        //  3432: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3435: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3438: putstatic       net/minecraft/world/entity/EntityType.WITCH:Lnet/minecraft/world/entity/EntityType;
        //  3441: ldc_w           "wither"
        //  3444: invokedynamic   BootstrapMethod #102, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3449: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3452: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3455: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3458: iconst_1       
        //  3459: anewarray       Lnet/minecraft/world/level/block/Block;
        //  3462: dup            
        //  3463: iconst_0       
        //  3464: getstatic       net/minecraft/world/level/block/Blocks.WITHER_ROSE:Lnet/minecraft/world/level/block/Block;
        //  3467: aastore        
        //  3468: invokevirtual   net/minecraft/world/entity/EntityType$Builder.immuneTo:([Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3471: ldc_w           0.9
        //  3474: ldc_w           3.5
        //  3477: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3480: bipush          10
        //  3482: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3485: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3488: putstatic       net/minecraft/world/entity/EntityType.WITHER:Lnet/minecraft/world/entity/EntityType;
        //  3491: ldc_w           "wither_skeleton"
        //  3494: invokedynamic   BootstrapMethod #103, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3499: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3502: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3505: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3508: iconst_1       
        //  3509: anewarray       Lnet/minecraft/world/level/block/Block;
        //  3512: dup            
        //  3513: iconst_0       
        //  3514: getstatic       net/minecraft/world/level/block/Blocks.WITHER_ROSE:Lnet/minecraft/world/level/block/Block;
        //  3517: aastore        
        //  3518: invokevirtual   net/minecraft/world/entity/EntityType$Builder.immuneTo:([Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3521: ldc_w           0.7
        //  3524: ldc_w           2.4
        //  3527: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3530: bipush          8
        //  3532: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3535: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3538: putstatic       net/minecraft/world/entity/EntityType.WITHER_SKELETON:Lnet/minecraft/world/entity/EntityType;
        //  3541: ldc_w           "wither_skull"
        //  3544: invokedynamic   BootstrapMethod #104, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3549: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3552: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3555: ldc_w           0.3125
        //  3558: ldc_w           0.3125
        //  3561: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3564: iconst_4       
        //  3565: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3568: bipush          10
        //  3570: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3573: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3576: putstatic       net/minecraft/world/entity/EntityType.WITHER_SKULL:Lnet/minecraft/world/entity/EntityType;
        //  3579: ldc_w           "wolf"
        //  3582: invokedynamic   BootstrapMethod #105, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3587: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  3590: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3593: ldc_w           0.6
        //  3596: ldc_w           0.85
        //  3599: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3602: bipush          10
        //  3604: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3607: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3610: putstatic       net/minecraft/world/entity/EntityType.WOLF:Lnet/minecraft/world/entity/EntityType;
        //  3613: ldc_w           "zoglin"
        //  3616: invokedynamic   BootstrapMethod #106, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3621: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3624: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3627: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3630: ldc_w           1.3964844
        //  3633: ldc_w           1.4
        //  3636: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3639: bipush          8
        //  3641: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3644: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3647: putstatic       net/minecraft/world/entity/EntityType.ZOGLIN:Lnet/minecraft/world/entity/EntityType;
        //  3650: ldc_w           "zombie"
        //  3653: invokedynamic   BootstrapMethod #107, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3658: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3661: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3664: ldc_w           0.6
        //  3667: ldc_w           1.95
        //  3670: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3673: bipush          8
        //  3675: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3678: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3681: putstatic       net/minecraft/world/entity/EntityType.ZOMBIE:Lnet/minecraft/world/entity/EntityType;
        //  3684: ldc_w           "zombie_horse"
        //  3687: invokedynamic   BootstrapMethod #108, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3692: getstatic       net/minecraft/world/entity/MobCategory.CREATURE:Lnet/minecraft/world/entity/MobCategory;
        //  3695: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3698: ldc_w           1.3964844
        //  3701: ldc_w           1.6
        //  3704: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3707: bipush          10
        //  3709: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3712: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3715: putstatic       net/minecraft/world/entity/EntityType.ZOMBIE_HORSE:Lnet/minecraft/world/entity/EntityType;
        //  3718: ldc_w           "zombie_villager"
        //  3721: invokedynamic   BootstrapMethod #109, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3726: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3729: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3732: ldc_w           0.6
        //  3735: ldc_w           1.95
        //  3738: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3741: bipush          8
        //  3743: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3746: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3749: putstatic       net/minecraft/world/entity/EntityType.ZOMBIE_VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  3752: ldc_w           "zombified_piglin"
        //  3755: invokedynamic   BootstrapMethod #110, create:()Lnet/minecraft/world/entity/EntityType$EntityFactory;
        //  3760: getstatic       net/minecraft/world/entity/MobCategory.MONSTER:Lnet/minecraft/world/entity/MobCategory;
        //  3763: invokestatic    net/minecraft/world/entity/EntityType$Builder.of:(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3766: invokevirtual   net/minecraft/world/entity/EntityType$Builder.fireImmune:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3769: ldc_w           0.6
        //  3772: ldc_w           1.95
        //  3775: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3778: bipush          8
        //  3780: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3783: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3786: putstatic       net/minecraft/world/entity/EntityType.ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  3789: ldc_w           "player"
        //  3792: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3795: invokestatic    net/minecraft/world/entity/EntityType$Builder.createNothing:(Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3798: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSave:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3801: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSummon:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3804: ldc_w           0.6
        //  3807: ldc_w           1.8
        //  3810: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3813: bipush          32
        //  3815: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3818: iconst_2       
        //  3819: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3822: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3825: putstatic       net/minecraft/world/entity/EntityType.PLAYER:Lnet/minecraft/world/entity/EntityType;
        //  3828: ldc_w           "fishing_bobber"
        //  3831: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  3834: invokestatic    net/minecraft/world/entity/EntityType$Builder.createNothing:(Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3837: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSave:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3840: invokevirtual   net/minecraft/world/entity/EntityType$Builder.noSummon:()Lnet/minecraft/world/entity/EntityType$Builder;
        //  3843: ldc_w           0.25
        //  3846: ldc_w           0.25
        //  3849: invokevirtual   net/minecraft/world/entity/EntityType$Builder.sized:(FF)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3852: iconst_4       
        //  3853: invokevirtual   net/minecraft/world/entity/EntityType$Builder.clientTrackingRange:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3856: iconst_5       
        //  3857: invokevirtual   net/minecraft/world/entity/EntityType$Builder.updateInterval:(I)Lnet/minecraft/world/entity/EntityType$Builder;
        //  3860: invokestatic    net/minecraft/world/entity/EntityType.register:(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;
        //  3863: putstatic       net/minecraft/world/entity/EntityType.FISHING_BOBBER:Lnet/minecraft/world/entity/EntityType;
        //  3866: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2502)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.execLocalTasks(ForkJoinPool.java:1040)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1058)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static class Builder<T extends Entity> {
        private final EntityFactory<T> factory;
        private final MobCategory category;
        private ImmutableSet<Block> immuneTo;
        private boolean serialize;
        private boolean summon;
        private boolean fireImmune;
        private boolean canSpawnFarFromPlayer;
        private int clientTrackingRange;
        private int updateInterval;
        private EntityDimensions dimensions;
        
        private Builder(final EntityFactory<T> b, final MobCategory aql) {
            this.immuneTo = (ImmutableSet<Block>)ImmutableSet.of();
            this.serialize = true;
            this.summon = true;
            this.clientTrackingRange = 5;
            this.updateInterval = 3;
            this.dimensions = EntityDimensions.scalable(0.6f, 1.8f);
            this.factory = b;
            this.category = aql;
            this.canSpawnFarFromPlayer = (aql == MobCategory.CREATURE || aql == MobCategory.MISC);
        }
        
        public static <T extends Entity> Builder<T> of(final EntityFactory<T> b, final MobCategory aql) {
            return new Builder<T>(b, aql);
        }
        
        public static <T extends Entity> Builder<T> createNothing(final MobCategory aql) {
            return new Builder<T>((aqb, bru) -> null, aql);
        }
        
        public Builder<T> sized(final float float1, final float float2) {
            this.dimensions = EntityDimensions.scalable(float1, float2);
            return this;
        }
        
        public Builder<T> noSummon() {
            this.summon = false;
            return this;
        }
        
        public Builder<T> noSave() {
            this.serialize = false;
            return this;
        }
        
        public Builder<T> fireImmune() {
            this.fireImmune = true;
            return this;
        }
        
        public Builder<T> immuneTo(final Block... arr) {
            this.immuneTo = (ImmutableSet<Block>)ImmutableSet.copyOf((Object[])arr);
            return this;
        }
        
        public Builder<T> canSpawnFarFromPlayer() {
            this.canSpawnFarFromPlayer = true;
            return this;
        }
        
        public Builder<T> clientTrackingRange(final int integer) {
            this.clientTrackingRange = integer;
            return this;
        }
        
        public Builder<T> updateInterval(final int integer) {
            this.updateInterval = integer;
            return this;
        }
        
        public EntityType<T> build(final String string) {
            if (this.serialize) {
                Util.fetchChoiceType(References.ENTITY_TREE, string);
            }
            return new EntityType<T>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval);
        }
    }
    
    public interface EntityFactory<T extends Entity> {
        T create(final EntityType<T> aqb, final Level bru);
    }
}
