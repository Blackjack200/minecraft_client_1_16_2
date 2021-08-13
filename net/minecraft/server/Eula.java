package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.SharedConstants;
import java.nio.file.Path;
import org.apache.logging.log4j.Logger;

public class Eula {
    private static final Logger LOGGER;
    private final Path file;
    private final boolean agreed;
    
    public Eula(final Path path) {
        this.file = path;
        this.agreed = (SharedConstants.IS_RUNNING_IN_IDE || this.readFile());
    }
    
    private boolean readFile() {
        try (final InputStream inputStream2 = Files.newInputStream(this.file, new OpenOption[0])) {
            final Properties properties4 = new Properties();
            properties4.load(inputStream2);
            return Boolean.parseBoolean(properties4.getProperty("eula", "false"));
        }
        catch (Exception exception2) {
            Eula.LOGGER.warn("Failed to load {}", this.file);
            this.saveDefaults();
            return false;
        }
    }
    
    public boolean hasAgreedToEULA() {
        return this.agreed;
    }
    
    private void saveDefaults() {
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            return;
        }
        try (final OutputStream outputStream2 = Files.newOutputStream(this.file, new OpenOption[0])) {
            final Properties properties4 = new Properties();
            properties4.setProperty("eula", "false");
            properties4.store(outputStream2, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
        }
        catch (Exception exception2) {
            Eula.LOGGER.warn("Failed to save {}", this.file, exception2);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
