package com.sample.orderservice.Event;

import com.sample.orderservice.Entity.Order;
import org.springframework.context.ApplicationEvent;

public class OrderChangeEvent extends ApplicationEvent implements OrderEvent{

    private static final long serialVersionUID = 7484258772054118761L;


    public OrderChangeEvent(Order source) {
        super(source);
    }

    @Override
    public String getOrderId() {
            return ((Order)this.getSource()).getOrderId();
    }
}
