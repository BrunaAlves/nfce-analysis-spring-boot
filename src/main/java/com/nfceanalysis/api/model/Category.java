package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "categories")
public class Category implements Serializable {

    @Id
    private String id;

    @NotNull
    private String name;
    private ObjectId userId;
    private List<String> itemCodes = new ArrayList<>();

    public String getUserId() { return userId.toHexString(); }
}
