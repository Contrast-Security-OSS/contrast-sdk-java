package com.contrastsecurity.exceptions;

public class ApplicationCreateException extends Exception {
    public ApplicationCreateException(int rc, String message) {
        super("Recieved Response code: "+rc+" with message: " + message);
    }
    private static final long serialVersionUID = -9049287248312255189L;
}
