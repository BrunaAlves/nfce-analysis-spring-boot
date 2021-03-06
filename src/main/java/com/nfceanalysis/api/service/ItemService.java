package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.CategoryRepository;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private final UserDetailsService userDetailsService;

    @Autowired
    MongoTemplate mongoTemplate;

    public ItemService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public List<Item> getAll(boolean uniqueItemCode){
        List<Item> itemList = itemRepository.findByAssignedTo(new ObjectId(userDetailsService.getUserId()));

        if(uniqueItemCode)
            return itemList.stream().filter(distinctByKey(i -> i.getItemCode())).collect(Collectors.toList());

        return itemList;
    }

    public List<Item> getItemCodeList(List<String> itemCodes){
        List<Item> itemList = new ArrayList<>();

        for (String itemCode : itemCodes) {
            Optional<Item> item = itemRepository
                    .findByAssignedToAndItemCode(new ObjectId(userDetailsService.getUserId()), itemCode)
                    .orElse(null).stream()
                    .findFirst();

            item.ifPresent(itemList::add);
        }

        return itemList;
    }

    public Item getItemById(String id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item Not Found with id: " + id));
    }

    public List<Item> getItemByNfce(String nfce){
        return itemRepository.findByNfce(new ObjectId(nfce));
    }

    public Item updateCategory(Item item){
        Item itemObj = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NoSuchElementException("Item Not Found id: " + item.getId()));

        itemObj.setCategoryId(new ObjectId(item.getCategoryId()));
        itemRepository.save(itemObj);

        return itemObj;
    }

    public Item updateByItemCode(Item item){
        Category category = categoryRepository.findById(new ObjectId(item.getCategoryId()))
                .orElseThrow(() ->
                        new NoSuchElementException("Category Not Found with id: " + item.getCategoryId()));

        List<Item> items = getItemByItemCode(item.getItemCode());

        for (Item it : items) {
            it.setCategoryId(new ObjectId(item.getCategoryId()));
            updateCategory(it);
        }

        category.getItemCodes().add(item.getItemCode());
        category.setItemCodes(category.getItemCodes().stream().distinct().collect(Collectors.toList()));
        categoryRepository.save(category);

        return item;
    }

    public List<Item> getItemByItemCode(String itemCode){
        return itemRepository.findByAssignedToAndItemCode(new ObjectId(userDetailsService.getUserId()), itemCode)
                .orElseThrow(() -> new NoSuchElementException("Item Not Found id: " + itemCode));
    }


    public List<Item> getItemsByCategoryId(String categoryId){
        return itemRepository.findByCategoryId(new ObjectId(categoryId));
    }

    public void removeItemCategory(String categoryId){
        List<Item> items = getItemsByCategoryId(categoryId);

        for (Item it : items) {
            it.setCategoryId(null);
            itemRepository.save(it);
        }

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
