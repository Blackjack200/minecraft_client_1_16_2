package net.minecraft.server.players;

import org.apache.logging.log4j.LogManager;
import java.text.ParseException;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.server.dedicated.DedicatedServer;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.util.StringUtil;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import java.util.Date;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class OldUsersConverter {
    private static final Logger LOGGER;
    public static final File OLD_IPBANLIST;
    public static final File OLD_USERBANLIST;
    public static final File OLD_OPLIST;
    public static final File OLD_WHITELIST;
    
    static List<String> readOldListFormat(final File file, final Map<String, String[]> map) throws IOException {
        final List<String> list3 = (List<String>)Files.readLines(file, StandardCharsets.UTF_8);
        for (String string5 : list3) {
            string5 = string5.trim();
            if (!string5.startsWith("#")) {
                if (string5.length() < 1) {
                    continue;
                }
                final String[] arr6 = string5.split("\\|");
                map.put(arr6[0].toLowerCase(Locale.ROOT), arr6);
            }
        }
        return list3;
    }
    
    private static void lookupPlayers(final MinecraftServer minecraftServer, final Collection<String> collection, final ProfileLookupCallback profileLookupCallback) {
        final String[] arr4 = (String[])collection.stream().filter(string -> !StringUtil.isNullOrEmpty(string)).toArray(String[]::new);
        if (minecraftServer.usesAuthentication()) {
            minecraftServer.getProfileRepository().findProfilesByNames(arr4, Agent.MINECRAFT, profileLookupCallback);
        }
        else {
            for (final String string8 : arr4) {
                final UUID uUID9 = Player.createPlayerUUID(new GameProfile((UUID)null, string8));
                final GameProfile gameProfile10 = new GameProfile(uUID9, string8);
                profileLookupCallback.onProfileLookupSucceeded(gameProfile10);
            }
        }
    }
    
    public static boolean convertUserBanlist(final MinecraftServer minecraftServer) {
        final UserBanList acx2 = new UserBanList(PlayerList.USERBANLIST_FILE);
        if (OldUsersConverter.OLD_USERBANLIST.exists() && OldUsersConverter.OLD_USERBANLIST.isFile()) {
            if (acx2.getFile().exists()) {
                try {
                    acx2.load();
                }
                catch (IOException iOException3) {
                    OldUsersConverter.LOGGER.warn("Could not load existing file {}", acx2.getFile().getName(), iOException3);
                }
            }
            try {
                final Map<String, String[]> map3 = (Map<String, String[]>)Maps.newHashMap();
                readOldListFormat(OldUsersConverter.OLD_USERBANLIST, map3);
                final ProfileLookupCallback profileLookupCallback4 = (ProfileLookupCallback)new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                        minecraftServer.getProfileCache().add(gameProfile);
                        final String[] arr3 = (String[])map3.get(gameProfile.getName().toLowerCase(Locale.ROOT));
                        if (arr3 == null) {
                            OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", gameProfile.getName());
                            throw new ConversionError("Profile not in the conversionlist");
                        }
                        final Date date4 = (arr3.length > 1) ? parseDate(arr3[1], null) : null;
                        final String string5 = (arr3.length > 2) ? arr3[2] : null;
                        final Date date5 = (arr3.length > 3) ? parseDate(arr3[3], null) : null;
                        final String string6 = (arr3.length > 4) ? arr3[4] : null;
                        ((StoredUserList<K, UserBanListEntry>)acx2).add(new UserBanListEntry(gameProfile, date4, string5, date5, string6));
                    }
                    
                    public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                        OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", gameProfile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new ConversionError("Could not request user " + gameProfile.getName() + " from backend systems", (Throwable)exception);
                        }
                    }
                };
                lookupPlayers(minecraftServer, (Collection<String>)map3.keySet(), profileLookupCallback4);
                acx2.save();
                renameOldFile(OldUsersConverter.OLD_USERBANLIST);
            }
            catch (IOException iOException3) {
                OldUsersConverter.LOGGER.warn("Could not read old user banlist to convert it!", (Throwable)iOException3);
                return false;
            }
            catch (ConversionError a3) {
                OldUsersConverter.LOGGER.error("Conversion failed, please try again later", (Throwable)a3);
                return false;
            }
            return true;
        }
        return true;
    }
    
    public static boolean convertIpBanlist(final MinecraftServer minecraftServer) {
        final IpBanList acp2 = new IpBanList(PlayerList.IPBANLIST_FILE);
        if (OldUsersConverter.OLD_IPBANLIST.exists() && OldUsersConverter.OLD_IPBANLIST.isFile()) {
            if (acp2.getFile().exists()) {
                try {
                    acp2.load();
                }
                catch (IOException iOException3) {
                    OldUsersConverter.LOGGER.warn("Could not load existing file {}", acp2.getFile().getName(), iOException3);
                }
            }
            try {
                final Map<String, String[]> map3 = (Map<String, String[]>)Maps.newHashMap();
                readOldListFormat(OldUsersConverter.OLD_IPBANLIST, map3);
                for (final String string5 : map3.keySet()) {
                    final String[] arr6 = (String[])map3.get(string5);
                    final Date date7 = (arr6.length > 1) ? parseDate(arr6[1], null) : null;
                    final String string6 = (arr6.length > 2) ? arr6[2] : null;
                    final Date date8 = (arr6.length > 3) ? parseDate(arr6[3], null) : null;
                    final String string7 = (arr6.length > 4) ? arr6[4] : null;
                    ((StoredUserList<K, IpBanListEntry>)acp2).add(new IpBanListEntry(string5, date7, string6, date8, string7));
                }
                acp2.save();
                renameOldFile(OldUsersConverter.OLD_IPBANLIST);
            }
            catch (IOException iOException3) {
                OldUsersConverter.LOGGER.warn("Could not parse old ip banlist to convert it!", (Throwable)iOException3);
                return false;
            }
            return true;
        }
        return true;
    }
    
    public static boolean convertOpsList(final MinecraftServer minecraftServer) {
        final ServerOpList act2 = new ServerOpList(PlayerList.OPLIST_FILE);
        if (OldUsersConverter.OLD_OPLIST.exists() && OldUsersConverter.OLD_OPLIST.isFile()) {
            if (act2.getFile().exists()) {
                try {
                    act2.load();
                }
                catch (IOException iOException3) {
                    OldUsersConverter.LOGGER.warn("Could not load existing file {}", act2.getFile().getName(), iOException3);
                }
            }
            try {
                final List<String> list3 = (List<String>)Files.readLines(OldUsersConverter.OLD_OPLIST, StandardCharsets.UTF_8);
                final ProfileLookupCallback profileLookupCallback4 = (ProfileLookupCallback)new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                        minecraftServer.getProfileCache().add(gameProfile);
                        ((StoredUserList<K, ServerOpListEntry>)act2).add(new ServerOpListEntry(gameProfile, minecraftServer.getOperatorUserPermissionLevel(), false));
                    }
                    
                    public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                        OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", gameProfile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new ConversionError("Could not request user " + gameProfile.getName() + " from backend systems", (Throwable)exception);
                        }
                    }
                };
                lookupPlayers(minecraftServer, (Collection<String>)list3, profileLookupCallback4);
                act2.save();
                renameOldFile(OldUsersConverter.OLD_OPLIST);
            }
            catch (IOException iOException3) {
                OldUsersConverter.LOGGER.warn("Could not read old oplist to convert it!", (Throwable)iOException3);
                return false;
            }
            catch (ConversionError a3) {
                OldUsersConverter.LOGGER.error("Conversion failed, please try again later", (Throwable)a3);
                return false;
            }
            return true;
        }
        return true;
    }
    
    public static boolean convertWhiteList(final MinecraftServer minecraftServer) {
        final UserWhiteList acz2 = new UserWhiteList(PlayerList.WHITELIST_FILE);
        if (OldUsersConverter.OLD_WHITELIST.exists() && OldUsersConverter.OLD_WHITELIST.isFile()) {
            if (acz2.getFile().exists()) {
                try {
                    acz2.load();
                }
                catch (IOException iOException3) {
                    OldUsersConverter.LOGGER.warn("Could not load existing file {}", acz2.getFile().getName(), iOException3);
                }
            }
            try {
                final List<String> list3 = (List<String>)Files.readLines(OldUsersConverter.OLD_WHITELIST, StandardCharsets.UTF_8);
                final ProfileLookupCallback profileLookupCallback4 = (ProfileLookupCallback)new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                        minecraftServer.getProfileCache().add(gameProfile);
                        ((StoredUserList<K, UserWhiteListEntry>)acz2).add(new UserWhiteListEntry(gameProfile));
                    }
                    
                    public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                        OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", gameProfile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new ConversionError("Could not request user " + gameProfile.getName() + " from backend systems", (Throwable)exception);
                        }
                    }
                };
                lookupPlayers(minecraftServer, (Collection<String>)list3, profileLookupCallback4);
                acz2.save();
                renameOldFile(OldUsersConverter.OLD_WHITELIST);
            }
            catch (IOException iOException3) {
                OldUsersConverter.LOGGER.warn("Could not read old whitelist to convert it!", (Throwable)iOException3);
                return false;
            }
            catch (ConversionError a3) {
                OldUsersConverter.LOGGER.error("Conversion failed, please try again later", (Throwable)a3);
                return false;
            }
            return true;
        }
        return true;
    }
    
    @Nullable
    public static UUID convertMobOwnerIfNecessary(final MinecraftServer minecraftServer, final String string) {
        Label_0024: {
            if (!StringUtil.isNullOrEmpty(string)) {
                if (string.length() <= 16) {
                    break Label_0024;
                }
            }
            try {
                return UUID.fromString(string);
            }
            catch (IllegalArgumentException illegalArgumentException3) {
                return null;
            }
        }
        final GameProfile gameProfile3 = minecraftServer.getProfileCache().get(string);
        if (gameProfile3 != null && gameProfile3.getId() != null) {
            return gameProfile3.getId();
        }
        if (minecraftServer.isSingleplayer() || !minecraftServer.usesAuthentication()) {
            return Player.createPlayerUUID(new GameProfile((UUID)null, string));
        }
        final List<GameProfile> list4 = (List<GameProfile>)Lists.newArrayList();
        final ProfileLookupCallback profileLookupCallback5 = (ProfileLookupCallback)new ProfileLookupCallback() {
            public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                minecraftServer.getProfileCache().add(gameProfile);
                list4.add(gameProfile);
            }
            
            public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", gameProfile.getName(), exception);
            }
        };
        lookupPlayers(minecraftServer, (Collection<String>)Lists.newArrayList((Object[])new String[] { string }), profileLookupCallback5);
        if (!list4.isEmpty() && ((GameProfile)list4.get(0)).getId() != null) {
            return ((GameProfile)list4.get(0)).getId();
        }
        return null;
    }
    
    public static boolean convertPlayers(final DedicatedServer zg) {
        final File file2 = getWorldPlayersDirectory(zg);
        final File file3 = new File(file2.getParentFile(), "playerdata");
        final File file4 = new File(file2.getParentFile(), "unknownplayers");
        if (!file2.exists() || !file2.isDirectory()) {
            return true;
        }
        final File[] arr5 = file2.listFiles();
        final List<String> list6 = (List<String>)Lists.newArrayList();
        for (final File file5 : arr5) {
            final String string11 = file5.getName();
            if (string11.toLowerCase(Locale.ROOT).endsWith(".dat")) {
                final String string12 = string11.substring(0, string11.length() - ".dat".length());
                if (!string12.isEmpty()) {
                    list6.add(string12);
                }
            }
        }
        try {
            final String[] arr6 = (String[])list6.toArray((Object[])new String[list6.size()]);
            final ProfileLookupCallback profileLookupCallback8 = (ProfileLookupCallback)new ProfileLookupCallback() {
                public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                    zg.getProfileCache().add(gameProfile);
                    final UUID uUID3 = gameProfile.getId();
                    if (uUID3 == null) {
                        throw new ConversionError("Missing UUID for user profile " + gameProfile.getName());
                    }
                    this.movePlayerFile(file3, this.getFileNameForProfile(gameProfile), uUID3.toString());
                }
                
                public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                    OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", gameProfile.getName(), exception);
                    if (exception instanceof ProfileNotFoundException) {
                        final String string4 = this.getFileNameForProfile(gameProfile);
                        this.movePlayerFile(file4, string4, string4);
                        return;
                    }
                    throw new ConversionError("Could not request user " + gameProfile.getName() + " from backend systems", (Throwable)exception);
                }
                
                private void movePlayerFile(final File file, final String string2, final String string3) {
                    final File file2 = new File(file2, string2 + ".dat");
                    final File file3 = new File(file, string3 + ".dat");
                    ensureDirectoryExists(file);
                    if (!file2.renameTo(file3)) {
                        throw new ConversionError("Could not convert file for " + string2);
                    }
                }
                
                private String getFileNameForProfile(final GameProfile gameProfile) {
                    String string3 = null;
                    for (final String string4 : arr6) {
                        if (string4 != null && string4.equalsIgnoreCase(gameProfile.getName())) {
                            string3 = string4;
                            break;
                        }
                    }
                    if (string3 == null) {
                        throw new ConversionError("Could not find the filename for " + gameProfile.getName() + " anymore");
                    }
                    return string3;
                }
            };
            lookupPlayers(zg, (Collection<String>)Lists.newArrayList((Object[])arr6), profileLookupCallback8);
        }
        catch (ConversionError a7) {
            OldUsersConverter.LOGGER.error("Conversion failed, please try again later", (Throwable)a7);
            return false;
        }
        return true;
    }
    
    private static void ensureDirectoryExists(final File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return;
            }
            throw new ConversionError("Can't create directory " + file.getName() + " in world save directory.");
        }
        else if (!file.mkdirs()) {
            throw new ConversionError("Can't create directory " + file.getName() + " in world save directory.");
        }
    }
    
    public static boolean serverReadyAfterUserconversion(final MinecraftServer minecraftServer) {
        boolean boolean2 = areOldUserlistsRemoved();
        boolean2 = (boolean2 && areOldPlayersConverted(minecraftServer));
        return boolean2;
    }
    
    private static boolean areOldUserlistsRemoved() {
        boolean boolean1 = false;
        if (OldUsersConverter.OLD_USERBANLIST.exists() && OldUsersConverter.OLD_USERBANLIST.isFile()) {
            boolean1 = true;
        }
        boolean boolean2 = false;
        if (OldUsersConverter.OLD_IPBANLIST.exists() && OldUsersConverter.OLD_IPBANLIST.isFile()) {
            boolean2 = true;
        }
        boolean boolean3 = false;
        if (OldUsersConverter.OLD_OPLIST.exists() && OldUsersConverter.OLD_OPLIST.isFile()) {
            boolean3 = true;
        }
        boolean boolean4 = false;
        if (OldUsersConverter.OLD_WHITELIST.exists() && OldUsersConverter.OLD_WHITELIST.isFile()) {
            boolean4 = true;
        }
        if (boolean1 || boolean2 || boolean3 || boolean4) {
            OldUsersConverter.LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
            OldUsersConverter.LOGGER.warn("** please remove the following files and restart the server:");
            if (boolean1) {
                OldUsersConverter.LOGGER.warn("* {}", OldUsersConverter.OLD_USERBANLIST.getName());
            }
            if (boolean2) {
                OldUsersConverter.LOGGER.warn("* {}", OldUsersConverter.OLD_IPBANLIST.getName());
            }
            if (boolean3) {
                OldUsersConverter.LOGGER.warn("* {}", OldUsersConverter.OLD_OPLIST.getName());
            }
            if (boolean4) {
                OldUsersConverter.LOGGER.warn("* {}", OldUsersConverter.OLD_WHITELIST.getName());
            }
            return false;
        }
        return true;
    }
    
    private static boolean areOldPlayersConverted(final MinecraftServer minecraftServer) {
        final File file2 = getWorldPlayersDirectory(minecraftServer);
        if (file2.exists() && file2.isDirectory() && (file2.list().length > 0 || !file2.delete())) {
            OldUsersConverter.LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
            OldUsersConverter.LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
            OldUsersConverter.LOGGER.warn("** please restart the server and if the problem persists, remove the directory '{}'", file2.getPath());
            return false;
        }
        return true;
    }
    
    private static File getWorldPlayersDirectory(final MinecraftServer minecraftServer) {
        return minecraftServer.getWorldPath(LevelResource.PLAYER_OLD_DATA_DIR).toFile();
    }
    
    private static void renameOldFile(final File file) {
        final File file2 = new File(file.getName() + ".converted");
        file.renameTo(file2);
    }
    
    private static Date parseDate(final String string, final Date date) {
        Date date2;
        try {
            date2 = BanListEntry.DATE_FORMAT.parse(string);
        }
        catch (ParseException parseException4) {
            date2 = date;
        }
        return date2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        OLD_IPBANLIST = new File("banned-ips.txt");
        OLD_USERBANLIST = new File("banned-players.txt");
        OLD_OPLIST = new File("ops.txt");
        OLD_WHITELIST = new File("white-list.txt");
    }
    
    static class ConversionError extends RuntimeException {
        private ConversionError(final String string, final Throwable throwable) {
            super(string, throwable);
        }
        
        private ConversionError(final String string) {
            super(string);
        }
    }
}
