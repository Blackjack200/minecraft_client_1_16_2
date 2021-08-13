package net.minecraft.client.main;

import org.apache.logging.log4j.LogManager;
import joptsimple.ArgumentAcceptingOptionSpec;
import javax.annotation.Nullable;
import com.google.gson.Gson;
import java.util.OptionalInt;
import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.client.Options;
import net.minecraft.client.resources.language.LanguageManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.DisplayData;
import net.minecraft.client.User;
import net.minecraft.server.Bootstrap;
import net.minecraft.CrashReport;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.GsonHelper;
import java.lang.reflect.Type;
import com.mojang.authlib.properties.PropertyMap;
import com.google.gson.GsonBuilder;
import java.net.PasswordAuthentication;
import java.net.Authenticator;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import joptsimple.OptionSpec;
import net.minecraft.Util;
import java.io.File;
import joptsimple.OptionParser;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER;
    
    public static void main(final String[] arr) {
        final OptionParser optionParser2 = new OptionParser();
        optionParser2.allowsUnrecognizedOptions();
        optionParser2.accepts("demo");
        optionParser2.accepts("disableMultiplayer");
        optionParser2.accepts("disableChat");
        optionParser2.accepts("fullscreen");
        optionParser2.accepts("checkGlErrors");
        final OptionSpec<String> optionSpec3 = (OptionSpec<String>)optionParser2.accepts("server").withRequiredArg();
        final OptionSpec<Integer> optionSpec4 = (OptionSpec<Integer>)optionParser2.accepts("port").withRequiredArg().ofType((Class)Integer.class).defaultsTo(25565, (Object[])new Integer[0]);
        final OptionSpec<File> optionSpec5 = (OptionSpec<File>)optionParser2.accepts("gameDir").withRequiredArg().ofType((Class)File.class).defaultsTo(new File("."), (Object[])new File[0]);
        final OptionSpec<File> optionSpec6 = (OptionSpec<File>)optionParser2.accepts("assetsDir").withRequiredArg().ofType((Class)File.class);
        final OptionSpec<File> optionSpec7 = (OptionSpec<File>)optionParser2.accepts("resourcePackDir").withRequiredArg().ofType((Class)File.class);
        final OptionSpec<File> optionSpec8 = (OptionSpec<File>)optionParser2.accepts("dataPackDir").withRequiredArg().ofType((Class)File.class);
        final OptionSpec<String> optionSpec9 = (OptionSpec<String>)optionParser2.accepts("proxyHost").withRequiredArg();
        final OptionSpec<Integer> optionSpec10 = (OptionSpec<Integer>)optionParser2.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType((Class)Integer.class);
        final OptionSpec<String> optionSpec11 = (OptionSpec<String>)optionParser2.accepts("proxyUser").withRequiredArg();
        final OptionSpec<String> optionSpec12 = (OptionSpec<String>)optionParser2.accepts("proxyPass").withRequiredArg();
        final OptionSpec<String> optionSpec13 = (OptionSpec<String>)optionParser2.accepts("username").withRequiredArg().defaultsTo(new StringBuilder().append("Player").append(Util.getMillis() % 1000L).toString(), (Object[])new String[0]);
        final OptionSpec<String> optionSpec14 = (OptionSpec<String>)optionParser2.accepts("uuid").withRequiredArg();
        final OptionSpec<String> optionSpec15 = (OptionSpec<String>)optionParser2.accepts("accessToken").withRequiredArg().required();
        final OptionSpec<String> optionSpec16 = (OptionSpec<String>)optionParser2.accepts("version").withRequiredArg().required();
        final OptionSpec<Integer> optionSpec17 = (OptionSpec<Integer>)optionParser2.accepts("width").withRequiredArg().ofType((Class)Integer.class).defaultsTo(854, (Object[])new Integer[0]);
        final OptionSpec<Integer> optionSpec18 = (OptionSpec<Integer>)optionParser2.accepts("height").withRequiredArg().ofType((Class)Integer.class).defaultsTo(480, (Object[])new Integer[0]);
        final OptionSpec<Integer> optionSpec19 = (OptionSpec<Integer>)optionParser2.accepts("fullscreenWidth").withRequiredArg().ofType((Class)Integer.class);
        final OptionSpec<Integer> optionSpec20 = (OptionSpec<Integer>)optionParser2.accepts("fullscreenHeight").withRequiredArg().ofType((Class)Integer.class);
        final OptionSpec<String> optionSpec21 = (OptionSpec<String>)optionParser2.accepts("userProperties").withRequiredArg().defaultsTo("{}", (Object[])new String[0]);
        final OptionSpec<String> optionSpec22 = (OptionSpec<String>)optionParser2.accepts("profileProperties").withRequiredArg().defaultsTo("{}", (Object[])new String[0]);
        final OptionSpec<String> optionSpec23 = (OptionSpec<String>)optionParser2.accepts("assetIndex").withRequiredArg();
        final OptionSpec<String> optionSpec24 = (OptionSpec<String>)optionParser2.accepts("userType").withRequiredArg().defaultsTo("legacy", (Object[])new String[0]);
        final OptionSpec<String> optionSpec25 = (OptionSpec<String>)optionParser2.accepts("versionType").withRequiredArg().defaultsTo("release", (Object[])new String[0]);
        final OptionSpec<String> optionSpec26 = (OptionSpec<String>)optionParser2.nonOptions();
        final OptionSet optionSet27 = optionParser2.parse(arr);
        final List<String> list28 = (List<String>)optionSet27.valuesOf((OptionSpec)optionSpec26);
        if (!list28.isEmpty()) {
            System.out.println(new StringBuilder().append("Completely ignored arguments: ").append(list28).toString());
        }
        final String string29 = Main.<String>parseArgument(optionSet27, optionSpec9);
        Proxy proxy30 = Proxy.NO_PROXY;
        if (string29 != null) {
            try {
                proxy30 = new Proxy(Proxy.Type.SOCKS, (SocketAddress)new InetSocketAddress(string29, (int)Main.<Integer>parseArgument(optionSet27, optionSpec10)));
            }
            catch (Exception ex) {}
        }
        final String string30 = Main.<String>parseArgument(optionSet27, optionSpec11);
        final String string31 = Main.<String>parseArgument(optionSet27, optionSpec12);
        if (!proxy30.equals(Proxy.NO_PROXY) && stringHasValue(string30) && stringHasValue(string31)) {
            Authenticator.setDefault((Authenticator)new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(string30, string31.toCharArray());
                }
            });
        }
        final int integer33 = Main.<Integer>parseArgument(optionSet27, optionSpec17);
        final int integer34 = Main.<Integer>parseArgument(optionSet27, optionSpec18);
        final OptionalInt optionalInt35 = ofNullable(Main.<Integer>parseArgument(optionSet27, optionSpec19));
        final OptionalInt optionalInt36 = ofNullable(Main.<Integer>parseArgument(optionSet27, optionSpec20));
        final boolean boolean37 = optionSet27.has("fullscreen");
        final boolean boolean38 = optionSet27.has("demo");
        final boolean boolean39 = optionSet27.has("disableMultiplayer");
        final boolean boolean40 = optionSet27.has("disableChat");
        final String string32 = Main.<String>parseArgument(optionSet27, optionSpec16);
        final Gson gson42 = new GsonBuilder().registerTypeAdapter((Type)PropertyMap.class, new PropertyMap.Serializer()).create();
        final PropertyMap propertyMap43 = GsonHelper.<PropertyMap>fromJson(gson42, Main.<String>parseArgument(optionSet27, optionSpec21), PropertyMap.class);
        final PropertyMap propertyMap44 = GsonHelper.<PropertyMap>fromJson(gson42, Main.<String>parseArgument(optionSet27, optionSpec22), PropertyMap.class);
        final String string33 = Main.<String>parseArgument(optionSet27, optionSpec25);
        final File file46 = Main.<File>parseArgument(optionSet27, optionSpec5);
        final File file47 = optionSet27.has((OptionSpec)optionSpec6) ? Main.<File>parseArgument(optionSet27, optionSpec6) : new File(file46, "assets/");
        final File file48 = optionSet27.has((OptionSpec)optionSpec7) ? Main.<File>parseArgument(optionSet27, optionSpec7) : new File(file46, "resourcepacks/");
        final String string34 = (String)(optionSet27.has((OptionSpec)optionSpec14) ? optionSpec14.value(optionSet27) : Player.createPlayerUUID((String)optionSpec13.value(optionSet27)).toString());
        final String string35 = optionSet27.has((OptionSpec)optionSpec23) ? ((String)optionSpec23.value(optionSet27)) : null;
        final String string36 = Main.<String>parseArgument(optionSet27, optionSpec3);
        final Integer integer35 = Main.<Integer>parseArgument(optionSet27, optionSpec4);
        CrashReport.preload();
        Bootstrap.bootStrap();
        Bootstrap.validate();
        Util.startTimerHackThread();
        final User dkj53 = new User((String)optionSpec13.value(optionSet27), string34, (String)optionSpec15.value(optionSet27), (String)optionSpec24.value(optionSet27));
        final GameConfig dsr54 = new GameConfig(new GameConfig.UserData(dkj53, propertyMap43, propertyMap44, proxy30), new DisplayData(integer33, integer34, optionalInt35, optionalInt36, boolean37), new GameConfig.FolderData(file46, file48, file47, string35), new GameConfig.GameData(boolean38, string32, string33, boolean39, boolean40), new GameConfig.ServerData(string36, integer35));
        final Thread thread55 = new Thread("Client Shutdown Thread") {
            public void run() {
                final Minecraft djw2 = Minecraft.getInstance();
                if (djw2 == null) {
                    return;
                }
                final IntegratedServer emy3 = djw2.getSingleplayerServer();
                if (emy3 != null) {
                    emy3.halt(true);
                }
            }
        };
        thread55.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(Main.LOGGER));
        Runtime.getRuntime().addShutdownHook(thread55);
        final RenderPipeline dec56 = new RenderPipeline();
        Minecraft djw57;
        try {
            Thread.currentThread().setName("Render thread");
            RenderSystem.initRenderThread();
            RenderSystem.beginInitialization();
            djw57 = new Minecraft(dsr54);
            RenderSystem.finishInitialization();
        }
        catch (SilentInitException dss58) {
            Main.LOGGER.warn("Failed to create window: ", (Throwable)dss58);
            return;
        }
        catch (Throwable throwable58) {
            final CrashReport l59 = CrashReport.forThrowable(throwable58, "Initializing game");
            l59.addCategory("Initialization");
            Minecraft.fillReport(null, dsr54.game.launchVersion, null, l59);
            Minecraft.crash(l59);
            return;
        }
        Thread thread56;
        if (djw57.renderOnThread()) {
            thread56 = new Thread("Game thread") {
                public void run() {
                    try {
                        RenderSystem.initGameThread(true);
                        djw57.run();
                    }
                    catch (Throwable throwable2) {
                        Main.LOGGER.error("Exception in client thread", throwable2);
                    }
                }
            };
            thread56.start();
            while (djw57.isRunning()) {}
        }
        else {
            thread56 = null;
            try {
                RenderSystem.initGameThread(false);
                djw57.run();
            }
            catch (Throwable throwable59) {
                Main.LOGGER.error("Unhandled game exception", throwable59);
            }
        }
        try {
            djw57.stop();
            if (thread56 != null) {
                thread56.join();
            }
        }
        catch (InterruptedException interruptedException59) {
            Main.LOGGER.error("Exception during client thread shutdown", (Throwable)interruptedException59);
        }
        finally {
            djw57.destroy();
        }
    }
    
    private static OptionalInt ofNullable(@Nullable final Integer integer) {
        return (integer != null) ? OptionalInt.of((int)integer) : OptionalInt.empty();
    }
    
    @Nullable
    private static <T> T parseArgument(final OptionSet optionSet, final OptionSpec<T> optionSpec) {
        try {
            return (T)optionSet.valueOf((OptionSpec)optionSpec);
        }
        catch (Throwable throwable3) {
            if (optionSpec instanceof ArgumentAcceptingOptionSpec) {
                final ArgumentAcceptingOptionSpec<T> argumentAcceptingOptionSpec4 = (ArgumentAcceptingOptionSpec<T>)optionSpec;
                final List<T> list5 = (List<T>)argumentAcceptingOptionSpec4.defaultValues();
                if (!list5.isEmpty()) {
                    return (T)list5.get(0);
                }
            }
            throw throwable3;
        }
    }
    
    private static boolean stringHasValue(@Nullable final String string) {
        return string != null && !string.isEmpty();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        System.setProperty("java.awt.headless", "true");
    }
}
