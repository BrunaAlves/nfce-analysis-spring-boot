package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.CategoryRepository;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final UserDetailsService userDetailsService;

    public CategoryService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    public Category findById(String categoryId){
        return categoryRepository.findById(new ObjectId(categoryId))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found with id: " + categoryId));
    }

    public List<Category> findByUserId(){
        return categoryRepository.findByUserId(new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found for the user"));
    }

    public Category save(Category category){
        category.setUserId(new ObjectId(userDetailsService.getUserId()));
        Category cat = categoryRepository.save(category);

        category.getItemCodes().forEach(iCode -> {
            List<Item> itemList = itemRepository
                    .findByAssignedToAndItemCode(new ObjectId(userDetailsService.getUserId()), iCode)
                    .orElse(null);
            if(itemList!=null){
                itemList.forEach(it -> it.setCategoryId(new ObjectId(cat.getId())));
                itemRepository.saveAll(itemList);
            }
        });

        return category;
    }


    public String delete(String id){
        categoryRepository.deleteById(id);
        return "Successfully delete category with id: " + id;
    }

    public Map<String, Double> categoryMatch(Item item){
        final Map<String, Double> categoryAndMatch = new HashMap<>();
        List<Category> categoryList = findByUserId();

        for (Category category : categoryList) {
            double match = 0;
            List<Item> categoryItems = itemRepository.findByCategoryId(new ObjectId(category.getId()));

            Item matchItem = categoryItems.stream()
                    .filter(c -> c.getItemCode().equalsIgnoreCase(item.getItemCode()))
                    .findFirst()
                    .orElse(null);

            if(matchItem !=null){
                match = 1;
            }else{
                for (Item categoryItem : categoryItems) {
                    JaroWinklerDistance distance = new JaroWinklerDistance();
                    double porcentMatch = distance.apply(item.getItemName(), categoryItem.getItemName());
                    if(porcentMatch > match) match = porcentMatch;
                }
            }
            categoryAndMatch.put(category.getName(), match);
        }

        return categoryAndMatch;
    }
}
