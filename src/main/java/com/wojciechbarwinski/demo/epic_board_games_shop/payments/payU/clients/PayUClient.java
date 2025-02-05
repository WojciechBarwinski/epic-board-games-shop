package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUTokenResponseDTO;
import okhttp3.Response;

public interface PayUClient {

    PayUTokenResponseDTO getAccessToken(String grantType,
                                        String clientId,
                                        String clientSecret);

    Response executeOrderRequest(String token,
                                 String orderRequestJSON);
}
