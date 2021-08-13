package com.mojang.realmsclient.client;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.Proxy;
import java.io.IOException;
import java.net.MalformedURLException;
import com.mojang.realmsclient.exception.RealmsHttpException;
import java.net.URL;
import java.net.HttpURLConnection;

public abstract class Request<T extends Request<T>> {
    protected HttpURLConnection connection;
    private boolean connected;
    protected String url;
    
    public Request(final String string, final int integer2, final int integer3) {
        try {
            this.url = string;
            final Proxy proxy5 = RealmsClientConfig.getProxy();
            if (proxy5 != null) {
                this.connection = (HttpURLConnection)new URL(string).openConnection(proxy5);
            }
            else {
                this.connection = (HttpURLConnection)new URL(string).openConnection();
            }
            this.connection.setConnectTimeout(integer2);
            this.connection.setReadTimeout(integer3);
        }
        catch (MalformedURLException malformedURLException5) {
            throw new RealmsHttpException(malformedURLException5.getMessage(), (Exception)malformedURLException5);
        }
        catch (IOException iOException5) {
            throw new RealmsHttpException(iOException5.getMessage(), (Exception)iOException5);
        }
    }
    
    public void cookie(final String string1, final String string2) {
        cookie(this.connection, string1, string2);
    }
    
    public static void cookie(final HttpURLConnection httpURLConnection, final String string2, final String string3) {
        final String string4 = httpURLConnection.getRequestProperty("Cookie");
        if (string4 == null) {
            httpURLConnection.setRequestProperty("Cookie", string2 + "=" + string3);
        }
        else {
            httpURLConnection.setRequestProperty("Cookie", string4 + ";" + string2 + "=" + string3);
        }
    }
    
    public int getRetryAfterHeader() {
        return getRetryAfterHeader(this.connection);
    }
    
    public static int getRetryAfterHeader(final HttpURLConnection httpURLConnection) {
        final String string2 = httpURLConnection.getHeaderField("Retry-After");
        try {
            return Integer.valueOf(string2);
        }
        catch (Exception exception3) {
            return 5;
        }
    }
    
    public int responseCode() {
        try {
            this.connect();
            return this.connection.getResponseCode();
        }
        catch (Exception exception2) {
            throw new RealmsHttpException(exception2.getMessage(), exception2);
        }
    }
    
    public String text() {
        try {
            this.connect();
            String string2 = null;
            if (this.responseCode() >= 400) {
                string2 = this.read(this.connection.getErrorStream());
            }
            else {
                string2 = this.read(this.connection.getInputStream());
            }
            this.dispose();
            return string2;
        }
        catch (IOException iOException2) {
            throw new RealmsHttpException(iOException2.getMessage(), (Exception)iOException2);
        }
    }
    
    private String read(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        final InputStreamReader inputStreamReader3 = new InputStreamReader(inputStream, "UTF-8");
        final StringBuilder stringBuilder4 = new StringBuilder();
        for (int integer5 = inputStreamReader3.read(); integer5 != -1; integer5 = inputStreamReader3.read()) {
            stringBuilder4.append((char)integer5);
        }
        return stringBuilder4.toString();
    }
    
    private void dispose() {
        final byte[] arr2 = new byte[1024];
        try {
            final InputStream inputStream3 = this.connection.getInputStream();
            while (inputStream3.read(arr2) > 0) {}
            inputStream3.close();
        }
        catch (Exception exception3) {
            try {
                final InputStream inputStream4 = this.connection.getErrorStream();
                if (inputStream4 == null) {
                    return;
                }
                while (inputStream4.read(arr2) > 0) {}
                inputStream4.close();
            }
            catch (IOException ex) {}
        }
        finally {
            if (this.connection != null) {
                this.connection.disconnect();
            }
        }
    }
    
    protected T connect() {
        if (this.connected) {
            return (T)this;
        }
        final T dgb2 = this.doConnect();
        this.connected = true;
        return dgb2;
    }
    
    protected abstract T doConnect();
    
    public static Request<?> get(final String string) {
        return new Get(string, 5000, 60000);
    }
    
    public static Request<?> get(final String string, final int integer2, final int integer3) {
        return new Get(string, integer2, integer3);
    }
    
    public static Request<?> post(final String string1, final String string2) {
        return new Post(string1, string2, 5000, 60000);
    }
    
    public static Request<?> post(final String string1, final String string2, final int integer3, final int integer4) {
        return new Post(string1, string2, integer3, integer4);
    }
    
    public static Request<?> delete(final String string) {
        return new Delete(string, 5000, 60000);
    }
    
    public static Request<?> put(final String string1, final String string2) {
        return new Put(string1, string2, 5000, 60000);
    }
    
    public static Request<?> put(final String string1, final String string2, final int integer3, final int integer4) {
        return new Put(string1, string2, integer3, integer4);
    }
    
    public String getHeader(final String string) {
        return getHeader(this.connection, string);
    }
    
    public static String getHeader(final HttpURLConnection httpURLConnection, final String string) {
        try {
            return httpURLConnection.getHeaderField(string);
        }
        catch (Exception exception3) {
            return "";
        }
    }
    
    public static class Delete extends Request<Delete> {
        public Delete(final String string, final int integer2, final int integer3) {
            super(string, integer2, integer3);
        }
        
        public Delete doConnect() {
            try {
                this.connection.setDoOutput(true);
                this.connection.setRequestMethod("DELETE");
                this.connection.connect();
                return this;
            }
            catch (Exception exception2) {
                throw new RealmsHttpException(exception2.getMessage(), exception2);
            }
        }
    }
    
    public static class Get extends Request<Get> {
        public Get(final String string, final int integer2, final int integer3) {
            super(string, integer2, integer3);
        }
        
        public Get doConnect() {
            try {
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("GET");
                return this;
            }
            catch (Exception exception2) {
                throw new RealmsHttpException(exception2.getMessage(), exception2);
            }
        }
    }
    
    public static class Put extends Request<Put> {
        private final String content;
        
        public Put(final String string1, final String string2, final int integer3, final int integer4) {
            super(string1, integer3, integer4);
            this.content = string2;
        }
        
        public Put doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoOutput(true);
                this.connection.setDoInput(true);
                this.connection.setRequestMethod("PUT");
                final OutputStream outputStream2 = this.connection.getOutputStream();
                final OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream2, "UTF-8");
                outputStreamWriter3.write(this.content);
                outputStreamWriter3.close();
                outputStream2.flush();
                return this;
            }
            catch (Exception exception2) {
                throw new RealmsHttpException(exception2.getMessage(), exception2);
            }
        }
    }
    
    public static class Post extends Request<Post> {
        private final String content;
        
        public Post(final String string1, final String string2, final int integer3, final int integer4) {
            super(string1, integer3, integer4);
            this.content = string2;
        }
        
        public Post doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("POST");
                final OutputStream outputStream2 = this.connection.getOutputStream();
                final OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream2, "UTF-8");
                outputStreamWriter3.write(this.content);
                outputStreamWriter3.close();
                outputStream2.flush();
                return this;
            }
            catch (Exception exception2) {
                throw new RealmsHttpException(exception2.getMessage(), exception2);
            }
        }
    }
}
