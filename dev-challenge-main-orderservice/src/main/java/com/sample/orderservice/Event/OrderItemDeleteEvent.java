package com.sample.orderservice.Event;

import org.springframework.context.ApplicationEvent;

public class OrderItemDeleteEvent extends ApplicationEvent implements OrderItemEvent{

    private static final long serialVersionUID = 7485258772054118891L;

    public OrderItemDeleteEvent(String source) {
        super(source);
    }

    @Override
    public String getItemId() {
        return (String)this.getSource();
    }
}
