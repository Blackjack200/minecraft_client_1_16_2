package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.Util;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import java.io.File;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ServerList {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final List<ServerData> serverList;
    
    public ServerList(final Minecraft djw) {
        this.serverList = (List<ServerData>)Lists.newArrayList();
        this.minecraft = djw;
        this.load();
    }
    
    public void load() {
        try {
            this.serverList.clear();
            final CompoundTag md2 = NbtIo.read(new File(this.minecraft.gameDirectory, "servers.dat"));
            if (md2 == null) {
                return;
            }
            final ListTag mj3 = md2.getList("servers", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                this.serverList.add(ServerData.read(mj3.getCompound(integer4)));
            }
        }
        catch (Exception exception2) {
            ServerList.LOGGER.error("Couldn't load server list", (Throwable)exception2);
        }
    }
    
    public void save() {
        try {
            final ListTag mj2 = new ListTag();
            for (final ServerData dwr4 : this.serverList) {
                mj2.add(dwr4.write());
            }
            final CompoundTag md3 = new CompoundTag();
            md3.put("servers", (Tag)mj2);
            final File file4 = File.createTempFile("servers", ".dat", this.minecraft.gameDirectory);
            NbtIo.write(md3, file4);
            final File file5 = new File(this.minecraft.gameDirectory, "servers.dat_old");
            final File file6 = new File(this.minecraft.gameDirectory, "servers.dat");
            Util.safeReplaceFile(file6, file4, file5);
        }
        catch (Exception exception2) {
            ServerList.LOGGER.error("Couldn't save server list", (Throwable)exception2);
        }
    }
    
    public ServerData get(final int integer) {
        return (ServerData)this.serverList.get(integer);
    }
    
    public void remove(final ServerData dwr) {
        this.serverList.remove(dwr);
    }
    
    public void add(final ServerData dwr) {
        this.serverList.add(dwr);
    }
    
    public int size() {
        return this.serverList.size();
    }
    
    public void swap(final int integer1, final int integer2) {
        final ServerData dwr4 = this.get(integer1);
        this.serverList.set(integer1, this.get(integer2));
        this.serverList.set(integer2, dwr4);
        this.save();
    }
    
    public void replace(final int integer, final ServerData dwr) {
        this.serverList.set(integer, dwr);
    }
    
    public static void saveSingleServer(final ServerData dwr) {
        final ServerList dws2 = new ServerList(Minecraft.getInstance());
        dws2.load();
        for (int integer3 = 0; integer3 < dws2.size(); ++integer3) {
            final ServerData dwr2 = dws2.get(integer3);
            if (dwr2.name.equals(dwr.name) && dwr2.ip.equals(dwr.ip)) {
                dws2.replace(integer3, dwr);
                break;
            }
        }
        dws2.save();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
