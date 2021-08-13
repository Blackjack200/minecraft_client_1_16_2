package com.mojang.realmsclient.gui.screens;

public class UploadResult {
    public final int statusCode;
    public final String errorMessage;
    
    private UploadResult(final int integer, final String string) {
        this.statusCode = integer;
        this.errorMessage = string;
    }
    
    public static class Builder {
        private int statusCode;
        private String errorMessage;
        
        public Builder() {
            this.statusCode = -1;
        }
        
        public Builder withStatusCode(final int integer) {
            this.statusCode = integer;
            return this;
        }
        
        public Builder withErrorMessage(final String string) {
            this.errorMessage = string;
            return this;
        }
        
        public UploadResult build() {
            return new UploadResult(this.statusCode, this.errorMessage, null);
        }
    }
}
