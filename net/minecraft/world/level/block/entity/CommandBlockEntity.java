package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseCommandBlock;

public class CommandBlockEntity extends BlockEntity {
    private boolean powered;
    private boolean auto;
    private boolean conditionMet;
    private boolean sendToClient;
    private final BaseCommandBlock commandBlock;
    
    public CommandBlockEntity() {
        super(BlockEntityType.COMMAND_BLOCK);
        this.commandBlock = new BaseCommandBlock() {
            @Override
            public void setCommand(final String string) {
                super.setCommand(string);
                CommandBlockEntity.this.setChanged();
            }
            
            @Override
            public ServerLevel getLevel() {
                return (ServerLevel)CommandBlockEntity.this.level;
            }
            
            @Override
            public void onUpdated() {
                final BlockState cee2 = CommandBlockEntity.this.level.getBlockState(CommandBlockEntity.this.worldPosition);
                this.getLevel().sendBlockUpdated(CommandBlockEntity.this.worldPosition, cee2, cee2, 3);
            }
            
            @Override
            public Vec3 getPosition() {
                return Vec3.atCenterOf(CommandBlockEntity.this.worldPosition);
            }
            
            @Override
            public CommandSourceStack createCommandSourceStack() {
                return new CommandSourceStack(this, Vec3.atCenterOf(CommandBlockEntity.this.worldPosition), Vec2.ZERO, this.getLevel(), 2, this.getName().getString(), this.getName(), this.getLevel().getServer(), null);
            }
        };
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        this.commandBlock.save(md);
        md.putBoolean("powered", this.isPowered());
        md.putBoolean("conditionMet", this.wasConditionMet());
        md.putBoolean("auto", this.isAutomatic());
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.commandBlock.load(md);
        this.powered = md.getBoolean("powered");
        this.conditionMet = md.getBoolean("conditionMet");
        this.setAutomatic(md.getBoolean("auto"));
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        if (this.isSendToClient()) {
            this.setSendToClient(false);
            final CompoundTag md2 = this.save(new CompoundTag());
            return new ClientboundBlockEntityDataPacket(this.worldPosition, 2, md2);
        }
        return null;
    }
    
    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
    
    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }
    
    public void setPowered(final boolean boolean1) {
        this.powered = boolean1;
    }
    
    public boolean isPowered() {
        return this.powered;
    }
    
    public boolean isAutomatic() {
        return this.auto;
    }
    
    public void setAutomatic(final boolean boolean1) {
        final boolean boolean2 = this.auto;
        this.auto = boolean1;
        if (!boolean2 && boolean1 && !this.powered && this.level != null && this.getMode() != Mode.SEQUENCE) {
            this.scheduleTick();
        }
    }
    
    public void onModeSwitch() {
        final Mode a2 = this.getMode();
        if (a2 == Mode.AUTO && (this.powered || this.auto) && this.level != null) {
            this.scheduleTick();
        }
    }
    
    private void scheduleTick() {
        final Block bul2 = this.getBlockState().getBlock();
        if (bul2 instanceof CommandBlock) {
            this.markConditionMet();
            this.level.getBlockTicks().scheduleTick(this.worldPosition, bul2, 1);
        }
    }
    
    public boolean wasConditionMet() {
        return this.conditionMet;
    }
    
    public boolean markConditionMet() {
        this.conditionMet = true;
        if (this.isConditional()) {
            final BlockPos fx2 = this.worldPosition.relative(this.level.getBlockState(this.worldPosition).<Direction>getValue((Property<Direction>)CommandBlock.FACING).getOpposite());
            if (this.level.getBlockState(fx2).getBlock() instanceof CommandBlock) {
                final BlockEntity ccg3 = this.level.getBlockEntity(fx2);
                this.conditionMet = (ccg3 instanceof CommandBlockEntity && ((CommandBlockEntity)ccg3).getCommandBlock().getSuccessCount() > 0);
            }
            else {
                this.conditionMet = false;
            }
        }
        return this.conditionMet;
    }
    
    public boolean isSendToClient() {
        return this.sendToClient;
    }
    
    public void setSendToClient(final boolean boolean1) {
        this.sendToClient = boolean1;
    }
    
    public Mode getMode() {
        final BlockState cee2 = this.getBlockState();
        if (cee2.is(Blocks.COMMAND_BLOCK)) {
            return Mode.REDSTONE;
        }
        if (cee2.is(Blocks.REPEATING_COMMAND_BLOCK)) {
            return Mode.AUTO;
        }
        if (cee2.is(Blocks.CHAIN_COMMAND_BLOCK)) {
            return Mode.SEQUENCE;
        }
        return Mode.REDSTONE;
    }
    
    public boolean isConditional() {
        final BlockState cee2 = this.level.getBlockState(this.getBlockPos());
        return cee2.getBlock() instanceof CommandBlock && cee2.<Boolean>getValue((Property<Boolean>)CommandBlock.CONDITIONAL);
    }
    
    @Override
    public void clearRemoved() {
        this.clearCache();
        super.clearRemoved();
    }
    
    public enum Mode {
        SEQUENCE, 
        AUTO, 
        REDSTONE;
    }
}
