package net.minecraft.gametest.framework;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Consumer;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class GameTestRegistry {
    private static final Collection<TestFunction> testFunctions;
    private static final Set<String> testClassNames;
    private static final Map<String, Consumer<ServerLevel>> beforeBatchFunctions;
    private static final Collection<TestFunction> lastFailedTests;
    
    public static Collection<TestFunction> getTestFunctionsForClassName(final String string) {
        return (Collection<TestFunction>)GameTestRegistry.testFunctions.stream().filter(lu -> isTestFunctionPartOfClass(lu, string)).collect(Collectors.toList());
    }
    
    public static Collection<TestFunction> getAllTestFunctions() {
        return GameTestRegistry.testFunctions;
    }
    
    public static Collection<String> getAllTestClassNames() {
        return (Collection<String>)GameTestRegistry.testClassNames;
    }
    
    public static boolean isTestClass(final String string) {
        return GameTestRegistry.testClassNames.contains(string);
    }
    
    @Nullable
    public static Consumer<ServerLevel> getBeforeBatchFunction(final String string) {
        return (Consumer<ServerLevel>)GameTestRegistry.beforeBatchFunctions.get(string);
    }
    
    public static Optional<TestFunction> findTestFunction(final String string) {
        return (Optional<TestFunction>)getAllTestFunctions().stream().filter(lu -> lu.getTestName().equalsIgnoreCase(string)).findFirst();
    }
    
    public static TestFunction getTestFunction(final String string) {
        final Optional<TestFunction> optional2 = findTestFunction(string);
        if (!optional2.isPresent()) {
            throw new IllegalArgumentException("Can't find the test function for " + string);
        }
        return (TestFunction)optional2.get();
    }
    
    private static boolean isTestFunctionPartOfClass(final TestFunction lu, final String string) {
        return lu.getTestName().toLowerCase().startsWith(string.toLowerCase() + ".");
    }
    
    public static Collection<TestFunction> getLastFailedTests() {
        return GameTestRegistry.lastFailedTests;
    }
    
    public static void rememberFailedTest(final TestFunction lu) {
        GameTestRegistry.lastFailedTests.add(lu);
    }
    
    public static void forgetFailedTests() {
        GameTestRegistry.lastFailedTests.clear();
    }
    
    static {
        testFunctions = (Collection)Lists.newArrayList();
        testClassNames = (Set)Sets.newHashSet();
        beforeBatchFunctions = (Map)Maps.newHashMap();
        lastFailedTests = (Collection)Sets.newHashSet();
    }
}
