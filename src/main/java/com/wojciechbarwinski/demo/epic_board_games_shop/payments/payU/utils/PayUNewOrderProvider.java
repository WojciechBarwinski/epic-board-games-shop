package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentDataDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients.PayUClient;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUOrderRequestDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions.PayUInternalException;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions.PayUOrderExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayUNewOrderProvider {

    private static final int payUCreateOrderCodeResponse = 302;

    private final PayUObjectMapper objectMapper;
    private final PayUClient payUClient;

    public PaymentDataDTO createNewOrder(String authorizationToken, PayUOrderRequestDTO payUOrderRequestDTO) {

        String orderRequestJSON = objectMapper.mapPayUOrderRequestToJSON(payUOrderRequestDTO);

        log.info("Executing HTTP request with order to PayU.");
        try (Response response = payUClient.executeOrderRequest(authorizationToken, orderRequestJSON)) {
            if (response.code() != payUCreateOrderCodeResponse) {
                log.warn("Unsuccessful response from PayU. HTTP status: {}", response.code());
                throw new PayUOrderExecutionException(response.code());
            }

            log.info("Successfully received PayU order response.");
            return objectMapper.mapPayUResponseToPaymentDataDTO(response.body().string());

        } catch (Exception e) {
            log.warn("Unexpected error during request execution: {}", e.getMessage());
            throw new PayUInternalException(e.getMessage());
        }
    }
}
