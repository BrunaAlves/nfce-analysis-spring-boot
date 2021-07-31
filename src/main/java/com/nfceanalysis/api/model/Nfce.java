package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "nfces")
public class Nfce implements Serializable {

    @Id
    private String _Id;

    private List<String> items = new ArrayList<>();

    private String url;

    private String user;

    private Date createdAt;

    private String totalItems;

    private String totalValue;

    private String paidValue;

    private String typePayment;

    private String accesskey;

    private String socialName;

    private String cnpj;

    private String stateRegistration;

    private String uf;

    private String operationDestination;

    private String finalCostumer;

    private String buyerPresence;

    private String model;

    private String series;

    private String number;

    private String issuanceDate;

    private String totalValueService;

    private String icmsCalculationBasis;

    private String icmsValue;

    private String protocol;

    private String address;

}
