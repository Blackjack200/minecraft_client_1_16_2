package net.minecraft;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.io.IOException;
import java.util.regex.Matcher;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FileUtil {
    private static final Pattern COPY_COUNTER_PATTERN;
    private static final Pattern RESERVED_WINDOWS_FILENAMES;
    
    public static String findAvailableName(final Path path, String string2, final String string3) throws IOException {
        for (final char character7 : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
            string2 = string2.replace(character7, '_');
        }
        string2 = string2.replaceAll("[./\"]", "_");
        if (FileUtil.RESERVED_WINDOWS_FILENAMES.matcher((CharSequence)string2).matches()) {
            string2 = "_" + string2 + "_";
        }
        final Matcher matcher4 = FileUtil.COPY_COUNTER_PATTERN.matcher((CharSequence)string2);
        int integer5 = 0;
        if (matcher4.matches()) {
            string2 = matcher4.group("name");
            integer5 = Integer.parseInt(matcher4.group("count"));
        }
        if (string2.length() > 255 - string3.length()) {
            string2 = string2.substring(0, 255 - string3.length());
        }
        while (true) {
            String string4 = string2;
            if (integer5 != 0) {
                final String string5 = new StringBuilder().append(" (").append(integer5).append(")").toString();
                final int integer6 = 255 - string5.length();
                if (string4.length() > integer6) {
                    string4 = string4.substring(0, integer6);
                }
                string4 += string5;
            }
            string4 += string3;
            final Path path2 = path.resolve(string4);
            try {
                final Path path3 = Files.createDirectory(path2, new FileAttribute[0]);
                Files.deleteIfExists(path3);
                return path.relativize(path3).toString();
            }
            catch (FileAlreadyExistsException fileAlreadyExistsException8) {
                ++integer5;
            }
        }
    }
    
    public static boolean isPathNormalized(final Path path) {
        final Path path2 = path.normalize();
        return path2.equals(path);
    }
    
    public static boolean isPathPortable(final Path path) {
        for (final Path path2 : path) {
            if (FileUtil.RESERVED_WINDOWS_FILENAMES.matcher((CharSequence)path2.toString()).matches()) {
                return false;
            }
        }
        return true;
    }
    
    public static Path createPathToResource(final Path path, final String string2, final String string3) {
        final String string4 = string2 + string3;
        final Path path2 = Paths.get(string4, new String[0]);
        if (path2.endsWith(string3)) {
            throw new InvalidPathException(string4, "empty resource name");
        }
        return path.resolve(path2);
    }
    
    static {
        COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
        RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);
    }
}
