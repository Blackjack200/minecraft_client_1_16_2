package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Registry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.arguments.ArgumentType;

public class EntitySummonArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENTITY;
    
    public static EntitySummonArgument id() {
        return new EntitySummonArgument();
    }
    
    public static ResourceLocation getSummonableEntity(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return verifyCanSummon((ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class));
    }
    
    private static ResourceLocation verifyCanSummon(final ResourceLocation vk) throws CommandSyntaxException {
        Registry.ENTITY_TYPE.getOptional(vk).filter(EntityType::canSummon).orElseThrow(() -> EntitySummonArgument.ERROR_UNKNOWN_ENTITY.create(vk));
        return vk;
    }
    
    public ResourceLocation parse(final StringReader stringReader) throws CommandSyntaxException {
        return verifyCanSummon(ResourceLocation.read(stringReader));
    }
    
    public Collection<String> getExamples() {
        return EntitySummonArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "minecraft:pig", "cow" });
        ERROR_UNKNOWN_ENTITY = new DynamicCommandExceptionType(object -> new TranslatableComponent("entity.notFound", new Object[] { object }));
    }
}
