package net.minecraft.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.BlockGetter;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;

public class SwordItem extends TieredItem implements Vanishable {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    
    public SwordItem(final Tier bne, final int integer, final float float3, final Properties a) {
        super(bne, a);
        this.attackDamage = integer + bne.getAttackDamageBonus();
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder6 = (ImmutableMultimap.Builder<Attribute, AttributeModifier>)ImmutableMultimap.builder();
        builder6.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(SwordItem.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder6.put(Attributes.ATTACK_SPEED, new AttributeModifier(SwordItem.BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)float3, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)builder6.build();
    }
    
    public float getDamage() {
        return this.attackDamage;
    }
    
    @Override
    public boolean canAttackBlock(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        return !bft.isCreative();
    }
    
    @Override
    public float getDestroySpeed(final ItemStack bly, final BlockState cee) {
        if (cee.is(Blocks.COBWEB)) {
            return 15.0f;
        }
        final Material cux4 = cee.getMaterial();
        if (cux4 == Material.PLANT || cux4 == Material.REPLACEABLE_PLANT || cux4 == Material.CORAL || cee.is(BlockTags.LEAVES) || cux4 == Material.VEGETABLE) {
            return 1.5f;
        }
        return 1.0f;
    }
    
    @Override
    public boolean hurtEnemy(final ItemStack bly, final LivingEntity aqj2, final LivingEntity aqj3) {
        bly.<LivingEntity>hurtAndBreak(1, aqj3, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        return true;
    }
    
    @Override
    public boolean mineBlock(final ItemStack bly, final Level bru, final BlockState cee, final BlockPos fx, final LivingEntity aqj) {
        if (cee.getDestroySpeed(bru, fx) != 0.0f) {
            bly.<LivingEntity>hurtAndBreak(2, aqj, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        }
        return true;
    }
    
    @Override
    public boolean isCorrectToolForDrops(final BlockState cee) {
        return cee.is(Blocks.COBWEB);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final EquipmentSlot aqc) {
        if (aqc == EquipmentSlot.MAINHAND) {
            return this.defaultModifiers;
        }
        return super.getDefaultAttributeModifiers(aqc);
    }
}
