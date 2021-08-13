package net.minecraft.world.level.chunk;

import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Function;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LevelChunkSection {
    private static final Palette<BlockState> GLOBAL_BLOCKSTATE_PALETTE;
    private final int bottomBlockY;
    private short nonEmptyBlockCount;
    private short tickingBlockCount;
    private short tickingFluidCount;
    private final PalettedContainer<BlockState> states;
    
    public LevelChunkSection(final int integer) {
        this(integer, (short)0, (short)0, (short)0);
    }
    
    public LevelChunkSection(final int integer, final short short2, final short short3, final short short4) {
        this.bottomBlockY = integer;
        this.nonEmptyBlockCount = short2;
        this.tickingBlockCount = short3;
        this.tickingFluidCount = short4;
        this.states = new PalettedContainer<BlockState>(LevelChunkSection.GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, (java.util.function.Function<CompoundTag, BlockState>)NbtUtils::readBlockState, (java.util.function.Function<BlockState, CompoundTag>)NbtUtils::writeBlockState, Blocks.AIR.defaultBlockState());
    }
    
    public BlockState getBlockState(final int integer1, final int integer2, final int integer3) {
        return this.states.get(integer1, integer2, integer3);
    }
    
    public FluidState getFluidState(final int integer1, final int integer2, final int integer3) {
        return this.states.get(integer1, integer2, integer3).getFluidState();
    }
    
    public void acquire() {
        this.states.acquire();
    }
    
    public void release() {
        this.states.release();
    }
    
    public BlockState setBlockState(final int integer1, final int integer2, final int integer3, final BlockState cee) {
        return this.setBlockState(integer1, integer2, integer3, cee, true);
    }
    
    public BlockState setBlockState(final int integer1, final int integer2, final int integer3, final BlockState cee, final boolean boolean5) {
        BlockState cee2;
        if (boolean5) {
            cee2 = this.states.getAndSet(integer1, integer2, integer3, cee);
        }
        else {
            cee2 = this.states.getAndSetUnchecked(integer1, integer2, integer3, cee);
        }
        final FluidState cuu8 = cee2.getFluidState();
        final FluidState cuu9 = cee.getFluidState();
        if (!cee2.isAir()) {
            --this.nonEmptyBlockCount;
            if (cee2.isRandomlyTicking()) {
                --this.tickingBlockCount;
            }
        }
        if (!cuu8.isEmpty()) {
            --this.tickingFluidCount;
        }
        if (!cee.isAir()) {
            ++this.nonEmptyBlockCount;
            if (cee.isRandomlyTicking()) {
                ++this.tickingBlockCount;
            }
        }
        if (!cuu9.isEmpty()) {
            ++this.tickingFluidCount;
        }
        return cee2;
    }
    
    public boolean isEmpty() {
        return this.nonEmptyBlockCount == 0;
    }
    
    public static boolean isEmpty(@Nullable final LevelChunkSection cgf) {
        return cgf == LevelChunk.EMPTY_SECTION || cgf.isEmpty();
    }
    
    public boolean isRandomlyTicking() {
        return this.isRandomlyTickingBlocks() || this.isRandomlyTickingFluids();
    }
    
    public boolean isRandomlyTickingBlocks() {
        return this.tickingBlockCount > 0;
    }
    
    public boolean isRandomlyTickingFluids() {
        return this.tickingFluidCount > 0;
    }
    
    public int bottomBlockY() {
        return this.bottomBlockY;
    }
    
    public void recalcBlockCounts() {
        this.nonEmptyBlockCount = 0;
        this.tickingBlockCount = 0;
        this.tickingFluidCount = 0;
        final FluidState cuu4;
        this.states.count((cee, integer) -> {
            cuu4 = cee.getFluidState();
            if (!cee.isAir()) {
                this.nonEmptyBlockCount += integer;
                if (cee.isRandomlyTicking()) {
                    this.tickingBlockCount += integer;
                }
            }
            if (!cuu4.isEmpty()) {
                this.nonEmptyBlockCount += integer;
                if (cuu4.isRandomlyTicking()) {
                    this.tickingFluidCount += integer;
                }
            }
        });
    }
    
    public PalettedContainer<BlockState> getStates() {
        return this.states;
    }
    
    public void read(final FriendlyByteBuf nf) {
        this.nonEmptyBlockCount = nf.readShort();
        this.states.read(nf);
    }
    
    public void write(final FriendlyByteBuf nf) {
        nf.writeShort(this.nonEmptyBlockCount);
        this.states.write(nf);
    }
    
    public int getSerializedSize() {
        return 2 + this.states.getSerializedSize();
    }
    
    public boolean maybeHas(final Predicate<BlockState> predicate) {
        return this.states.maybeHas(predicate);
    }
    
    static {
        GLOBAL_BLOCKSTATE_PALETTE = new GlobalPalette<BlockState>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState());
    }
}
