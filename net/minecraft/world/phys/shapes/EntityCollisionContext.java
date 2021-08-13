package net.minecraft.world.phys.shapes;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Predicate;
import net.minecraft.world.item.Item;

public class EntityCollisionContext implements CollisionContext {
    protected static final CollisionContext EMPTY;
    private final boolean descending;
    private final double entityBottom;
    private final Item heldItem;
    private final Predicate<Fluid> canStandOnFluid;
    
    protected EntityCollisionContext(final boolean boolean1, final double double2, final Item blu, final Predicate<Fluid> predicate) {
        this.descending = boolean1;
        this.entityBottom = double2;
        this.heldItem = blu;
        this.canStandOnFluid = predicate;
    }
    
    @Deprecated
    protected EntityCollisionContext(final Entity apx) {
        this(apx.isDescending(), apx.getY(), (apx instanceof LivingEntity) ? ((LivingEntity)apx).getMainHandItem().getItem() : Items.AIR, (Predicate<Fluid>)((apx instanceof LivingEntity) ? (LivingEntity)apx::canStandOnFluid : (cut -> false)));
    }
    
    public boolean isHoldingItem(final Item blu) {
        return this.heldItem == blu;
    }
    
    public boolean canStandOnFluid(final FluidState cuu, final FlowingFluid cus) {
        return this.canStandOnFluid.test(cus) && !cuu.getType().isSame(cus);
    }
    
    public boolean isDescending() {
        return this.descending;
    }
    
    public boolean isAbove(final VoxelShape dde, final BlockPos fx, final boolean boolean3) {
        return this.entityBottom > fx.getY() + dde.max(Direction.Axis.Y) - 9.999999747378752E-6;
    }
    
    static {
        EMPTY = new EntityCollisionContext(false, -1.7976931348623157E308, Items.AIR, cut -> false) {
            @Override
            public boolean isAbove(final VoxelShape dde, final BlockPos fx, final boolean boolean3) {
                return boolean3;
            }
        };
    }
}
