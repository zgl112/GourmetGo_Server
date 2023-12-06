package org.gg.service;

import org.gg.model.Order;
import org.gg.repository.OrderRepository;
import org.gg.utils.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Override
    public Order addOrder(Order order) {return orderRepository.save(order);}

    @Override
    public Optional<Order> getOrderByID(String id) {return orderRepository.findById(id);}

    @Override
    public Order updateOrder(String id, Order order) {
        // Find existing Restaurant
        Optional<Order> optionalOrder = orderRepository.findById(id);

        // Patches in updated fields to new object and uses existing values if null.
        if (optionalOrder.isPresent()) {
            Order currentOrder = optionalOrder.get();
            BeanUtils.copyProperties(order, currentOrder, BeanUtil.getNullPropertyNames(order));
            // Matches ID
            currentOrder.setId(id);

            return orderRepository.save(currentOrder);
        }
        return null;
    }

    @Override
    public void removeOrder(String id) { orderRepository.deleteById(id);}
}
