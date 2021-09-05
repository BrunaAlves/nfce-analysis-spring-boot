package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.*;
import com.nfceanalysis.api.repository.AcquisitionRepository;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.repository.NfceRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AcquisitionService {

    @Autowired
    AcquisitionRepository acquisitionRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    NfceRepository nfceRepository;


    private final UserDetailsService userDetailsService;

    public AcquisitionService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Acquisition findById(String id){
        return acquisitionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Category Not Found with id: " + id));
    }

    public List<Acquisition> findAll(){
        return acquisitionRepository.findByUserId(new ObjectId(userDetailsService.getUserId()))
                .orElse(new ArrayList<>());
    }

    public Acquisition create(Acquisition acquisition){
        acquisition.setLastPurchase(findLatestIssuanceDateByItemCodes(acquisition.getItemCodes()));

        if(acquisition.getLastPurchase() != null)
            acquisition.setNextPurchase(
                    findNextAcquisition(acquisition.getLastPurchase(), acquisition.getFrequency()));

        return acquisitionRepository.save(acquisition);
    }

    public Acquisition update(Acquisition acquisition){
        acquisition.setLastPurchase(findLatestIssuanceDateByItemCodes(acquisition.getItemCodes()));

        if(acquisition.getLastPurchase() != null)
            acquisition.setNextPurchase(
                    findNextAcquisition(acquisition.getLastPurchase(), acquisition.getFrequency()));

        return acquisitionRepository.save(acquisition);
    }

    public void delete(String id){ acquisitionRepository.deleteById(id);}

    public Date findLatestIssuanceDateByItemCodes(List<String> itemCode){
        List<Item> itemList = new ArrayList<>();
        AtomicReference<Date> lastIssuanceDate = new AtomicReference<>();

        SimpleDateFormat sdfGet = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        itemCode.forEach(ic -> itemList.addAll(itemRepository
                .findByAssignedToAndItemCode(new ObjectId(userDetailsService.getUserId()), ic)
                .orElse(new ArrayList<>())));

        itemList.forEach(i -> {
            try {
                String currentIssuanceDateStr = nfceRepository.findById(i.getNfce()).orElse(new Nfce()).getIssuanceDate();
                if(currentIssuanceDateStr != null){
                    Date currentIssuanceDate = sdfGet.parse(currentIssuanceDateStr);
                    if(lastIssuanceDate.get() != null){
                        if(currentIssuanceDate.after(lastIssuanceDate.get())) lastIssuanceDate.set(currentIssuanceDate);
                    }else lastIssuanceDate.set(currentIssuanceDate);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return lastIssuanceDate.get();
    }

    public Date findNextAcquisition(Date date, Frequency frequency){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);

        if(frequency.equals(Frequency.diario)){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.semanal)){
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.quinzenal)){
            calendar.add(Calendar.DAY_OF_MONTH, 15);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.mensal)){
            calendar.add(Calendar.MONTH, 1);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.bimestral)){
            calendar.add(Calendar.MONTH, 2);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.trimestral)){
            calendar.add(Calendar.MONTH, 3);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.semestral)){
            calendar.add(Calendar.MONTH, 6);
            return calendar.getTime();
        }else if(frequency.equals(Frequency.anual)){
            calendar.add(Calendar.YEAR, 1);
            return calendar.getTime();
        }

        return null;

    }

    @Scheduled(cron="0 0 0 * * ?", zone="America/Sao_Paulo")
    public void scheduleLastAndNextAcquisition(){
        List<Acquisition> acquisitionList = acquisitionRepository.findAll();

        acquisitionList.forEach(acquisition -> {
            acquisition.setLastPurchase(findLatestIssuanceDateByItemCodes(acquisition.getItemCodes()));

            if(acquisition.getLastPurchase() != null)
                acquisition.setNextPurchase(
                        findNextAcquisition(acquisition.getLastPurchase(), acquisition.getFrequency()));

            acquisitionRepository.save(acquisition);
        });

    }
}
