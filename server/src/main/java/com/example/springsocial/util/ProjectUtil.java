package com.example.springsocial.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;

public class ProjectUtil {


    public static String getMediaUrl(String fileName){
        String encodedFilePath = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        return String.format(Constants.DOWNLOAD_URL, encodedFilePath);
    }

    // Format the LocalDateTime to show year, month, day, and hours,minutes
    public static String convertDateToString(Date date){
        LocalDateTime localDateTime =date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // Format the LocalDateTime to show year, month, day, and hours
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }
    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Format matching your cursor
        return dateFormat.parse(dateString);
    }

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
