package net.minecraft.gametest.framework;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Consumer;
import java.util.Collection;

public class GameTestBatch {
    private final String name;
    private final Collection<TestFunction> testFunctions;
    @Nullable
    private final Consumer<ServerLevel> beforeBatchFunction;
    
    public GameTestBatch(final String string, final Collection<TestFunction> collection, @Nullable final Consumer<ServerLevel> consumer) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("A GameTestBatch must include at least one TestFunction!");
        }
        this.name = string;
        this.testFunctions = collection;
        this.beforeBatchFunction = consumer;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Collection<TestFunction> getTestFunctions() {
        return this.testFunctions;
    }
    
    public void runBeforeBatchFunction(final ServerLevel aag) {
        if (this.beforeBatchFunction != null) {
            this.beforeBatchFunction.accept(aag);
        }
    }
}
