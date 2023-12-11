package org.gg.service;

import org.gg.model.Order;
import org.gg.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.addOrder(order);

        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrderByID() {
        String orderId = "123";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderByID(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testUpdateOrder() {
        String orderId = "123";
        Order existingOrder = new Order();
        Order updatedOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.updateOrder(orderId, updatedOrder);

        assertNotNull(result);
        assertEquals(existingOrder, result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void testRemoveOrder() {
        String orderId = "123";

        orderService.removeOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}