package org.gg.service;

import org.gg.model.Order;
import java.util.Optional;

public interface OrderService {
    Order addOrder(Order order);
    Optional<Order> getOrderByID(String id);
    Order updateOrder(String id, Order order);
    void removeOrder(String id);
}
