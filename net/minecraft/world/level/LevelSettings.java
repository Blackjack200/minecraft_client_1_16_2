package net.minecraft.world.level;

import com.mojang.serialization.DynamicLike;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.Difficulty;

public final class LevelSettings {
    private final String levelName;
    private final GameType gameType;
    private final boolean hardcore;
    private final Difficulty difficulty;
    private final boolean allowCommands;
    private final GameRules gameRules;
    private final DataPackConfig dataPackConfig;
    
    public LevelSettings(final String string, final GameType brr, final boolean boolean3, final Difficulty aoo, final boolean boolean5, final GameRules brq, final DataPackConfig brh) {
        this.levelName = string;
        this.gameType = brr;
        this.hardcore = boolean3;
        this.difficulty = aoo;
        this.allowCommands = boolean5;
        this.gameRules = brq;
        this.dataPackConfig = brh;
    }
    
    public static LevelSettings parse(final Dynamic<?> dynamic, final DataPackConfig brh) {
        final GameType brr3 = GameType.byId(dynamic.get("GameType").asInt(0));
        return new LevelSettings(dynamic.get("LevelName").asString(""), brr3, dynamic.get("hardcore").asBoolean(false), (Difficulty)dynamic.get("Difficulty").asNumber().map(number -> Difficulty.byId(number.byteValue())).result().orElse(Difficulty.NORMAL), dynamic.get("allowCommands").asBoolean(brr3 == GameType.CREATIVE), new GameRules(dynamic.get("GameRules")), brh);
    }
    
    public String levelName() {
        return this.levelName;
    }
    
    public GameType gameType() {
        return this.gameType;
    }
    
    public boolean hardcore() {
        return this.hardcore;
    }
    
    public Difficulty difficulty() {
        return this.difficulty;
    }
    
    public boolean allowCommands() {
        return this.allowCommands;
    }
    
    public GameRules gameRules() {
        return this.gameRules;
    }
    
    public DataPackConfig getDataPackConfig() {
        return this.dataPackConfig;
    }
    
    public LevelSettings withGameType(final GameType brr) {
        return new LevelSettings(this.levelName, brr, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, this.dataPackConfig);
    }
    
    public LevelSettings withDifficulty(final Difficulty aoo) {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, aoo, this.allowCommands, this.gameRules, this.dataPackConfig);
    }
    
    public LevelSettings withDataPackConfig(final DataPackConfig brh) {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, brh);
    }
    
    public LevelSettings copy() {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules.copy(), this.dataPackConfig);
    }
}
