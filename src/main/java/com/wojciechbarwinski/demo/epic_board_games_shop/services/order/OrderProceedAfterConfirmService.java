package com.wojciechbarwinski.demo.epic_board_games_shop.services.order;

import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.OrderResponseDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.OrderStatus;
import com.wojciechbarwinski.demo.epic_board_games_shop.mappers.MapperFacade;
import com.wojciechbarwinski.demo.epic_board_games_shop.messageSenders.MessageSenderPort;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentService;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.PaymentDataDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.repositories.OrderRepository;
import com.wojciechbarwinski.demo.epic_board_games_shop.validations.OrderStatusChangeValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
class OrderProceedAfterConfirmService {

    private final OrderRepository orderRepository;
    private final MapperFacade mapper;
    private final OrderHelper orderHelper;
    private final MessageSenderPort messageSenderPort;
    private final OrderStatusChangeValidation orderStatusChangeValidation;
    private final PaymentService paymentService;

    OrderResponseDTO proceedOrderAfterConfirm(Long id) {
        Order order = orderHelper.getOrderById(id);

        orderStatusChangeValidation.assertOrderStatusTransitionIsAllowed(order.getOrderStatus(), OrderStatus.CONFIRMED);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        log.info("Order with id {} was confirm", order.getId());
        PaymentDataDTO payUOrder = paymentService.createOrderPayment(order);
        //TODO -> Dodać do ORDER pole payU ID [ES-018] i dopisywać je do order
        messageSenderPort.sendOrderConfirmationWithPaymentUrl(order, payUOrder.getRedirectUri());

        orderRepository.save(order);
        return mapper.mapOrderToOrderResponseDTO(order);
    }

}
