package com.sample.orderservice.Rest;

import com.sample.orderservice.Entity.OrderItem;
import com.sample.orderservice.Service.OrderItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/orderItem")
public class OrderItemApi {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping
    @ApiOperation(value="Create Order", notes="This function creates a new order entry on the DB."
            + "Externally supplied ids are ignored.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderItem> create(@RequestBody OrderItem orderItem) {
        orderItem.setItemId(null);
        return new ResponseEntity<OrderItem>(orderItemService.saveOrderItem(orderItem), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value="Get Order List", notes="This function retrieves all order from the Database.")
    public ResponseEntity<OrderItem[]> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size ) {

        if (page == null || size == null) {
            return new ResponseEntity<OrderItem[]>(
                    StreamSupport.stream(orderItemService.listOrderItems().spliterator(), false)
                            .toArray(OrderItem[]::new),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<OrderItem[]>(
                    StreamSupport.stream(orderItemService.listOrderItems(page.intValue(),size.intValue()).spliterator(), false)
                            .toArray(OrderItem[]::new),
                    HttpStatus.OK);
        }
    }


    @PutMapping(path="/{itemId}")
    @ApiOperation(value="Update Order item", notes="This function updates a order from on the database. "
            + "This does not support delta updates.")
    public ResponseEntity<OrderItem> upsert(@PathVariable("itemId") String itemId,
                                        @RequestBody OrderItem orderItem) {

        orderItem.setItemId(itemId);

        return new ResponseEntity<OrderItem>(orderItemService.saveOrderItem(orderItem), HttpStatus.OK);
    }

    @PatchMapping(path="/{itemId}")
    @ApiOperation(value="Update Order item", notes="This function updates a order from on the database. "
            + "This does support delta updates.")
    public ResponseEntity<OrderItem> deltaUpdate(@PathVariable("itemId") String itemId,
                                             @RequestBody OrderItem orderItem) {

        orderItem.setItemId(itemId);
        return new ResponseEntity<OrderItem>(orderItemService.deltaUpdate(orderItem), HttpStatus.OK);
    }

    @GetMapping(path="/{itemId}")
    @ApiOperation(value="Read Order item by ID", notes="This function reads a single order from the database.")
    public ResponseEntity<OrderItem> read(@PathVariable("itemId") String itemId) {

        return new ResponseEntity<OrderItem>(orderItemService.findOrderItem(itemId), HttpStatus.OK);
    }

    @DeleteMapping(path="/{itemId}")
    @ApiOperation(value="Delete Order item by ID", notes="This function deletes a order from the database.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("itemId") String itemId) {
        orderItemService.deleteOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @ApiOperation(value="Delete all Order items in DB", notes="This function deletes all  orders from the database.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        orderItemService.deleteAllOrderItems();
        return ResponseEntity.noContent().build();

    }

    @PostMapping(path="/search")
    @ApiOperation(value="Search for Order items", notes="This function performs a people search in the database, and returns the order that meet the criteria " +
            "specified in the request.")
    public ResponseEntity<OrderItem[]> search(@RequestBody OrderItem orderItem,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            return new ResponseEntity<OrderItem[]>(
                    StreamSupport.stream(orderItemService.search(orderItem).spliterator(), false)
                            .toArray(OrderItem[]::new),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<OrderItem[]>(
                    StreamSupport.stream(orderItemService.search(orderItem, page, size).spliterator(), false)
                            .toArray(OrderItem[]::new),
                    HttpStatus.OK);
        }
    }


}
