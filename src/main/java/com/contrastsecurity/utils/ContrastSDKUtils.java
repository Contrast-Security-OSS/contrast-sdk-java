package com.contrastsecurity.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

public class ContrastSDKUtils {

    public static String makeAuthorizationToken(String username, String serviceKey) throws IOException {
        String token = username + ":" + serviceKey;

        return DatatypeConverter.printBase64Binary(token.getBytes()).trim();
    }

    public static void validateUrl(String url) throws IllegalArgumentException {
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
        if (!u.getProtocol().startsWith("http")) {
            throw new IllegalArgumentException("Invalid protocol");
        }
    }
}
