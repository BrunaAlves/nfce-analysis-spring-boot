package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.ItemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public Item getItemById(String id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item Not Found with id: " + id));
    }

    public List<Item> getItemByNfce(String nfce){
        return itemRepository.findByNfce(new ObjectId(nfce));
               // .orElseThrow(() -> new NoSuchElementException("Item Not Found by nfce id: " + nfce));
    }
}
