package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Nfce;
import com.nfceanalysis.api.model.Timeline;
import com.nfceanalysis.api.repository.NfceRepository;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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
}