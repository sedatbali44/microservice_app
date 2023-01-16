package com.sample.orderservice.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 49994416335608157L;

    @Id
    @ApiModelProperty(name="ID", example="1060f9e4",
            notes="Unique MongoDB identifier")
    private String orderId;


    @ApiModelProperty(name="Order Date", example="2021-07-17",
           notes="Date of Order Capture")
    private String orderDate;

    @ApiModelProperty(name="Sold To", example="Salih Bali",
            notes="Person whom the order was sold to")
    private String soldTo;

    @ApiModelProperty(name="Bill To", example="Salih Bali",
            notes="Person whom the order was billed to")
    private String billTo;

    @ApiModelProperty(name="Ship To", example="Salih Bali",
            notes="Person whom the order was shipped to")
    private String shipTo;

    @ApiModelProperty(name="Order Value", example="107.26",
            notes="Value of the Order incl. taxes")
    private Number orderValue;

    @ApiModelProperty(name="Tax Value", example="10.1",
            notes="Total taxes of the Order")
    private Number taxValue;

    @ApiModelProperty(name="Currency Code", example="CHF",
            notes="Order Currency")
    private String currencyCode;

    @ApiModelProperty(name="Extension Fields",
            notes="Arbitrary json key value pairs")
    private Map<String, Object> extensionFields = new HashMap<String, Object>();
}
