package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OkHttpClientConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .followRedirects(false)
                .build();
    }
}
