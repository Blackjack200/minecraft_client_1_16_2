package net.minecraft.server.packs;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException extends FileNotFoundException {
    public ResourcePackFileNotFoundException(final File file, final String string) {
        super(String.format("'%s' in ResourcePack '%s'", new Object[] { string, file }));
    }
}
