package fr.supermax_8.skinlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageUtils {

    /**
     * Generates the hash of a given image file.
     *
     * @param file The image file to hash.
     * @return The hash of the file as a hexadecimal string.
     * @throws IOException If an error occurs while reading the file.
     * @throws NoSuchAlgorithmException If the specified hashing algorithm is not available.
     */
    public static String generateImageHash(File file) throws IOException, NoSuchAlgorithmException {
        // Ensure the file exists
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file: " + file.getPath());
        }

        // Use SHA-256 algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Read the file into the hash function
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        // Convert the hash bytes to a hexadecimal string
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }



}