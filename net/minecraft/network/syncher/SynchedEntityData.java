package net.minecraft.network.syncher;

import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.collect.Maps;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.world.entity.Entity;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class SynchedEntityData {
    private static final Logger LOGGER;
    private static final Map<Class<? extends Entity>, Integer> ENTITY_ID_POOL;
    private final Entity entity;
    private final Map<Integer, DataItem<?>> itemsById;
    private final ReadWriteLock lock;
    private boolean isEmpty;
    private boolean isDirty;
    
    public SynchedEntityData(final Entity apx) {
        this.itemsById = (Map<Integer, DataItem<?>>)Maps.newHashMap();
        this.lock = (ReadWriteLock)new ReentrantReadWriteLock();
        this.isEmpty = true;
        this.entity = apx;
    }
    
    public static <T> EntityDataAccessor<T> defineId(final Class<? extends Entity> class1, final EntityDataSerializer<T> ut) {
        if (SynchedEntityData.LOGGER.isDebugEnabled()) {
            try {
                final Class<?> class2 = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
                if (!class2.equals(class1)) {
                    SynchedEntityData.LOGGER.debug("defineId called for: {} from {}", class1, class2, new RuntimeException());
                }
            }
            catch (ClassNotFoundException ex) {}
        }
        int integer3;
        if (SynchedEntityData.ENTITY_ID_POOL.containsKey(class1)) {
            integer3 = (int)SynchedEntityData.ENTITY_ID_POOL.get(class1) + 1;
        }
        else {
            int integer4 = 0;
            Class<?> class3 = class1;
            while (class3 != Entity.class) {
                class3 = class3.getSuperclass();
                if (SynchedEntityData.ENTITY_ID_POOL.containsKey(class3)) {
                    integer4 = (int)SynchedEntityData.ENTITY_ID_POOL.get(class3) + 1;
                    break;
                }
            }
            integer3 = integer4;
        }
        if (integer3 > 254) {
            throw new IllegalArgumentException(new StringBuilder().append("Data value id is too big with ").append(integer3).append("! (Max is ").append(254).append(")").toString());
        }
        SynchedEntityData.ENTITY_ID_POOL.put(class1, integer3);
        return ut.createAccessor(integer3);
    }
    
    public <T> void define(final EntityDataAccessor<T> us, final T object) {
        final int integer4 = us.getId();
        if (integer4 > 254) {
            throw new IllegalArgumentException(new StringBuilder().append("Data value id is too big with ").append(integer4).append("! (Max is ").append(254).append(")").toString());
        }
        if (this.itemsById.containsKey(integer4)) {
            throw new IllegalArgumentException(new StringBuilder().append("Duplicate id value for ").append(integer4).append("!").toString());
        }
        if (EntityDataSerializers.getSerializedId(us.getSerializer()) < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Unregistered serializer ").append(us.getSerializer()).append(" for ").append(integer4).append("!").toString());
        }
        this.createDataItem((EntityDataAccessor<Object>)us, object);
    }
    
    private <T> void createDataItem(final EntityDataAccessor<T> us, final T object) {
        final DataItem<T> a4 = new DataItem<T>(us, object);
        this.lock.writeLock().lock();
        this.itemsById.put(us.getId(), a4);
        this.isEmpty = false;
        this.lock.writeLock().unlock();
    }
    
    private <T> DataItem<T> getItem(final EntityDataAccessor<T> us) {
        this.lock.readLock().lock();
        DataItem<T> a3;
        try {
            a3 = (DataItem<T>)this.itemsById.get(us.getId());
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, "Getting synched entity data");
            final CrashReportCategory m6 = l5.addCategory("Synched entity data");
            m6.setDetail("Data ID", us);
            throw new ReportedException(l5);
        }
        finally {
            this.lock.readLock().unlock();
        }
        return a3;
    }
    
    public <T> T get(final EntityDataAccessor<T> us) {
        return this.<T>getItem(us).getValue();
    }
    
    public <T> void set(final EntityDataAccessor<T> us, final T object) {
        final DataItem<T> a4 = this.<T>getItem(us);
        if (ObjectUtils.notEqual(object, a4.getValue())) {
            a4.setValue(object);
            this.entity.onSyncedDataUpdated(us);
            a4.setDirty(true);
            this.isDirty = true;
        }
    }
    
    public boolean isDirty() {
        return this.isDirty;
    }
    
    public static void pack(final List<DataItem<?>> list, final FriendlyByteBuf nf) throws IOException {
        if (list != null) {
            for (int integer3 = 0, integer4 = list.size(); integer3 < integer4; ++integer3) {
                SynchedEntityData.writeDataItem(nf, (DataItem<Object>)list.get(integer3));
            }
        }
        nf.writeByte(255);
    }
    
    @Nullable
    public List<DataItem<?>> packDirty() {
        List<DataItem<?>> list2 = null;
        if (this.isDirty) {
            this.lock.readLock().lock();
            for (final DataItem<?> a4 : this.itemsById.values()) {
                if (a4.isDirty()) {
                    a4.setDirty(false);
                    if (list2 == null) {
                        list2 = (List<DataItem<?>>)Lists.newArrayList();
                    }
                    list2.add(a4.copy());
                }
            }
            this.lock.readLock().unlock();
        }
        this.isDirty = false;
        return list2;
    }
    
    @Nullable
    public List<DataItem<?>> getAll() {
        List<DataItem<?>> list2 = null;
        this.lock.readLock().lock();
        for (final DataItem<?> a4 : this.itemsById.values()) {
            if (list2 == null) {
                list2 = (List<DataItem<?>>)Lists.newArrayList();
            }
            list2.add(a4.copy());
        }
        this.lock.readLock().unlock();
        return list2;
    }
    
    private static <T> void writeDataItem(final FriendlyByteBuf nf, final DataItem<T> a) throws IOException {
        final EntityDataAccessor<T> us3 = a.getAccessor();
        final int integer4 = EntityDataSerializers.getSerializedId(us3.getSerializer());
        if (integer4 < 0) {
            throw new EncoderException(new StringBuilder().append("Unknown serializer type ").append(us3.getSerializer()).toString());
        }
        nf.writeByte(us3.getId());
        nf.writeVarInt(integer4);
        us3.getSerializer().write(nf, a.getValue());
    }
    
    @Nullable
    public static List<DataItem<?>> unpack(final FriendlyByteBuf nf) throws IOException {
        List<DataItem<?>> list2 = null;
        int integer3;
        while ((integer3 = nf.readUnsignedByte()) != 255) {
            if (list2 == null) {
                list2 = (List<DataItem<?>>)Lists.newArrayList();
            }
            final int integer4 = nf.readVarInt();
            final EntityDataSerializer<?> ut5 = EntityDataSerializers.getSerializer(integer4);
            if (ut5 == null) {
                throw new DecoderException(new StringBuilder().append("Unknown serializer type ").append(integer4).toString());
            }
            list2.add(SynchedEntityData.genericHelper(nf, integer3, ut5));
        }
        return list2;
    }
    
    private static <T> DataItem<T> genericHelper(final FriendlyByteBuf nf, final int integer, final EntityDataSerializer<T> ut) {
        return new DataItem<T>(ut.createAccessor(integer), ut.read(nf));
    }
    
    public void assignValues(final List<DataItem<?>> list) {
        this.lock.writeLock().lock();
        for (final DataItem<?> a4 : list) {
            final DataItem<?> a5 = this.itemsById.get(a4.getAccessor().getId());
            if (a5 != null) {
                this.assignValue(a5, a4);
                this.entity.onSyncedDataUpdated(a4.getAccessor());
            }
        }
        this.lock.writeLock().unlock();
        this.isDirty = true;
    }
    
    private <T> void assignValue(final DataItem<T> a1, final DataItem<?> a2) {
        if (!Objects.equals(((DataItem<Object>)a2).accessor.getSerializer(), ((DataItem<Object>)a1).accessor.getSerializer())) {
            throw new IllegalStateException(String.format("Invalid entity data item type for field %d on entity %s: old=%s(%s), new=%s(%s)", new Object[] { ((DataItem<Object>)a1).accessor.getId(), this.entity, ((DataItem<Object>)a1).value, ((DataItem<Object>)a1).value.getClass(), ((DataItem<Object>)a2).value, ((DataItem<Object>)a2).value.getClass() }));
        }
        a1.setValue((T)a2.getValue());
    }
    
    public boolean isEmpty() {
        return this.isEmpty;
    }
    
    public void clearDirty() {
        this.isDirty = false;
        this.lock.readLock().lock();
        for (final DataItem<?> a3 : this.itemsById.values()) {
            a3.setDirty(false);
        }
        this.lock.readLock().unlock();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ENTITY_ID_POOL = (Map)Maps.newHashMap();
    }
    
    public static class DataItem<T> {
        private final EntityDataAccessor<T> accessor;
        private T value;
        private boolean dirty;
        
        public DataItem(final EntityDataAccessor<T> us, final T object) {
            this.accessor = us;
            this.value = object;
            this.dirty = true;
        }
        
        public EntityDataAccessor<T> getAccessor() {
            return this.accessor;
        }
        
        public void setValue(final T object) {
            this.value = object;
        }
        
        public T getValue() {
            return this.value;
        }
        
        public boolean isDirty() {
            return this.dirty;
        }
        
        public void setDirty(final boolean boolean1) {
            this.dirty = boolean1;
        }
        
        public DataItem<T> copy() {
            return new DataItem<T>(this.accessor, this.accessor.getSerializer().copy(this.value));
        }
    }
}
