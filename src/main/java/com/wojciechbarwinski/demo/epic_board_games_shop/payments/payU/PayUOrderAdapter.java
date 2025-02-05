package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU;


import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentDataDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentPort;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUOrderRequestDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils.PayUAuthTokenProvider;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils.PayUNewOrderProvider;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils.PayUOrderCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayUOrderAdapter implements PaymentPort {


    private final PayUAuthTokenProvider payUAuthTokenProvider;
    private final PayUNewOrderProvider payUNewOrderProvider;
    private final PayUOrderCreator payUOrderCreator;

    @Override
    public PaymentDataDTO createPayUOrder(Order order) {

        String accessToken = payUAuthTokenProvider.getAccessToken();
        PayUOrderRequestDTO payUOrderRequestDTO = payUOrderCreator.createPayUOrderDTOFromOrder(order);

        return payUNewOrderProvider.createNewOrder(accessToken, payUOrderRequestDTO);
    }
}

