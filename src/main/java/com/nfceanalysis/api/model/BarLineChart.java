package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BarLineChart {
    List<String> labels = new ArrayList<>();
    List<BarLineChartValue> data = new ArrayList<>();
}
