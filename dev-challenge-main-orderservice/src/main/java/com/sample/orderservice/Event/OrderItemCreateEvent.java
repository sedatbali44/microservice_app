package com.sample.orderservice.Event;

import com.sample.orderservice.Entity.OrderItem;
import org.springframework.context.ApplicationEvent;

public class OrderItemCreateEvent extends ApplicationEvent implements OrderItemEvent{
    private static final long serialVersionUID = 7484258772054118761L;
    public OrderItemCreateEvent(OrderItem source) {
        super(source);
    }

    @Override
    public String getItemId() {
        return ((OrderItem)this.getSource()).getItemId();
    }
}
