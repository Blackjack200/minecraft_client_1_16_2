package net.minecraft.server.commands.data;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import java.util.Locale;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.advancements.critereon.NbtPredicate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import java.util.function.Function;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class EntityDataAccessor implements DataAccessor {
    private static final SimpleCommandExceptionType ERROR_NO_PLAYERS;
    public static final Function<String, DataCommands.DataProvider> PROVIDER;
    private final Entity entity;
    
    public EntityDataAccessor(final Entity apx) {
        this.entity = apx;
    }
    
    public void setData(final CompoundTag md) throws CommandSyntaxException {
        if (this.entity instanceof Player) {
            throw EntityDataAccessor.ERROR_NO_PLAYERS.create();
        }
        final UUID uUID3 = this.entity.getUUID();
        this.entity.load(md);
        this.entity.setUUID(uUID3);
    }
    
    public CompoundTag getData() {
        return NbtPredicate.getEntityTagToCompare(this.entity);
    }
    
    public Component getModifiedSuccess() {
        return new TranslatableComponent("commands.data.entity.modified", new Object[] { this.entity.getDisplayName() });
    }
    
    public Component getPrintSuccess(final Tag mt) {
        return new TranslatableComponent("commands.data.entity.query", new Object[] { this.entity.getDisplayName(), mt.getPrettyDisplay() });
    }
    
    public Component getPrintSuccess(final NbtPathArgument.NbtPath h, final double double2, final int integer) {
        return new TranslatableComponent("commands.data.entity.get", new Object[] { h, this.entity.getDisplayName(), String.format(Locale.ROOT, "%.2f", new Object[] { double2 }), integer });
    }
    
    static {
        ERROR_NO_PLAYERS = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.entity.invalid"));
        PROVIDER = (string -> new DataCommands.DataProvider() {
            final /* synthetic */ String val$arg;
            
            public DataAccessor access(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return new EntityDataAccessor(EntityArgument.getEntity(commandContext, string));
            }
            
            public ArgumentBuilder<CommandSourceStack, ?> wrap(final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
                return argumentBuilder.then(Commands.literal("entity").then((ArgumentBuilder)function.apply(Commands.argument(string, (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()))));
            }
        });
    }
}
