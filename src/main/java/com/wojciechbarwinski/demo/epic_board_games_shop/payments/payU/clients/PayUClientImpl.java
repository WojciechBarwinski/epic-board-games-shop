package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayUClientImpl implements PayUClient {

    private final PayUAuthClient payUAuthClient;
    private final PayUOrderClient payUOrderClient;

    @Override
    public PayUTokenResponseDTO getAccessToken(String grantType, String clientId, String clientSecret) {
        return payUAuthClient.getAccessToken(grantType, clientId, clientSecret);
    }

    @Override
    public Response executeOrderRequest(String token, String orderRequestJSON) {
        return payUOrderClient.executeOrderRequest(token, orderRequestJSON);
    }
}
