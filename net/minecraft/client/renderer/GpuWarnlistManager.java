package net.minecraft.client.renderer;

import com.mojang.blaze3d.platform.GlUtil;
import java.util.regex.Matcher;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonElement;
import net.minecraft.server.packs.resources.Resource;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.regex.Pattern;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public class GpuWarnlistManager extends SimplePreparableReloadListener<Preparations> {
    private static final Logger LOGGER;
    private static final ResourceLocation GPU_WARNLIST_LOCATION;
    private ImmutableMap<String, String> warnings;
    private boolean showWarning;
    private boolean warningDismissed;
    private boolean skipFabulous;
    
    public GpuWarnlistManager() {
        this.warnings = (ImmutableMap<String, String>)ImmutableMap.of();
    }
    
    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }
    
    public boolean willShowWarning() {
        return this.hasWarnings() && !this.warningDismissed;
    }
    
    public void showWarning() {
        this.showWarning = true;
    }
    
    public void dismissWarning() {
        this.warningDismissed = true;
    }
    
    public void dismissWarningAndSkipFabulous() {
        this.warningDismissed = true;
        this.skipFabulous = true;
    }
    
    public boolean isShowingWarning() {
        return this.showWarning && !this.warningDismissed;
    }
    
    public boolean isSkippingFabulous() {
        return this.skipFabulous;
    }
    
    public void resetWarnings() {
        this.showWarning = false;
        this.warningDismissed = false;
        this.skipFabulous = false;
    }
    
    @Nullable
    public String getRendererWarnings() {
        return (String)this.warnings.get("renderer");
    }
    
    @Nullable
    public String getVersionWarnings() {
        return (String)this.warnings.get("version");
    }
    
    @Nullable
    public String getVendorWarnings() {
        return (String)this.warnings.get("vendor");
    }
    
    @Nullable
    public String getAllWarnings() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        this.warnings.forEach((string2, string3) -> stringBuilder2.append(string2).append(": ").append(string3));
        return (stringBuilder2.length() == 0) ? null : stringBuilder2.toString();
    }
    
    @Override
    protected Preparations prepare(final ResourceManager acf, final ProfilerFiller ant) {
        final List<Pattern> list4 = (List<Pattern>)Lists.newArrayList();
        final List<Pattern> list5 = (List<Pattern>)Lists.newArrayList();
        final List<Pattern> list6 = (List<Pattern>)Lists.newArrayList();
        ant.startTick();
        final JsonObject jsonObject7 = parseJson(acf, ant);
        if (jsonObject7 != null) {
            ant.push("compile_regex");
            compilePatterns(jsonObject7.getAsJsonArray("renderer"), list4);
            compilePatterns(jsonObject7.getAsJsonArray("version"), list5);
            compilePatterns(jsonObject7.getAsJsonArray("vendor"), list6);
            ant.pop();
        }
        ant.endTick();
        return new Preparations((List)list4, (List)list5, (List)list6);
    }
    
    @Override
    protected void apply(final Preparations a, final ResourceManager acf, final ProfilerFiller ant) {
        this.warnings = a.apply();
    }
    
    private static void compilePatterns(final JsonArray jsonArray, final List<Pattern> list) {
        jsonArray.forEach(jsonElement -> list.add(Pattern.compile(jsonElement.getAsString(), 2)));
    }
    
    @Nullable
    private static JsonObject parseJson(final ResourceManager acf, final ProfilerFiller ant) {
        ant.push("parse_json");
        JsonObject jsonObject3 = null;
        try (final Resource ace4 = acf.getResource(GpuWarnlistManager.GPU_WARNLIST_LOCATION);
             final BufferedReader bufferedReader6 = new BufferedReader((Reader)new InputStreamReader(ace4.getInputStream(), StandardCharsets.UTF_8))) {
            jsonObject3 = new JsonParser().parse((Reader)bufferedReader6).getAsJsonObject();
        }
        catch (IOException | JsonSyntaxException ex2) {
            final Exception ex;
            final Exception exception4 = ex;
            GpuWarnlistManager.LOGGER.warn("Failed to load GPU warnlist");
        }
        ant.pop();
        return jsonObject3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GPU_WARNLIST_LOCATION = new ResourceLocation("gpu_warnlist.json");
    }
    
    public static final class Preparations {
        private final List<Pattern> rendererPatterns;
        private final List<Pattern> versionPatterns;
        private final List<Pattern> vendorPatterns;
        
        private Preparations(final List<Pattern> list1, final List<Pattern> list2, final List<Pattern> list3) {
            this.rendererPatterns = list1;
            this.versionPatterns = list2;
            this.vendorPatterns = list3;
        }
        
        private static String matchAny(final List<Pattern> list, final String string) {
            final List<String> list2 = (List<String>)Lists.newArrayList();
            for (final Pattern pattern5 : list) {
                final Matcher matcher6 = pattern5.matcher((CharSequence)string);
                while (matcher6.find()) {
                    list2.add(matcher6.group());
                }
            }
            return String.join(", ", (Iterable)list2);
        }
        
        private ImmutableMap<String, String> apply() {
            final ImmutableMap.Builder<String, String> builder2 = (ImmutableMap.Builder<String, String>)new ImmutableMap.Builder();
            final String string3 = matchAny(this.rendererPatterns, GlUtil.getRenderer());
            if (!string3.isEmpty()) {
                builder2.put("renderer", string3);
            }
            final String string4 = matchAny(this.versionPatterns, GlUtil.getOpenGLVersion());
            if (!string4.isEmpty()) {
                builder2.put("version", string4);
            }
            final String string5 = matchAny(this.vendorPatterns, GlUtil.getVendor());
            if (!string5.isEmpty()) {
                builder2.put("vendor", string5);
            }
            return (ImmutableMap<String, String>)builder2.build();
        }
    }
}
