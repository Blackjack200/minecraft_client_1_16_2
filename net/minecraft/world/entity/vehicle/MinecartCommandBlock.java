package net.minecraft.world.entity.vehicle;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;

public class MinecartCommandBlock extends AbstractMinecart {
    private static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME;
    private static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT;
    private final BaseCommandBlock commandBlock;
    private int lastActivated;
    
    public MinecartCommandBlock(final EntityType<? extends MinecartCommandBlock> aqb, final Level bru) {
        super(aqb, bru);
        this.commandBlock = new MinecartCommandBase();
    }
    
    public MinecartCommandBlock(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.COMMAND_BLOCK_MINECART, bru, double2, double3, double4);
        this.commandBlock = new MinecartCommandBase();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().<String>define(MinecartCommandBlock.DATA_ID_COMMAND_NAME, "");
        this.getEntityData().<Component>define(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, TextComponent.EMPTY);
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.commandBlock.load(md);
        this.getEntityData().<String>set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, this.getCommandBlock().getCommand());
        this.getEntityData().<Component>set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, this.getCommandBlock().getLastOutput());
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        this.commandBlock.save(md);
    }
    
    @Override
    public Type getMinecartType() {
        return Type.COMMAND_BLOCK;
    }
    
    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.COMMAND_BLOCK.defaultBlockState();
    }
    
    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }
    
    @Override
    public void activateMinecart(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        if (boolean4 && this.tickCount - this.lastActivated >= 4) {
            this.getCommandBlock().performCommand(this.level);
            this.lastActivated = this.tickCount;
        }
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        return this.commandBlock.usedBy(bft);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (MinecartCommandBlock.DATA_ID_LAST_OUTPUT.equals(us)) {
            try {
                this.commandBlock.setLastOutput(this.getEntityData().<Component>get(MinecartCommandBlock.DATA_ID_LAST_OUTPUT));
            }
            catch (Throwable t) {}
        }
        else if (MinecartCommandBlock.DATA_ID_COMMAND_NAME.equals(us)) {
            this.commandBlock.setCommand(this.getEntityData().<String>get(MinecartCommandBlock.DATA_ID_COMMAND_NAME));
        }
    }
    
    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
    
    static {
        DATA_ID_COMMAND_NAME = SynchedEntityData.<String>defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);
        DATA_ID_LAST_OUTPUT = SynchedEntityData.<Component>defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);
    }
    
    public class MinecartCommandBase extends BaseCommandBlock {
        @Override
        public ServerLevel getLevel() {
            return (ServerLevel)MinecartCommandBlock.this.level;
        }
        
        @Override
        public void onUpdated() {
            MinecartCommandBlock.this.getEntityData().<String>set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, this.getCommand());
            MinecartCommandBlock.this.getEntityData().<Component>set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, this.getLastOutput());
        }
        
        @Override
        public Vec3 getPosition() {
            return MinecartCommandBlock.this.position();
        }
        
        public MinecartCommandBlock getMinecart() {
            return MinecartCommandBlock.this;
        }
        
        @Override
        public CommandSourceStack createCommandSourceStack() {
            return new CommandSourceStack(this, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), this.getLevel(), 2, this.getName().getString(), MinecartCommandBlock.this.getDisplayName(), this.getLevel().getServer(), MinecartCommandBlock.this);
        }
    }
}
