package net.minecraft.client.resources.metadata.language;

import net.minecraft.client.resources.language.LanguageInfo;
import java.util.Collection;

public class LanguageMetadataSection {
    public static final LanguageMetadataSectionSerializer SERIALIZER;
    private final Collection<LanguageInfo> languages;
    
    public LanguageMetadataSection(final Collection<LanguageInfo> collection) {
        this.languages = collection;
    }
    
    public Collection<LanguageInfo> getLanguages() {
        return this.languages;
    }
    
    static {
        SERIALIZER = new LanguageMetadataSectionSerializer();
    }
}
