package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Nfce;
import com.nfceanalysis.api.repository.NfceRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NfceService {

    @Autowired
    NfceRepository nfceRepository;

    private final UserDetailsService userDetailsService;

    public NfceService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Nfce getNfceById(String id){
        return nfceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found with id: " + id));
    }

    public List<Nfce> getNfceByUser(){
        return nfceRepository.findByUser(new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Nfce Not Found by userId "));
    }

}
