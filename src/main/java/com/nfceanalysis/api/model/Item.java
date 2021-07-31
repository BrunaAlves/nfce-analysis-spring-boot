package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Document(collection = "items")
public class Item implements Serializable {

    @Id
    private String _Id;

    private String itemName;

    private String itemCode;

    private int qtdItem;

    private String unItem;

    private int itemValue;

    private Date createdAt;

    private String nfce;
}
