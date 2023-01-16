package com.sample.orderservice.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 49994416335608157L;

    @Id
    @ApiModelProperty(name="ID", example="0001",
            notes="Identifier of the order item")
    private String itemId;

    @ApiModelProperty(name="Product ID", example="100000101",
            notes="Identifier of the product")
    private String productID;

    @ApiModelProperty(name="Order Value", example="10",
            notes="Quantity of the product")
    private Number quantity;

    @ApiModelProperty(name="Item Price", example="107.26",
            notes="Value of the Order incl. taxes")
    private Number itemPrice;


}
