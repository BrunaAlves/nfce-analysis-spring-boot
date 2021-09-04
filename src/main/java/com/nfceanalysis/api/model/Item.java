package com.nfceanalysis.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Document(collection = "items")
public class Item implements Serializable {

    @Id
    private String _id;

    private String itemName;

    private String itemCode;

    private int qtdItem;

    private String unItem;

    private float itemValue;

    private Date createdAt;

    private ObjectId nfce;

    private ObjectId categoryId;

    private ObjectId assignedTo;

    public String getNfce() { return nfce.toHexString(); }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getCategoryId() {
        return categoryId != null ? categoryId.toHexString() : null;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getAssignedTo() {
        return assignedTo != null ? assignedTo.toHexString() : null;
    }
}
