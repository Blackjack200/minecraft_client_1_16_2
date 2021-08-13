package net.minecraft.world.item;

import com.google.common.collect.Maps;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.NonNullList;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.Registry;
import net.minecraft.world.food.FoodProperties;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import net.minecraft.world.level.ItemLike;

public class Item implements ItemLike {
    public static final Map<Block, Item> BY_BLOCK;
    protected static final UUID BASE_ATTACK_DAMAGE_UUID;
    protected static final UUID BASE_ATTACK_SPEED_UUID;
    protected static final Random random;
    protected final CreativeModeTab category;
    private final Rarity rarity;
    private final int maxStackSize;
    private final int maxDamage;
    private final boolean isFireResistant;
    private final Item craftingRemainingItem;
    @Nullable
    private String descriptionId;
    @Nullable
    private final FoodProperties foodProperties;
    
    public static int getId(final Item blu) {
        return (blu == null) ? 0 : Registry.ITEM.getId(blu);
    }
    
    public static Item byId(final int integer) {
        return Registry.ITEM.byId(integer);
    }
    
    @Deprecated
    public static Item byBlock(final Block bul) {
        return (Item)Item.BY_BLOCK.getOrDefault(bul, Items.AIR);
    }
    
    public Item(final Properties a) {
        this.category = a.category;
        this.rarity = a.rarity;
        this.craftingRemainingItem = a.craftingRemainingItem;
        this.maxDamage = a.maxDamage;
        this.maxStackSize = a.maxStackSize;
        this.foodProperties = a.foodProperties;
        this.isFireResistant = a.isFireResistant;
    }
    
    public void onUseTick(final Level bru, final LivingEntity aqj, final ItemStack bly, final int integer) {
    }
    
    public boolean verifyTagAfterLoad(final CompoundTag md) {
        return false;
    }
    
    public boolean canAttackBlock(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        return true;
    }
    
    public Item asItem() {
        return this;
    }
    
    public InteractionResult useOn(final UseOnContext bnx) {
        return InteractionResult.PASS;
    }
    
    public float getDestroySpeed(final ItemStack bly, final BlockState cee) {
        return 1.0f;
    }
    
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        if (!this.isEdible()) {
            return InteractionResultHolder.<ItemStack>pass(bft.getItemInHand(aoq));
        }
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (bft.canEat(this.getFoodProperties().canAlwaysEat())) {
            bft.startUsingItem(aoq);
            return InteractionResultHolder.<ItemStack>consume(bly5);
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
    
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        if (this.isEdible()) {
            return aqj.eat(bru, bly);
        }
        return bly;
    }
    
    public final int getMaxStackSize() {
        return this.maxStackSize;
    }
    
    public final int getMaxDamage() {
        return this.maxDamage;
    }
    
    public boolean canBeDepleted() {
        return this.maxDamage > 0;
    }
    
    public boolean hurtEnemy(final ItemStack bly, final LivingEntity aqj2, final LivingEntity aqj3) {
        return false;
    }
    
    public boolean mineBlock(final ItemStack bly, final Level bru, final BlockState cee, final BlockPos fx, final LivingEntity aqj) {
        return false;
    }
    
    public boolean isCorrectToolForDrops(final BlockState cee) {
        return false;
    }
    
    public InteractionResult interactLivingEntity(final ItemStack bly, final Player bft, final LivingEntity aqj, final InteractionHand aoq) {
        return InteractionResult.PASS;
    }
    
    public Component getDescription() {
        return new TranslatableComponent(this.getDescriptionId());
    }
    
    public String toString() {
        return Registry.ITEM.getKey(this).getPath();
    }
    
    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("item", Registry.ITEM.getKey(this));
        }
        return this.descriptionId;
    }
    
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
    
    public String getDescriptionId(final ItemStack bly) {
        return this.getDescriptionId();
    }
    
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }
    
    @Nullable
    public final Item getCraftingRemainingItem() {
        return this.craftingRemainingItem;
    }
    
    public boolean hasCraftingRemainingItem() {
        return this.craftingRemainingItem != null;
    }
    
    public void inventoryTick(final ItemStack bly, final Level bru, final Entity apx, final int integer, final boolean boolean5) {
    }
    
    public void onCraftedBy(final ItemStack bly, final Level bru, final Player bft) {
    }
    
    public boolean isComplex() {
        return false;
    }
    
    public UseAnim getUseAnimation(final ItemStack bly) {
        return bly.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE;
    }
    
    public int getUseDuration(final ItemStack bly) {
        if (bly.getItem().isEdible()) {
            return this.getFoodProperties().isFastFood() ? 16 : 32;
        }
        return 0;
    }
    
    public void releaseUsing(final ItemStack bly, final Level bru, final LivingEntity aqj, final int integer) {
    }
    
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
    }
    
    public Component getName(final ItemStack bly) {
        return new TranslatableComponent(this.getDescriptionId(bly));
    }
    
    public boolean isFoil(final ItemStack bly) {
        return bly.isEnchanted();
    }
    
    public Rarity getRarity(final ItemStack bly) {
        if (!bly.isEnchanted()) {
            return this.rarity;
        }
        switch (this.rarity) {
            case COMMON:
            case UNCOMMON: {
                return Rarity.RARE;
            }
            case RARE: {
                return Rarity.EPIC;
            }
            default: {
                return this.rarity;
            }
        }
    }
    
    public boolean isEnchantable(final ItemStack bly) {
        return this.getMaxStackSize() == 1 && this.canBeDepleted();
    }
    
    protected static BlockHitResult getPlayerPOVHitResult(final Level bru, final Player bft, final ClipContext.Fluid b) {
        final float float4 = bft.xRot;
        final float float5 = bft.yRot;
        final Vec3 dck6 = bft.getEyePosition(1.0f);
        final float float6 = Mth.cos(-float5 * 0.017453292f - 3.1415927f);
        final float float7 = Mth.sin(-float5 * 0.017453292f - 3.1415927f);
        final float float8 = -Mth.cos(-float4 * 0.017453292f);
        final float float9 = Mth.sin(-float4 * 0.017453292f);
        final float float10 = float7 * float8;
        final float float11 = float9;
        final float float12 = float6 * float8;
        final double double14 = 5.0;
        final Vec3 dck7 = dck6.add(float10 * 5.0, float11 * 5.0, float12 * 5.0);
        return bru.clip(new ClipContext(dck6, dck7, ClipContext.Block.OUTLINE, b, bft));
    }
    
    public int getEnchantmentValue() {
        return 0;
    }
    
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (this.allowdedIn(bkp)) {
            gj.add(new ItemStack(this));
        }
    }
    
    protected boolean allowdedIn(final CreativeModeTab bkp) {
        final CreativeModeTab bkp2 = this.getItemCategory();
        return bkp2 != null && (bkp == CreativeModeTab.TAB_SEARCH || bkp == bkp2);
    }
    
    @Nullable
    public final CreativeModeTab getItemCategory() {
        return this.category;
    }
    
    public boolean isValidRepairItem(final ItemStack bly1, final ItemStack bly2) {
        return false;
    }
    
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final EquipmentSlot aqc) {
        return (Multimap<Attribute, AttributeModifier>)ImmutableMultimap.of();
    }
    
    public boolean useOnRelease(final ItemStack bly) {
        return bly.getItem() == Items.CROSSBOW;
    }
    
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }
    
    public boolean is(final Tag<Item> aej) {
        return aej.contains(this);
    }
    
    public boolean isEdible() {
        return this.foodProperties != null;
    }
    
    @Nullable
    public FoodProperties getFoodProperties() {
        return this.foodProperties;
    }
    
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
    
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }
    
    public boolean isFireResistant() {
        return this.isFireResistant;
    }
    
    public boolean canBeHurtBy(final DamageSource aph) {
        return !this.isFireResistant || !aph.isFire();
    }
    
    static {
        BY_BLOCK = (Map)Maps.newHashMap();
        BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
        BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
        random = new Random();
    }
    
    public static class Properties {
        private int maxStackSize;
        private int maxDamage;
        private Item craftingRemainingItem;
        private CreativeModeTab category;
        private Rarity rarity;
        private FoodProperties foodProperties;
        private boolean isFireResistant;
        
        public Properties() {
            this.maxStackSize = 64;
            this.rarity = Rarity.COMMON;
        }
        
        public Properties food(final FoodProperties bhw) {
            this.foodProperties = bhw;
            return this;
        }
        
        public Properties stacksTo(final int integer) {
            if (this.maxDamage > 0) {
                throw new RuntimeException("Unable to have damage AND stack.");
            }
            this.maxStackSize = integer;
            return this;
        }
        
        public Properties defaultDurability(final int integer) {
            return (this.maxDamage == 0) ? this.durability(integer) : this;
        }
        
        public Properties durability(final int integer) {
            this.maxDamage = integer;
            this.maxStackSize = 1;
            return this;
        }
        
        public Properties craftRemainder(final Item blu) {
            this.craftingRemainingItem = blu;
            return this;
        }
        
        public Properties tab(final CreativeModeTab bkp) {
            this.category = bkp;
            return this;
        }
        
        public Properties rarity(final Rarity bmm) {
            this.rarity = bmm;
            return this;
        }
        
        public Properties fireResistant() {
            this.isFireResistant = true;
            return this;
        }
    }
}
