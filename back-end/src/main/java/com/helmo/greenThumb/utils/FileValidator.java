package com.helmo.greenThumb.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    private static final Map<String, String> MAGIC_NUMBERS = new HashMap<>();

    static {
        MAGIC_NUMBERS.put("jpg", "FFD8FF"); // JPEG
        MAGIC_NUMBERS.put("png", "89504E47"); // PNG
        MAGIC_NUMBERS.put("gif", "47494638"); // GIF
        MAGIC_NUMBERS.put("bmp", "424D"); // BMP
        MAGIC_NUMBERS.put("tif", "49492A00"); // TIFF
    }
    
    public static boolean validateImage(MultipartFile file) {
        if (file.isEmpty()) return false;

        try {
            // Read first byte of the files
            byte[] fileBytes = file.getInputStream().readAllBytes();
            String fileHeader = bytesToHex(fileBytes, 4);

            // Check magic number
            for (String magicNumber : MAGIC_NUMBERS.values()) {
                if (fileHeader.startsWith(magicNumber)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < length && i < bytes.length; i++) {
            hexString.append(String.format("%02X", bytes[i]));
        }
        return hexString.toString();
    }
}
