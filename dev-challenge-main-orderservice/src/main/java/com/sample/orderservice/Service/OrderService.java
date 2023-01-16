package com.sample.orderservice.Service;

import com.sample.orderservice.Entity.Order;

public interface OrderService {

    Order saveOrder(Order order);

    Order deltaUpdate(Order order);

    Iterable<Order> listOrders();

    Order findOrder(String id);

    void deleteOrder(String id);

    Iterable<Order> search(Order order);

    void deleteAllOrders();

    Iterable<Order> listOrders(int page, int size);

    Iterable<Order> search(Order order, int page, int size);
}
