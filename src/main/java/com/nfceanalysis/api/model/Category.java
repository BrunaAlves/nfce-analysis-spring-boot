package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "categories")
public class Category implements Serializable {

    @Id
    private String id;
    private String name;
}
