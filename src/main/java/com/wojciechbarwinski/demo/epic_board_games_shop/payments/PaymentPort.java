package com.wojciechbarwinski.demo.epic_board_games_shop.payments;

import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;

public interface PaymentPort {

    PaymentDataDTO createPayUOrder(Order order);
}
