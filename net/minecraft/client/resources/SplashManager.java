package net.minecraft.client.resources;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.common.collect.Lists;
import net.minecraft.client.User;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public class SplashManager extends SimplePreparableReloadListener<List<String>> {
    private static final ResourceLocation SPLASHES_LOCATION;
    private static final Random RANDOM;
    private final List<String> splashes;
    private final User user;
    
    public SplashManager(final User dkj) {
        this.splashes = (List<String>)Lists.newArrayList();
        this.user = dkj;
    }
    
    @Override
    protected List<String> prepare(final ResourceManager acf, final ProfilerFiller ant) {
        try (final Resource ace4 = Minecraft.getInstance().getResourceManager().getResource(SplashManager.SPLASHES_LOCATION);
             final BufferedReader bufferedReader6 = new BufferedReader((Reader)new InputStreamReader(ace4.getInputStream(), StandardCharsets.UTF_8))) {
            return (List<String>)bufferedReader6.lines().map(String::trim).filter(string -> string.hashCode() != 125780783).collect(Collectors.toList());
        }
        catch (IOException iOException4) {
            return (List<String>)Collections.emptyList();
        }
    }
    
    @Override
    protected void apply(final List<String> list, final ResourceManager acf, final ProfilerFiller ant) {
        this.splashes.clear();
        this.splashes.addAll((Collection)list);
    }
    
    @Nullable
    public String getSplash() {
        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        if (calendar2.get(2) + 1 == 12 && calendar2.get(5) == 24) {
            return "Merry X-mas!";
        }
        if (calendar2.get(2) + 1 == 1 && calendar2.get(5) == 1) {
            return "Happy new year!";
        }
        if (calendar2.get(2) + 1 == 10 && calendar2.get(5) == 31) {
            return "OOoooOOOoooo! Spooky!";
        }
        if (this.splashes.isEmpty()) {
            return null;
        }
        if (this.user != null && SplashManager.RANDOM.nextInt(this.splashes.size()) == 42) {
            return this.user.getName().toUpperCase(Locale.ROOT) + " IS YOU";
        }
        return (String)this.splashes.get(SplashManager.RANDOM.nextInt(this.splashes.size()));
    }
    
    static {
        SPLASHES_LOCATION = new ResourceLocation("texts/splashes.txt");
        RANDOM = new Random();
    }
}
