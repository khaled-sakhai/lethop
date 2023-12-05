package com.example.springsocial.util;

import java.util.Arrays;
import java.util.UUID;

public class ProjectUtil {

  public static String generateRandomId() {
    return UUID.randomUUID().toString().replace("-", "");
  }


  // this method checks the string is a valid enum
public static <T extends Enum<T>> boolean isInEnum(Class<T> enumClass, String value) {
  try {
      Enum.valueOf(enumClass, value.replaceAll("\\s+", "_").toUpperCase());
      return true;
  } catch (IllegalArgumentException e) {
      return false;
  }
}


}
