package com.example.springsocial.util;

import java.util.UUID;

public class ProjectUtil {

  public static String generateRandomId() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
