package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BarLineChartValue {
    private String name;
    private String type;
    private List<Float> data = new ArrayList<>();
}
