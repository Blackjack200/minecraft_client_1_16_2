package com.mojang.realmsclient.client;

import org.apache.http.util.Args;
import java.io.OutputStream;
import org.apache.http.entity.InputStreamEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.http.Header;
import java.time.Duration;
import com.google.gson.JsonElement;
import java.util.Optional;
import com.google.gson.JsonParser;
import org.apache.http.util.EntityUtils;
import java.io.FileNotFoundException;
import org.apache.http.HttpEntity;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import java.util.function.Consumer;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.User;
import org.apache.http.client.config.RequestConfig;
import com.mojang.realmsclient.gui.screens.UploadResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import com.mojang.realmsclient.dto.UploadInfo;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class FileUpload {
    private static final Logger LOGGER;
    private final File file;
    private final long worldId;
    private final int slotId;
    private final UploadInfo uploadInfo;
    private final String sessionId;
    private final String username;
    private final String clientVersion;
    private final UploadStatus uploadStatus;
    private final AtomicBoolean cancelled;
    private CompletableFuture<UploadResult> uploadTask;
    private final RequestConfig requestConfig;
    
    public FileUpload(final File file, final long long2, final int integer, final UploadInfo dgy, final User dkj, final String string, final UploadStatus dgc) {
        this.cancelled = new AtomicBoolean(false);
        this.requestConfig = RequestConfig.custom().setSocketTimeout((int)TimeUnit.MINUTES.toMillis(10L)).setConnectTimeout((int)TimeUnit.SECONDS.toMillis(15L)).build();
        this.file = file;
        this.worldId = long2;
        this.slotId = integer;
        this.uploadInfo = dgy;
        this.sessionId = dkj.getSessionId();
        this.username = dkj.getName();
        this.clientVersion = string;
        this.uploadStatus = dgc;
    }
    
    public void upload(final Consumer<UploadResult> consumer) {
        if (this.uploadTask != null) {
            return;
        }
        (this.uploadTask = (CompletableFuture<UploadResult>)CompletableFuture.supplyAsync(() -> this.requestUpload(0))).thenAccept((Consumer)consumer);
    }
    
    public void cancel() {
        this.cancelled.set(true);
        if (this.uploadTask != null) {
            this.uploadTask.cancel(false);
            this.uploadTask = null;
        }
    }
    
    private UploadResult requestUpload(final int integer) {
        final UploadResult.Builder a3 = new UploadResult.Builder();
        if (this.cancelled.get()) {
            return a3.build();
        }
        this.uploadStatus.totalBytes = this.file.length();
        final HttpPost httpPost4 = new HttpPost(this.uploadInfo.getUploadEndpoint().resolve(new StringBuilder().append("/upload/").append(this.worldId).append("/").append(this.slotId).toString()));
        final CloseableHttpClient closeableHttpClient5 = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
        try {
            this.setupRequest(httpPost4);
            final HttpResponse httpResponse6 = (HttpResponse)closeableHttpClient5.execute((HttpUriRequest)httpPost4);
            final long long7 = this.getRetryDelaySeconds(httpResponse6);
            if (this.shouldRetry(long7, integer)) {
                return this.retryUploadAfter(long7, integer);
            }
            this.handleResponse(httpResponse6, a3);
        }
        catch (Exception exception6) {
            if (!this.cancelled.get()) {
                FileUpload.LOGGER.error("Caught exception while uploading: ", (Throwable)exception6);
            }
        }
        finally {
            this.cleanup(httpPost4, closeableHttpClient5);
        }
        return a3.build();
    }
    
    private void cleanup(final HttpPost httpPost, final CloseableHttpClient closeableHttpClient) {
        httpPost.releaseConnection();
        if (closeableHttpClient != null) {
            try {
                closeableHttpClient.close();
            }
            catch (IOException iOException4) {
                FileUpload.LOGGER.error("Failed to close Realms upload client");
            }
        }
    }
    
    private void setupRequest(final HttpPost httpPost) throws FileNotFoundException {
        httpPost.setHeader("Cookie", "sid=" + this.sessionId + ";token=" + this.uploadInfo.getToken() + ";user=" + this.username + ";version=" + this.clientVersion);
        final CustomInputStreamEntity a3 = new CustomInputStreamEntity((InputStream)new FileInputStream(this.file), this.file.length(), this.uploadStatus);
        a3.setContentType("application/octet-stream");
        httpPost.setEntity((HttpEntity)a3);
    }
    
    private void handleResponse(final HttpResponse httpResponse, final UploadResult.Builder a) throws IOException {
        final int integer4 = httpResponse.getStatusLine().getStatusCode();
        if (integer4 == 401) {
            FileUpload.LOGGER.debug(new StringBuilder().append("Realms server returned 401: ").append(httpResponse.getFirstHeader("WWW-Authenticate")).toString());
        }
        a.withStatusCode(integer4);
        if (httpResponse.getEntity() != null) {
            final String string5 = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            if (string5 != null) {
                try {
                    final JsonParser jsonParser6 = new JsonParser();
                    final JsonElement jsonElement7 = jsonParser6.parse(string5).getAsJsonObject().get("errorMsg");
                    final Optional<String> optional8 = (Optional<String>)Optional.ofNullable(jsonElement7).map(JsonElement::getAsString);
                    a.withErrorMessage((String)optional8.orElse(null));
                }
                catch (Exception ex) {}
            }
        }
    }
    
    private boolean shouldRetry(final long long1, final int integer) {
        return long1 > 0L && integer + 1 < 5;
    }
    
    private UploadResult retryUploadAfter(final long long1, final int integer) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(long1).toMillis());
        return this.requestUpload(integer + 1);
    }
    
    private long getRetryDelaySeconds(final HttpResponse httpResponse) {
        return (long)Optional.ofNullable(httpResponse.getFirstHeader("Retry-After")).map(Header::getValue).map(Long::valueOf).orElse(0L);
    }
    
    public boolean isFinished() {
        return this.uploadTask.isDone() || this.uploadTask.isCancelled();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class CustomInputStreamEntity extends InputStreamEntity {
        private final long length;
        private final InputStream content;
        private final UploadStatus uploadStatus;
        
        public CustomInputStreamEntity(final InputStream inputStream, final long long2, final UploadStatus dgc) {
            super(inputStream);
            this.content = inputStream;
            this.length = long2;
            this.uploadStatus = dgc;
        }
        
        public void writeTo(final OutputStream outputStream) throws IOException {
            Args.notNull(outputStream, "Output stream");
            final InputStream inputStream3 = this.content;
            try {
                final byte[] arr4 = new byte[4096];
                if (this.length < 0L) {
                    int integer5;
                    while ((integer5 = inputStream3.read(arr4)) != -1) {
                        outputStream.write(arr4, 0, integer5);
                        final UploadStatus uploadStatus = this.uploadStatus;
                        uploadStatus.bytesWritten += integer5;
                    }
                }
                else {
                    long long6 = this.length;
                    while (long6 > 0L) {
                        final int integer5 = inputStream3.read(arr4, 0, (int)Math.min(4096L, long6));
                        if (integer5 == -1) {
                            break;
                        }
                        outputStream.write(arr4, 0, integer5);
                        final UploadStatus uploadStatus2 = this.uploadStatus;
                        uploadStatus2.bytesWritten += integer5;
                        long6 -= integer5;
                        outputStream.flush();
                    }
                }
            }
            finally {
                inputStream3.close();
            }
        }
    }
}
