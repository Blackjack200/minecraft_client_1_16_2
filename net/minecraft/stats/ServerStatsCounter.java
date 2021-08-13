package net.minecraft.stats;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.SharedConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.Tag;
import java.util.Map;
import com.google.gson.JsonObject;
import java.util.Optional;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import com.google.gson.internal.Streams;
import java.io.Reader;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import com.mojang.datafixers.DataFixer;
import net.minecraft.world.entity.player.Player;
import com.google.gson.JsonParseException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Sets;
import java.util.Set;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class ServerStatsCounter extends StatsCounter {
    private static final Logger LOGGER;
    private final MinecraftServer server;
    private final File file;
    private final Set<Stat<?>> dirty;
    private int lastStatRequest;
    
    public ServerStatsCounter(final MinecraftServer minecraftServer, final File file) {
        this.dirty = (Set<Stat<?>>)Sets.newHashSet();
        this.lastStatRequest = -300;
        this.server = minecraftServer;
        this.file = file;
        if (file.isFile()) {
            try {
                this.parseLocal(minecraftServer.getFixerUpper(), FileUtils.readFileToString(file));
            }
            catch (IOException iOException4) {
                ServerStatsCounter.LOGGER.error("Couldn't read statistics file {}", file, iOException4);
            }
            catch (JsonParseException jsonParseException4) {
                ServerStatsCounter.LOGGER.error("Couldn't parse statistics file {}", file, jsonParseException4);
            }
        }
    }
    
    public void save() {
        try {
            FileUtils.writeStringToFile(this.file, this.toJson());
        }
        catch (IOException iOException2) {
            ServerStatsCounter.LOGGER.error("Couldn't save stats", (Throwable)iOException2);
        }
    }
    
    @Override
    public void setValue(final Player bft, final Stat<?> adv, final int integer) {
        super.setValue(bft, adv, integer);
        this.dirty.add(adv);
    }
    
    private Set<Stat<?>> getDirty() {
        final Set<Stat<?>> set2 = (Set<Stat<?>>)Sets.newHashSet((Iterable)this.dirty);
        this.dirty.clear();
        return set2;
    }
    
    public void parseLocal(final DataFixer dataFixer, final String string) {
        try (final JsonReader jsonReader4 = new JsonReader((Reader)new StringReader(string))) {
            jsonReader4.setLenient(false);
            final JsonElement jsonElement6 = Streams.parse(jsonReader4);
            if (jsonElement6.isJsonNull()) {
                ServerStatsCounter.LOGGER.error("Unable to parse Stat data from {}", this.file);
                return;
            }
            CompoundTag md7 = fromJson(jsonElement6.getAsJsonObject());
            if (!md7.contains("DataVersion", 99)) {
                md7.putInt("DataVersion", 1343);
            }
            md7 = NbtUtils.update(dataFixer, DataFixTypes.STATS, md7, md7.getInt("DataVersion"));
            if (md7.contains("stats", 10)) {
                final CompoundTag md8 = md7.getCompound("stats");
                for (final String string2 : md8.getAllKeys()) {
                    if (md8.contains(string2, 10)) {
                        Util.<StatType<?>>ifElse(Registry.STAT_TYPE.getOptional(new ResourceLocation(string2)), (java.util.function.Consumer<StatType<?>>)(adx -> {
                            final CompoundTag md2 = md8.getCompound(string2);
                            for (final String string2 : md2.getAllKeys()) {
                                if (md2.contains(string2, 99)) {
                                    Util.<Stat<Object>>ifElse(this.getStat((StatType<Object>)adx, string2), (java.util.function.Consumer<Stat<Object>>)(adv -> this.stats.put(adv, md2.getInt(string2))), () -> ServerStatsCounter.LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.file, string2));
                                }
                                else {
                                    ServerStatsCounter.LOGGER.warn("Invalid statistic value in {}: Don't know what {} is for key {}", this.file, md2.get(string2), string2);
                                }
                            }
                        }), () -> ServerStatsCounter.LOGGER.warn("Invalid statistic type in {}: Don't know what {} is", this.file, string2));
                    }
                }
            }
        }
        catch (JsonParseException | IOException ex2) {
            final Exception ex;
            final Exception exception4 = ex;
            ServerStatsCounter.LOGGER.error("Unable to parse Stat data from {}", this.file, exception4);
        }
    }
    
    private <T> Optional<Stat<T>> getStat(final StatType<T> adx, final String string) {
        return (Optional<Stat<T>>)Optional.ofNullable(ResourceLocation.tryParse(string)).flatMap(adx.getRegistry()::getOptional).map(adx::get);
    }
    
    private static CompoundTag fromJson(final JsonObject jsonObject) {
        final CompoundTag md2 = new CompoundTag();
        for (final Map.Entry<String, JsonElement> entry4 : jsonObject.entrySet()) {
            final JsonElement jsonElement5 = (JsonElement)entry4.getValue();
            if (jsonElement5.isJsonObject()) {
                md2.put((String)entry4.getKey(), fromJson(jsonElement5.getAsJsonObject()));
            }
            else {
                if (!jsonElement5.isJsonPrimitive()) {
                    continue;
                }
                final JsonPrimitive jsonPrimitive6 = jsonElement5.getAsJsonPrimitive();
                if (!jsonPrimitive6.isNumber()) {
                    continue;
                }
                md2.putInt((String)entry4.getKey(), jsonPrimitive6.getAsInt());
            }
        }
        return md2;
    }
    
    protected String toJson() {
        final Map<StatType<?>, JsonObject> map2 = (Map<StatType<?>, JsonObject>)Maps.newHashMap();
        for (final Object2IntMap.Entry<Stat<?>> entry4 : this.stats.object2IntEntrySet()) {
            final Stat<?> adv5 = entry4.getKey();
            ((JsonObject)map2.computeIfAbsent(adv5.getType(), adx -> new JsonObject())).addProperty(ServerStatsCounter.getKey(adv5).toString(), (Number)entry4.getIntValue());
        }
        final JsonObject jsonObject3 = new JsonObject();
        for (final Map.Entry<StatType<?>, JsonObject> entry5 : map2.entrySet()) {
            jsonObject3.add(Registry.STAT_TYPE.getKey(entry5.getKey()).toString(), (JsonElement)entry5.getValue());
        }
        final JsonObject jsonObject4 = new JsonObject();
        jsonObject4.add("stats", (JsonElement)jsonObject3);
        jsonObject4.addProperty("DataVersion", (Number)SharedConstants.getCurrentVersion().getWorldVersion());
        return jsonObject4.toString();
    }
    
    private static <T> ResourceLocation getKey(final Stat<T> adv) {
        return adv.getType().getRegistry().getKey(adv.getValue());
    }
    
    public void markAllDirty() {
        this.dirty.addAll((Collection)this.stats.keySet());
    }
    
    public void sendStats(final ServerPlayer aah) {
        final int integer3 = this.server.getTickCount();
        final Object2IntMap<Stat<?>> object2IntMap4 = (Object2IntMap<Stat<?>>)new Object2IntOpenHashMap();
        if (integer3 - this.lastStatRequest > 300) {
            this.lastStatRequest = integer3;
            for (final Stat<?> adv6 : this.getDirty()) {
                object2IntMap4.put(adv6, this.getValue(adv6));
            }
        }
        aah.connection.send(new ClientboundAwardStatsPacket(object2IntMap4));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
