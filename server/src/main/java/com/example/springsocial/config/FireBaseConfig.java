package com.example.springsocial.config;

import com.example.springsocial.service.FireBaseService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfig {

    private final String BUCKET_NAME = "lethop-1d201.appspot.com";
    private final String CONF_FILE="lethop-1d201-firebase-adminsdk-hbtmd-dd23869f6e.json";

    @Bean
    public FirebaseApp fireBaseInit() throws IOException {
        InputStream inputStream = FireBaseService.class.getClassLoader().getResourceAsStream(CONF_FILE); //
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setStorageBucket(BUCKET_NAME)
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
