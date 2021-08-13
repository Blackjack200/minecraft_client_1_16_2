package net.minecraft.world.entity.animal;

import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.Blocks;
import java.util.EnumMap;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeType;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.DyeColor;
import java.util.Map;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Shearable;

public class Sheep extends Animal implements Shearable {
    private static final EntityDataAccessor<Byte> DATA_WOOL_ID;
    private static final Map<DyeColor, ItemLike> ITEM_BY_DYE;
    private static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR;
    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;
    
    private static float[] createSheepColor(final DyeColor bku) {
        if (bku == DyeColor.WHITE) {
            return new float[] { 0.9019608f, 0.9019608f, 0.9019608f };
        }
        final float[] arr2 = bku.getTextureDiffuseColors();
        final float float3 = 0.75f;
        return new float[] { arr2[0] * 0.75f, arr2[1] * 0.75f, arr2[2] * 0.75f };
    }
    
    public static float[] getColorArray(final DyeColor bku) {
        return (float[])Sheep.COLORARRAY_BY_COLOR.get(bku);
    }
    
    public Sheep(final EntityType<? extends Sheep> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    protected void registerGoals() {
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }
    
    @Override
    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }
    
    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
        super.aiStep();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Sheep.DATA_WOOL_ID, (Byte)0);
    }
    
    public ResourceLocation getDefaultLootTable() {
        if (this.isSheared()) {
            return this.getType().getDefaultLootTable();
        }
        switch (this.getColor()) {
            default: {
                return BuiltInLootTables.SHEEP_WHITE;
            }
            case ORANGE: {
                return BuiltInLootTables.SHEEP_ORANGE;
            }
            case MAGENTA: {
                return BuiltInLootTables.SHEEP_MAGENTA;
            }
            case LIGHT_BLUE: {
                return BuiltInLootTables.SHEEP_LIGHT_BLUE;
            }
            case YELLOW: {
                return BuiltInLootTables.SHEEP_YELLOW;
            }
            case LIME: {
                return BuiltInLootTables.SHEEP_LIME;
            }
            case PINK: {
                return BuiltInLootTables.SHEEP_PINK;
            }
            case GRAY: {
                return BuiltInLootTables.SHEEP_GRAY;
            }
            case LIGHT_GRAY: {
                return BuiltInLootTables.SHEEP_LIGHT_GRAY;
            }
            case CYAN: {
                return BuiltInLootTables.SHEEP_CYAN;
            }
            case PURPLE: {
                return BuiltInLootTables.SHEEP_PURPLE;
            }
            case BLUE: {
                return BuiltInLootTables.SHEEP_BLUE;
            }
            case BROWN: {
                return BuiltInLootTables.SHEEP_BROWN;
            }
            case GREEN: {
                return BuiltInLootTables.SHEEP_GREEN;
            }
            case RED: {
                return BuiltInLootTables.SHEEP_RED;
            }
            case BLACK: {
                return BuiltInLootTables.SHEEP_BLACK;
            }
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 10) {
            this.eatAnimationTick = 40;
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public float getHeadEatPositionScale(final float float1) {
        if (this.eatAnimationTick <= 0) {
            return 0.0f;
        }
        if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0f;
        }
        if (this.eatAnimationTick < 4) {
            return (this.eatAnimationTick - float1) / 4.0f;
        }
        return -(this.eatAnimationTick - 40 - float1) / 4.0f;
    }
    
    public float getHeadEatAngleScale(final float float1) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            final float float2 = (this.eatAnimationTick - 4 - float1) / 32.0f;
            return 0.62831855f + 0.21991149f * Mth.sin(float2 * 28.7f);
        }
        if (this.eatAnimationTick > 0) {
            return 0.62831855f;
        }
        return this.xRot * 0.017453292f;
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() != Items.SHEARS) {
            return super.mobInteract(bft, aoq);
        }
        if (!this.level.isClientSide && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            bly4.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public void shear(final SoundSource adp) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, adp, 1.0f, 1.0f);
        this.setSheared(true);
        for (int integer3 = 1 + this.random.nextInt(3), integer4 = 0; integer4 < integer3; ++integer4) {
            final ItemEntity bcs5 = this.spawnAtLocation((ItemLike)Sheep.ITEM_BY_DYE.get(this.getColor()), 1);
            if (bcs5 != null) {
                bcs5.setDeltaMovement(bcs5.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
            }
        }
    }
    
    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("Sheared", this.isSheared());
        md.putByte("Color", (byte)this.getColor().getId());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setSheared(md.getBoolean("Sheared"));
        this.setColor(DyeColor.byId(md.getByte("Color")));
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SHEEP_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15f, 1.0f);
    }
    
    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.<Byte>get(Sheep.DATA_WOOL_ID) & 0xF);
    }
    
    public void setColor(final DyeColor bku) {
        final byte byte3 = this.entityData.<Byte>get(Sheep.DATA_WOOL_ID);
        this.entityData.<Byte>set(Sheep.DATA_WOOL_ID, (byte)((byte3 & 0xF0) | (bku.getId() & 0xF)));
    }
    
    public boolean isSheared() {
        return (this.entityData.<Byte>get(Sheep.DATA_WOOL_ID) & 0x10) != 0x0;
    }
    
    public void setSheared(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(Sheep.DATA_WOOL_ID);
        if (boolean1) {
            this.entityData.<Byte>set(Sheep.DATA_WOOL_ID, (byte)(byte3 | 0x10));
        }
        else {
            this.entityData.<Byte>set(Sheep.DATA_WOOL_ID, (byte)(byte3 & 0xFFFFFFEF));
        }
    }
    
    public static DyeColor getRandomSheepColor(final Random random) {
        final int integer2 = random.nextInt(100);
        if (integer2 < 5) {
            return DyeColor.BLACK;
        }
        if (integer2 < 10) {
            return DyeColor.GRAY;
        }
        if (integer2 < 15) {
            return DyeColor.LIGHT_GRAY;
        }
        if (integer2 < 18) {
            return DyeColor.BROWN;
        }
        if (random.nextInt(500) == 0) {
            return DyeColor.PINK;
        }
        return DyeColor.WHITE;
    }
    
    @Override
    public Sheep getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Sheep bap4 = (Sheep)apv;
        final Sheep bap5 = EntityType.SHEEP.create(aag);
        bap5.setColor(this.getOffspringColor(this, bap4));
        return bap5;
    }
    
    public void ate() {
        this.setSheared(false);
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setColor(getRandomSheepColor(bsh.getRandom()));
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private DyeColor getOffspringColor(final Animal azw1, final Animal azw2) {
        final DyeColor bku4 = ((Sheep)azw1).getColor();
        final DyeColor bku5 = ((Sheep)azw2).getColor();
        final CraftingContainer bil6 = makeContainer(bku4, bku5);
        return (DyeColor)this.level.getRecipeManager().<CraftingContainer, CraftingRecipe>getRecipeFor(RecipeType.CRAFTING, bil6, this.level).map(bof -> bof.assemble(bil6)).map(ItemStack::getItem).filter(DyeItem.class::isInstance).map(DyeItem.class::cast).map(DyeItem::getDyeColor).orElseGet(() -> this.level.random.nextBoolean() ? bku4 : bku5);
    }
    
    private static CraftingContainer makeContainer(final DyeColor bku1, final DyeColor bku2) {
        final CraftingContainer bil3 = new CraftingContainer(new AbstractContainerMenu(null, -1) {
            @Override
            public boolean stillValid(final Player bft) {
                return false;
            }
        }, 2, 1);
        bil3.setItem(0, new ItemStack(DyeItem.byColor(bku1)));
        bil3.setItem(1, new ItemStack(DyeItem.byColor(bku2)));
        return bil3;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.95f * apy.height;
    }
    
    static {
        DATA_WOOL_ID = SynchedEntityData.<Byte>defineId(Sheep.class, EntityDataSerializers.BYTE);
        ITEM_BY_DYE = Util.<Map>make((Map)Maps.newEnumMap((Class)DyeColor.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)DyeColor.WHITE, Blocks.WHITE_WOOL);
            enumMap.put((Enum)DyeColor.ORANGE, Blocks.ORANGE_WOOL);
            enumMap.put((Enum)DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
            enumMap.put((Enum)DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
            enumMap.put((Enum)DyeColor.YELLOW, Blocks.YELLOW_WOOL);
            enumMap.put((Enum)DyeColor.LIME, Blocks.LIME_WOOL);
            enumMap.put((Enum)DyeColor.PINK, Blocks.PINK_WOOL);
            enumMap.put((Enum)DyeColor.GRAY, Blocks.GRAY_WOOL);
            enumMap.put((Enum)DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
            enumMap.put((Enum)DyeColor.CYAN, Blocks.CYAN_WOOL);
            enumMap.put((Enum)DyeColor.PURPLE, Blocks.PURPLE_WOOL);
            enumMap.put((Enum)DyeColor.BLUE, Blocks.BLUE_WOOL);
            enumMap.put((Enum)DyeColor.BROWN, Blocks.BROWN_WOOL);
            enumMap.put((Enum)DyeColor.GREEN, Blocks.GREEN_WOOL);
            enumMap.put((Enum)DyeColor.RED, Blocks.RED_WOOL);
            enumMap.put((Enum)DyeColor.BLACK, Blocks.BLACK_WOOL);
        }));
        COLORARRAY_BY_COLOR = (Map)Maps.newEnumMap((Map)Arrays.stream((Object[])DyeColor.values()).collect(Collectors.toMap(bku -> bku, Sheep::createSheepColor)));
    }
}
