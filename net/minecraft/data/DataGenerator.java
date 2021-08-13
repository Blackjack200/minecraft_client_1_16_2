package net.minecraft.data;

import net.minecraft.server.Bootstrap;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import java.util.List;
import java.nio.file.Path;
import java.util.Collection;
import org.apache.logging.log4j.Logger;

public class DataGenerator {
    private static final Logger LOGGER;
    private final Collection<Path> inputFolders;
    private final Path outputFolder;
    private final List<DataProvider> providers;
    
    public DataGenerator(final Path path, final Collection<Path> collection) {
        this.providers = (List<DataProvider>)Lists.newArrayList();
        this.outputFolder = path;
        this.inputFolders = collection;
    }
    
    public Collection<Path> getInputFolders() {
        return this.inputFolders;
    }
    
    public Path getOutputFolder() {
        return this.outputFolder;
    }
    
    public void run() throws IOException {
        final HashCache hn2 = new HashCache(this.outputFolder, "cache");
        hn2.keep(this.getOutputFolder().resolve("version.json"));
        final Stopwatch stopwatch3 = Stopwatch.createStarted();
        final Stopwatch stopwatch4 = Stopwatch.createUnstarted();
        for (final DataProvider hm6 : this.providers) {
            DataGenerator.LOGGER.info("Starting provider: {}", hm6.getName());
            stopwatch4.start();
            hm6.run(hn2);
            stopwatch4.stop();
            DataGenerator.LOGGER.info("{} finished after {} ms", hm6.getName(), stopwatch4.elapsed(TimeUnit.MILLISECONDS));
            stopwatch4.reset();
        }
        DataGenerator.LOGGER.info("All providers took: {} ms", stopwatch3.elapsed(TimeUnit.MILLISECONDS));
        hn2.purgeStaleAndWrite();
    }
    
    public void addProvider(final DataProvider hm) {
        this.providers.add(hm);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        Bootstrap.bootStrap();
    }
}
