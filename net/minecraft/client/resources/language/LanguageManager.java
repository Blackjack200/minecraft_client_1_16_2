package net.minecraft.client.resources.language;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
import com.google.common.collect.Sets;
import java.util.SortedSet;
import java.util.List;
import net.minecraft.locale.Language;
import com.google.common.collect.Lists;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.PackResources;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class LanguageManager implements ResourceManagerReloadListener {
    private static final Logger LOGGER;
    private static final LanguageInfo DEFAULT_LANGUAGE;
    private Map<String, LanguageInfo> languages;
    private String currentCode;
    private LanguageInfo currentLanguage;
    
    public LanguageManager(final String string) {
        this.languages = (Map<String, LanguageInfo>)ImmutableMap.of("en_us", LanguageManager.DEFAULT_LANGUAGE);
        this.currentLanguage = LanguageManager.DEFAULT_LANGUAGE;
        this.currentCode = string;
    }
    
    private static Map<String, LanguageInfo> extractLanguages(final Stream<PackResources> stream) {
        final Map<String, LanguageInfo> map2 = (Map<String, LanguageInfo>)Maps.newHashMap();
        stream.forEach(abh -> {
            try {
                final LanguageMetadataSection ekz3 = abh.<LanguageMetadataSection>getMetadataSection((MetadataSectionSerializer<LanguageMetadataSection>)LanguageMetadataSection.SERIALIZER);
                if (ekz3 != null) {
                    for (final LanguageInfo ekq5 : ekz3.getLanguages()) {
                        map2.putIfAbsent(ekq5.getCode(), ekq5);
                    }
                }
            }
            catch (RuntimeException | IOException ex2) {
                final Exception ex;
                final Exception exception3 = ex;
                LanguageManager.LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", abh.getName(), exception3);
            }
        });
        return (Map<String, LanguageInfo>)ImmutableMap.copyOf((Map)map2);
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        this.languages = extractLanguages(acf.listPacks());
        final LanguageInfo ekq3 = (LanguageInfo)this.languages.getOrDefault("en_us", LanguageManager.DEFAULT_LANGUAGE);
        this.currentLanguage = (LanguageInfo)this.languages.getOrDefault(this.currentCode, ekq3);
        final List<LanguageInfo> list4 = (List<LanguageInfo>)Lists.newArrayList((Object[])new LanguageInfo[] { ekq3 });
        if (this.currentLanguage != ekq3) {
            list4.add(this.currentLanguage);
        }
        final ClientLanguage ekn5 = ClientLanguage.loadFrom(acf, list4);
        I18n.setLanguage(ekn5);
        Language.inject(ekn5);
    }
    
    public void setSelected(final LanguageInfo ekq) {
        this.currentCode = ekq.getCode();
        this.currentLanguage = ekq;
    }
    
    public LanguageInfo getSelected() {
        return this.currentLanguage;
    }
    
    public SortedSet<LanguageInfo> getLanguages() {
        return (SortedSet<LanguageInfo>)Sets.newTreeSet((Iterable)this.languages.values());
    }
    
    public LanguageInfo getLanguage(final String string) {
        return (LanguageInfo)this.languages.get(string);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DEFAULT_LANGUAGE = new LanguageInfo("en_us", "US", "English", false);
    }
}
