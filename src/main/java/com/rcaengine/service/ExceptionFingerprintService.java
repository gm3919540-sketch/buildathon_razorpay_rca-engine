package com.rcaengine.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ExceptionFingerprintService {

    public String generateFingerprint(
            String exceptionType,
            String message
    ) {

        String normalized = normalize(exceptionType, message);

        return sha256(normalized);
    }

    private String normalize(
            String exceptionType,
            String message
    ) {

        String safeExceptionType =
                exceptionType == null ? "" : exceptionType;

        String safeMessage =
                message == null ? "" : message;

        String normalizedMessage = safeMessage
                .toLowerCase()
                .replaceAll("\\b\\d+\\b", "?")
                .replaceAll("\\b[0-9a-f]{8,}\\b", "?")
                .replaceAll("\\s+", " ")
                .trim();

        return safeExceptionType.toLowerCase()
                + ":"
                + normalizedMessage;
    }

    private String sha256(String value) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            value.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder result = new StringBuilder();

            for (byte b : hash) {
                result.append(
                        String.format("%02x", b)
                );
            }

            return result.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    exception
            );
        }
    }
}