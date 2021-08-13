package net.minecraft.server.commands.data;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import java.util.Locale;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.Function;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class BlockDataAccessor implements DataAccessor {
    private static final SimpleCommandExceptionType ERROR_NOT_A_BLOCK_ENTITY;
    public static final Function<String, DataCommands.DataProvider> PROVIDER;
    private final BlockEntity entity;
    private final BlockPos pos;
    
    public BlockDataAccessor(final BlockEntity ccg, final BlockPos fx) {
        this.entity = ccg;
        this.pos = fx;
    }
    
    public void setData(final CompoundTag md) {
        md.putInt("x", this.pos.getX());
        md.putInt("y", this.pos.getY());
        md.putInt("z", this.pos.getZ());
        final BlockState cee3 = this.entity.getLevel().getBlockState(this.pos);
        this.entity.load(cee3, md);
        this.entity.setChanged();
        this.entity.getLevel().sendBlockUpdated(this.pos, cee3, cee3, 3);
    }
    
    public CompoundTag getData() {
        return this.entity.save(new CompoundTag());
    }
    
    public Component getModifiedSuccess() {
        return new TranslatableComponent("commands.data.block.modified", new Object[] { this.pos.getX(), this.pos.getY(), this.pos.getZ() });
    }
    
    public Component getPrintSuccess(final Tag mt) {
        return new TranslatableComponent("commands.data.block.query", new Object[] { this.pos.getX(), this.pos.getY(), this.pos.getZ(), mt.getPrettyDisplay() });
    }
    
    public Component getPrintSuccess(final NbtPathArgument.NbtPath h, final double double2, final int integer) {
        return new TranslatableComponent("commands.data.block.get", new Object[] { h, this.pos.getX(), this.pos.getY(), this.pos.getZ(), String.format(Locale.ROOT, "%.2f", new Object[] { double2 }), integer });
    }
    
    static {
        ERROR_NOT_A_BLOCK_ENTITY = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.block.invalid"));
        PROVIDER = (string -> new DataCommands.DataProvider() {
            final /* synthetic */ String val$argPrefix;
            
            public DataAccessor access(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                final BlockPos fx3 = BlockPosArgument.getLoadedBlockPos(commandContext, string + "Pos");
                final BlockEntity ccg4 = ((CommandSourceStack)commandContext.getSource()).getLevel().getBlockEntity(fx3);
                if (ccg4 == null) {
                    throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
                }
                return new BlockDataAccessor(ccg4, fx3);
            }
            
            public ArgumentBuilder<CommandSourceStack, ?> wrap(final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
                return argumentBuilder.then(Commands.literal("block").then((ArgumentBuilder)function.apply(Commands.argument(string + "Pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()))));
            }
        });
    }
}
