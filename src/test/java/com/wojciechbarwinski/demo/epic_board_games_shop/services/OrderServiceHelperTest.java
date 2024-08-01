package com.wojciechbarwinski.demo.epic_board_games_shop.services;

import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.AddressDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.CreateOrderRequestDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.dtos.OrderLineDTO;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Address;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Order;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.OrderStatus;
import com.wojciechbarwinski.demo.epic_board_games_shop.entities.Product;
import com.wojciechbarwinski.demo.epic_board_games_shop.exceptions.InsufficientStockException;
import com.wojciechbarwinski.demo.epic_board_games_shop.exceptions.ProductsNotFoundException;
import com.wojciechbarwinski.demo.epic_board_games_shop.repositories.ProductRepository;
import com.wojciechbarwinski.demo.epic_board_games_shop.security.AuthenticationComponent;
import com.wojciechbarwinski.demo.epic_board_games_shop.security.exceptions.InvalidSellerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceHelperTest {


    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuthenticationComponent authenticationComponent;

    @InjectMocks
    private OrderServiceHelper orderHelper;

    @BeforeEach
    void setUp() {
    }


    @Test
    void shouldPrepareOrderFromOrderDTOToSave() {
        //given
        CreateOrderRequestDTO orderDTO = createCorrectOrderRequestDTO();
        Order order = createOrderInStageAfterMapping();
        String employeeId = "username@mail";
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(175); // 2 x 50 and 3 x 25 look into createProductsListForMock

        when(authenticationComponent.getSellerId()).thenReturn(employeeId);
        when(productRepository.findAllById(Mockito.any())).thenReturn(createProductsListForMock());

        //when
        Order preparedOrder = orderHelper.prepareOrderToSave(orderDTO, order);

        //then
        assertEquals(2, preparedOrder.getOrderLines().size());
        assertEquals(expectedTotalPrice, preparedOrder.getTotalPrice());
        assertEquals(OrderStatus.PLACED, preparedOrder.getOrderStatus());
        assertEquals(employeeId, preparedOrder.getEmployeeId());
        assertEquals(2, preparedOrder.getOrderLines().get(0).getQuantity());
    }


    @Test
    void shouldThrowExceptionBecauseThereIsNoAuthUserWhenOrderIsPlace() {
        //given
        // setUpSecurityContextForTests(false);
        CreateOrderRequestDTO orderDTO = createCorrectOrderRequestDTO();
        Order order = createOrderInStageAfterMapping();
        String expectedMessage = "No authenticated user found.";

        when(authenticationComponent.getSellerId()).thenThrow(new InvalidSellerException());
        when(productRepository.findAllById(Mockito.any())).thenReturn(createProductsListForMock());

        //when
        InvalidSellerException exception = assertThrows(InvalidSellerException.class,
                () -> orderHelper.prepareOrderToSave(orderDTO, order));

        //then
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionBecauseInOrderThereIsProductIdWithIsNotInDB() {
        //given
        Long incorrectId = 4L; //more than 3
        CreateOrderRequestDTO orderDTO = createOrderRequestDTOWithIncorrectProductId(incorrectId);
        Order order = createOrderInStageAfterMapping();
        String expectedMessage = "There are missing products with id = " + incorrectId;

        when(productRepository.findAllById(Mockito.any())).thenReturn(createProductsListForMock());

        //when
        ProductsNotFoundException exception = assertThrows(ProductsNotFoundException.class,
                () -> orderHelper.prepareOrderToSave(orderDTO, order));

        //then
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionBecauseInOrderThereIsWrongQuantity() {
        //given
        OrderLineDTO orderLineWithWrongQuantity = new OrderLineDTO(3L, 100);
        CreateOrderRequestDTO orderDTO = createOrderRequestDTOWithWrongQuantity(orderLineWithWrongQuantity);
        Order order = createOrderInStageAfterMapping();
        String expectedMessage = "There is incorrect data about stock of this items = 3";

        when(productRepository.findAllById(Mockito.any())).thenReturn(createProductsListForMock());

        //when
        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> orderHelper.prepareOrderToSave(orderDTO, order));

        //then
        assertEquals(expectedMessage, exception.getMessage());
    }

    private List<Product> createProductsListForMock() {
        Product product1 = Product.builder()
                .id(1L)
                .name("first product name")
                .price(BigDecimal.valueOf(50))
                .quantity(10)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("second product name")
                .price(BigDecimal.valueOf(25))
                .quantity(10)
                .build();
        Product product3 = Product.builder()
                .id(3L)
                .name("third product name")
                .price(BigDecimal.valueOf(25))
                .quantity(10)
                .build();

        return List.of(product1, product2, product3);
    }

    private Order createOrderInStageAfterMapping() {
        Address address = Address.builder()
                .street("Street")
                .city("City")
                .zipCode("ZipCode")
                .phoneNumber("PhoneNumber")
                .build();

        return Order.builder()
                .ordererMail("orderer@example.com")
                .address(address)
                .build();
    }

    private CreateOrderRequestDTO createCorrectOrderRequestDTO() {
        AddressDTO addressToSend = new AddressDTO("Street", "City", "ZipCode", "PhoneNumber");
        OrderLineDTO orderLine1 = new OrderLineDTO(1L, 2);
        OrderLineDTO orderLine2 = new OrderLineDTO(2L, 3);

        return new CreateOrderRequestDTO("orderer@example.com", addressToSend, List.of(orderLine1, orderLine2));
    }

    private CreateOrderRequestDTO createOrderRequestDTOWithIncorrectProductId(Long incorrectId) {
        AddressDTO addressToSend = new AddressDTO("Street", "City", "ZipCode", "PhoneNumber");
        OrderLineDTO orderLine1 = new OrderLineDTO(1L, 2);
        OrderLineDTO orderLine2 = new OrderLineDTO(incorrectId, 3);

        return new CreateOrderRequestDTO("orderer@example.com", addressToSend, List.of(orderLine1, orderLine2));
    }

    private CreateOrderRequestDTO createOrderRequestDTOWithWrongQuantity(OrderLineDTO orderLine) {
        AddressDTO addressToSend = new AddressDTO("Street", "City", "ZipCode", "PhoneNumber");
        OrderLineDTO orderLine1 = new OrderLineDTO(1L, 5);
        OrderLineDTO orderLine2 = new OrderLineDTO(2L, 5);

        return new CreateOrderRequestDTO("orderer@example.com", addressToSend, List.of(orderLine1, orderLine2, orderLine));
    }
}