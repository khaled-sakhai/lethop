package com.example.springsocial.enums;

import java.util.Arrays;

public enum Tag {
  FINANCE,
  SUCCESS,
  HEALTH,
  EDUCATION,
  CAREER,
  ADDICTION,
  HOPE,
  MENTAL_HEALTH;

   public static boolean isInEnum(String value) {
     return Arrays.stream(Tag.values()).anyMatch(e -> e.name().equalsIgnoreCase(value));
}
}
