package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Nfce;
import com.nfceanalysis.api.repository.NfceRepository;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NfceService {

    @Autowired
    NfceRepository nfceRepository;

    public Nfce getNfceById(String id){
        return nfceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found with id: " + id));
    }

    public List<Nfce> getNfceByUser(String user){
        return nfceRepository.findByUser(new ObjectId(user))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by user: " + user));
    }

}
