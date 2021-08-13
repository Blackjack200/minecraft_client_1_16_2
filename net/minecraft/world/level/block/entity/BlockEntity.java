package net.minecraft.world.level.block.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.Supplier;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReportCategory;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

public abstract class BlockEntity {
    private static final Logger LOGGER;
    private final BlockEntityType<?> type;
    @Nullable
    protected Level level;
    protected BlockPos worldPosition;
    protected boolean remove;
    @Nullable
    private BlockState blockState;
    private boolean hasLoggedInvalidStateBefore;
    
    public BlockEntity(final BlockEntityType<?> cch) {
        this.worldPosition = BlockPos.ZERO;
        this.type = cch;
    }
    
    @Nullable
    public Level getLevel() {
        return this.level;
    }
    
    public void setLevelAndPosition(final Level bru, final BlockPos fx) {
        this.level = bru;
        this.worldPosition = fx.immutable();
    }
    
    public boolean hasLevel() {
        return this.level != null;
    }
    
    public void load(final BlockState cee, final CompoundTag md) {
        this.worldPosition = new BlockPos(md.getInt("x"), md.getInt("y"), md.getInt("z"));
    }
    
    public CompoundTag save(final CompoundTag md) {
        return this.saveMetadata(md);
    }
    
    private CompoundTag saveMetadata(final CompoundTag md) {
        final ResourceLocation vk3 = BlockEntityType.getKey(this.getType());
        if (vk3 == null) {
            throw new RuntimeException(new StringBuilder().append(this.getClass()).append(" is missing a mapping! This is a bug!").toString());
        }
        md.putString("id", vk3.toString());
        md.putInt("x", this.worldPosition.getX());
        md.putInt("y", this.worldPosition.getY());
        md.putInt("z", this.worldPosition.getZ());
        return md;
    }
    
    @Nullable
    public static BlockEntity loadStatic(final BlockState cee, final CompoundTag md) {
        final String string3 = md.getString("id");
        return (BlockEntity)Registry.BLOCK_ENTITY_TYPE.getOptional(new ResourceLocation(string3)).map(cch -> {
            try {
                return cch.create();
            }
            catch (Throwable throwable3) {
                BlockEntity.LOGGER.error("Failed to create block entity {}", string3, throwable3);
                return null;
            }
        }).map(ccg -> {
            try {
                ccg.load(cee, md);
                return ccg;
            }
            catch (Throwable throwable5) {
                BlockEntity.LOGGER.error("Failed to load data for block entity {}", string3, throwable5);
                return null;
            }
        }).orElseGet(() -> {
            BlockEntity.LOGGER.warn("Skipping BlockEntity with id {}", string3);
            return null;
        });
    }
    
    public void setChanged() {
        if (this.level != null) {
            this.blockState = this.level.getBlockState(this.worldPosition);
            this.level.blockEntityChanged(this.worldPosition, this);
            if (!this.blockState.isAir()) {
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.blockState.getBlock());
            }
        }
    }
    
    public double getViewDistance() {
        return 64.0;
    }
    
    public BlockPos getBlockPos() {
        return this.worldPosition;
    }
    
    public BlockState getBlockState() {
        if (this.blockState == null) {
            this.blockState = this.level.getBlockState(this.worldPosition);
        }
        return this.blockState;
    }
    
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return null;
    }
    
    public CompoundTag getUpdateTag() {
        return this.saveMetadata(new CompoundTag());
    }
    
    public boolean isRemoved() {
        return this.remove;
    }
    
    public void setRemoved() {
        this.remove = true;
    }
    
    public void clearRemoved() {
        this.remove = false;
    }
    
    public boolean triggerEvent(final int integer1, final int integer2) {
        return false;
    }
    
    public void clearCache() {
        this.blockState = null;
    }
    
    public void fillCrashReportCategory(final CrashReportCategory m) {
        m.setDetail("Name", (CrashReportDetail<String>)(() -> new StringBuilder().append(Registry.BLOCK_ENTITY_TYPE.getKey(this.getType())).append(" // ").append(this.getClass().getCanonicalName()).toString()));
        if (this.level == null) {
            return;
        }
        CrashReportCategory.populateBlockDetails(m, this.worldPosition, this.getBlockState());
        CrashReportCategory.populateBlockDetails(m, this.worldPosition, this.level.getBlockState(this.worldPosition));
    }
    
    public void setPosition(final BlockPos fx) {
        this.worldPosition = fx.immutable();
    }
    
    public boolean onlyOpCanSetNbt() {
        return false;
    }
    
    public void rotate(final Rotation bzj) {
    }
    
    public void mirror(final Mirror byd) {
    }
    
    public BlockEntityType<?> getType() {
        return this.type;
    }
    
    public void logInvalidState() {
        if (this.hasLoggedInvalidStateBefore) {
            return;
        }
        this.hasLoggedInvalidStateBefore = true;
        BlockEntity.LOGGER.warn("Block entity invalid: {} @ {}", new Supplier[] { () -> Registry.BLOCK_ENTITY_TYPE.getKey(this.getType()), this::getBlockPos });
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
