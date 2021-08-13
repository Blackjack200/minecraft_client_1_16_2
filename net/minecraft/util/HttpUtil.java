package net.minecraft.util;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;
import net.minecraft.DefaultUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import java.util.Locale;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.net.Proxy;
import javax.annotation.Nullable;
import java.util.Map;
import java.io.File;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
    private static final Logger LOGGER;
    public static final ListeningExecutorService DOWNLOAD_EXECUTOR;
    
    public static CompletableFuture<?> downloadTo(final File file, final String string, final Map<String, String> map, final int integer, @Nullable final ProgressListener afk, final Proxy proxy) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection httpURLConnection7 = null;
            InputStream inputStream8 = null;
            OutputStream outputStream9 = null;
            if (afk != null) {
                afk.progressStart(new TranslatableComponent("resourcepack.downloading"));
                afk.progressStage(new TranslatableComponent("resourcepack.requesting"));
            }
            try {
                final byte[] arr10 = new byte[4096];
                final URL uRL11 = new URL(string);
                httpURLConnection7 = (HttpURLConnection)uRL11.openConnection(proxy);
                httpURLConnection7.setInstanceFollowRedirects(true);
                float float12 = 0.0f;
                float float13 = (float)map.entrySet().size();
                for (final Map.Entry<String, String> entry15 : map.entrySet()) {
                    httpURLConnection7.setRequestProperty((String)entry15.getKey(), (String)entry15.getValue());
                    if (afk != null) {
                        afk.progressStagePercentage((int)(++float12 / float13 * 100.0f));
                    }
                }
                inputStream8 = httpURLConnection7.getInputStream();
                float13 = (float)httpURLConnection7.getContentLength();
                final int integer2 = httpURLConnection7.getContentLength();
                if (afk != null) {
                    afk.progressStage(new TranslatableComponent("resourcepack.progress", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { float13 / 1000.0f / 1000.0f }) }));
                }
                if (file.exists()) {
                    final long long15 = file.length();
                    if (long15 == integer2) {
                        if (afk != null) {
                            afk.stop();
                        }
                        return null;
                    }
                    HttpUtil.LOGGER.warn("Deleting {} as it does not match what we currently have ({} vs our {}).", file, integer2, long15);
                    FileUtils.deleteQuietly(file);
                }
                else if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                outputStream9 = (OutputStream)new DataOutputStream((OutputStream)new FileOutputStream(file));
                if (integer > 0 && float13 > integer) {
                    if (afk != null) {
                        afk.stop();
                    }
                    throw new IOException(new StringBuilder().append("Filesize is bigger than maximum allowed (file is ").append(float12).append(", limit is ").append(integer).append(")").toString());
                }
                int integer3;
                while ((integer3 = inputStream8.read(arr10)) >= 0) {
                    float12 += integer3;
                    if (afk != null) {
                        afk.progressStagePercentage((int)(float12 / float13 * 100.0f));
                    }
                    if (integer > 0 && float12 > integer) {
                        if (afk != null) {
                            afk.stop();
                        }
                        throw new IOException(new StringBuilder().append("Filesize was bigger than maximum allowed (got >= ").append(float12).append(", limit was ").append(integer).append(")").toString());
                    }
                    if (Thread.interrupted()) {
                        HttpUtil.LOGGER.error("INTERRUPTED");
                        if (afk != null) {
                            afk.stop();
                        }
                        return null;
                    }
                    outputStream9.write(arr10, 0, integer3);
                }
                if (afk != null) {
                    afk.stop();
                }
            }
            catch (Throwable throwable10) {
                throwable10.printStackTrace();
                if (httpURLConnection7 != null) {
                    final InputStream inputStream9 = httpURLConnection7.getErrorStream();
                    try {
                        HttpUtil.LOGGER.error(IOUtils.toString(inputStream9));
                    }
                    catch (IOException iOException12) {
                        iOException12.printStackTrace();
                    }
                }
                if (afk != null) {
                    afk.stop();
                }
            }
            finally {
                IOUtils.closeQuietly(inputStream8);
                IOUtils.closeQuietly(outputStream9);
            }
            return null;
        }, (Executor)HttpUtil.DOWNLOAD_EXECUTOR);
    }
    
    public static int getAvailablePort() {
        try (final ServerSocket serverSocket1 = new ServerSocket(0)) {
            return serverSocket1.getLocalPort();
        }
        catch (IOException iOException1) {
            return 25564;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DOWNLOAD_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(HttpUtil.LOGGER)).setNameFormat("Downloader %d").build()));
    }
}
