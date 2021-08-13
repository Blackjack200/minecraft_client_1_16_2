package com.mojang.realmsclient.exception;

public class RealmsHttpException extends RuntimeException {
    public RealmsHttpException(final String string, final Exception exception) {
        super(string, (Throwable)exception);
    }
}
