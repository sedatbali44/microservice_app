package com.sample.orderservice.Service;


import com.sample.orderservice.Event.OrderChangeEvent;
import com.sample.orderservice.Event.OrderCreateEvent;
import com.sample.orderservice.Event.OrderDeleteEvent;
import com.sample.orderservice.Event.OrderEvent;
import com.sample.orderservice.Entity.Order;
import com.sample.orderservice.Exception.NotFoundException;
import com.sample.orderservice.Exception.OrderServiceException;
import com.sample.orderservice.Repository.OrderRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;
//import org.sample.contactservice.entity.Person;

@Service("OrderService")
public class OrderServiceImpl implements OrderService{

    private OrderRepository repository;

    //private Person  person;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    //
    @Autowired
    public OrderServiceImpl(MeterRegistry registry, OrderRepository repository) {
        this.repository = repository;

        Gauge.builder("orderservice.persistency.repository.size", repository, OrderRepository::count)
                .tag("Repository", "Orders").register(registry);

    }


    private void publishOrderEvent(OrderEvent event) {
    		applicationEventPublisher.publishEvent(event);
    }


    @Override
    public Order saveOrder(Order order) {
        Order savedOrder;
        if(order.getOrderId() != null) {
            Optional<Order> dbResult = repository.findById(order.getOrderId());
            if(dbResult.isPresent()) {
                 if(dbResult.get().equals(order)) {
                     return dbResult.get();
                 }else {
                     savedOrder = repository.save(order);
//                     if(person.getFirstName() == order.getSoldTo()) {
//                       System.out.println("the customer is already existing");
//                     }
                     publishOrderEvent(new OrderChangeEvent(savedOrder));
                     return savedOrder;
                 }
            }else {
                savedOrder = repository.save(order);
                publishOrderEvent(new OrderCreateEvent(savedOrder));
                return savedOrder;
            }
        }else {
            savedOrder = repository.save(order);
            publishOrderEvent(new OrderCreateEvent(savedOrder));
            return savedOrder;
        }
    }

    @Override
    public Order deltaUpdate(Order order) {
        Order savedOrder;
        if (order.getOrderId() == null) {
            throw new OrderServiceException("ID of Order must not be null");
        }
        Optional<Order> currentOrderOptional = repository.findById(order.getOrderId());
        if (currentOrderOptional.isPresent()) {
            Order mergedOrder = currentOrderOptional.get();
            mergedOrder.setOrderDate(
                    order.getOrderDate() == null ? mergedOrder.getOrderDate() : order.getOrderDate()
            );

            mergedOrder.setSoldTo(
                    order.getSoldTo() == null ? mergedOrder.getSoldTo() : order.getSoldTo()
            );

            mergedOrder.setBillTo(
                    order.getBillTo() == null ? mergedOrder.getBillTo() : order.getBillTo()
            );
            mergedOrder.setShipTo(
                    order.getShipTo() == null ? mergedOrder.getShipTo() : order.getShipTo()
            );
            mergedOrder.setOrderValue(
                    order.getOrderValue() == null ? mergedOrder.getOrderValue() : order.getOrderValue()
            );
            mergedOrder.setTaxValue(
                    order.getTaxValue() == null ? mergedOrder.getTaxValue() : order.getTaxValue()
            );
            mergedOrder.setCurrencyCode(
                    order.getCurrencyCode() == null ? mergedOrder.getCurrencyCode() : order.getCurrencyCode()
            );
            order.getExtensionFields()
                    .forEach((key, value) -> mergedOrder.getExtensionFields().put(key, value));

            currentOrderOptional = repository.findById(order.getOrderId());

            if(currentOrderOptional.get().equals(mergedOrder)) {

                return currentOrderOptional.get();
            } else  {

                savedOrder = repository.save(mergedOrder);
                publishOrderEvent(new OrderChangeEvent(order));
                return savedOrder;
            }

        } else {
            savedOrder = repository.save(order);
            publishOrderEvent(new OrderCreateEvent(order));
            return savedOrder;
        }
    }

    @Override
    public Iterable<Order> listOrders() {
        return repository.findAll();
    }

    @Override
    public Order findOrder(String id) {
        Optional<Order> result = repository.findById(id);
        if(!result.isPresent()) {
            throw new NotFoundException(String.format(
                    "Order with id '%s' not found", id));
        }

        return result.isPresent() ? result.get() : null;
    }

    @Override
    public void deleteOrder(String id) {
         if(id==null) {
             throw new OrderServiceException("ID of Order must not be null");
         }
         repository.deleteById(id);
         publishOrderEvent(new OrderDeleteEvent(id));
    }



    @Override
    public Iterable<Order> listOrders(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Iterable<Order> search(Order order, int page, int size) {
        return repository.findAll(Example.of(order), PageRequest.of(page, size));
    }

    @Override
    public Iterable<Order> search(Order order) {
        return repository.findAll(Example.of(order));
    }

    @Override
    public void deleteAllOrders() {
        repository.findAll().forEach(order -> {deleteOrder(order.getOrderId());
        });
    }
}
