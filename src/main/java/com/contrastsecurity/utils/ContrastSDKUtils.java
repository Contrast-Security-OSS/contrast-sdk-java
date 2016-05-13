package com.contrastsecurity.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ContrastSDKUtils {

    public static String makeAuthorizationToken(String username, String serviceKey) throws IOException {
        String token = username + ":" + serviceKey;
        return Base64.encodeBase64String(token.getBytes(CharEncoding.US_ASCII)).trim();
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
