package com.sample.orderservice.Repository;

import com.sample.orderservice.Entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface OrderItemRepository extends CrudRepository<OrderItem, String>,
        QueryByExampleExecutor<OrderItem>, PagingAndSortingRepository<OrderItem, String> {
}
