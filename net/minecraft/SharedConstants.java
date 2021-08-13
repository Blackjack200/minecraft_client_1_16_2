package net.minecraft;

import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import net.minecraft.commands.BrigadierExceptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.time.Duration;
import com.mojang.bridge.game.GameVersion;
import io.netty.util.ResourceLeakDetector;

public class SharedConstants {
    public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION;
    public static final long MAXIMUM_TICK_TIME_NANOS;
    public static boolean CHECK_DATA_FIXER_SCHEMA;
    public static boolean IS_RUNNING_IN_IDE;
    public static final char[] ILLEGAL_FILE_CHARACTERS;
    private static GameVersion CURRENT_VERSION;
    
    public static boolean isAllowedChatCharacter(final char character) {
        return character != '§' && character >= ' ' && character != '\u007f';
    }
    
    public static String filterText(final String string) {
        final StringBuilder stringBuilder2 = new StringBuilder();
        for (final char character6 : string.toCharArray()) {
            if (isAllowedChatCharacter(character6)) {
                stringBuilder2.append(character6);
            }
        }
        return stringBuilder2.toString();
    }
    
    public static GameVersion getCurrentVersion() {
        if (SharedConstants.CURRENT_VERSION == null) {
            SharedConstants.CURRENT_VERSION = DetectedVersion.tryDetectVersion();
        }
        return SharedConstants.CURRENT_VERSION;
    }
    
    static {
        NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
        MAXIMUM_TICK_TIME_NANOS = Duration.ofMillis(300L).toNanos();
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = true;
        ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
        ResourceLeakDetector.setLevel(SharedConstants.NETTY_LEAK_DETECTION);
        CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES = false;
        CommandSyntaxException.BUILT_IN_EXCEPTIONS = (BuiltInExceptionProvider)new BrigadierExceptions();
    }
}
