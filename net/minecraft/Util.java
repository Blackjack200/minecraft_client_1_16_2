package net.minecraft;

import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.PrivilegedActionException;
import org.apache.commons.io.IOUtils;
import java.security.AccessController;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.Arrays;
import com.mojang.serialization.DataResult;
import java.util.stream.IntStream;
import java.io.File;
import java.nio.file.LinkOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.util.function.BooleanSupplier;
import java.nio.file.Path;
import java.util.Random;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.List;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import java.util.stream.Stream;
import java.util.Locale;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.util.datafix.DataFixers;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.server.Bootstrap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import com.google.common.util.concurrent.MoreExecutors;
import net.minecraft.util.Mth;
import java.time.Instant;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.stream.Collector;
import org.apache.logging.log4j.Logger;
import java.util.UUID;
import java.util.function.LongSupplier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Util {
    private static final AtomicInteger WORKER_COUNT;
    private static final ExecutorService BOOTSTRAP_EXECUTOR;
    private static final ExecutorService BACKGROUND_EXECUTOR;
    private static final ExecutorService IO_POOL;
    public static LongSupplier timeSource;
    public static final UUID NIL_UUID;
    private static final Logger LOGGER;
    
    public static <K, V> Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }
    
    public static <T extends Comparable<T>> String getPropertyName(final Property<T> cfg, final Object object) {
        return cfg.getName((T)object);
    }
    
    public static String makeDescriptionId(final String string, @Nullable final ResourceLocation vk) {
        if (vk == null) {
            return string + ".unregistered_sadface";
        }
        return string + '.' + vk.getNamespace() + '.' + vk.getPath().replace('/', '.');
    }
    
    public static long getMillis() {
        return getNanos() / 1000000L;
    }
    
    public static long getNanos() {
        return Util.timeSource.getAsLong();
    }
    
    public static long getEpochMillis() {
        return Instant.now().toEpochMilli();
    }
    
    private static ExecutorService makeExecutor(final String string) {
        final int integer2 = Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 7);
        ExecutorService executorService3;
        if (integer2 <= 0) {
            executorService3 = (ExecutorService)MoreExecutors.newDirectExecutorService();
        }
        else {
            executorService3 = (ExecutorService)new ForkJoinPool(integer2, forkJoinPool -> {
                final ForkJoinWorkerThread forkJoinWorkerThread3 = new ForkJoinWorkerThread(forkJoinPool) {
                    protected void onTermination(final Throwable throwable) {
                        if (throwable != null) {
                            Util.LOGGER.warn("{} died", this.getName(), throwable);
                        }
                        else {
                            Util.LOGGER.debug("{} shutdown", this.getName());
                        }
                        super.onTermination(throwable);
                    }
                };
                forkJoinWorkerThread3.setName("Worker-" + string + "-" + Util.WORKER_COUNT.getAndIncrement());
                return forkJoinWorkerThread3;
            }, Util::onThreadException, true);
        }
        return executorService3;
    }
    
    public static Executor bootstrapExecutor() {
        return (Executor)Util.BOOTSTRAP_EXECUTOR;
    }
    
    public static Executor backgroundExecutor() {
        return (Executor)Util.BACKGROUND_EXECUTOR;
    }
    
    public static Executor ioPool() {
        return (Executor)Util.IO_POOL;
    }
    
    public static void shutdownExecutors() {
        shutdownExecutor(Util.BACKGROUND_EXECUTOR);
        shutdownExecutor(Util.IO_POOL);
    }
    
    private static void shutdownExecutor(final ExecutorService executorService) {
        executorService.shutdown();
        boolean boolean2;
        try {
            boolean2 = executorService.awaitTermination(3L, TimeUnit.SECONDS);
        }
        catch (InterruptedException interruptedException3) {
            boolean2 = false;
        }
        if (!boolean2) {
            executorService.shutdownNow();
        }
    }
    
    private static ExecutorService makeIoExecutor() {
        return Executors.newCachedThreadPool(runnable -> {
            final Thread thread2 = new Thread(runnable);
            thread2.setName(new StringBuilder().append("IO-Worker-").append(Util.WORKER_COUNT.getAndIncrement()).toString());
            thread2.setUncaughtExceptionHandler(Util::onThreadException);
            return thread2;
        });
    }
    
    public static <T> CompletableFuture<T> failedFuture(final Throwable throwable) {
        final CompletableFuture<T> completableFuture2 = (CompletableFuture<T>)new CompletableFuture();
        completableFuture2.completeExceptionally(throwable);
        return completableFuture2;
    }
    
    public static void throwAsRuntime(final Throwable throwable) {
        throw (throwable instanceof RuntimeException) ? throwable : new RuntimeException(throwable);
    }
    
    private static void onThreadException(final Thread thread, Throwable throwable) {
        Util.<Throwable>pauseInIde(throwable);
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }
        if (throwable instanceof ReportedException) {
            Bootstrap.realStdoutPrintln(((ReportedException)throwable).getReport().getFriendlyReport());
            System.exit(-1);
        }
        Util.LOGGER.error(String.format("Caught exception in thread %s", new Object[] { thread }), throwable);
    }
    
    @Nullable
    public static Type<?> fetchChoiceType(final DSL.TypeReference typeReference, final String string) {
        if (!SharedConstants.CHECK_DATA_FIXER_SCHEMA) {
            return null;
        }
        return doFetchChoiceType(typeReference, string);
    }
    
    @Nullable
    private static Type<?> doFetchChoiceType(final DSL.TypeReference typeReference, final String string) {
        Type<?> type3 = null;
        try {
            type3 = DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getWorldVersion())).getChoiceType(typeReference, string);
        }
        catch (IllegalArgumentException illegalArgumentException4) {
            Util.LOGGER.error("No data fixer registered for {}", string);
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw illegalArgumentException4;
            }
        }
        return type3;
    }
    
    public static OS getPlatform() {
        final String string1 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string1.contains("win")) {
            return OS.WINDOWS;
        }
        if (string1.contains("mac")) {
            return OS.OSX;
        }
        if (string1.contains("solaris")) {
            return OS.SOLARIS;
        }
        if (string1.contains("sunos")) {
            return OS.SOLARIS;
        }
        if (string1.contains("linux")) {
            return OS.LINUX;
        }
        if (string1.contains("unix")) {
            return OS.LINUX;
        }
        return OS.UNKNOWN;
    }
    
    public static Stream<String> getVmArguments() {
        final RuntimeMXBean runtimeMXBean1 = ManagementFactory.getRuntimeMXBean();
        return (Stream<String>)runtimeMXBean1.getInputArguments().stream().filter(string -> string.startsWith("-X"));
    }
    
    public static <T> T lastOf(final List<T> list) {
        return (T)list.get(list.size() - 1);
    }
    
    public static <T> T findNextInIterable(final Iterable<T> iterable, @Nullable final T object) {
        final Iterator<T> iterator3 = (Iterator<T>)iterable.iterator();
        final T object2 = (T)iterator3.next();
        if (object != null) {
            for (T object3 = object2; object3 != object; object3 = (T)iterator3.next()) {
                if (iterator3.hasNext()) {}
            }
            if (iterator3.hasNext()) {
                return (T)iterator3.next();
            }
        }
        return object2;
    }
    
    public static <T> T findPreviousInIterable(final Iterable<T> iterable, @Nullable final T object) {
        final Iterator<T> iterator3 = (Iterator<T>)iterable.iterator();
        T object2 = null;
        while (iterator3.hasNext()) {
            final T object3 = (T)iterator3.next();
            if (object3 == object) {
                if (object2 == null) {
                    object2 = (T)(iterator3.hasNext() ? Iterators.getLast((Iterator)iterator3) : object);
                    break;
                }
                break;
            }
            else {
                object2 = object3;
            }
        }
        return object2;
    }
    
    public static <T> T make(final Supplier<T> supplier) {
        return (T)supplier.get();
    }
    
    public static <T> T make(final T object, final Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }
    
    public static <K> Hash.Strategy<K> identityStrategy() {
        return (Hash.Strategy<K>)IdentityStrategy.INSTANCE;
    }
    
    public static <V> CompletableFuture<List<V>> sequence(final List<? extends CompletableFuture<? extends V>> list) {
        final List<V> list2 = (List<V>)Lists.newArrayListWithCapacity(list.size());
        final CompletableFuture<?>[] arr3 = new CompletableFuture[list.size()];
        final CompletableFuture<Void> completableFuture4 = (CompletableFuture<Void>)new CompletableFuture();
        list.forEach(completableFuture4 -> {
            final int integer5 = list2.size();
            list2.add(null);
            arr3[integer5] = completableFuture4.whenComplete((object, throwable) -> {
                if (throwable != null) {
                    completableFuture4.completeExceptionally(throwable);
                }
                else {
                    list2.set(integer5, object);
                }
            });
        });
        return (CompletableFuture<List<V>>)CompletableFuture.allOf((CompletableFuture[])arr3).applyToEither((CompletionStage)completableFuture4, void2 -> list2);
    }
    
    public static <T> Stream<T> toStream(final Optional<? extends T> optional) {
        return (Stream<T>)DataFixUtils.orElseGet(optional.map(Stream::of), Stream::empty);
    }
    
    public static <T> Optional<T> ifElse(final Optional<T> optional, final Consumer<T> consumer, final Runnable runnable) {
        if (optional.isPresent()) {
            consumer.accept(optional.get());
        }
        else {
            runnable.run();
        }
        return optional;
    }
    
    public static Runnable name(final Runnable runnable, final Supplier<String> supplier) {
        return runnable;
    }
    
    public static <T extends Throwable> T pauseInIde(final T throwable) {
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            Util.LOGGER.error("Trying to throw a fatal exception, pausing in IDE", (Throwable)throwable);
            try {
                while (true) {
                    Thread.sleep(1000L);
                    Util.LOGGER.error("paused");
                }
            }
            catch (InterruptedException interruptedException2) {
                return throwable;
            }
        }
        return throwable;
    }
    
    public static String describeError(final Throwable throwable) {
        if (throwable.getCause() != null) {
            return describeError(throwable.getCause());
        }
        if (throwable.getMessage() != null) {
            return throwable.getMessage();
        }
        return throwable.toString();
    }
    
    public static <T> T getRandom(final T[] arr, final Random random) {
        return arr[random.nextInt(arr.length)];
    }
    
    public static int getRandom(final int[] arr, final Random random) {
        return arr[random.nextInt(arr.length)];
    }
    
    private static BooleanSupplier createRenamer(final Path path1, final Path path2) {
        return (BooleanSupplier)new BooleanSupplier() {
            public boolean getAsBoolean() {
                try {
                    Files.move(path1, path2, new CopyOption[0]);
                    return true;
                }
                catch (IOException iOException2) {
                    Util.LOGGER.error("Failed to rename", (Throwable)iOException2);
                    return false;
                }
            }
            
            public String toString() {
                return new StringBuilder().append("rename ").append(path1).append(" to ").append(path2).toString();
            }
        };
    }
    
    private static BooleanSupplier createDeleter(final Path path) {
        return (BooleanSupplier)new BooleanSupplier() {
            public boolean getAsBoolean() {
                try {
                    Files.deleteIfExists(path);
                    return true;
                }
                catch (IOException iOException2) {
                    Util.LOGGER.warn("Failed to delete", (Throwable)iOException2);
                    return false;
                }
            }
            
            public String toString() {
                return new StringBuilder().append("delete old ").append(path).toString();
            }
        };
    }
    
    private static BooleanSupplier createFileDeletedCheck(final Path path) {
        return (BooleanSupplier)new BooleanSupplier() {
            public boolean getAsBoolean() {
                return !Files.exists(path, new LinkOption[0]);
            }
            
            public String toString() {
                return new StringBuilder().append("verify that ").append(path).append(" is deleted").toString();
            }
        };
    }
    
    private static BooleanSupplier createFileCreatedCheck(final Path path) {
        return (BooleanSupplier)new BooleanSupplier() {
            public boolean getAsBoolean() {
                return Files.isRegularFile(path, new LinkOption[0]);
            }
            
            public String toString() {
                return new StringBuilder().append("verify that ").append(path).append(" is present").toString();
            }
        };
    }
    
    private static boolean executeInSequence(final BooleanSupplier... arr) {
        for (final BooleanSupplier booleanSupplier5 : arr) {
            if (!booleanSupplier5.getAsBoolean()) {
                Util.LOGGER.warn("Failed to execute {}", booleanSupplier5);
                return false;
            }
        }
        return true;
    }
    
    private static boolean runWithRetries(final int integer, final String string, final BooleanSupplier... arr) {
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            if (executeInSequence(arr)) {
                return true;
            }
            Util.LOGGER.error("Failed to {}, retrying {}/{}", string, integer2, integer);
        }
        Util.LOGGER.error("Failed to {}, aborting, progress might be lost", string);
        return false;
    }
    
    public static void safeReplaceFile(final File file1, final File file2, final File file3) {
        safeReplaceFile(file1.toPath(), file2.toPath(), file3.toPath());
    }
    
    public static void safeReplaceFile(final Path path1, final Path path2, final Path path3) {
        final int integer4 = 10;
        if (Files.exists(path1, new LinkOption[0]) && !runWithRetries(10, new StringBuilder().append("create backup ").append(path3).toString(), createDeleter(path3), createRenamer(path1, path3), createFileCreatedCheck(path3))) {
            return;
        }
        if (!runWithRetries(10, new StringBuilder().append("remove old ").append(path1).toString(), createDeleter(path1), createFileDeletedCheck(path1))) {
            return;
        }
        if (!runWithRetries(10, new StringBuilder().append("replace ").append(path1).append(" with ").append(path2).toString(), createRenamer(path2, path1), createFileCreatedCheck(path1))) {
            runWithRetries(10, new StringBuilder().append("restore ").append(path1).append(" from ").append(path3).toString(), createRenamer(path3, path1), createFileCreatedCheck(path1));
        }
    }
    
    public static int offsetByCodepoints(final String string, int integer2, final int integer3) {
        final int integer4 = string.length();
        if (integer3 >= 0) {
            for (int integer5 = 0; integer2 < integer4 && integer5 < integer3; ++integer5) {
                if (Character.isHighSurrogate(string.charAt(integer2++)) && integer2 < integer4 && Character.isLowSurrogate(string.charAt(integer2))) {
                    ++integer2;
                }
            }
        }
        else {
            for (int integer5 = integer3; integer2 > 0 && integer5 < 0; ++integer5) {
                if (Character.isLowSurrogate(string.charAt(--integer2)) && integer2 > 0 && Character.isHighSurrogate(string.charAt(integer2 - 1))) {
                    --integer2;
                }
            }
        }
        return integer2;
    }
    
    public static Consumer<String> prefix(final String string, final Consumer<String> consumer) {
        return (Consumer<String>)(string3 -> consumer.accept((string + string3)));
    }
    
    public static DataResult<int[]> fixedSize(final IntStream intStream, final int integer) {
        final int[] arr3 = intStream.limit((long)(integer + 1)).toArray();
        if (arr3.length == integer) {
            return (DataResult<int[]>)DataResult.success(arr3);
        }
        final String string4 = new StringBuilder().append("Input is not a list of ").append(integer).append(" ints").toString();
        if (arr3.length >= integer) {
            return (DataResult<int[]>)DataResult.error(string4, Arrays.copyOf(arr3, integer));
        }
        return (DataResult<int[]>)DataResult.error(string4);
    }
    
    public static void startTimerHackThread() {
        final Thread thread1 = new Thread("Timer hack thread") {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(2147483647L);
                    }
                }
                catch (InterruptedException interruptedException2) {
                    Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
                }
            }
        };
        thread1.setDaemon(true);
        thread1.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(Util.LOGGER));
        thread1.start();
    }
    
    public static void copyBetweenDirs(final Path path1, final Path path2, final Path path3) throws IOException {
        final Path path4 = path1.relativize(path3);
        final Path path5 = path2.resolve(path4);
        Files.copy(path3, path5, new CopyOption[0]);
    }
    
    public static String sanitizeName(final String string, final CharPredicate j) {
        return (String)string.toLowerCase(Locale.ROOT).chars().mapToObj(integer -> j.test((char)integer) ? Character.toString((char)integer) : "_").collect(Collectors.joining());
    }
    
    static {
        WORKER_COUNT = new AtomicInteger(1);
        BOOTSTRAP_EXECUTOR = makeExecutor("Bootstrap");
        BACKGROUND_EXECUTOR = makeExecutor("Main");
        IO_POOL = makeIoExecutor();
        Util.timeSource = System::nanoTime;
        NIL_UUID = new UUID(0L, 0L);
        LOGGER = LogManager.getLogger();
    }
    
    public enum OS {
        LINUX, 
        SOLARIS, 
        WINDOWS {
            @Override
            protected String[] getOpenUrlArguments(final URL uRL) {
                return new String[] { "rundll32", "url.dll,FileProtocolHandler", uRL.toString() };
            }
        }, 
        OSX {
            @Override
            protected String[] getOpenUrlArguments(final URL uRL) {
                return new String[] { "open", uRL.toString() };
            }
        }, 
        UNKNOWN;
        
        public void openUrl(final URL uRL) {
            try {
                final Process process3 = (Process)AccessController.doPrivileged(() -> Runtime.getRuntime().exec(this.getOpenUrlArguments(uRL)));
                for (final String string5 : IOUtils.readLines(process3.getErrorStream())) {
                    Util.LOGGER.error(string5);
                }
                process3.getInputStream().close();
                process3.getErrorStream().close();
                process3.getOutputStream().close();
            }
            catch (PrivilegedActionException | IOException ex2) {
                final Exception ex;
                final Exception exception3 = ex;
                Util.LOGGER.error("Couldn't open url '{}'", uRL, exception3);
            }
        }
        
        public void openUri(final URI uRI) {
            try {
                this.openUrl(uRI.toURL());
            }
            catch (MalformedURLException malformedURLException3) {
                Util.LOGGER.error("Couldn't open uri '{}'", uRI, malformedURLException3);
            }
        }
        
        public void openFile(final File file) {
            try {
                this.openUrl(file.toURI().toURL());
            }
            catch (MalformedURLException malformedURLException3) {
                Util.LOGGER.error("Couldn't open file '{}'", file, malformedURLException3);
            }
        }
        
        protected String[] getOpenUrlArguments(final URL uRL) {
            String string3 = uRL.toString();
            if ("file".equals(uRL.getProtocol())) {
                string3 = string3.replace("file:", "file://");
            }
            return new String[] { "xdg-open", string3 };
        }
        
        public void openUri(final String string) {
            try {
                this.openUrl(new URI(string).toURL());
            }
            catch (URISyntaxException | MalformedURLException | IllegalArgumentException ex2) {
                final Exception ex;
                final Exception exception3 = ex;
                Util.LOGGER.error("Couldn't open uri '{}'", string, exception3);
            }
        }
    }
    
    enum IdentityStrategy implements Hash.Strategy<Object> {
        INSTANCE;
        
        public int hashCode(final Object object) {
            return System.identityHashCode(object);
        }
        
        public boolean equals(final Object object1, final Object object2) {
            return object1 == object2;
        }
    }
}
