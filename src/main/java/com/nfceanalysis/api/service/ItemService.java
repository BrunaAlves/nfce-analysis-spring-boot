package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.CategoryRepository;
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

    @Autowired
    CategoryRepository categoryRepository;

    public Item getItemById(String id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item Not Found with id: " + id));
    }

    public List<Item> getItemByNfce(String nfce){
        return itemRepository.findByNfce(new ObjectId(nfce));
    }

    public Item updateCategory(Item item){
        Item itemObj = itemRepository.findById(item.get_id())
                .orElseThrow(() -> new NoSuchElementException("Item Not Found id: " + item.get_id()));

        itemObj.setCategoryId(new ObjectId(item.getCategoryId()));
        itemRepository.save(itemObj);

        return itemObj;
    }

    public Item updateByItemCode(Item item){
        categoryRepository.findById(new ObjectId(item.getCategoryId()))
                .orElseThrow(() ->
                        new NoSuchElementException("Category Not Found with id: " + item.getCategoryId()));

        List<Item> items = getItemByItemCode(item.getItemCode());

        for (Item it : items) {
            it.setCategoryId(new ObjectId(item.getCategoryId()));
            updateCategory(it);
        }

        return item;
    }

    public List<Item> getItemByItemCode(String itemCode){
        return itemRepository.findByItemCode(itemCode)
                .orElseThrow(() -> new NoSuchElementException("Item Not Found id: " + itemCode));
    }


    public List<Item> getItemByCategoryId(String categoryId){
        return itemRepository.findByCategoryId(new ObjectId(categoryId));
    }

    public String removeItemCategory(String categoryId){
        List<Item> items = getItemByCategoryId(categoryId);

        for (Item it : items) {
            it.setCategoryId(null);
            itemRepository.save(it);
        }

        return "Successfully remove category with id: " + categoryId;
    }
}
