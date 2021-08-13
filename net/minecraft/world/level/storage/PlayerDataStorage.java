package net.minecraft.world.level.storage;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.Util;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import com.mojang.datafixers.DataFixer;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class PlayerDataStorage {
    private static final Logger LOGGER;
    private final File playerDir;
    protected final DataFixer fixerUpper;
    
    public PlayerDataStorage(final LevelStorageSource.LevelStorageAccess a, final DataFixer dataFixer) {
        this.fixerUpper = dataFixer;
        (this.playerDir = a.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile()).mkdirs();
    }
    
    public void save(final Player bft) {
        try {
            final CompoundTag md3 = bft.saveWithoutId(new CompoundTag());
            final File file4 = File.createTempFile(bft.getStringUUID() + "-", ".dat", this.playerDir);
            NbtIo.writeCompressed(md3, file4);
            final File file5 = new File(this.playerDir, bft.getStringUUID() + ".dat");
            final File file6 = new File(this.playerDir, bft.getStringUUID() + ".dat_old");
            Util.safeReplaceFile(file5, file4, file6);
        }
        catch (Exception exception3) {
            PlayerDataStorage.LOGGER.warn("Failed to save player data for {}", bft.getName().getString());
        }
    }
    
    @Nullable
    public CompoundTag load(final Player bft) {
        CompoundTag md3 = null;
        try {
            final File file4 = new File(this.playerDir, bft.getStringUUID() + ".dat");
            if (file4.exists() && file4.isFile()) {
                md3 = NbtIo.readCompressed(file4);
            }
        }
        catch (Exception exception4) {
            PlayerDataStorage.LOGGER.warn("Failed to load player data for {}", bft.getName().getString());
        }
        if (md3 != null) {
            final int integer4 = md3.contains("DataVersion", 3) ? md3.getInt("DataVersion") : -1;
            bft.load(NbtUtils.update(this.fixerUpper, DataFixTypes.PLAYER, md3, integer4));
        }
        return md3;
    }
    
    public String[] getSeenPlayers() {
        String[] arr2 = this.playerDir.list();
        if (arr2 == null) {
            arr2 = new String[0];
        }
        for (int integer3 = 0; integer3 < arr2.length; ++integer3) {
            if (arr2[integer3].endsWith(".dat")) {
                arr2[integer3] = arr2[integer3].substring(0, arr2[integer3].length() - 4);
            }
        }
        return arr2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
