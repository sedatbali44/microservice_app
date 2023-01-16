package com.sample.orderservice.Service;


import com.sample.orderservice.Entity.OrderItem;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItem deltaUpdate(OrderItem orderItem);

    Iterable<OrderItem> listOrderItems();

    OrderItem findOrderItem(String id);

    void deleteOrderItem(String id);

    Iterable<OrderItem> search(OrderItem orderItem);

    void deleteAllOrderItems();

    Iterable<OrderItem> listOrderItems(int page, int size);

    Iterable<OrderItem> search(OrderItem orderItem, int page, int size);
}
