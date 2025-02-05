package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients.PayUClient;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions.PayUAuthException;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions.PayUInternalException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayUAuthTokenProvider {

    private final PayUClient payUClient;

    @Value("${payU.grant_type}")
    private String grant_type;

    @Value("${payU.client_id}")
    private String client_id;

    @Value("${payU.client_secret}")
    private String client_secret;


    public String getAccessToken() {

        try {
            log.info("Attempting to retrieve access token from PayU");
            return payUClient.getAccessToken(grant_type, client_id, client_secret).getAccess_token();
        } catch (FeignException e) {
            log.warn("Failed to retrieve access token: {}", e.getMessage());
            throw new PayUAuthException(e);
        } catch (Exception e) {
            log.warn("Unexpected error occurred during access token retrieval: {}", e.getMessage());
            throw new PayUInternalException(e.getMessage());
        }
    }
}
