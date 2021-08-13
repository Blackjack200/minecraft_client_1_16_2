package net.minecraft.world;

import java.net.MalformedURLException;
import java.util.UUID;
import com.google.common.collect.Maps;
import java.util.Timer;
import java.net.URL;
import java.util.Map;

public class Snooper {
    private final Map<String, Object> fixedData;
    private final Map<String, Object> dynamicData;
    private final String token;
    private final URL url;
    private final SnooperPopulator populator;
    private final Timer timer;
    private final Object lock;
    private final long startupTime;
    private boolean started;
    
    public Snooper(final String string, final SnooperPopulator apa, final long long3) {
        this.fixedData = (Map<String, Object>)Maps.newHashMap();
        this.dynamicData = (Map<String, Object>)Maps.newHashMap();
        this.token = UUID.randomUUID().toString();
        this.timer = new Timer("Snooper Timer", true);
        this.lock = new Object();
        try {
            this.url = new URL("http://snoop.minecraft.net/" + string + "?version=" + 2);
        }
        catch (MalformedURLException malformedURLException6) {
            throw new IllegalArgumentException();
        }
        this.populator = apa;
        this.startupTime = long3;
    }
    
    public void start() {
        if (!this.started) {}
    }
    
    public void prepare() {
        this.setFixedData("memory_total", Runtime.getRuntime().totalMemory());
        this.setFixedData("memory_max", Runtime.getRuntime().maxMemory());
        this.setFixedData("memory_free", Runtime.getRuntime().freeMemory());
        this.setFixedData("cpu_cores", Runtime.getRuntime().availableProcessors());
        this.populator.populateSnooper(this);
    }
    
    public void setDynamicData(final String string, final Object object) {
        synchronized (this.lock) {
            this.dynamicData.put(string, object);
        }
    }
    
    public void setFixedData(final String string, final Object object) {
        synchronized (this.lock) {
            this.fixedData.put(string, object);
        }
    }
    
    public boolean isStarted() {
        return this.started;
    }
    
    public void interrupt() {
        this.timer.cancel();
    }
    
    public String getToken() {
        return this.token;
    }
    
    public long getStartupTime() {
        return this.startupTime;
    }
}
