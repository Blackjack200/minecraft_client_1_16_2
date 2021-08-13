package com.mojang.realmsclient.exception;

public class RetryCallException extends RealmsServiceException {
    public final int delaySeconds;
    
    public RetryCallException(final int integer) {
        super(503, "Retry operation", -1, "");
        if (integer < 0 || integer > 120) {
            this.delaySeconds = 5;
        }
        else {
            this.delaySeconds = integer;
        }
    }
}
