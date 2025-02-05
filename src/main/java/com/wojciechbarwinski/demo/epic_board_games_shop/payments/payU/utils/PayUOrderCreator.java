package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.utils;

import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.OrderLine;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUOrderRequestDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUProductDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PayUOrderCreator {

    private final HttpServletRequest request;

    @Value("${payU.currencyCode}")
    private String currencyCode;

    @Value("${payU.order_description}")
    private String description;

    @Value("${payU.merchant_posId}")
    private String merchantPosId;

    public PayUOrderRequestDTO createPayUOrderDTOFromOrder(Order order) {
        PayUOrderRequestDTO orderRequestDTO = new PayUOrderRequestDTO();
        orderRequestDTO.setCustomerIp(request.getRemoteAddr());
        orderRequestDTO.setMerchantPosId(merchantPosId);
        orderRequestDTO.setDescription(description);
        orderRequestDTO.setCurrencyCode(currencyCode);
        orderRequestDTO.setTotalAmount(convertToPayUAmount(order.getTotalPrice()));

        List<PayUProductDTO> products = new ArrayList<>();

        for (OrderLine orderLine : order.getOrderLines()) {
            products.add(new PayUProductDTO(
                    orderLine.getProduct().getName(),
                    convertToPayUAmount(orderLine.getProduct().getPrice()),
                    orderLine.getQuantity()));
        }

        orderRequestDTO.setProducts(products);

        return orderRequestDTO;
    }

    private long convertToPayUAmount(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
