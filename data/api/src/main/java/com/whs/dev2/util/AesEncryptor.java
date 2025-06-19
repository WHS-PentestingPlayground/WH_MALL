package com.whs.dev2.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

public class AesEncryptor {

    // Base64로 인코딩된 AES Key / IV (16바이트 기준)
    private static final String KEY_B64 = "MTIzNDU2Nzg5MGFiY2RlZg==";         // "1234567890abcdef"
    private static final String IV_B64  = "QUJDREVGR0hJSktMTU5PQQ==";         // "ABCDEFGHIJKLMNOP"

    public static String encryptFileToBase64(String inputFilePath, String encryptedFilePath) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(KEY_B64);
        byte[] ivBytes  = Base64.getDecoder().decode(IV_B64);

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        FileInputStream fis = new FileInputStream(inputFilePath);
        byte[] inputBytes = fis.readAllBytes();
        fis.close();

        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        // 암호화 파일 저장
        FileOutputStream fos = new FileOutputStream(encryptedFilePath);
        fos.write(encryptedBytes);
        fos.close();

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
