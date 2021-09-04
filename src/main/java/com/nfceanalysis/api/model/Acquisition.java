package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "acquisitions")
public class Acquisition {

    @Id
    private String id;

    private String name;
    private ObjectId userId;
    private Frequency frequency;
    private Date lastPurchase;
    private Date nextPurchase;

    private List<String> itemCodes = new ArrayList<>();

    public String getUserId() { return userId.toHexString(); }
}
