package com.example.springsocial.util;

public class Constants {
    public static final int MAX_PAGE_SIZE = 20;

    public static final String  BUCKET_NAME = "lethop-1d201.appspot.com";
    public static final String  DOWNLOAD_URL="https://firebasestorage.googleapis.com/v0/b/"+BUCKET_NAME+"/o/%s?alt=media";
    public static final String NETWORK_URL="https://www.lethop.com/";
    public static final String NETWORK_NAME= "LETHOP";
    public static final String NETWORK_EMAIL= "mohdz2024@hotmail.com";

    public static final String[] AllowedTags = {"motivation","addiction","success",
                                             "education","finance","sport",
                                             "health","religion"};

    public static String[] AllowedCategory = {"good","learn","question"};
    public static String[] AllowedPostSorting ={"createdDate","lastModifiedDate","likesCount","commentsCount","savesCount"};
    public static String[] AllowedCommentSorting ={"createdDate","lastModifiedDate","numberOfReplies"};

}
