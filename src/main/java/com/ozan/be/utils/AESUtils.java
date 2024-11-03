package com.ozan.be.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AESUtils {

  @Value("${application.security.aes.secret-key}")
  private String DEFAULT_SECRET_KEY;

  @Value("${application.security.aes.init-vector}")
  private String DEFAULT_INIT_VECTOR;

  @Value("${application.security.aes.algorithm}")
  private String ALGORITHM;

  public String encrypt(String value) {
    return encrypt(value, DEFAULT_SECRET_KEY, DEFAULT_INIT_VECTOR);
  }

  public String encrypt(String value, String SECRET_KEY, String INIT_VECTOR) {
    try {
      byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
      byte[] decodedIV = Base64.getDecoder().decode(INIT_VECTOR);

      SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(decodedIV);

      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

      byte[] encrypted = cipher.doFinal(value.getBytes());

      return Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      throw new RuntimeException("Error occurred during encryption: " + e.getMessage());
    }
  }

  public String decrypt(String encryptedValue) {
    return decrypt(encryptedValue, DEFAULT_SECRET_KEY, DEFAULT_INIT_VECTOR);
  }

  public String decrypt(String encryptedValue, String SECRET_KEY, String INIT_VECTOR) {
    try {
      byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
      byte[] decodedIV = Base64.getDecoder().decode(INIT_VECTOR);
      byte[] decodedEncryptedValue = Base64.getDecoder().decode(encryptedValue);

      SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(decodedIV);

      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

      byte[] decrypted = cipher.doFinal(decodedEncryptedValue);

      return new String(decrypted);

    } catch (Exception e) {
      throw new RuntimeException("Error occurred during decryption: " + e.getMessage());
    }
  }
}
