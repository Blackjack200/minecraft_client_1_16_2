package com.mojang.realmsclient.client;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import java.awt.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.http.HttpResponse;
import java.awt.event.ActionListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import java.nio.file.Path;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.util.regex.Matcher;
import java.util.Iterator;
import net.minecraft.world.level.storage.LevelResource;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import net.minecraft.client.Minecraft;
import java.util.Locale;
import net.minecraft.world.level.storage.LevelSummary;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.SharedConstants;
import java.util.regex.Pattern;
import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
import net.minecraft.world.level.storage.LevelStorageSource;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.dto.WorldDownload;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class FileDownload {
    private static final Logger LOGGER;
    private volatile boolean cancelled;
    private volatile boolean finished;
    private volatile boolean error;
    private volatile boolean extracting;
    private volatile File tempFile;
    private volatile File resourcePackPath;
    private volatile HttpGet request;
    private Thread currentThread;
    private final RequestConfig requestConfig;
    private static final String[] INVALID_FILE_NAMES;
    
    public FileDownload() {
        this.requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
    }
    
    public long contentLength(final String string) {
        CloseableHttpClient closeableHttpClient3 = null;
        HttpGet httpGet4 = null;
        try {
            httpGet4 = new HttpGet(string);
            closeableHttpClient3 = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
            final CloseableHttpResponse closeableHttpResponse5 = closeableHttpClient3.execute((HttpUriRequest)httpGet4);
            return Long.parseLong(closeableHttpResponse5.getFirstHeader("Content-Length").getValue());
        }
        catch (Throwable throwable5) {
            FileDownload.LOGGER.error("Unable to get content length for download");
            return 0L;
        }
        finally {
            if (httpGet4 != null) {
                httpGet4.releaseConnection();
            }
            if (closeableHttpClient3 != null) {
                try {
                    closeableHttpClient3.close();
                }
                catch (IOException iOException10) {
                    FileDownload.LOGGER.error("Could not close http client", (Throwable)iOException10);
                }
            }
        }
    }
    
    public void download(final WorldDownload dha, final String string, final RealmsDownloadLatestWorldScreen.DownloadStatus a, final LevelStorageSource cyd) {
        if (this.currentThread != null) {
            return;
        }
        (this.currentThread = new Thread(() -> {
            CloseableHttpClient closeableHttpClient6 = null;
            try {
                this.tempFile = File.createTempFile("backup", ".tar.gz");
                this.request = new HttpGet(dha.downloadLink);
                closeableHttpClient6 = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
                final HttpResponse httpResponse7 = (HttpResponse)closeableHttpClient6.execute((HttpUriRequest)this.request);
                a.totalBytes = Long.parseLong(httpResponse7.getFirstHeader("Content-Length").getValue());
                if (httpResponse7.getStatusLine().getStatusCode() != 200) {
                    this.error = true;
                    this.request.abort();
                    return;
                }
                final OutputStream outputStream8 = (OutputStream)new FileOutputStream(this.tempFile);
                final ProgressListener b9 = new ProgressListener(string.trim(), this.tempFile, cyd, a);
                final DownloadCountingOutputStream a2 = new DownloadCountingOutputStream(outputStream8);
                a2.setListener((ActionListener)b9);
                IOUtils.copy(httpResponse7.getEntity().getContent(), (OutputStream)a2);
            }
            catch (Exception exception7) {
                FileDownload.LOGGER.error("Caught exception while downloading: " + exception7.getMessage());
                this.error = true;
                this.request.releaseConnection();
                if (this.tempFile != null) {
                    this.tempFile.delete();
                }
                if (!this.error) {
                    if (!dha.resourcePackUrl.isEmpty() && !dha.resourcePackHash.isEmpty()) {
                        try {
                            this.tempFile = File.createTempFile("resources", ".tar.gz");
                            this.request = new HttpGet(dha.resourcePackUrl);
                            final HttpResponse httpResponse7 = (HttpResponse)closeableHttpClient6.execute((HttpUriRequest)this.request);
                            a.totalBytes = Long.parseLong(httpResponse7.getFirstHeader("Content-Length").getValue());
                            if (httpResponse7.getStatusLine().getStatusCode() != 200) {
                                this.error = true;
                                this.request.abort();
                                return;
                            }
                            final OutputStream outputStream8 = (OutputStream)new FileOutputStream(this.tempFile);
                            final ResourcePackProgressListener c9 = new ResourcePackProgressListener(this.tempFile, a, dha);
                            final DownloadCountingOutputStream a2 = new DownloadCountingOutputStream(outputStream8);
                            a2.setListener((ActionListener)c9);
                            IOUtils.copy(httpResponse7.getEntity().getContent(), (OutputStream)a2);
                        }
                        catch (Exception exception7) {
                            FileDownload.LOGGER.error("Caught exception while downloading: " + exception7.getMessage());
                            this.error = true;
                        }
                        finally {
                            this.request.releaseConnection();
                            if (this.tempFile != null) {
                                this.tempFile.delete();
                            }
                        }
                    }
                    else {
                        this.finished = true;
                    }
                }
                if (closeableHttpClient6 != null) {
                    try {
                        closeableHttpClient6.close();
                    }
                    catch (IOException iOException7) {
                        FileDownload.LOGGER.error("Failed to close Realms download client");
                    }
                }
            }
            finally {
                this.request.releaseConnection();
                if (this.tempFile != null) {
                    this.tempFile.delete();
                }
                if (!this.error) {
                    if (!dha.resourcePackUrl.isEmpty() && !dha.resourcePackHash.isEmpty()) {
                        try {
                            this.tempFile = File.createTempFile("resources", ".tar.gz");
                            this.request = new HttpGet(dha.resourcePackUrl);
                            final HttpResponse httpResponse8 = (HttpResponse)closeableHttpClient6.execute((HttpUriRequest)this.request);
                            a.totalBytes = Long.parseLong(httpResponse8.getFirstHeader("Content-Length").getValue());
                            if (httpResponse8.getStatusLine().getStatusCode() != 200) {
                                this.error = true;
                                this.request.abort();
                                return;
                            }
                            final OutputStream outputStream9 = (OutputStream)new FileOutputStream(this.tempFile);
                            final ResourcePackProgressListener c10 = new ResourcePackProgressListener(this.tempFile, a, dha);
                            final DownloadCountingOutputStream a3 = new DownloadCountingOutputStream(outputStream9);
                            a3.setListener((ActionListener)c10);
                            IOUtils.copy(httpResponse8.getEntity().getContent(), (OutputStream)a3);
                        }
                        catch (Exception exception8) {
                            FileDownload.LOGGER.error("Caught exception while downloading: " + exception8.getMessage());
                            this.error = true;
                            this.request.releaseConnection();
                            if (this.tempFile != null) {
                                this.tempFile.delete();
                            }
                        }
                        finally {
                            this.request.releaseConnection();
                            if (this.tempFile != null) {
                                this.tempFile.delete();
                            }
                        }
                    }
                    else {
                        this.finished = true;
                    }
                }
                if (closeableHttpClient6 != null) {
                    try {
                        closeableHttpClient6.close();
                    }
                    catch (IOException iOException8) {
                        FileDownload.LOGGER.error("Failed to close Realms download client");
                    }
                }
            }
        })).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(FileDownload.LOGGER));
        this.currentThread.start();
    }
    
    public void cancel() {
        if (this.request != null) {
            this.request.abort();
        }
        if (this.tempFile != null) {
            this.tempFile.delete();
        }
        this.cancelled = true;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isError() {
        return this.error;
    }
    
    public boolean isExtracting() {
        return this.extracting;
    }
    
    public static String findAvailableFolderName(String string) {
        string = string.replaceAll("[\\./\"]", "_");
        for (final String string2 : FileDownload.INVALID_FILE_NAMES) {
            if (string.equalsIgnoreCase(string2)) {
                string = "_" + string + "_";
            }
        }
        return string;
    }
    
    private void untarGzipArchive(String string, final File file, final LevelStorageSource cyd) throws IOException {
        final Pattern pattern5 = Pattern.compile(".*-([0-9]+)$");
        int integer7 = 1;
        for (final char character11 : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
            string = string.replace(character11, '_');
        }
        if (StringUtils.isEmpty((CharSequence)string)) {
            string = "Realm";
        }
        string = findAvailableFolderName(string);
        try {
            for (final LevelSummary cye9 : cyd.getLevelList()) {
                if (cye9.getLevelId().toLowerCase(Locale.ROOT).startsWith(string.toLowerCase(Locale.ROOT))) {
                    final Matcher matcher10 = pattern5.matcher((CharSequence)cye9.getLevelId());
                    if (matcher10.matches()) {
                        if (Integer.valueOf(matcher10.group(1)) <= integer7) {
                            continue;
                        }
                        integer7 = Integer.valueOf(matcher10.group(1));
                    }
                    else {
                        ++integer7;
                    }
                }
            }
        }
        catch (Exception exception8) {
            FileDownload.LOGGER.error("Error getting level list", (Throwable)exception8);
            this.error = true;
            return;
        }
        String string2;
        if (!cyd.isNewLevelIdAcceptable(string) || integer7 > 1) {
            string2 = string + ((integer7 == 1) ? "" : new StringBuilder().append("-").append(integer7).toString());
            if (!cyd.isNewLevelIdAcceptable(string2)) {
                for (boolean boolean8 = false; !boolean8; boolean8 = true) {
                    ++integer7;
                    string2 = string + ((integer7 == 1) ? "" : new StringBuilder().append("-").append(integer7).toString());
                    if (cyd.isNewLevelIdAcceptable(string2)) {}
                }
            }
        }
        else {
            string2 = string;
        }
        TarArchiveInputStream tarArchiveInputStream8 = null;
        final File file2 = new File(Minecraft.getInstance().gameDirectory.getAbsolutePath(), "saves");
        try {
            file2.mkdir();
            tarArchiveInputStream8 = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream((InputStream)new BufferedInputStream((InputStream)new FileInputStream(file))));
            for (TarArchiveEntry tarArchiveEntry10 = tarArchiveInputStream8.getNextTarEntry(); tarArchiveEntry10 != null; tarArchiveEntry10 = tarArchiveInputStream8.getNextTarEntry()) {
                final File file3 = new File(file2, tarArchiveEntry10.getName().replace("world", (CharSequence)string2));
                if (tarArchiveEntry10.isDirectory()) {
                    file3.mkdirs();
                }
                else {
                    file3.createNewFile();
                    try (final FileOutputStream fileOutputStream12 = new FileOutputStream(file3)) {
                        IOUtils.copy((InputStream)tarArchiveInputStream8, (OutputStream)fileOutputStream12);
                    }
                }
            }
        }
        catch (Exception exception9) {
            FileDownload.LOGGER.error("Error extracting world", (Throwable)exception9);
            this.error = true;
        }
        finally {
            if (tarArchiveInputStream8 != null) {
                tarArchiveInputStream8.close();
            }
            if (file != null) {
                file.delete();
            }
            try (final LevelStorageSource.LevelStorageAccess a22 = cyd.createAccess(string2)) {
                a22.renameLevel(string2.trim());
                final Path path24 = a22.getLevelPath(LevelResource.LEVEL_DATA_FILE);
                deletePlayerTag(path24.toFile());
            }
            catch (IOException iOException22) {
                FileDownload.LOGGER.error("Failed to rename unpacked realms level {}", string2, iOException22);
            }
            this.resourcePackPath = new File(file2, string2 + File.separator + "resources.zip");
        }
    }
    
    private static void deletePlayerTag(final File file) {
        if (file.exists()) {
            try {
                final CompoundTag md2 = NbtIo.readCompressed(file);
                final CompoundTag md3 = md2.getCompound("Data");
                md3.remove("Player");
                NbtIo.writeCompressed(md2, file);
            }
            catch (Exception exception2) {
                exception2.printStackTrace();
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        INVALID_FILE_NAMES = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
    }
    
    class ProgressListener implements ActionListener {
        private final String worldName;
        private final File tempFile;
        private final LevelStorageSource levelStorageSource;
        private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
        
        private ProgressListener(final String string, final File file, final LevelStorageSource cyd, final RealmsDownloadLatestWorldScreen.DownloadStatus a) {
            this.worldName = string;
            this.tempFile = file;
            this.levelStorageSource = cyd;
            this.downloadStatus = a;
        }
        
        public void actionPerformed(final ActionEvent actionEvent) {
            this.downloadStatus.bytesWritten = ((DownloadCountingOutputStream)actionEvent.getSource()).getByteCount();
            if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.this.cancelled && !FileDownload.this.error) {
                try {
                    FileDownload.this.extracting = true;
                    FileDownload.this.untarGzipArchive(this.worldName, this.tempFile, this.levelStorageSource);
                }
                catch (IOException iOException3) {
                    FileDownload.LOGGER.error("Error extracting archive", (Throwable)iOException3);
                    FileDownload.this.error = true;
                }
            }
        }
    }
    
    class ResourcePackProgressListener implements ActionListener {
        private final File tempFile;
        private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
        private final WorldDownload worldDownload;
        
        private ResourcePackProgressListener(final File file, final RealmsDownloadLatestWorldScreen.DownloadStatus a, final WorldDownload dha) {
            this.tempFile = file;
            this.downloadStatus = a;
            this.worldDownload = dha;
        }
        
        public void actionPerformed(final ActionEvent actionEvent) {
            this.downloadStatus.bytesWritten = ((DownloadCountingOutputStream)actionEvent.getSource()).getByteCount();
            if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.this.cancelled) {
                try {
                    final String string3 = Hashing.sha1().hashBytes(Files.toByteArray(this.tempFile)).toString();
                    if (string3.equals(this.worldDownload.resourcePackHash)) {
                        FileUtils.copyFile(this.tempFile, FileDownload.this.resourcePackPath);
                        FileDownload.this.finished = true;
                    }
                    else {
                        FileDownload.LOGGER.error("Resourcepack had wrong hash (expected " + this.worldDownload.resourcePackHash + ", found " + string3 + "). Deleting it.");
                        FileUtils.deleteQuietly(this.tempFile);
                        FileDownload.this.error = true;
                    }
                }
                catch (IOException iOException3) {
                    FileDownload.LOGGER.error("Error copying resourcepack file", iOException3.getMessage());
                    FileDownload.this.error = true;
                }
            }
        }
    }
    
    class DownloadCountingOutputStream extends CountingOutputStream {
        private ActionListener listener;
        
        public DownloadCountingOutputStream(final OutputStream outputStream) {
            super(outputStream);
        }
        
        public void setListener(final ActionListener actionListener) {
            this.listener = actionListener;
        }
        
        protected void afterWrite(final int integer) throws IOException {
            super.afterWrite(integer);
            if (this.listener != null) {
                this.listener.actionPerformed(new ActionEvent(this, 0, (String)null));
            }
        }
    }
}
