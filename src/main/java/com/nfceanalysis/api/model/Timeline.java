package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Timeline implements Comparable<Timeline>{
    private String id;
    private String title;
    private Date time;
    private String type;

    @Override
    public int compareTo(Timeline o) {
        return getTime().compareTo(o.getTime());
    }
}
