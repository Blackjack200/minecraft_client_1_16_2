package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.Map;

public class InfestedBlock extends Block {
    private final Block hostBlock;
    private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK;
    
    public InfestedBlock(final Block bul, final Properties c) {
        super(c);
        this.hostBlock = bul;
        InfestedBlock.BLOCK_BY_HOST_BLOCK.put(bul, this);
    }
    
    public Block getHostBlock() {
        return this.hostBlock;
    }
    
    public static boolean isCompatibleHostBlock(final BlockState cee) {
        return InfestedBlock.BLOCK_BY_HOST_BLOCK.containsKey(cee.getBlock());
    }
    
    private void spawnInfestation(final ServerLevel aag, final BlockPos fx) {
        final Silverfish bdu4 = EntityType.SILVERFISH.create(aag);
        bdu4.moveTo(fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, 0.0f, 0.0f);
        aag.addFreshEntity(bdu4);
        bdu4.spawnAnim();
    }
    
    @Override
    public void spawnAfterBreak(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
        super.spawnAfterBreak(cee, aag, fx, bly);
        if (aag.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, bly) == 0) {
            this.spawnInfestation(aag, fx);
        }
    }
    
    @Override
    public void wasExploded(final Level bru, final BlockPos fx, final Explosion brm) {
        if (bru instanceof ServerLevel) {
            this.spawnInfestation((ServerLevel)bru, fx);
        }
    }
    
    public static BlockState stateByHostBlock(final Block bul) {
        return ((Block)InfestedBlock.BLOCK_BY_HOST_BLOCK.get(bul)).defaultBlockState();
    }
    
    static {
        BLOCK_BY_HOST_BLOCK = (Map)Maps.newIdentityHashMap();
    }
}
