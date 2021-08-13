package net.minecraft.world.entity.ai;

import com.mojang.serialization.Dynamic;
import org.apache.logging.log4j.LogManager;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import com.mojang.serialization.RecordBuilder;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import java.util.stream.Stream;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.Collection;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.ai.behavior.Behavior;
import java.util.Set;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import java.util.Optional;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Map;
import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.entity.LivingEntity;

public class Brain<E extends LivingEntity> {
    private static final Logger LOGGER;
    private final Supplier<Codec<Brain<E>>> codec;
    private final Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories;
    private final Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors;
    private final Map<Integer, Map<Activity, Set<Behavior<? super E>>>> availableBehaviorsByPriority;
    private Schedule schedule;
    private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements;
    private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped;
    private Set<Activity> coreActivities;
    private final Set<Activity> activeActivities;
    private Activity defaultActivity;
    private long lastScheduleUpdate;
    
    public static <E extends LivingEntity> Provider<E> provider(final Collection<? extends MemoryModuleType<?>> collection1, final Collection<? extends SensorType<? extends Sensor<? super E>>> collection2) {
        return new Provider<E>((Collection)collection1, (Collection)collection2);
    }
    
    public static <E extends LivingEntity> Codec<Brain<E>> codec(final Collection<? extends MemoryModuleType<?>> collection1, final Collection<? extends SensorType<? extends Sensor<? super E>>> collection2) {
        final MutableObject<Codec<Brain<E>>> mutableObject3 = (MutableObject<Codec<Brain<E>>>)new MutableObject();
        mutableObject3.setValue(new MapCodec<Brain<E>>() {
            public <T> Stream<T> keys(final DynamicOps<T> dynamicOps) {
                return (Stream<T>)collection1.stream().flatMap(aya -> Util.toStream((java.util.Optional<?>)aya.getCodec().map(codec -> Registry.MEMORY_MODULE_TYPE.getKey(aya)))).map(vk -> dynamicOps.createString(vk.toString()));
            }
            
            public <T> DataResult<Brain<E>> decode(final DynamicOps<T> dynamicOps, final MapLike<T> mapLike) {
                final MutableObject<DataResult<ImmutableList.Builder<MemoryValue<?>>>> mutableObject4 = (MutableObject<DataResult<ImmutableList.Builder<MemoryValue<?>>>>)new MutableObject((Object)DataResult.success((Object)ImmutableList.builder()));
                mapLike.entries().forEach(pair -> {
                    final DataResult<MemoryModuleType<?>> dataResult5 = (DataResult<MemoryModuleType<?>>)Registry.MEMORY_MODULE_TYPE.parse(dynamicOps, pair.getFirst());
                    final DataResult<? extends MemoryValue<?>> dataResult6 = dataResult5.flatMap(aya -> this.captureRead((MemoryModuleType<Object>)aya, (com.mojang.serialization.DynamicOps<Object>)dynamicOps, pair.getSecond()));
                    mutableObject4.setValue((Object)((DataResult)mutableObject4.getValue()).apply2(ImmutableList.Builder::add, (DataResult)dataResult6));
                });
                final ImmutableList<MemoryValue<?>> immutableList5 = (ImmutableList<MemoryValue<?>>)((DataResult)mutableObject4.getValue()).resultOrPartial(Brain.LOGGER::error).map(ImmutableList.Builder::build).orElseGet(ImmutableList::of);
                return (DataResult<Brain<E>>)DataResult.success((Object)new Brain(collection1, (java.util.Collection<? extends SensorType<? extends Sensor<? super LivingEntity>>>)collection2, immutableList5, (java.util.function.Supplier<com.mojang.serialization.Codec<Brain<LivingEntity>>>)mutableObject3::getValue));
            }
            
            private <T, U> DataResult<MemoryValue<U>> captureRead(final MemoryModuleType<U> aya, final DynamicOps<T> dynamicOps, final T object) {
                return (DataResult<MemoryValue<U>>)((DataResult)aya.getCodec().map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("No codec for memory: ").append((Object)aya).toString()))).flatMap(codec -> codec.parse(dynamicOps, object)).map(axz -> new MemoryValue(aya, Optional.of((Object)axz)));
            }
            
            public <T> RecordBuilder<T> encode(final Brain<E> arc, final DynamicOps<T> dynamicOps, final RecordBuilder<T> recordBuilder) {
                ((Brain<LivingEntity>)arc).memories().forEach(a -> a.serialize(dynamicOps, recordBuilder));
                return recordBuilder;
            }
        }.fieldOf("memories").codec());
        return (Codec<Brain<E>>)mutableObject3.getValue();
    }
    
    public Brain(final Collection<? extends MemoryModuleType<?>> collection1, final Collection<? extends SensorType<? extends Sensor<? super E>>> collection2, final ImmutableList<MemoryValue<?>> immutableList, final Supplier<Codec<Brain<E>>> supplier) {
        this.memories = (Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>>)Maps.newHashMap();
        this.sensors = (Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>>)Maps.newLinkedHashMap();
        this.availableBehaviorsByPriority = (Map<Integer, Map<Activity, Set<Behavior<? super E>>>>)Maps.newTreeMap();
        this.schedule = Schedule.EMPTY;
        this.activityRequirements = (Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>>)Maps.newHashMap();
        this.activityMemoriesToEraseWhenStopped = (Map<Activity, Set<MemoryModuleType<?>>>)Maps.newHashMap();
        this.coreActivities = (Set<Activity>)Sets.newHashSet();
        this.activeActivities = (Set<Activity>)Sets.newHashSet();
        this.defaultActivity = Activity.IDLE;
        this.lastScheduleUpdate = -9999L;
        this.codec = supplier;
        for (final MemoryModuleType<?> aya7 : collection1) {
            this.memories.put(aya7, Optional.empty());
        }
        for (final SensorType<? extends Sensor<? super E>> ayz7 : collection2) {
            this.sensors.put(ayz7, ayz7.create());
        }
        for (final Sensor<? super E> ayy7 : this.sensors.values()) {
            for (final MemoryModuleType<?> aya8 : ayy7.requires()) {
                this.memories.put(aya8, Optional.empty());
            }
        }
        for (final MemoryValue<?> a7 : immutableList) {
            ((MemoryValue<Object>)a7).setMemoryInternal(this);
        }
    }
    
    public <T> DataResult<T> serializeStart(final DynamicOps<T> dynamicOps) {
        return (DataResult<T>)((Codec)this.codec.get()).encodeStart((DynamicOps)dynamicOps, this);
    }
    
    private Stream<MemoryValue<?>> memories() {
        return (Stream<MemoryValue<?>>)this.memories.entrySet().stream().map(entry -> MemoryValue.createUnchecked((MemoryModuleType<Object>)entry.getKey(), entry.getValue()));
    }
    
    public boolean hasMemoryValue(final MemoryModuleType<?> aya) {
        return this.checkMemory(aya, MemoryStatus.VALUE_PRESENT);
    }
    
    public <U> void eraseMemory(final MemoryModuleType<U> aya) {
        this.<U>setMemory(aya, (java.util.Optional<? extends U>)Optional.empty());
    }
    
    public <U> void setMemory(final MemoryModuleType<U> aya, @Nullable final U object) {
        this.<U>setMemory(aya, (java.util.Optional<? extends U>)Optional.ofNullable(object));
    }
    
    public <U> void setMemoryWithExpiry(final MemoryModuleType<U> aya, final U object, final long long3) {
        this.<U>setMemoryInternal(aya, Optional.of(ExpirableValue.<U>of(object, long3)));
    }
    
    public <U> void setMemory(final MemoryModuleType<U> aya, final Optional<? extends U> optional) {
        this.setMemoryInternal((MemoryModuleType<Object>)aya, optional.map(ExpirableValue::of));
    }
    
    private <U> void setMemoryInternal(final MemoryModuleType<U> aya, final Optional<? extends ExpirableValue<?>> optional) {
        if (this.memories.containsKey(aya)) {
            if (optional.isPresent() && this.isEmptyCollection(((ExpirableValue)optional.get()).getValue())) {
                this.<U>eraseMemory(aya);
            }
            else {
                this.memories.put(aya, optional);
            }
        }
    }
    
    public <U> Optional<U> getMemory(final MemoryModuleType<U> aya) {
        return (Optional<U>)((Optional)this.memories.get(aya)).map(ExpirableValue::getValue);
    }
    
    public <U> boolean isMemoryValue(final MemoryModuleType<U> aya, final U object) {
        return this.hasMemoryValue(aya) && this.<U>getMemory(aya).filter(object2 -> object2.equals(object)).isPresent();
    }
    
    public boolean checkMemory(final MemoryModuleType<?> aya, final MemoryStatus ayb) {
        final Optional<? extends ExpirableValue<?>> optional4 = this.memories.get(aya);
        return optional4 != null && (ayb == MemoryStatus.REGISTERED || (ayb == MemoryStatus.VALUE_PRESENT && optional4.isPresent()) || (ayb == MemoryStatus.VALUE_ABSENT && !optional4.isPresent()));
    }
    
    public Schedule getSchedule() {
        return this.schedule;
    }
    
    public void setSchedule(final Schedule bhe) {
        this.schedule = bhe;
    }
    
    public void setCoreActivities(final Set<Activity> set) {
        this.coreActivities = set;
    }
    
    @Deprecated
    public List<Behavior<? super E>> getRunningBehaviors() {
        final List<Behavior<? super E>> list2 = (List<Behavior<? super E>>)new ObjectArrayList();
        for (final Map<Activity, Set<Behavior<? super E>>> map4 : this.availableBehaviorsByPriority.values()) {
            for (final Set<Behavior<? super E>> set6 : map4.values()) {
                for (final Behavior<? super E> ars8 : set6) {
                    if (ars8.getStatus() == Behavior.Status.RUNNING) {
                        list2.add(ars8);
                    }
                }
            }
        }
        return list2;
    }
    
    public void useDefaultActivity() {
        this.setActiveActivity(this.defaultActivity);
    }
    
    public Optional<Activity> getActiveNonCoreActivity() {
        for (final Activity bhc3 : this.activeActivities) {
            if (!this.coreActivities.contains(bhc3)) {
                return (Optional<Activity>)Optional.of(bhc3);
            }
        }
        return (Optional<Activity>)Optional.empty();
    }
    
    public void setActiveActivityIfPossible(final Activity bhc) {
        if (this.activityRequirementsAreMet(bhc)) {
            this.setActiveActivity(bhc);
        }
        else {
            this.useDefaultActivity();
        }
    }
    
    private void setActiveActivity(final Activity bhc) {
        if (this.isActive(bhc)) {
            return;
        }
        this.eraseMemoriesForOtherActivitesThan(bhc);
        this.activeActivities.clear();
        this.activeActivities.addAll((Collection)this.coreActivities);
        this.activeActivities.add(bhc);
    }
    
    private void eraseMemoriesForOtherActivitesThan(final Activity bhc) {
        for (final Activity bhc2 : this.activeActivities) {
            if (bhc2 != bhc) {
                final Set<MemoryModuleType<?>> set5 = (Set<MemoryModuleType<?>>)this.activityMemoriesToEraseWhenStopped.get(bhc2);
                if (set5 == null) {
                    continue;
                }
                for (final MemoryModuleType<?> aya7 : set5) {
                    this.eraseMemory(aya7);
                }
            }
        }
    }
    
    public void updateActivityFromSchedule(final long long1, final long long2) {
        if (long2 - this.lastScheduleUpdate > 20L) {
            this.lastScheduleUpdate = long2;
            final Activity bhc6 = this.getSchedule().getActivityAt((int)(long1 % 24000L));
            if (!this.activeActivities.contains(bhc6)) {
                this.setActiveActivityIfPossible(bhc6);
            }
        }
    }
    
    public void setActiveActivityToFirstValid(final List<Activity> list) {
        for (final Activity bhc4 : list) {
            if (this.activityRequirementsAreMet(bhc4)) {
                this.setActiveActivity(bhc4);
                break;
            }
        }
    }
    
    public void setDefaultActivity(final Activity bhc) {
        this.defaultActivity = bhc;
    }
    
    public void addActivity(final Activity bhc, final int integer, final ImmutableList<? extends Behavior<? super E>> immutableList) {
        this.addActivity(bhc, this.createPriorityPairs(integer, immutableList));
    }
    
    public void addActivityAndRemoveMemoryWhenStopped(final Activity bhc, final int integer, final ImmutableList<? extends Behavior<? super E>> immutableList, final MemoryModuleType<?> aya) {
        final Set<Pair<MemoryModuleType<?>, MemoryStatus>> set6 = (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(Pair.of((Object)aya, (Object)MemoryStatus.VALUE_PRESENT));
        final Set<MemoryModuleType<?>> set7 = (Set<MemoryModuleType<?>>)ImmutableSet.of(aya);
        this.addActivityAndRemoveMemoriesWhenStopped(bhc, this.createPriorityPairs(integer, immutableList), set6, set7);
    }
    
    public void addActivity(final Activity bhc, final ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> immutableList) {
        this.addActivityAndRemoveMemoriesWhenStopped(bhc, immutableList, (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(), (Set<MemoryModuleType<?>>)Sets.newHashSet());
    }
    
    public void addActivityWithConditions(final Activity bhc, final ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> immutableList, final Set<Pair<MemoryModuleType<?>, MemoryStatus>> set) {
        this.addActivityAndRemoveMemoriesWhenStopped(bhc, immutableList, set, (Set<MemoryModuleType<?>>)Sets.newHashSet());
    }
    
    private void addActivityAndRemoveMemoriesWhenStopped(final Activity bhc, final ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> immutableList, final Set<Pair<MemoryModuleType<?>, MemoryStatus>> set3, final Set<MemoryModuleType<?>> set4) {
        this.activityRequirements.put(bhc, set3);
        if (!set4.isEmpty()) {
            this.activityMemoriesToEraseWhenStopped.put(bhc, set4);
        }
        for (final Pair<Integer, ? extends Behavior<? super E>> pair7 : immutableList) {
            ((Set)((Map)this.availableBehaviorsByPriority.computeIfAbsent(pair7.getFirst(), integer -> Maps.newHashMap())).computeIfAbsent(bhc, bhc -> Sets.newLinkedHashSet())).add(pair7.getSecond());
        }
    }
    
    public boolean isActive(final Activity bhc) {
        return this.activeActivities.contains(bhc);
    }
    
    public Brain<E> copyWithoutBehaviors() {
        final Brain<E> arc2 = new Brain<E>(this.memories.keySet(), this.sensors.keySet(), (ImmutableList<MemoryValue<?>>)ImmutableList.of(), this.codec);
        for (final Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> entry4 : this.memories.entrySet()) {
            final MemoryModuleType<?> aya5 = entry4.getKey();
            if (((Optional)entry4.getValue()).isPresent()) {
                arc2.memories.put(aya5, entry4.getValue());
            }
        }
        return arc2;
    }
    
    public void tick(final ServerLevel aag, final E aqj) {
        this.forgetOutdatedMemories();
        this.tickSensors(aag, aqj);
        this.startEachNonRunningBehavior(aag, aqj);
        this.tickEachRunningBehavior(aag, aqj);
    }
    
    private void tickSensors(final ServerLevel aag, final E aqj) {
        for (final Sensor<? super E> ayy5 : this.sensors.values()) {
            ayy5.tick(aag, aqj);
        }
    }
    
    private void forgetOutdatedMemories() {
        for (final Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> entry3 : this.memories.entrySet()) {
            if (((Optional)entry3.getValue()).isPresent()) {
                final ExpirableValue<?> axz4 = ((Optional)entry3.getValue()).get();
                axz4.tick();
                if (!axz4.hasExpired()) {
                    continue;
                }
                this.eraseMemory((MemoryModuleType<Object>)entry3.getKey());
            }
        }
    }
    
    public void stopAll(final ServerLevel aag, final E aqj) {
        final long long4 = aqj.level.getGameTime();
        for (final Behavior<? super E> ars7 : this.getRunningBehaviors()) {
            ars7.doStop(aag, aqj, long4);
        }
    }
    
    private void startEachNonRunningBehavior(final ServerLevel aag, final E aqj) {
        final long long4 = aag.getGameTime();
        for (final Map<Activity, Set<Behavior<? super E>>> map7 : this.availableBehaviorsByPriority.values()) {
            for (final Map.Entry<Activity, Set<Behavior<? super E>>> entry9 : map7.entrySet()) {
                final Activity bhc10 = (Activity)entry9.getKey();
                if (this.activeActivities.contains(bhc10)) {
                    final Set<Behavior<? super E>> set11 = (Set<Behavior<? super E>>)entry9.getValue();
                    for (final Behavior<? super E> ars13 : set11) {
                        if (ars13.getStatus() == Behavior.Status.STOPPED) {
                            ars13.tryStart(aag, aqj, long4);
                        }
                    }
                }
            }
        }
    }
    
    private void tickEachRunningBehavior(final ServerLevel aag, final E aqj) {
        final long long4 = aag.getGameTime();
        for (final Behavior<? super E> ars7 : this.getRunningBehaviors()) {
            ars7.tickOrStop(aag, aqj, long4);
        }
    }
    
    private boolean activityRequirementsAreMet(final Activity bhc) {
        if (!this.activityRequirements.containsKey(bhc)) {
            return false;
        }
        for (final Pair<MemoryModuleType<?>, MemoryStatus> pair4 : (Set)this.activityRequirements.get(bhc)) {
            final MemoryModuleType<?> aya5 = pair4.getFirst();
            final MemoryStatus ayb6 = (MemoryStatus)pair4.getSecond();
            if (!this.checkMemory(aya5, ayb6)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isEmptyCollection(final Object object) {
        return object instanceof Collection && ((Collection)object).isEmpty();
    }
    
    ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> createPriorityPairs(final int integer, final ImmutableList<? extends Behavior<? super E>> immutableList) {
        int integer2 = integer;
        final ImmutableList.Builder<Pair<Integer, ? extends Behavior<? super E>>> builder5 = (ImmutableList.Builder<Pair<Integer, ? extends Behavior<? super E>>>)ImmutableList.builder();
        for (final Behavior<? super E> ars7 : immutableList) {
            builder5.add(Pair.of((Object)(integer2++), (Object)ars7));
        }
        return builder5.build();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static final class Provider<E extends LivingEntity> {
        private final Collection<? extends MemoryModuleType<?>> memoryTypes;
        private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
        private final Codec<Brain<E>> codec;
        
        private Provider(final Collection<? extends MemoryModuleType<?>> collection1, final Collection<? extends SensorType<? extends Sensor<? super E>>> collection2) {
            this.memoryTypes = collection1;
            this.sensorTypes = collection2;
            this.codec = Brain.<E>codec(collection1, collection2);
        }
        
        public Brain<E> makeBrain(final Dynamic<?> dynamic) {
            return (Brain<E>)this.codec.parse((Dynamic)dynamic).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain(this.memoryTypes, this.sensorTypes, (ImmutableList<MemoryValue<?>>)ImmutableList.of(), (Supplier<Codec<Brain<E>>>)(() -> this.codec)));
        }
    }
    
    static final class MemoryValue<U> {
        private final MemoryModuleType<U> type;
        private final Optional<? extends ExpirableValue<U>> value;
        
        private static <U> MemoryValue<U> createUnchecked(final MemoryModuleType<U> aya, final Optional<? extends ExpirableValue<?>> optional) {
            return new MemoryValue<U>(aya, (java.util.Optional<? extends ExpirableValue<U>>)optional);
        }
        
        private MemoryValue(final MemoryModuleType<U> aya, final Optional<? extends ExpirableValue<U>> optional) {
            this.type = aya;
            this.value = optional;
        }
        
        private void setMemoryInternal(final Brain<?> arc) {
            ((Brain<LivingEntity>)arc).setMemoryInternal(this.type, this.value);
        }
        
        public <T> void serialize(final DynamicOps<T> dynamicOps, final RecordBuilder<T> recordBuilder) {
            this.type.getCodec().ifPresent(codec -> this.value.ifPresent(axz -> recordBuilder.add(Registry.MEMORY_MODULE_TYPE.encodeStart(dynamicOps, this.type), codec.encodeStart(dynamicOps, axz))));
        }
    }
}
