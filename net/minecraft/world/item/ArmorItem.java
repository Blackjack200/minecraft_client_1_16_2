package net.minecraft.world.item;

import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.level.ItemLike;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import java.util.UUID;

public class ArmorItem extends Item implements Wearable {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT;
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR;
    protected final EquipmentSlot slot;
    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
    protected final ArmorMaterial material;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    
    public static boolean dispenseArmor(final BlockSource fy, final ItemStack bly) {
        final BlockPos fx3 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
        final List<LivingEntity> list4 = fy.getLevel().<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, new AABB(fx3), (java.util.function.Predicate<? super LivingEntity>)EntitySelector.NO_SPECTATORS.and((Predicate)new EntitySelector.MobCanWearArmorEntitySelector(bly)));
        if (list4.isEmpty()) {
            return false;
        }
        final LivingEntity aqj5 = (LivingEntity)list4.get(0);
        final EquipmentSlot aqc6 = Mob.getEquipmentSlotForItem(bly);
        final ItemStack bly2 = bly.split(1);
        aqj5.setItemSlot(aqc6, bly2);
        if (aqj5 instanceof Mob) {
            ((Mob)aqj5).setDropChance(aqc6, 2.0f);
            ((Mob)aqj5).setPersistenceRequired();
        }
        return true;
    }
    
    public ArmorItem(final ArmorMaterial bjw, final EquipmentSlot aqc, final Properties a) {
        super(a.defaultDurability(bjw.getDurabilityForSlot(aqc)));
        this.material = bjw;
        this.slot = aqc;
        this.defense = bjw.getDefenseForSlot(aqc);
        this.toughness = bjw.getToughness();
        this.knockbackResistance = bjw.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder5 = (ImmutableMultimap.Builder<Attribute, AttributeModifier>)ImmutableMultimap.builder();
        final UUID uUID6 = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[aqc.getIndex()];
        builder5.put(Attributes.ARMOR, new AttributeModifier(uUID6, "Armor modifier", (double)this.defense, AttributeModifier.Operation.ADDITION));
        builder5.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uUID6, "Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
        if (bjw == ArmorMaterials.NETHERITE) {
            builder5.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uUID6, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)builder5.build();
    }
    
    public EquipmentSlot getSlot() {
        return this.slot;
    }
    
    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }
    
    public ArmorMaterial getMaterial() {
        return this.material;
    }
    
    @Override
    public boolean isValidRepairItem(final ItemStack bly1, final ItemStack bly2) {
        return this.material.getRepairIngredient().test(bly2) || super.isValidRepairItem(bly1, bly2);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final EquipmentSlot aqc6 = Mob.getEquipmentSlotForItem(bly5);
        final ItemStack bly6 = bft.getItemBySlot(aqc6);
        if (bly6.isEmpty()) {
            bft.setItemSlot(aqc6, bly5.copy());
            bly5.setCount(0);
            return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final EquipmentSlot aqc) {
        if (aqc == this.slot) {
            return this.defaultModifiers;
        }
        return super.getDefaultAttributeModifiers(aqc);
    }
    
    public int getDefense() {
        return this.defense;
    }
    
    public float getToughness() {
        return this.toughness;
    }
    
    static {
        ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150") };
        DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                return ArmorItem.dispenseArmor(fy, bly) ? bly : super.execute(fy, bly);
            }
        };
    }
}
