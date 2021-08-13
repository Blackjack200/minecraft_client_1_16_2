package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import java.util.Collection;

public class GameTestTicker {
    public static final GameTestTicker singleton;
    private final Collection<GameTestInfo> testInfos;
    
    public GameTestTicker() {
        this.testInfos = (Collection<GameTestInfo>)Lists.newCopyOnWriteArrayList();
    }
    
    public void add(final GameTestInfo lf) {
        this.testInfos.add(lf);
    }
    
    public void clear() {
        this.testInfos.clear();
    }
    
    public void tick() {
        this.testInfos.forEach(GameTestInfo::tick);
        this.testInfos.removeIf(GameTestInfo::isDone);
    }
    
    static {
        singleton = new GameTestTicker();
    }
}
