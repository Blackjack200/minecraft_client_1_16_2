package net.minecraft.server.commands;

import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class LocateBiomeCommand {
    public static final DynamicCommandExceptionType ERROR_INVALID_BIOME;
    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("locatebiome").requires(db -> db.hasPermission(2))).then(Commands.argument("biome", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)SuggestionProviders.AVAILABLE_BIOMES).executes(commandContext -> locateBiome((CommandSourceStack)commandContext.getSource(), (ResourceLocation)commandContext.getArgument("biome", (Class)ResourceLocation.class)))));
    }
    
    private static int locateBiome(final CommandSourceStack db, final ResourceLocation vk) throws CommandSyntaxException {
        final Biome bss3 = (Biome)db.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(vk).orElseThrow(() -> LocateBiomeCommand.ERROR_INVALID_BIOME.create(vk));
        final BlockPos fx4 = new BlockPos(db.getPosition());
        final BlockPos fx5 = db.getLevel().findNearestBiome(bss3, fx4, 6400, 8);
        final String string6 = vk.toString();
        if (fx5 == null) {
            throw LocateBiomeCommand.ERROR_BIOME_NOT_FOUND.create(string6);
        }
        return LocateCommand.showLocateResult(db, string6, fx4, fx5, "commands.locatebiome.success");
    }
    
    static {
        ERROR_INVALID_BIOME = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.locatebiome.invalid", new Object[] { object }));
        ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.locatebiome.notFound", new Object[] { object }));
    }
}
