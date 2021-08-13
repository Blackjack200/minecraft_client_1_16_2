package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import java.util.Locale;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.CommandStorage;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public class StorageDataAccessor implements DataAccessor {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_STORAGE;
    public static final Function<String, DataCommands.DataProvider> PROVIDER;
    private final CommandStorage storage;
    private final ResourceLocation id;
    
    private static CommandStorage getGlobalTags(final CommandContext<CommandSourceStack> commandContext) {
        return ((CommandSourceStack)commandContext.getSource()).getServer().getCommandStorage();
    }
    
    private StorageDataAccessor(final CommandStorage cxx, final ResourceLocation vk) {
        this.storage = cxx;
        this.id = vk;
    }
    
    public void setData(final CompoundTag md) {
        this.storage.set(this.id, md);
    }
    
    public CompoundTag getData() {
        return this.storage.get(this.id);
    }
    
    public Component getModifiedSuccess() {
        return new TranslatableComponent("commands.data.storage.modified", new Object[] { this.id });
    }
    
    public Component getPrintSuccess(final Tag mt) {
        return new TranslatableComponent("commands.data.storage.query", new Object[] { this.id, mt.getPrettyDisplay() });
    }
    
    public Component getPrintSuccess(final NbtPathArgument.NbtPath h, final double double2, final int integer) {
        return new TranslatableComponent("commands.data.storage.get", new Object[] { h, this.id, String.format(Locale.ROOT, "%.2f", new Object[] { double2 }), integer });
    }
    
    static {
        SUGGEST_STORAGE = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource(getGlobalTags((CommandContext<CommandSourceStack>)commandContext).keys(), suggestionsBuilder));
        PROVIDER = (string -> new DataCommands.DataProvider() {
            final /* synthetic */ String val$arg;
            
            public DataAccessor access(final CommandContext<CommandSourceStack> commandContext) {
                return new StorageDataAccessor(getGlobalTags(commandContext), ResourceLocationArgument.getId(commandContext, string), null);
            }
            
            public ArgumentBuilder<CommandSourceStack, ?> wrap(final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
                return argumentBuilder.then(Commands.literal("storage").then((ArgumentBuilder)function.apply(Commands.argument(string, (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
            }
        });
    }
}
