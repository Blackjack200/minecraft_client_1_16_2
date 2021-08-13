package com.mojang.realmsclient.exception;

import net.minecraft.client.resources.language.I18n;
import com.mojang.realmsclient.client.RealmsError;

public class RealmsServiceException extends Exception {
    public final int httpResultCode;
    public final String httpResponseContent;
    public final int errorCode;
    public final String errorMsg;
    
    public RealmsServiceException(final int integer, final String string, final RealmsError dga) {
        super(string);
        this.httpResultCode = integer;
        this.httpResponseContent = string;
        this.errorCode = dga.getErrorCode();
        this.errorMsg = dga.getErrorMessage();
    }
    
    public RealmsServiceException(final int integer1, final String string2, final int integer3, final String string4) {
        super(string2);
        this.httpResultCode = integer1;
        this.httpResponseContent = string2;
        this.errorCode = integer3;
        this.errorMsg = string4;
    }
    
    public String toString() {
        if (this.errorCode == -1) {
            return new StringBuilder().append("Realms (").append(this.httpResultCode).append(") ").append(this.httpResponseContent).toString();
        }
        final String string2 = new StringBuilder().append("mco.errorMessage.").append(this.errorCode).toString();
        final String string3 = I18n.get(string2);
        return (string3.equals(string2) ? this.errorMsg : string3) + " - " + this.errorCode;
    }
}
