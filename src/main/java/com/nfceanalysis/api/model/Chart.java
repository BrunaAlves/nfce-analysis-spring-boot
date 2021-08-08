package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Chart {
    List<String> label = new ArrayList<>();
    List<Float> series = new ArrayList<>();
}
