package net.minecraft.client.renderer.entity;

import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.Sheets;
import com.mojang.math.Vector3f;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.phys.AABB;
import com.mojang.math.Matrix4f;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelReader;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.player.AbstractClientPlayer;
import java.util.Iterator;
import net.minecraft.core.Registry;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
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
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.projectile.ThrownPotion;
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
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;
import com.mojang.math.Quaternion;
import net.minecraft.client.Camera;
import net.minecraft.world.level.Level;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import net.minecraft.client.renderer.RenderType;

public class EntityRenderDispatcher {
    private static final RenderType SHADOW_RENDER_TYPE;
    private final Map<EntityType<?>, EntityRenderer<?>> renderers;
    private final Map<String, PlayerRenderer> playerRenderers;
    private final PlayerRenderer defaultPlayerRenderer;
    private final Font font;
    public final TextureManager textureManager;
    private Level level;
    public Camera camera;
    private Quaternion cameraOrientation;
    public Entity crosshairPickEntity;
    public final Options options;
    private boolean shouldRenderShadow;
    private boolean renderHitBoxes;
    
    public <E extends Entity> int getPackedLightCoords(final E apx, final float float2) {
        return this.<E>getRenderer(apx).getPackedLightCoords(apx, float2);
    }
    
    private <T extends Entity> void register(final EntityType<T> aqb, final EntityRenderer<? super T> eem) {
        this.renderers.put(aqb, eem);
    }
    
    private void registerRenderers(final ItemRenderer efg, final ReloadableResourceManager acd) {
        this.<AreaEffectCloud>register(EntityType.AREA_EFFECT_CLOUD, new AreaEffectCloudRenderer(this));
        this.<ArmorStand>register(EntityType.ARMOR_STAND, new ArmorStandRenderer(this));
        this.<Arrow>register(EntityType.ARROW, new TippableArrowRenderer(this));
        this.<Bat>register(EntityType.BAT, new BatRenderer(this));
        this.<Bee>register(EntityType.BEE, new BeeRenderer(this));
        this.<Blaze>register(EntityType.BLAZE, new BlazeRenderer(this));
        this.<Boat>register(EntityType.BOAT, new BoatRenderer(this));
        this.<Cat>register(EntityType.CAT, new CatRenderer(this));
        this.<CaveSpider>register(EntityType.CAVE_SPIDER, new CaveSpiderRenderer(this));
        this.<MinecartChest>register(EntityType.CHEST_MINECART, new MinecartRenderer<>(this));
        this.<Chicken>register(EntityType.CHICKEN, new ChickenRenderer(this));
        this.<Cod>register(EntityType.COD, new CodRenderer(this));
        this.<MinecartCommandBlock>register(EntityType.COMMAND_BLOCK_MINECART, new MinecartRenderer<>(this));
        this.<Cow>register(EntityType.COW, new CowRenderer(this));
        this.<Creeper>register(EntityType.CREEPER, new CreeperRenderer(this));
        this.<Dolphin>register(EntityType.DOLPHIN, new DolphinRenderer(this));
        this.<Donkey>register(EntityType.DONKEY, new ChestedHorseRenderer(this, 0.87f));
        this.<DragonFireball>register(EntityType.DRAGON_FIREBALL, new DragonFireballRenderer(this));
        this.<Drowned>register(EntityType.DROWNED, new DrownedRenderer(this));
        this.<ThrownEgg>register(EntityType.EGG, new ThrownItemRenderer<>(this, efg));
        this.<ElderGuardian>register(EntityType.ELDER_GUARDIAN, new ElderGuardianRenderer(this));
        this.<EndCrystal>register(EntityType.END_CRYSTAL, new EndCrystalRenderer(this));
        this.<EnderDragon>register(EntityType.ENDER_DRAGON, new EnderDragonRenderer(this));
        this.<EnderMan>register(EntityType.ENDERMAN, new EndermanRenderer(this));
        this.<Endermite>register(EntityType.ENDERMITE, new EndermiteRenderer(this));
        this.<ThrownEnderpearl>register(EntityType.ENDER_PEARL, new ThrownItemRenderer<>(this, efg));
        this.<EvokerFangs>register(EntityType.EVOKER_FANGS, new EvokerFangsRenderer(this));
        this.<Evoker>register(EntityType.EVOKER, new EvokerRenderer(this));
        this.<ThrownExperienceBottle>register(EntityType.EXPERIENCE_BOTTLE, new ThrownItemRenderer<>(this, efg));
        this.<ExperienceOrb>register(EntityType.EXPERIENCE_ORB, new ExperienceOrbRenderer(this));
        this.<EyeOfEnder>register(EntityType.EYE_OF_ENDER, new ThrownItemRenderer<>(this, efg, 1.0f, true));
        this.<FallingBlockEntity>register(EntityType.FALLING_BLOCK, new FallingBlockRenderer(this));
        this.<LargeFireball>register(EntityType.FIREBALL, new ThrownItemRenderer<>(this, efg, 3.0f, true));
        this.<FireworkRocketEntity>register(EntityType.FIREWORK_ROCKET, new FireworkEntityRenderer(this, efg));
        this.<FishingHook>register(EntityType.FISHING_BOBBER, new FishingHookRenderer(this));
        this.<Fox>register(EntityType.FOX, new FoxRenderer(this));
        this.<MinecartFurnace>register(EntityType.FURNACE_MINECART, new MinecartRenderer<>(this));
        this.<Ghast>register(EntityType.GHAST, new GhastRenderer(this));
        this.<Giant>register(EntityType.GIANT, new GiantMobRenderer(this, 6.0f));
        this.<Guardian>register(EntityType.GUARDIAN, new GuardianRenderer(this));
        this.<Hoglin>register(EntityType.HOGLIN, new HoglinRenderer(this));
        this.<MinecartHopper>register(EntityType.HOPPER_MINECART, new MinecartRenderer<>(this));
        this.<Horse>register(EntityType.HORSE, new HorseRenderer(this));
        this.<Husk>register(EntityType.HUSK, new HuskRenderer(this));
        this.<Illusioner>register(EntityType.ILLUSIONER, new IllusionerRenderer(this));
        this.<IronGolem>register(EntityType.IRON_GOLEM, new IronGolemRenderer(this));
        this.<ItemEntity>register(EntityType.ITEM, new ItemEntityRenderer(this, efg));
        this.<ItemFrame>register(EntityType.ITEM_FRAME, new ItemFrameRenderer(this, efg));
        this.<LeashFenceKnotEntity>register(EntityType.LEASH_KNOT, new LeashKnotRenderer(this));
        this.<LightningBolt>register(EntityType.LIGHTNING_BOLT, new LightningBoltRenderer(this));
        this.<Llama>register(EntityType.LLAMA, new LlamaRenderer(this));
        this.<LlamaSpit>register(EntityType.LLAMA_SPIT, new LlamaSpitRenderer(this));
        this.<MagmaCube>register(EntityType.MAGMA_CUBE, new MagmaCubeRenderer(this));
        this.<Minecart>register(EntityType.MINECART, new MinecartRenderer<>(this));
        this.<MushroomCow>register(EntityType.MOOSHROOM, new MushroomCowRenderer(this));
        this.<Mule>register(EntityType.MULE, new ChestedHorseRenderer(this, 0.92f));
        this.<Ocelot>register(EntityType.OCELOT, new OcelotRenderer(this));
        this.<Painting>register(EntityType.PAINTING, new PaintingRenderer(this));
        this.<Panda>register(EntityType.PANDA, new PandaRenderer(this));
        this.<Parrot>register(EntityType.PARROT, new ParrotRenderer(this));
        this.<Phantom>register(EntityType.PHANTOM, new PhantomRenderer(this));
        this.<Pig>register(EntityType.PIG, new PigRenderer(this));
        this.<Piglin>register(EntityType.PIGLIN, new PiglinRenderer(this, false));
        this.<PiglinBrute>register(EntityType.PIGLIN_BRUTE, new PiglinRenderer(this, false));
        this.<Pillager>register(EntityType.PILLAGER, new PillagerRenderer(this));
        this.<PolarBear>register(EntityType.POLAR_BEAR, new PolarBearRenderer(this));
        this.<ThrownPotion>register(EntityType.POTION, new ThrownItemRenderer<>(this, efg));
        this.<Pufferfish>register(EntityType.PUFFERFISH, new PufferfishRenderer(this));
        this.<Rabbit>register(EntityType.RABBIT, new RabbitRenderer(this));
        this.<Ravager>register(EntityType.RAVAGER, new RavagerRenderer(this));
        this.<Salmon>register(EntityType.SALMON, new SalmonRenderer(this));
        this.<Sheep>register(EntityType.SHEEP, new SheepRenderer(this));
        this.<ShulkerBullet>register(EntityType.SHULKER_BULLET, new ShulkerBulletRenderer(this));
        this.<Shulker>register(EntityType.SHULKER, new ShulkerRenderer(this));
        this.<Silverfish>register(EntityType.SILVERFISH, new SilverfishRenderer(this));
        this.<SkeletonHorse>register(EntityType.SKELETON_HORSE, new UndeadHorseRenderer(this));
        this.<Skeleton>register(EntityType.SKELETON, new SkeletonRenderer(this));
        this.<Slime>register(EntityType.SLIME, new SlimeRenderer(this));
        this.<SmallFireball>register(EntityType.SMALL_FIREBALL, new ThrownItemRenderer<>(this, efg, 0.75f, true));
        this.<Snowball>register(EntityType.SNOWBALL, new ThrownItemRenderer<>(this, efg));
        this.<SnowGolem>register(EntityType.SNOW_GOLEM, new SnowGolemRenderer(this));
        this.<MinecartSpawner>register(EntityType.SPAWNER_MINECART, new MinecartRenderer<>(this));
        this.<SpectralArrow>register(EntityType.SPECTRAL_ARROW, new SpectralArrowRenderer(this));
        this.<Spider>register(EntityType.SPIDER, new SpiderRenderer(this));
        this.<Squid>register(EntityType.SQUID, new SquidRenderer(this));
        this.<Stray>register(EntityType.STRAY, new StrayRenderer(this));
        this.<MinecartTNT>register(EntityType.TNT_MINECART, new TntMinecartRenderer(this));
        this.<PrimedTnt>register(EntityType.TNT, new TntRenderer(this));
        this.<TraderLlama>register(EntityType.TRADER_LLAMA, new LlamaRenderer(this));
        this.<ThrownTrident>register(EntityType.TRIDENT, new ThrownTridentRenderer(this));
        this.<TropicalFish>register(EntityType.TROPICAL_FISH, new TropicalFishRenderer(this));
        this.<Turtle>register(EntityType.TURTLE, new TurtleRenderer(this));
        this.<Vex>register(EntityType.VEX, new VexRenderer(this));
        this.<Villager>register(EntityType.VILLAGER, new VillagerRenderer(this, acd));
        this.<Vindicator>register(EntityType.VINDICATOR, new VindicatorRenderer(this));
        this.<WanderingTrader>register(EntityType.WANDERING_TRADER, new WanderingTraderRenderer(this));
        this.<Witch>register(EntityType.WITCH, new WitchRenderer(this));
        this.<WitherBoss>register(EntityType.WITHER, new WitherBossRenderer(this));
        this.<WitherSkeleton>register(EntityType.WITHER_SKELETON, new WitherSkeletonRenderer(this));
        this.<WitherSkull>register(EntityType.WITHER_SKULL, new WitherSkullRenderer(this));
        this.<Wolf>register(EntityType.WOLF, new WolfRenderer(this));
        this.<Zoglin>register(EntityType.ZOGLIN, new ZoglinRenderer(this));
        this.<ZombieHorse>register(EntityType.ZOMBIE_HORSE, new UndeadHorseRenderer(this));
        this.<Zombie>register(EntityType.ZOMBIE, new ZombieRenderer(this));
        this.<ZombifiedPiglin>register(EntityType.ZOMBIFIED_PIGLIN, new PiglinRenderer(this, true));
        this.<ZombieVillager>register(EntityType.ZOMBIE_VILLAGER, new ZombieVillagerRenderer(this, acd));
        this.<Strider>register(EntityType.STRIDER, new StriderRenderer(this));
    }
    
    public EntityRenderDispatcher(final TextureManager ejv, final ItemRenderer efg, final ReloadableResourceManager acd, final Font dkr, final Options dka) {
        this.renderers = (Map<EntityType<?>, EntityRenderer<?>>)Maps.newHashMap();
        this.playerRenderers = (Map<String, PlayerRenderer>)Maps.newHashMap();
        this.shouldRenderShadow = true;
        this.textureManager = ejv;
        this.font = dkr;
        this.options = dka;
        this.registerRenderers(efg, acd);
        this.defaultPlayerRenderer = new PlayerRenderer(this);
        this.playerRenderers.put("default", this.defaultPlayerRenderer);
        this.playerRenderers.put("slim", new PlayerRenderer(this, true));
        for (final EntityType<?> aqb8 : Registry.ENTITY_TYPE) {
            if (aqb8 == EntityType.PLAYER) {
                continue;
            }
            if (!this.renderers.containsKey(aqb8)) {
                throw new IllegalStateException(new StringBuilder().append("No renderer registered for ").append(Registry.ENTITY_TYPE.getKey(aqb8)).toString());
            }
        }
    }
    
    public <T extends Entity> EntityRenderer<? super T> getRenderer(final T apx) {
        if (!(apx instanceof AbstractClientPlayer)) {
            return this.renderers.get(apx.getType());
        }
        final String string3 = ((AbstractClientPlayer)apx).getModelName();
        final PlayerRenderer ejc4 = (PlayerRenderer)this.playerRenderers.get(string3);
        if (ejc4 != null) {
            return ejc4;
        }
        return this.defaultPlayerRenderer;
    }
    
    public void prepare(final Level bru, final Camera djh, final Entity apx) {
        this.level = bru;
        this.camera = djh;
        this.cameraOrientation = djh.rotation();
        this.crosshairPickEntity = apx;
    }
    
    public void overrideCameraOrientation(final Quaternion d) {
        this.cameraOrientation = d;
    }
    
    public void setRenderShadow(final boolean boolean1) {
        this.shouldRenderShadow = boolean1;
    }
    
    public void setRenderHitBoxes(final boolean boolean1) {
        this.renderHitBoxes = boolean1;
    }
    
    public boolean shouldRenderHitBoxes() {
        return this.renderHitBoxes;
    }
    
    public <E extends Entity> boolean shouldRender(final E apx, final Frustum ecr, final double double3, final double double4, final double double5) {
        final EntityRenderer<? super E> eem10 = this.<E>getRenderer(apx);
        return eem10.shouldRender(apx, ecr, double3, double4, double5);
    }
    
    public <E extends Entity> void render(final E apx, final double double2, final double double3, final double double4, final float float5, final float float6, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final EntityRenderer<? super E> eem14 = this.<E>getRenderer(apx);
        try {
            final Vec3 dck15 = eem14.getRenderOffset(apx, float6);
            final double double5 = double2 + dck15.x();
            final double double6 = double3 + dck15.y();
            final double double7 = double4 + dck15.z();
            dfj.pushPose();
            dfj.translate(double5, double6, double7);
            eem14.render(apx, float5, float6, dfj, dzy, integer);
            if (apx.displayFireAnimation()) {
                this.renderFlame(dfj, dzy, apx);
            }
            dfj.translate(-dck15.x(), -dck15.y(), -dck15.z());
            if (this.options.entityShadows && this.shouldRenderShadow && eem14.shadowRadius > 0.0f && !apx.isInvisible()) {
                final double double8 = this.distanceToSqr(apx.getX(), apx.getY(), apx.getZ());
                final float float7 = (float)((1.0 - double8 / 256.0) * eem14.shadowStrength);
                if (float7 > 0.0f) {
                    renderShadow(dfj, dzy, apx, float7, float6, this.level, eem14.shadowRadius);
                }
            }
            if (this.renderHitBoxes && !apx.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
                this.renderHitbox(dfj, dzy.getBuffer(RenderType.lines()), apx, float6);
            }
            dfj.popPose();
        }
        catch (Throwable throwable15) {
            final CrashReport l16 = CrashReport.forThrowable(throwable15, "Rendering entity in world");
            final CrashReportCategory m17 = l16.addCategory("Entity being rendered");
            apx.fillCrashReportCategory(m17);
            final CrashReportCategory m18 = l16.addCategory("Renderer details");
            m18.setDetail("Assigned renderer", eem14);
            m18.setDetail("Location", CrashReportCategory.formatLocation(double2, double3, double4));
            m18.setDetail("Rotation", float5);
            m18.setDetail("Delta", float6);
            throw new ReportedException(l16);
        }
    }
    
    private void renderHitbox(final PoseStack dfj, final VertexConsumer dfn, final Entity apx, final float float4) {
        final float float5 = apx.getBbWidth() / 2.0f;
        this.renderBox(dfj, dfn, apx, 1.0f, 1.0f, 1.0f);
        if (apx instanceof EnderDragon) {
            final double double7 = -Mth.lerp(float4, apx.xOld, apx.getX());
            final double double8 = -Mth.lerp(float4, apx.yOld, apx.getY());
            final double double9 = -Mth.lerp(float4, apx.zOld, apx.getZ());
            for (final EnderDragonPart bbm16 : ((EnderDragon)apx).getSubEntities()) {
                dfj.pushPose();
                final double double10 = double7 + Mth.lerp(float4, bbm16.xOld, bbm16.getX());
                final double double11 = double8 + Mth.lerp(float4, bbm16.yOld, bbm16.getY());
                final double double12 = double9 + Mth.lerp(float4, bbm16.zOld, bbm16.getZ());
                dfj.translate(double10, double11, double12);
                this.renderBox(dfj, dfn, bbm16, 0.25f, 1.0f, 0.0f);
                dfj.popPose();
            }
        }
        if (apx instanceof LivingEntity) {
            final float float6 = 0.01f;
            LevelRenderer.renderLineBox(dfj, dfn, -float5, apx.getEyeHeight() - 0.01f, -float5, float5, apx.getEyeHeight() + 0.01f, float5, 1.0f, 0.0f, 0.0f, 1.0f);
        }
        final Vec3 dck7 = apx.getViewVector(float4);
        final Matrix4f b8 = dfj.last().pose();
        dfn.vertex(b8, 0.0f, apx.getEyeHeight(), 0.0f).color(0, 0, 255, 255).endVertex();
        dfn.vertex(b8, (float)(dck7.x * 2.0), (float)(apx.getEyeHeight() + dck7.y * 2.0), (float)(dck7.z * 2.0)).color(0, 0, 255, 255).endVertex();
    }
    
    private void renderBox(final PoseStack dfj, final VertexConsumer dfn, final Entity apx, final float float4, final float float5, final float float6) {
        final AABB dcf8 = apx.getBoundingBox().move(-apx.getX(), -apx.getY(), -apx.getZ());
        LevelRenderer.renderLineBox(dfj, dfn, dcf8, float4, float5, float6, 1.0f);
    }
    
    private void renderFlame(final PoseStack dfj, final MultiBufferSource dzy, final Entity apx) {
        final TextureAtlasSprite eju5 = ModelBakery.FIRE_0.sprite();
        final TextureAtlasSprite eju6 = ModelBakery.FIRE_1.sprite();
        dfj.pushPose();
        final float float7 = apx.getBbWidth() * 1.4f;
        dfj.scale(float7, float7, float7);
        float float8 = 0.5f;
        final float float9 = 0.0f;
        float float10 = apx.getBbHeight() / float7;
        float float11 = 0.0f;
        dfj.mulPose(Vector3f.YP.rotationDegrees(-this.camera.getYRot()));
        dfj.translate(0.0, 0.0, -0.3f + (int)float10 * 0.02f);
        float float12 = 0.0f;
        int integer13 = 0;
        final VertexConsumer dfn14 = dzy.getBuffer(Sheets.cutoutBlockSheet());
        final PoseStack.Pose a15 = dfj.last();
        while (float10 > 0.0f) {
            final TextureAtlasSprite eju7 = (integer13 % 2 == 0) ? eju5 : eju6;
            float float13 = eju7.getU0();
            final float float14 = eju7.getV0();
            float float15 = eju7.getU1();
            final float float16 = eju7.getV1();
            if (integer13 / 2 % 2 == 0) {
                final float float17 = float15;
                float15 = float13;
                float13 = float17;
            }
            fireVertex(a15, dfn14, float8 - 0.0f, 0.0f - float11, float12, float15, float16);
            fireVertex(a15, dfn14, -float8 - 0.0f, 0.0f - float11, float12, float13, float16);
            fireVertex(a15, dfn14, -float8 - 0.0f, 1.4f - float11, float12, float13, float14);
            fireVertex(a15, dfn14, float8 - 0.0f, 1.4f - float11, float12, float15, float14);
            float10 -= 0.45f;
            float11 -= 0.45f;
            float8 *= 0.9f;
            float12 += 0.03f;
            ++integer13;
        }
        dfj.popPose();
    }
    
    private static void fireVertex(final PoseStack.Pose a, final VertexConsumer dfn, final float float3, final float float4, final float float5, final float float6, final float float7) {
        dfn.vertex(a.pose(), float3, float4, float5).color(255, 255, 255, 255).uv(float6, float7).overlayCoords(0, 10).uv2(240).normal(a.normal(), 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    private static void renderShadow(final PoseStack dfj, final MultiBufferSource dzy, final Entity apx, final float float4, final float float5, final LevelReader brw, final float float7) {
        float float8 = float7;
        if (apx instanceof Mob) {
            final Mob aqk9 = (Mob)apx;
            if (aqk9.isBaby()) {
                float8 *= 0.5f;
            }
        }
        final double double9 = Mth.lerp(float5, apx.xOld, apx.getX());
        final double double10 = Mth.lerp(float5, apx.yOld, apx.getY());
        final double double11 = Mth.lerp(float5, apx.zOld, apx.getZ());
        final int integer15 = Mth.floor(double9 - float8);
        final int integer16 = Mth.floor(double9 + float8);
        final int integer17 = Mth.floor(double10 - float8);
        final int integer18 = Mth.floor(double10);
        final int integer19 = Mth.floor(double11 - float8);
        final int integer20 = Mth.floor(double11 + float8);
        final PoseStack.Pose a21 = dfj.last();
        final VertexConsumer dfn22 = dzy.getBuffer(EntityRenderDispatcher.SHADOW_RENDER_TYPE);
        for (final BlockPos fx24 : BlockPos.betweenClosed(new BlockPos(integer15, integer17, integer19), new BlockPos(integer16, integer18, integer20))) {
            renderBlockShadow(a21, dfn22, brw, fx24, double9, double10, double11, float8, float4);
        }
    }
    
    private static void renderBlockShadow(final PoseStack.Pose a, final VertexConsumer dfn, final LevelReader brw, final BlockPos fx, final double double5, final double double6, final double double7, final float float8, final float float9) {
        final BlockPos fx2 = fx.below();
        final BlockState cee14 = brw.getBlockState(fx2);
        if (cee14.getRenderShape() == RenderShape.INVISIBLE || brw.getMaxLocalRawBrightness(fx) <= 3) {
            return;
        }
        if (!cee14.isCollisionShapeFullBlock(brw, fx2)) {
            return;
        }
        final VoxelShape dde15 = cee14.getShape(brw, fx.below());
        if (dde15.isEmpty()) {
            return;
        }
        float float10 = (float)((float9 - (double6 - fx.getY()) / 2.0) * 0.5 * brw.getBrightness(fx));
        if (float10 >= 0.0f) {
            if (float10 > 1.0f) {
                float10 = 1.0f;
            }
            final AABB dcf17 = dde15.bounds();
            final double double8 = fx.getX() + dcf17.minX;
            final double double9 = fx.getX() + dcf17.maxX;
            final double double10 = fx.getY() + dcf17.minY;
            final double double11 = fx.getZ() + dcf17.minZ;
            final double double12 = fx.getZ() + dcf17.maxZ;
            final float float11 = (float)(double8 - double5);
            final float float12 = (float)(double9 - double5);
            final float float13 = (float)(double10 - double6);
            final float float14 = (float)(double11 - double7);
            final float float15 = (float)(double12 - double7);
            final float float16 = -float11 / 2.0f / float8 + 0.5f;
            final float float17 = -float12 / 2.0f / float8 + 0.5f;
            final float float18 = -float14 / 2.0f / float8 + 0.5f;
            final float float19 = -float15 / 2.0f / float8 + 0.5f;
            shadowVertex(a, dfn, float10, float11, float13, float14, float16, float18);
            shadowVertex(a, dfn, float10, float11, float13, float15, float16, float19);
            shadowVertex(a, dfn, float10, float12, float13, float15, float17, float19);
            shadowVertex(a, dfn, float10, float12, float13, float14, float17, float18);
        }
    }
    
    private static void shadowVertex(final PoseStack.Pose a, final VertexConsumer dfn, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8) {
        dfn.vertex(a.pose(), float4, float5, float6).color(1.0f, 1.0f, 1.0f, float3).uv(float7, float8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(a.normal(), 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    public void setLevel(@Nullable final Level bru) {
        this.level = bru;
        if (bru == null) {
            this.camera = null;
        }
    }
    
    public double distanceToSqr(final Entity apx) {
        return this.camera.getPosition().distanceToSqr(apx.position());
    }
    
    public double distanceToSqr(final double double1, final double double2, final double double3) {
        return this.camera.getPosition().distanceToSqr(double1, double2, double3);
    }
    
    public Quaternion cameraOrientation() {
        return this.cameraOrientation;
    }
    
    public Font getFont() {
        return this.font;
    }
    
    static {
        SHADOW_RENDER_TYPE = RenderType.entityShadow(new ResourceLocation("textures/misc/shadow.png"));
    }
}
