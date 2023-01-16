package com.sample.orderservice.Repository;

import com.sample.orderservice.Entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface OrderRepository extends CrudRepository<Order, String>,
        QueryByExampleExecutor<Order>, PagingAndSortingRepository<Order, String> {
}
