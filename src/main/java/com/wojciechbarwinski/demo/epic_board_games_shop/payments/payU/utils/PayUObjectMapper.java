package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentDataDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
class PayUObjectMapper {

    private final ObjectMapper objectMapper;

    String mapPayUOrderRequestToJSON(PayUOrderRequestDTO orderRequest) {
        try {
            return objectMapper.writeValueAsString(orderRequest);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing order request: {}", e.getMessage());
            throw new RuntimeException("Error serializing order request: " + e.getMessage(), e);
        }
    }

    PaymentDataDTO mapPayUResponseToPaymentDataDTO(String response) {
        try {
            return objectMapper.readValue(response, PaymentDataDTO.class);
        } catch (IOException e) {
            log.warn("Error deserializing PayU response: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
