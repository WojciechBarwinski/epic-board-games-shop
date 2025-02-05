package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions.PayUNetworkException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
class PayUOrderClient {

    private static final String CONTENT_TYPE_JSON = "application/json";

    private final OkHttpClient client;

    @Value("${payU.order_url}")
    private String url;


    Response executeOrderRequest(String authorizationToken, String orderRequestJSON) {

        Request request = buildHttpRequest(authorizationToken, orderRequestJSON);

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            log.warn("Network error during request execution: {}", e.getMessage());
            throw new PayUNetworkException(e.getMessage());
        }
    }

    private Request buildHttpRequest(String authorizationToken, String jsonRequestBody) {

        RequestBody requestBody = RequestBody.create(jsonRequestBody,
                MediaType.parse(CONTENT_TYPE_JSON));

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authorizationToken)
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
    }
}


