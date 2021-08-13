package net.minecraft.world.item;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;

public class BoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE;
    private final Boat.Type type;
    
    public BoatItem(final Boat.Type b, final Properties a) {
        super(a);
        this.type = b;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final HitResult dci6 = Item.getPlayerPOVHitResult(bru, bft, ClipContext.Fluid.ANY);
        if (dci6.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        final Vec3 dck7 = bft.getViewVector(1.0f);
        final double double8 = 5.0;
        final List<Entity> list10 = bru.getEntities(bft, bft.getBoundingBox().expandTowards(dck7.scale(5.0)).inflate(1.0), BoatItem.ENTITY_PREDICATE);
        if (!list10.isEmpty()) {
            final Vec3 dck8 = bft.getEyePosition(1.0f);
            for (final Entity apx13 : list10) {
                final AABB dcf14 = apx13.getBoundingBox().inflate(apx13.getPickRadius());
                if (dcf14.contains(dck8)) {
                    return InteractionResultHolder.<ItemStack>pass(bly5);
                }
            }
        }
        if (dci6.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        final Boat bhk11 = new Boat(bru, dci6.getLocation().x, dci6.getLocation().y, dci6.getLocation().z);
        bhk11.setType(this.type);
        bhk11.yRot = bft.yRot;
        if (!bru.noCollision(bhk11, bhk11.getBoundingBox().inflate(-0.1))) {
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        if (!bru.isClientSide) {
            bru.addFreshEntity(bhk11);
            if (!bft.abilities.instabuild) {
                bly5.shrink(1);
            }
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
    
    static {
        ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    }
}
