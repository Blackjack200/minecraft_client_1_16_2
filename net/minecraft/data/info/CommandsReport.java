package net.minecraft.data.info;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import com.mojang.brigadier.CommandDispatcher;
import java.nio.file.Path;
import com.google.gson.JsonElement;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.Commands;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import net.minecraft.data.DataProvider;

public class CommandsReport implements DataProvider {
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public CommandsReport(final DataGenerator hl) {
        this.generator = hl;
    }
    
    public void run(final HashCache hn) throws IOException {
        final Path path3 = this.generator.getOutputFolder().resolve("reports/commands.json");
        final CommandDispatcher<CommandSourceStack> commandDispatcher4 = new Commands(Commands.CommandSelection.ALL).getDispatcher();
        DataProvider.save(CommandsReport.GSON, hn, (JsonElement)ArgumentTypes.<CommandSourceStack>serializeNodeToJson(commandDispatcher4, (com.mojang.brigadier.tree.CommandNode<CommandSourceStack>)commandDispatcher4.getRoot()), path3);
    }
    
    public String getName() {
        return "Command Syntax";
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }
}
