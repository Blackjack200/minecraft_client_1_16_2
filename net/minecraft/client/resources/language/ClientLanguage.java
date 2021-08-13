package net.minecraft.client.resources.language;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.FormattedText;
import java.io.InputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import net.minecraft.server.packs.resources.Resource;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.io.FileNotFoundException;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Maps;
import java.util.List;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.locale.Language;

public class ClientLanguage extends Language {
    private static final Logger LOGGER;
    private final Map<String, String> storage;
    private final boolean defaultRightToLeft;
    
    private ClientLanguage(final Map<String, String> map, final boolean boolean2) {
        this.storage = map;
        this.defaultRightToLeft = boolean2;
    }
    
    public static ClientLanguage loadFrom(final ResourceManager acf, final List<LanguageInfo> list) {
        final Map<String, String> map3 = (Map<String, String>)Maps.newHashMap();
        boolean boolean4 = false;
        for (final LanguageInfo ekq6 : list) {
            boolean4 |= ekq6.isBidirectional();
            final String string7 = String.format("lang/%s.json", new Object[] { ekq6.getCode() });
            for (final String string8 : acf.getNamespaces()) {
                try {
                    final ResourceLocation vk10 = new ResourceLocation(string8, string7);
                    appendFrom(acf.getResources(vk10), map3);
                }
                catch (FileNotFoundException ex) {}
                catch (Exception exception10) {
                    ClientLanguage.LOGGER.warn("Skipped language file: {}:{} ({})", string8, string7, exception10.toString());
                }
            }
        }
        return new ClientLanguage((Map<String, String>)ImmutableMap.copyOf((Map)map3), boolean4);
    }
    
    private static void appendFrom(final List<Resource> list, final Map<String, String> map) {
        for (final Resource ace4 : list) {
            try (final InputStream inputStream5 = ace4.getInputStream()) {
                Language.loadFromJson(inputStream5, (BiConsumer<String, String>)map::put);
            }
            catch (IOException iOException5) {
                ClientLanguage.LOGGER.warn("Failed to load translations from {}", ace4, iOException5);
            }
        }
    }
    
    @Override
    public String getOrDefault(final String string) {
        return (String)this.storage.getOrDefault(string, string);
    }
    
    @Override
    public boolean has(final String string) {
        return this.storage.containsKey(string);
    }
    
    @Override
    public boolean isDefaultRightToLeft() {
        return this.defaultRightToLeft;
    }
    
    @Override
    public FormattedCharSequence getVisualOrder(final FormattedText nu) {
        return FormattedBidiReorder.reorder(nu, this.defaultRightToLeft);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
