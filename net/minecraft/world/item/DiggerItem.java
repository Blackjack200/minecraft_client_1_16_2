package net.minecraft.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.level.block.Block;
import java.util.Set;

public class DiggerItem extends TieredItem implements Vanishable {
    private final Set<Block> blocks;
    protected final float speed;
    private final float attackDamageBaseline;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    
    protected DiggerItem(final float float1, final float float2, final Tier bne, final Set<Block> set, final Properties a) {
        super(bne, a);
        this.blocks = set;
        this.speed = bne.getSpeed();
        this.attackDamageBaseline = float1 + bne.getAttackDamageBonus();
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder7 = (ImmutableMultimap.Builder<Attribute, AttributeModifier>)ImmutableMultimap.builder();
        builder7.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(DiggerItem.BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
        builder7.put(Attributes.ATTACK_SPEED, new AttributeModifier(DiggerItem.BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)float2, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)builder7.build();
    }
    
    @Override
    public float getDestroySpeed(final ItemStack bly, final BlockState cee) {
        return this.blocks.contains(cee.getBlock()) ? this.speed : 1.0f;
    }
    
    @Override
    public boolean hurtEnemy(final ItemStack bly, final LivingEntity aqj2, final LivingEntity aqj3) {
        bly.<LivingEntity>hurtAndBreak(2, aqj3, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        return true;
    }
    
    @Override
    public boolean mineBlock(final ItemStack bly, final Level bru, final BlockState cee, final BlockPos fx, final LivingEntity aqj) {
        if (!bru.isClientSide && cee.getDestroySpeed(bru, fx) != 0.0f) {
            bly.<LivingEntity>hurtAndBreak(1, aqj, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        }
        return true;
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final EquipmentSlot aqc) {
        if (aqc == EquipmentSlot.MAINHAND) {
            return this.defaultModifiers;
        }
        return super.getDefaultAttributeModifiers(aqc);
    }
    
    public float getAttackDamage() {
        return this.attackDamageBaseline;
    }
}
