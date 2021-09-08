package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Document(collection = "discounts")
public class Discount {

    @Id
    private String id;

    @NotBlank
    private float itemValue;

    @NotBlank
    private float discountValue;

    @NotBlank
    private String itemCode;

    @NotBlank
    private ObjectId userId;

    @NotBlank
    private ObjectId itemId;

    public String getUserId() { return userId.toHexString(); }
    public String getItemId() { return itemId.toHexString(); }
}
