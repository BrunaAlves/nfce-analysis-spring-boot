package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Nfce;
import com.nfceanalysis.api.model.PieChart;
import com.nfceanalysis.api.model.Timeline;
import com.nfceanalysis.api.repository.NfceRepository;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

@Service
public class DashboardService {

    @Autowired
    NfceRepository nfceRepository;



    @SneakyThrows
    public List<Timeline> getTimeline(String user){
        List<Nfce> nfceList = nfceRepository.findByUser(new ObjectId(user))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user: " + user));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //create timeline object
        List<Timeline> timeline = new ArrayList<>();

        for (Nfce nfce : nfceList) {
            Timeline item = new Timeline();
            item.setTitle(nfce.getSocialName());
            item.setTime(sdf.parse(nfce.getIssuanceDate()));
            item.setType(nfce.getTypePayment());

            timeline.add(item);
        }

        Collections.sort(timeline, reverseOrder());

        return timeline;
    }

    public PieChart getPieChart(String user){
        List<Nfce> nfceList = nfceRepository.findByUser(new ObjectId(user))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user: " + user));

        List<String> socialNameList = nfceList.stream().map(Nfce::getSocialName).collect(Collectors.toList());
        Map<String, Long> counts = socialNameList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        PieChart pieChart = new PieChart();
        pieChart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        pieChart.setSeries( counts.values().stream().collect(Collectors.toList()));

        return pieChart;
    }

    public float getTotalSpentInTheLastYear(String user){
        Calendar calendar = getCalendar();
        calendar.add(Calendar.YEAR, -1);

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(String.valueOf(calendar.get(Calendar.YEAR)), new ObjectId(user));

        float total = 0;

        for (Nfce nfce : nfceList) {
            total += Float.parseFloat(nfce.getTotalValueService());

        }

        return total;
    }

    public float getTotalSpentInTheYear(String user){
        Calendar calendar = getCalendar();

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(String.valueOf(calendar.get(Calendar.YEAR)), new ObjectId(user));

        float total = 0;

        for (Nfce nfce : nfceList) {
            total += Float.parseFloat(nfce.getTotalValueService());

        }

        return total;
    }

    public float getTotalSpentInTheCurrentMonth(String user){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(user));

        float total = 0;
        for (Nfce nfce : nfceList) {
            total += Float.parseFloat(nfce.getTotalValueService());

        }

        return total;
    }


    public float getTotalSpentInTheLastMonth(String user){
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(user));

        float total = 0;
        for (Nfce nfce : nfceList) {
            total += Float.parseFloat(nfce.getTotalValueService());

        }

        return total;
    }

    public Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(new Date());

        return calendar;
    }
}
