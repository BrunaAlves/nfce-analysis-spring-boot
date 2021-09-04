package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Acquisition;
import com.nfceanalysis.api.repository.AcquisitionRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AcquisitionService {

    @Autowired
    AcquisitionRepository acquisitionRepository;


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

    public Acquisition create(Acquisition acquisition){ return acquisitionRepository.save(acquisition);}

    public Acquisition update(Acquisition acquisition){ return acquisitionRepository.save(acquisition);}

    public void delete(String id){ acquisitionRepository.deleteById(id);}
}
