package com.wojciechbarwinski.demo.epic_board_games_shop.services;

import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.OrderStatus;
import com.wojciechbarwinski.demo.epic_board_games_shop.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
class OrderCancelService {

    private final OrderRepository orderRepository;
    private final OrderHelper orderHelper;
    private final ProductServicesFacade productServicesFacade;

    void cancelOrder(String codeId) {
        Long id = Long.parseLong(codeId);// method to read codeId from link as order ID
        Order order = orderHelper.getOrderById(id);

        order.setOrderStatus(OrderStatus.CANCELLED);
        log.info("Order with id {} was cancelled", order.getId());
        //method to start back-cash process
        orderRepository.save(order);
        productServicesFacade.increaseProductQuantity(order.getOrderLines());
    }
}
