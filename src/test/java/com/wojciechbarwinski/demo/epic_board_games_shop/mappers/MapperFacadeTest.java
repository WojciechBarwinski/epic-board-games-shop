package com.wojciechbarwinski.demo.epic_board_games_shop.mappers;

import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.AddressDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.OrderLineDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.OrderRequestDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.OrderResponseDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Address;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.OrderLine;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperFacadeTest {

    MapperFacade mapperFacade = new MapperFacade(new OrderMapper());

    @Test
    void shouldMapOrderDTOToOrderEntity(){
        //given
        AddressDTO addressDTO = new AddressDTO("Ulica", "Miasto", "12-345", "123-456-789");
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("orderer mail",
                addressDTO,
                List.of(new OrderLineDTO(1L, 2)));

        //when
        Order order = mapperFacade.mapOrderDTOToOrderEntity(orderRequestDTO);

        //then
        assertNotNull(order);
        assertNull(order.getId()); //mapper should not map ID because don't have it
        assertNull(order.getOrderLines()); //mapper should not map order lines

        assertEquals(orderRequestDTO.getOrdererMail(), order.getOrdererMail());
        assertEquals(addressDTO.street(), order.getAddress().getStreet());
        assertEquals(addressDTO.city(), order.getAddress().getCity());
        assertEquals(addressDTO.zipCode(), order.getAddress().getZipCode());
        assertEquals(addressDTO.phoneNumber(), order.getAddress().getPhoneNumber());
    }

    @Test
    void shouldMapOrderDTOToOrderEntityWithNullValues(){
        //given
        AddressDTO addressDTO = new AddressDTO(null, null, null, null);
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("orderer mail",
                addressDTO,
                null);

        //when
        Order order = mapperFacade.mapOrderDTOToOrderEntity(orderRequestDTO);

        //then
        assertNotNull(order);
        assertNull(order.getId()); //mapper should not map ID because don't have it
        assertNull(order.getOrderLines()); //mapper should not map order lines
        assertEquals(orderRequestDTO.getOrdererMail(), order.getOrdererMail());
        assertNotNull(order.getAddress());
        assertNull(order.getAddress().getStreet());
        assertNull(order.getAddress().getCity());
        assertNull(order.getAddress().getZipCode());
        assertNull(order.getAddress().getPhoneNumber());
    }

    @Test
    void shouldMapOrderDTOToOrderEntityWithEmptyStrings(){
        //given
        AddressDTO addressDTO = new AddressDTO("", "", "", "");
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("orderer mail",
                addressDTO,
                List.of(new OrderLineDTO(1L, 2)));

        //when
        Order order = mapperFacade.mapOrderDTOToOrderEntity(orderRequestDTO);

        //then
        assertNotNull(order);
        assertNull(order.getId()); //mapper should not map ID because don't have it
        assertNull(order.getOrderLines()); //mapper should not map order lines

        assertEquals(orderRequestDTO.getOrdererMail(), order.getOrdererMail());
        assertEquals(addressDTO.street(), order.getAddress().getStreet());
        assertEquals(addressDTO.city(), order.getAddress().getCity());
        assertEquals(addressDTO.zipCode(), order.getAddress().getZipCode());
        assertEquals(addressDTO.phoneNumber(), order.getAddress().getPhoneNumber());
    }

    @Test
    void shouldMapOrderToOrderResponseDTO(){

        Order order = createOrder();

        OrderResponseDTO orderResponseDTO = mapperFacade.mapOrderToOrderResponseDTO(order);

        assertNotNull(orderResponseDTO);
        assertEquals(order.getEmployeeId(), orderResponseDTO.getSellerId());
        assertEquals(order.getTotalPrice(), orderResponseDTO.getTotalPrice());
        assertEquals(order.getOrdererMail(), orderResponseDTO.getOrdererMail());
        assertEquals(order.getAddress().getStreet() , orderResponseDTO.getAddressToSend().street());
        assertEquals(order.getAddress().getCity() , orderResponseDTO.getAddressToSend().city());
        assertEquals(order.getAddress().getZipCode() , orderResponseDTO.getAddressToSend().zipCode());
        assertEquals(order.getAddress().getPhoneNumber() , orderResponseDTO.getAddressToSend().phoneNumber());
        assertEquals(1, order.getOrderLines().size());
        assertEquals(order.getOrderLines().get(0).getQuantity(), orderResponseDTO.getOrderLineDTOs().get(0).quantity());
        assertEquals(order.getOrderLines().get(0).getProduct().getId(), orderResponseDTO.getOrderLineDTOs().get(0).productId());

    }

    //TODO ES-005-TESTS : when address is null, when OrderLines is null/empty

    private static Order createOrder() {
        Address address = Address.builder()
                .street("ulica")
                .city("miasto")
                .zipCode("12-345")
                .phoneNumber("123-456-789")
                .build();

        Order order = Order.builder()
                .id(1L)
                .totalPrice(BigDecimal.valueOf(200))
                .ordererMail("orderer mail")
                .employeeId("employee mail")
                .address(address)
                .build();

        Product product = Product.builder()
                .name("product name")
                .id(1L)
                .price(BigDecimal.valueOf(200))
                .build();

        OrderLine orderLine = OrderLine.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .build();

        order.setOrderLines(List.of(orderLine));

        return order;
    }
}