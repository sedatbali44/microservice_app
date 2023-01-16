package com.sample.orderservice.Rest;

import com.sample.orderservice.Entity.Order;
import com.sample.orderservice.Service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/order")
public class OrderApi {

    @Autowired
    private OrderService orderService;
    @PostMapping
    @ApiOperation(value="Create Order", notes="This function creates a new order entry on the DB."
            + "Externally supplied ids are ignored.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> create(@RequestBody Order order) {
        order.setOrderId(null);
        return new ResponseEntity<Order>(orderService.saveOrder(order), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value="Get Order List", notes="This function retrieves all order from the Database.")
    public ResponseEntity<Order[]> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size ) {

        if (page == null || size == null) {
            return new ResponseEntity<Order[]>(
                    StreamSupport.stream(orderService.listOrders().spliterator(), false)
                            .toArray(Order[]::new),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Order[]>(
                    StreamSupport.stream(orderService.listOrders(page.intValue(),size.intValue()).spliterator(), false)
                            .toArray(Order[]::new),
                    HttpStatus.OK);
        }
    }

    @PutMapping(path="/{id}")
    @ApiOperation(value="Update Order", notes="This function updates a order from on the database. "
            + "This does not support delta updates.")
    public ResponseEntity<Order> upsert(@PathVariable("id") String id,
                                         @RequestBody Order order) {

        order.setOrderId(id);

        return new ResponseEntity<Order>(orderService.saveOrder(order), HttpStatus.OK);
    }
    @PatchMapping(path="/{id}")
    @ApiOperation(value="Update Order", notes="This function updates a order from on the database. "
            + "This does support delta updates.")
    public ResponseEntity<Order> deltaUpdate(@PathVariable("id") String id,
                                              @RequestBody Order order) {

        order.setOrderId(id);
        return new ResponseEntity<Order>(orderService.deltaUpdate(order), HttpStatus.OK);
    }
    @GetMapping(path="/{id}")
    @ApiOperation(value="Read Order by ID", notes="This function reads a single order from the database.")
    public ResponseEntity<Order> read(@PathVariable("id") String id) {

        return new ResponseEntity<Order>(orderService.findOrder(id), HttpStatus.OK);
    }

    @DeleteMapping(path="/{id}")
    @ApiOperation(value="Delete Order by ID", notes="This function deletes a order from the database.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @ApiOperation(value="Delete all Orders in DB", notes="This function deletes all  orders from the database.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        orderService.deleteAllOrders();
        return ResponseEntity.noContent().build();

    }

    @PostMapping(path="/search")
    @ApiOperation(value="Search for Order", notes="This function performs a people search in the database, and returns the order that meet the criteria " +
            "specified in the request.")
    public ResponseEntity<Order[]> search(@RequestBody Order order,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            return new ResponseEntity<Order[]>(
                    StreamSupport.stream(orderService.search(order).spliterator(), false)
                            .toArray(Order[]::new),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Order[]>(
                    StreamSupport.stream(orderService.search(order, page, size).spliterator(), false)
                            .toArray(Order[]::new),
                    HttpStatus.OK);
        }
    }

}
