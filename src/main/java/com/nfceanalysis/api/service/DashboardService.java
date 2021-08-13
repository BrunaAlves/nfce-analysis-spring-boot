package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.*;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.repository.NfceRepository;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@Service
public class DashboardService {

    @Autowired
    NfceRepository nfceRepository;

    @Autowired
    ItemRepository itemRepository;


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

    public PieChart getPieChartPerLocation(String user){
        List<Nfce> nfceList = nfceRepository.findByUser(new ObjectId(user))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user: " + user));

        List<String> socialNameList = nfceList.stream().map(Nfce::getSocialName).collect(Collectors.toList());
        Map<String, Long> counts = socialNameList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        PieChart chart = new PieChart();
        chart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        chart.setSeries( counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public PieChart getPieChartCategory(String user){
        List<Nfce> nfceList = nfceRepository.findByUser(new ObjectId(user))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user: " + user));

        List<Item> itemList = new ArrayList<>();

        for (Nfce nfce : nfceList) {
            itemList.addAll(itemRepository.findByNfceAndCategoryNotNull(new ObjectId(nfce.get_id())));
        }

        List<String> categoryList = itemList.stream().map(e -> e.getCategory().getName() ).collect(Collectors.toList());
        Map<String, Long> counts = categoryList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        PieChart chart = new PieChart();
        chart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        chart.setSeries( counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public float getTotalSpentInTheLastYear(String user){
        Calendar calendar = getCalendar();
        calendar.add(Calendar.YEAR, -1);

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(String.valueOf(calendar.get(Calendar.YEAR)), new ObjectId(user));

        float total = 0;

        for (Nfce nfce : nfceList) {
            total += nfce.getTotalValueService();

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
            total += nfce.getTotalValueService();

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
            total += nfce.getTotalValueService();

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
            total += nfce.getTotalValueService();

        }

        return total;
    }

    public Chart getValuesPerMonths(String user){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        LinkedHashMap<String, Float> counts = new LinkedHashMap<>();

        int totalMonth = calendar.get(Calendar.MONTH);
        calendar.add(Calendar.MONTH, -totalMonth);

        for (int i = totalMonth; i >= 0; i--) {

            List<Nfce> nfceList = nfceRepository
                   .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(user));

            float total = 0;
            for (Nfce nfce : nfceList) {
                total += nfce.getTotalValueService();

            }

            counts.put(new SimpleDateFormat("MMMM")
                    .format(calendar.getTime()), total);
            calendar.add(Calendar.MONTH, 1);
        }

        Chart chart = new Chart();
        chart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        chart.setSeries(counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public BarLineChart getIcms(String user){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        DecimalFormat df = new DecimalFormat("#.##");

        List<String> issuanceDateList = new ArrayList<>();
        List<Float> totalValueServiceList = new ArrayList<>();
        List<Float> icmsCalculationBasisList = new ArrayList<>();
        List<Float> icmsValueList = new ArrayList<>();

        int totalMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -totalMonth);

        for (int i = totalMonth; i >= 0; i--) {
            List<Nfce> nfceList = nfceRepository
                    .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(user));

            float totalValueService = 0;
            float totalIcmsCalculationBasis = 0;
            float totalIcmsValue = 0;

            for (Nfce nfce : nfceList) {
                totalValueService += nfce.getTotalValueService();
                totalIcmsCalculationBasis += nfce.getIcmsCalculationBasis();
                totalIcmsValue += nfce.getIcmsValue();
            }

            issuanceDateList.add(new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime()));
            totalValueServiceList.add(Float.valueOf(df.format(totalValueService)));
            icmsCalculationBasisList.add(Float.valueOf(df.format(totalIcmsCalculationBasis)));
            icmsValueList.add(Float.valueOf(df.format(totalIcmsValue)));

            calendar.add(Calendar.MONTH, 1);
        }

        BarLineChart chart = new BarLineChart();
        chart.setLabels(issuanceDateList);

        BarLineChartValue icmsValue = new BarLineChartValue();
        icmsValue.setName("Valor ICMS");
        icmsValue.setType("column");
        icmsValue.setData(icmsValueList);
        chart.getData().add(icmsValue);

        BarLineChartValue totalValueService = new BarLineChartValue();
        totalValueService.setName("Valor total do serviço");
        totalValueService.setType("area");
        totalValueService.setData(totalValueServiceList);
        chart.getData().add(totalValueService);

        BarLineChartValue icmsCalculationBasis = new BarLineChartValue();
        icmsCalculationBasis.setName("Base de Cálculo ICMS");
        icmsCalculationBasis.setType("line");
        icmsCalculationBasis.setData(icmsCalculationBasisList);
        chart.getData().add(icmsCalculationBasis);

        return chart;
    }

    public Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(new Date());

        return calendar;
    }
}
