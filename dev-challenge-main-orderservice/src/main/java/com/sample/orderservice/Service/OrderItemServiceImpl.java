package com.sample.orderservice.Service;



import com.sample.orderservice.Entity.OrderItem;
import com.sample.orderservice.Event.*;
import com.sample.orderservice.Exception.NotFoundException;
import com.sample.orderservice.Exception.OrderServiceException;
import com.sample.orderservice.Repository.OrderItemRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("OrderItemService")
public class OrderItemServiceImpl implements  OrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private void publishOrderItemEvent(OrderItemEvent event) {
        applicationEventPublisher.publishEvent(event);
    }



    @Autowired
    public OrderItemServiceImpl(MeterRegistry registry, OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;

        Gauge.builder("orderitemservice.persistency.repository.size", orderItemRepository, OrderItemRepository::count)
                .tag("Repository", "OrderItems").register(registry);

    }

     private void publishOrderEvent(OrderItemEvent event) {
        		applicationEventPublisher.publishEvent(event);}

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        OrderItem savedItem;
        if(orderItem.getItemId() != null) {
            Optional<OrderItem> dbResult = orderItemRepository.findById(orderItem.getItemId());
            if(dbResult.isPresent()) {
                if(dbResult.get().equals(orderItem)) {
                    return dbResult.get();
                }else {
                    savedItem = orderItemRepository.save(orderItem);
                    publishOrderEvent(new OrderItemChangeEvent(savedItem));
                    return savedItem;
                }
            }else {
                savedItem = orderItemRepository.save(orderItem);
                publishOrderEvent(new OrderItemCreateEvent(savedItem));
                return savedItem;
            }
        }else {
            savedItem = orderItemRepository.save(orderItem);
            publishOrderEvent(new OrderItemCreateEvent(savedItem));
            return savedItem;
        }
    }

    @Override
    public OrderItem deltaUpdate(OrderItem orderItem) {
        OrderItem savedItem;
        if (orderItem.getItemId() == null) {
            throw new OrderServiceException("ID of OrderItem must not be null");
        }
        Optional<OrderItem> currentOrderOptional = orderItemRepository.findById(orderItem.getItemId());
        if (currentOrderOptional.isPresent()) {
            OrderItem mergedOrderItem = currentOrderOptional.get();
            mergedOrderItem.setProductID(
                    orderItem.getProductID() == null ? mergedOrderItem.getProductID() : orderItem.getProductID()
            );

            mergedOrderItem.setQuantity(
                    orderItem.getQuantity() == null ? mergedOrderItem.getQuantity() : orderItem.getQuantity()
            );

            mergedOrderItem.setItemPrice(
                    orderItem.getItemPrice() == null ? mergedOrderItem.getItemPrice() : orderItem.getItemPrice()
            );
            currentOrderOptional = orderItemRepository.findById(orderItem.getItemId());

            if(currentOrderOptional.get().equals(mergedOrderItem)) {

                return currentOrderOptional.get();
            } else  {

                savedItem = orderItemRepository.save(mergedOrderItem);
                publishOrderEvent(new OrderItemChangeEvent(orderItem));
                return savedItem;
            }

        } else {
            savedItem = orderItemRepository.save(orderItem);
            publishOrderItemEvent(new OrderItemCreateEvent(orderItem));
            return savedItem;
        }
    }

    @Override
    public Iterable<OrderItem> listOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findOrderItem(String itemId) {
       Optional<OrderItem> result = orderItemRepository.findById(itemId);
        if(!result.isPresent()) {
            throw new NotFoundException(String.format(
                    "Order with id '%s' not found", itemId));
        }

        return result.isPresent() ? result.get() : null;
    }

    @Override
    public void deleteOrderItem(String itemId) {
        if(itemId==null) {
            throw new OrderServiceException("ID of OrderItem must not be null");
        }
        orderItemRepository.deleteById(itemId);
        publishOrderEvent(new OrderItemDeleteEvent(itemId));
    }

    @Override
    public Iterable<OrderItem> listOrderItems(int page, int size) {
        return orderItemRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Iterable<OrderItem> search(OrderItem orderItem) {
        return orderItemRepository.findAll(Example.of(orderItem));
    }

    @Override
    public void deleteAllOrderItems() {
        orderItemRepository.findAll().forEach(order -> {deleteOrderItem(order.getItemId());
        });
    }

    @Override
    public Iterable<OrderItem> search(OrderItem orderItem, int page, int size) {
        return orderItemRepository.findAll(Example.of(orderItem));
    }
}
