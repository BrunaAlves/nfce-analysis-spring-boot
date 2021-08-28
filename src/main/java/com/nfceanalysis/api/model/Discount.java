package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Document(collection = "discounts")
public class Discount {

    @Id
    private String id;

    @NotNull
    private float itemValue;

    @NotNull
    private float discount;

    @NotNull
    private String itemCode;

    @NotNull
    private ObjectId userId;

    @NotNull
    private ObjectId itemId;

    public String getUserId() { return userId.toHexString(); }
    public String getItemId() { return itemId.toHexString(); }
}
