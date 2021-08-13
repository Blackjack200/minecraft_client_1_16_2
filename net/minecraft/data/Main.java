package net.minecraft.data;

import net.minecraft.data.worldgen.biome.BiomeReport;
import net.minecraft.data.info.CommandsReport;
import net.minecraft.data.info.RegistryDumpReport;
import net.minecraft.data.info.BlockListReport;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.structures.StructureUpdater;
import net.minecraft.data.structures.SnbtToNbt;
import java.io.IOException;
import joptsimple.OptionSet;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.Collection;
import java.nio.file.Paths;
import java.io.OutputStream;
import joptsimple.OptionSpec;
import joptsimple.OptionParser;

public class Main {
    public static void main(final String[] arr) throws IOException {
        final OptionParser optionParser2 = new OptionParser();
        final OptionSpec<Void> optionSpec3 = (OptionSpec<Void>)optionParser2.accepts("help", "Show the help menu").forHelp();
        final OptionSpec<Void> optionSpec4 = (OptionSpec<Void>)optionParser2.accepts("server", "Include server generators");
        final OptionSpec<Void> optionSpec5 = (OptionSpec<Void>)optionParser2.accepts("client", "Include client generators");
        final OptionSpec<Void> optionSpec6 = (OptionSpec<Void>)optionParser2.accepts("dev", "Include development tools");
        final OptionSpec<Void> optionSpec7 = (OptionSpec<Void>)optionParser2.accepts("reports", "Include data reports");
        final OptionSpec<Void> optionSpec8 = (OptionSpec<Void>)optionParser2.accepts("validate", "Validate inputs");
        final OptionSpec<Void> optionSpec9 = (OptionSpec<Void>)optionParser2.accepts("all", "Include all generators");
        final OptionSpec<String> optionSpec10 = (OptionSpec<String>)optionParser2.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", (Object[])new String[0]);
        final OptionSpec<String> optionSpec11 = (OptionSpec<String>)optionParser2.accepts("input", "Input folder").withRequiredArg();
        final OptionSet optionSet12 = optionParser2.parse(arr);
        if (optionSet12.has((OptionSpec)optionSpec3) || !optionSet12.hasOptions()) {
            optionParser2.printHelpOn((OutputStream)System.out);
            return;
        }
        final Path path13 = Paths.get((String)optionSpec10.value(optionSet12), new String[0]);
        final boolean boolean14 = optionSet12.has((OptionSpec)optionSpec9);
        final boolean boolean15 = boolean14 || optionSet12.has((OptionSpec)optionSpec5);
        final boolean boolean16 = boolean14 || optionSet12.has((OptionSpec)optionSpec4);
        final boolean boolean17 = boolean14 || optionSet12.has((OptionSpec)optionSpec6);
        final boolean boolean18 = boolean14 || optionSet12.has((OptionSpec)optionSpec7);
        final boolean boolean19 = boolean14 || optionSet12.has((OptionSpec)optionSpec8);
        final DataGenerator hl20 = createStandardGenerator(path13, (Collection<Path>)optionSet12.valuesOf((OptionSpec)optionSpec11).stream().map(string -> Paths.get(string, new String[0])).collect(Collectors.toList()), boolean15, boolean16, boolean17, boolean18, boolean19);
        hl20.run();
    }
    
    public static DataGenerator createStandardGenerator(final Path path, final Collection<Path> collection, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6, final boolean boolean7) {
        final DataGenerator hl8 = new DataGenerator(path, collection);
        if (boolean3 || boolean4) {
            hl8.addProvider(new SnbtToNbt(hl8).addFilter(new StructureUpdater()));
        }
        if (boolean3) {
            hl8.addProvider(new ModelProvider(hl8));
        }
        if (boolean4) {
            hl8.addProvider(new FluidTagsProvider(hl8));
            final BlockTagsProvider js9 = new BlockTagsProvider(hl8);
            hl8.addProvider(js9);
            hl8.addProvider(new ItemTagsProvider(hl8, js9));
            hl8.addProvider(new EntityTypeTagsProvider(hl8));
            hl8.addProvider(new RecipeProvider(hl8));
            hl8.addProvider(new AdvancementProvider(hl8));
            hl8.addProvider(new LootTableProvider(hl8));
        }
        if (boolean5) {
            hl8.addProvider(new NbtToSnbt(hl8));
        }
        if (boolean6) {
            hl8.addProvider(new BlockListReport(hl8));
            hl8.addProvider(new RegistryDumpReport(hl8));
            hl8.addProvider(new CommandsReport(hl8));
            hl8.addProvider(new BiomeReport(hl8));
        }
        return hl8;
    }
}
