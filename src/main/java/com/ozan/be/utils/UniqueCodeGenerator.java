package com.ozan.be.utils;

import java.security.SecureRandom;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniqueCodeGenerator {
  private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
  private static final Random RANDOM = new SecureRandom();

  public static String generateUniqueCode(int length) {
    StringBuilder code = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      code.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
    }
    return code.toString();
  }
}
