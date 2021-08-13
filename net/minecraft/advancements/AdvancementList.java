package net.minecraft.advancements;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;
import com.google.common.base.Functions;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AdvancementList {
    private static final Logger LOGGER;
    private final Map<ResourceLocation, Advancement> advancements;
    private final Set<Advancement> roots;
    private final Set<Advancement> tasks;
    private Listener listener;
    
    public AdvancementList() {
        this.advancements = (Map<ResourceLocation, Advancement>)Maps.newHashMap();
        this.roots = (Set<Advancement>)Sets.newLinkedHashSet();
        this.tasks = (Set<Advancement>)Sets.newLinkedHashSet();
    }
    
    private void remove(final Advancement y) {
        for (final Advancement y2 : y.getChildren()) {
            this.remove(y2);
        }
        AdvancementList.LOGGER.info("Forgot about advancement {}", y.getId());
        this.advancements.remove(y.getId());
        if (y.getParent() == null) {
            this.roots.remove(y);
            if (this.listener != null) {
                this.listener.onRemoveAdvancementRoot(y);
            }
        }
        else {
            this.tasks.remove(y);
            if (this.listener != null) {
                this.listener.onRemoveAdvancementTask(y);
            }
        }
    }
    
    public void remove(final Set<ResourceLocation> set) {
        for (final ResourceLocation vk4 : set) {
            final Advancement y5 = (Advancement)this.advancements.get(vk4);
            if (y5 == null) {
                AdvancementList.LOGGER.warn("Told to remove advancement {} but I don't know what that is", vk4);
            }
            else {
                this.remove(y5);
            }
        }
    }
    
    public void add(final Map<ResourceLocation, Advancement.Builder> map) {
        final Function<ResourceLocation, Advancement> function3 = (Function<ResourceLocation, Advancement>)Functions.forMap((Map)this.advancements, null);
        while (!map.isEmpty()) {
            boolean boolean4 = false;
            final Iterator<Map.Entry<ResourceLocation, Advancement.Builder>> iterator5 = (Iterator<Map.Entry<ResourceLocation, Advancement.Builder>>)map.entrySet().iterator();
            while (iterator5.hasNext()) {
                final Map.Entry<ResourceLocation, Advancement.Builder> entry6 = (Map.Entry<ResourceLocation, Advancement.Builder>)iterator5.next();
                final ResourceLocation vk7 = (ResourceLocation)entry6.getKey();
                final Advancement.Builder a8 = (Advancement.Builder)entry6.getValue();
                if (a8.canBuild(function3)) {
                    final Advancement y9 = a8.build(vk7);
                    this.advancements.put(vk7, y9);
                    boolean4 = true;
                    iterator5.remove();
                    if (y9.getParent() == null) {
                        this.roots.add(y9);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.onAddAdvancementRoot(y9);
                    }
                    else {
                        this.tasks.add(y9);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.onAddAdvancementTask(y9);
                    }
                }
            }
            if (!boolean4) {
                for (final Map.Entry<ResourceLocation, Advancement.Builder> entry6 : map.entrySet()) {
                    AdvancementList.LOGGER.error("Couldn't load advancement {}: {}", entry6.getKey(), entry6.getValue());
                }
                break;
            }
        }
        AdvancementList.LOGGER.info("Loaded {} advancements", this.advancements.size());
    }
    
    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.tasks.clear();
        if (this.listener != null) {
            this.listener.onAdvancementsCleared();
        }
    }
    
    public Iterable<Advancement> getRoots() {
        return (Iterable<Advancement>)this.roots;
    }
    
    public Collection<Advancement> getAllAdvancements() {
        return (Collection<Advancement>)this.advancements.values();
    }
    
    @Nullable
    public Advancement get(final ResourceLocation vk) {
        return (Advancement)this.advancements.get(vk);
    }
    
    public void setListener(@Nullable final Listener a) {
        this.listener = a;
        if (a != null) {
            for (final Advancement y4 : this.roots) {
                a.onAddAdvancementRoot(y4);
            }
            for (final Advancement y4 : this.tasks) {
                a.onAddAdvancementTask(y4);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public interface Listener {
        void onAddAdvancementRoot(final Advancement y);
        
        void onRemoveAdvancementRoot(final Advancement y);
        
        void onAddAdvancementTask(final Advancement y);
        
        void onRemoveAdvancementTask(final Advancement y);
        
        void onAdvancementsCleared();
    }
}
