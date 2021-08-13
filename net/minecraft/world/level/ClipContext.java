package net.minecraft.world.level;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.Vec3;

public class ClipContext {
    private final Vec3 from;
    private final Vec3 to;
    private final Block block;
    private final Fluid fluid;
    private final CollisionContext collisionContext;
    
    public ClipContext(final Vec3 dck1, final Vec3 dck2, final Block a, final Fluid b, final Entity apx) {
        this.from = dck1;
        this.to = dck2;
        this.block = a;
        this.fluid = b;
        this.collisionContext = CollisionContext.of(apx);
    }
    
    public Vec3 getTo() {
        return this.to;
    }
    
    public Vec3 getFrom() {
        return this.from;
    }
    
    public VoxelShape getBlockShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return this.block.get(cee, bqz, fx, this.collisionContext);
    }
    
    public VoxelShape getFluidShape(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        return this.fluid.canPick(cuu) ? cuu.getShape(bqz, fx) : Shapes.empty();
    }
    
    public enum Block implements ShapeGetter {
        COLLIDER(BlockBehaviour.BlockStateBase::getCollisionShape), 
        OUTLINE(BlockBehaviour.BlockStateBase::getShape), 
        VISUAL(BlockBehaviour.BlockStateBase::getVisualShape);
        
        private final ShapeGetter shapeGetter;
        
        private Block(final ShapeGetter c) {
            this.shapeGetter = c;
        }
        
        public VoxelShape get(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
            return this.shapeGetter.get(cee, bqz, fx, dcp);
        }
    }
    
    public enum Fluid {
        NONE((Predicate<FluidState>)(cuu -> false)), 
        SOURCE_ONLY((Predicate<FluidState>)FluidState::isSource), 
        ANY((Predicate<FluidState>)(cuu -> !cuu.isEmpty()));
        
        private final Predicate<FluidState> canPick;
        
        private Fluid(final Predicate<FluidState> predicate) {
            this.canPick = predicate;
        }
        
        public boolean canPick(final FluidState cuu) {
            return this.canPick.test(cuu);
        }
    }
    
    public interface ShapeGetter {
        VoxelShape get(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp);
    }
}
