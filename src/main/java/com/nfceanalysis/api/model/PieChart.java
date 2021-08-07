package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PieChart {
    List<String> label = new ArrayList<>();
    List<Long> series = new ArrayList<>();
}
