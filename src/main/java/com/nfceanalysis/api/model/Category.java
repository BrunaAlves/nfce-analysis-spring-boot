package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Document(collection = "categories")
public class Category implements Serializable {

    @Id
    private String id;
    private String name;
    private ObjectId userId;
    private List<ObjectId> items = new ArrayList<>();

    public String getUserId() { return userId.toHexString(); }

    public List<String> getItems() { return items.stream().map(o -> o.toHexString()).collect(Collectors.toList()); }
}
