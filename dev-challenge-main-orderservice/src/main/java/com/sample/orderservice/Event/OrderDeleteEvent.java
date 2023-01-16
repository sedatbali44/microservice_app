package com.sample.orderservice.Event;

import org.springframework.context.ApplicationEvent;

public class OrderDeleteEvent extends ApplicationEvent implements OrderEvent{

    private static final long serialVersionUID = 7485258772054118891L;

    public OrderDeleteEvent(String source) {
        super(source);
    }

    @Override
    public String getOrderId() {
        return (String)this.getSource();
    }

}
