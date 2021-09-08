package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.*;
import com.nfceanalysis.api.repository.CategoryRepository;
import com.nfceanalysis.api.repository.DiscountRepository;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.repository.NfceRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    DiscountRepository discountRepository;

    private final UserDetailsService userDetailsService;

    public DashboardService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @SneakyThrows
    public List<Timeline> getTimeline(){
        List<Nfce> nfceList = nfceRepository.findByUser(new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user "));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //create timeline object
        List<Timeline> timeline = new ArrayList<>();

        for (Nfce nfce : nfceList) {
            Timeline item = new Timeline();
            item.setId(nfce.getId());
            item.setTitle(nfce.getSocialName());
            item.setTime(sdf.parse(nfce.getIssuanceDate()));
            item.setType(nfce.getTypePayment());

            timeline.add(item);
        }

        Collections.sort(timeline, reverseOrder());

        return timeline;
    }

    public PieChart getPieChartPerLocation(int year, int month, int day){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(year !=0 && month!= 0 && day!=0){
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }else if(year !=0 && month!= 0 && day==0){
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            sdf = new SimpleDateFormat("MM/yyyy");
        }else if(year !=0 && month==0 && day==0){
            calendar.set(Calendar.YEAR, year);
            sdf = new SimpleDateFormat("yyyy");
        }

        List<Nfce> nfceList = nfceRepository
                .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(userDetailsService.getUserId()));

        List<String> socialNameList = nfceList.stream().map(Nfce::getSocialName).collect(Collectors.toList());
        Map<String, Long> counts = socialNameList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        PieChart chart = new PieChart();
        chart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        chart.setSeries( counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public PieChart getPieChartCategory(int year, int month, int day){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(year !=0 && month!= 0 && day!=0){
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }else if(year !=0 && month!= 0 && day==0){
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            sdf = new SimpleDateFormat("MM/yyyy");
        }else if(year !=0 && month==0 && day==0){
            calendar.set(Calendar.YEAR, year);
            sdf = new SimpleDateFormat("yyyy");
        }

        List<Nfce> nfceList = nfceRepository
                .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(userDetailsService.getUserId()));

        List<Item> itemList = new ArrayList<>();

        for (Nfce nfce : nfceList) {
            itemList.addAll(itemRepository.findByNfceAndCategoryIdNotNull(new ObjectId(nfce.getId())));
        }

        List<String> categoryIdList = itemList.stream().map(Item::getCategoryId).collect(Collectors.toList());
        Map<String, Long> counts = categoryIdList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        List<String> categoryIdUnique = counts.keySet().stream().collect(Collectors.toList());

        List<String> categoryNameList = new ArrayList<>();
        categoryIdUnique.forEach(e -> {
            Optional<Category> category = categoryRepository.findById(new ObjectId(e));
            categoryNameList.add(category.get().getName());
        });

        PieChart chart = new PieChart();
        chart.setLabel(categoryNameList);
        chart.setSeries( counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public float getTotalSpentInTheLastYear(){
        Calendar calendar = getCalendar();
        calendar.add(Calendar.YEAR, -1);

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(String.valueOf(calendar.get(Calendar.YEAR)),
                                new ObjectId(userDetailsService.getUserId()));

        float total = 0;

        for (Nfce nfce : nfceList) {
            total += nfce.getTotalValueService();

        }

        return total;
    }

    public float getTotalSpentInTheYear(){
        Calendar calendar = getCalendar();

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(String.valueOf(calendar.get(Calendar.YEAR)),
                                new ObjectId(userDetailsService.getUserId()));

        float total = 0;

        for (Nfce nfce : nfceList) {
            total += nfce.getTotalValueService();

        }

        return total;
    }

    public float getTotalSpentInTheCurrentMonth(){
        Calendar calendar = getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(sdf.format(calendar.getTime()),
                                new ObjectId(userDetailsService.getUserId()));

        float total = 0;
        for (Nfce nfce : nfceList) {
            total += nfce.getTotalValueService();

        }

        return total;
    }


    public float getTotalSpentInTheLastMonth(){
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        List<Nfce> nfceList =
                nfceRepository
                        .searchByIssuanceDate(sdf.format(calendar.getTime()),
                                new ObjectId(userDetailsService.getUserId()));

        float total = 0;
        for (Nfce nfce : nfceList) {
            total += nfce.getTotalValueService();

        }

        return total;
    }

    public Chart getValuesPerMonths(int year){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        LinkedHashMap<String, Float> counts = new LinkedHashMap<>();

        int totalMonth = calendar.get(Calendar.MONTH);
        calendar.add(Calendar.MONTH, -totalMonth);

        for (int i = totalMonth; i >= 0; i--) {

            List<Nfce> nfceList = nfceRepository
                   .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(userDetailsService.getUserId()));

            float total = 0;
            for (Nfce nfce : nfceList) {
                total += nfce.getTotalValueService();

            }

            counts.put(new SimpleDateFormat("MMMM", new Locale("pt"))
                    .format(calendar.getTime()), total);
            calendar.add(Calendar.MONTH, 1);
        }

        Chart chart = new Chart();
        chart.setLabel(counts.keySet().stream().collect(Collectors.toList()));
        chart.setSeries(counts.values().stream().collect(Collectors.toList()));

        return chart;
    }

    public BarLineChart getIcms(int year){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, year);
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
                    .searchByIssuanceDate(sdf.format(calendar.getTime()), new ObjectId(userDetailsService.getUserId()));

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

    public BarLineChart getItems(String itemCode){
        List<String> issuanceDateList = new ArrayList<>();
        List<Float> itemValueList = new ArrayList<>();
        List<Float> itemDiscountList = new ArrayList<>();

        List<Item> itemList = itemRepository.findByAssignedToAndItemCode(
                                                new ObjectId(userDetailsService.getUserId()), itemCode)
                                            .orElse(new ArrayList<>());

        for (Item item : itemList) {
            Nfce nfce = nfceRepository.findById(item.getNfce()).orElse(null);
            if(nfce != null){
                issuanceDateList.add(nfce.getIssuanceDate());
                itemValueList.add(item.getItemValue()/item.getQtdItem());

                float discountValue =  discountRepository.findByItemId(item.getId()).orElse(new Discount()).getDiscountValue();

                if(discountValue > 0){
                    itemDiscountList.add(item.getItemValue()/item.getQtdItem() - discountValue / item.getQtdItem());
                }else{
                    itemDiscountList.add(item.getItemValue()/item.getQtdItem());
                }
            }
        }

        BarLineChart chart = new BarLineChart();
        chart.setLabels(issuanceDateList);

        BarLineChartValue itemsValue = new BarLineChartValue();
        itemsValue.setName("Valor total do item");
        itemsValue.setType("column");
        itemsValue.setData(itemValueList);
        chart.getData().add(itemsValue);

        BarLineChartValue totalDiscount = new BarLineChartValue();
        totalDiscount.setName("Valor com desconto");
        totalDiscount.setType("column");
        totalDiscount.setData(itemDiscountList);
        chart.getData().add(totalDiscount);

        return chart;
    }

    public Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(new Date());

        return calendar;
    }
}
