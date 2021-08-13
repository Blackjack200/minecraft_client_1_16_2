package net.minecraft.world.level.storage;

public class LevelResource {
    public static final LevelResource PLAYER_ADVANCEMENTS_DIR;
    public static final LevelResource PLAYER_STATS_DIR;
    public static final LevelResource PLAYER_DATA_DIR;
    public static final LevelResource PLAYER_OLD_DATA_DIR;
    public static final LevelResource LEVEL_DATA_FILE;
    public static final LevelResource GENERATED_DIR;
    public static final LevelResource DATAPACK_DIR;
    public static final LevelResource MAP_RESOURCE_FILE;
    public static final LevelResource ROOT;
    private final String id;
    
    private LevelResource(final String string) {
        this.id = string;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String toString() {
        return "/" + this.id;
    }
    
    static {
        PLAYER_ADVANCEMENTS_DIR = new LevelResource("advancements");
        PLAYER_STATS_DIR = new LevelResource("stats");
        PLAYER_DATA_DIR = new LevelResource("playerdata");
        PLAYER_OLD_DATA_DIR = new LevelResource("players");
        LEVEL_DATA_FILE = new LevelResource("level.dat");
        GENERATED_DIR = new LevelResource("generated");
        DATAPACK_DIR = new LevelResource("datapacks");
        MAP_RESOURCE_FILE = new LevelResource("resources.zip");
        ROOT = new LevelResource(".");
    }
}
