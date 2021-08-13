package net.minecraft.util.monitoring.jmx;

import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;
import javax.management.AttributeList;
import javax.management.Attribute;
import javax.annotation.Nullable;
import javax.management.JMException;
import javax.management.NotCompliantMBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanAttributeInfo;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Map;
import javax.management.MBeanInfo;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import javax.management.DynamicMBean;

public final class MinecraftServerStatistics implements DynamicMBean {
    private static final Logger LOGGER;
    private final MinecraftServer server;
    private final MBeanInfo mBeanInfo;
    private final Map<String, AttributeDescription> attributeDescriptionByName;
    
    private MinecraftServerStatistics(final MinecraftServer minecraftServer) {
        this.attributeDescriptionByName = (Map<String, AttributeDescription>)Stream.of((Object[])new AttributeDescription[] { new AttributeDescription("tickTimes", this::getTickTimes, "Historical tick times (ms)", (Class)long[].class), new AttributeDescription("averageTickTime", this::getAverageTickTime, "Current average tick time (ms)", Long.TYPE) }).collect(Collectors.toMap(a -> a.name, Function.identity()));
        this.server = minecraftServer;
        final MBeanAttributeInfo[] arr3 = (MBeanAttributeInfo[])this.attributeDescriptionByName.values().stream().map(object -> ((AttributeDescription)object).asMBeanAttributeInfo()).toArray(MBeanAttributeInfo[]::new);
        this.mBeanInfo = new MBeanInfo(MinecraftServerStatistics.class.getSimpleName(), "metrics for dedicated server", arr3, (MBeanConstructorInfo[])null, (MBeanOperationInfo[])null, new MBeanNotificationInfo[0]);
    }
    
    public static void registerJmxMonitoring(final MinecraftServer minecraftServer) {
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(new MinecraftServerStatistics(minecraftServer), new ObjectName("net.minecraft.server:type=Server"));
        }
        catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex2) {
            final JMException ex;
            final JMException jMException2 = ex;
            MinecraftServerStatistics.LOGGER.warn("Failed to initialise server as JMX bean", (Throwable)jMException2);
        }
    }
    
    private float getAverageTickTime() {
        return this.server.getAverageTickTime();
    }
    
    private long[] getTickTimes() {
        return this.server.tickTimes;
    }
    
    @Nullable
    public Object getAttribute(final String string) {
        final AttributeDescription a3 = (AttributeDescription)this.attributeDescriptionByName.get(string);
        return (a3 == null) ? null : a3.getter.get();
    }
    
    public void setAttribute(final Attribute attribute) {
    }
    
    public AttributeList getAttributes(final String[] arr) {
        final List<Attribute> list3 = (List<Attribute>)Arrays.stream((Object[])arr).map(this.attributeDescriptionByName::get).filter(Objects::nonNull).map(a -> new Attribute(a.name, a.getter.get())).collect(Collectors.toList());
        return new AttributeList((List)list3);
    }
    
    public AttributeList setAttributes(final AttributeList attributeList) {
        return new AttributeList();
    }
    
    @Nullable
    public Object invoke(final String string, final Object[] arr, final String[] arr) {
        return null;
    }
    
    public MBeanInfo getMBeanInfo() {
        return this.mBeanInfo;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static final class AttributeDescription {
        private final String name;
        private final Supplier<Object> getter;
        private final String description;
        private final Class<?> type;
        
        private AttributeDescription(final String string1, final Supplier<Object> supplier, final String string3, final Class<?> class4) {
            this.name = string1;
            this.getter = supplier;
            this.description = string3;
            this.type = class4;
        }
        
        private MBeanAttributeInfo asMBeanAttributeInfo() {
            return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
        }
    }
}
