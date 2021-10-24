package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.*;
import com.nfceanalysis.api.repository.*;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class TriggerLogService {

    @Autowired
    AcquisitionRepository acquisitionRepository;

    @Autowired
    AcquisitionService acquisitionService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TriggerLogRepository triggerLogRepository;

    @Autowired
    UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final String ZONE_SAO_PAULO = "America/Sao_Paulo";

    public TriggerLogService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public TriggerLog getLatestTriggerLog(LogType logType){
        return triggerLogRepository
                .findTop1ByUserIdAndLogTypeOrderByUpdateAtDesc(new ObjectId(userDetailsService.getUserId()), logType);
    }

    public void updateAllAcquisitionByUser(){
        updateAcquisitionLastAndNextByUserId(userDetailsService.getUserId());
    }

    public void updateAllCategoryByUser(){
        updateItemCategoryByUserId(userDetailsService.getUserId());
    }

    public void updateAcquisitionLastAndNextByUserId(String userId){
        List<Acquisition> acquisitionList =
                acquisitionRepository.findByUserId(new ObjectId(userId)).orElse(null);

        acquisitionList.forEach(acquisition -> {
            acquisition.setLastPurchase(acquisitionService.findLatestIssuanceDateByItemCodes(acquisition.getItemCodes()));

            if(acquisition.getLastPurchase() != null)
                acquisition.setNextPurchase(
                        acquisitionService.findNextAcquisition(acquisition.getLastPurchase(), acquisition.getFrequency()));

            acquisitionRepository.save(acquisition);
        });

        TriggerLog triggerLog = new TriggerLog();
        triggerLog.setUserId(new ObjectId(userId));
        triggerLog.setLogType(LogType.acquisition);
        triggerLog.setUpdateAt(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(ZONE_SAO_PAULO)).toLocalDateTime());
        triggerLogRepository.save(triggerLog);
    }

    public void updateItemCategoryByUserId(String userId){
        List<Category> categoryList = categoryRepository.findByUserId(new ObjectId(userId)).orElse(null);

        for (Category category : categoryList) {
            category.getItemCodes().forEach(itemcode -> {
                List<Item> itemList = itemRepository
                        .findByAssignedToAndItemCodeAndCategoryIdNull(new ObjectId(category.getUserId()), itemcode)
                        .stream()
                        .filter(c -> c.getItemCode().equalsIgnoreCase(itemcode))
                        .collect(Collectors.toList());
                itemList.forEach(item -> {
                    item.setCategoryId(new ObjectId(category.getId()));
                    itemRepository.save(item);
                });
            });
        }

        TriggerLog triggerLog = new TriggerLog();
        triggerLog.setUserId(new ObjectId(userId));
        triggerLog.setLogType(LogType.category);
        triggerLog.setUpdateAt(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(ZONE_SAO_PAULO)).toLocalDateTime());
        triggerLogRepository.save(triggerLog);
    }

    @Scheduled(cron="0 0 0 * * ?", zone="America/Sao_Paulo")
    public void scheduleItemCategory(){
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            updateItemCategoryByUserId(user.getId());
        });


    }

    @Scheduled(cron="0 0 0 * * ?", zone="America/Sao_Paulo")
    public void scheduleLastAndNextAcquisition(){
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            updateAcquisitionLastAndNextByUserId(user.getId());
        });

    }

}
