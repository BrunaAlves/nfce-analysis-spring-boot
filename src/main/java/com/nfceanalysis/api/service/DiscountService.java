package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Discount;
import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.DiscountRepository;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ItemRepository itemRepository;

    private final UserDetailsService userDetailsService;

    public DiscountService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Discount findById(String id){
        return discountRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new NoSuchElementException("Discount Not Found for id: " + id));
    }

    public List<Discount> findByUserId(){
        return discountRepository.findByUserId(new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Discount Not Found by userId"));
    }

    public Discount findByItemId(String itemId){
        return discountRepository.findByItemId(new ObjectId(itemId))
                .orElseThrow(() -> new NoSuchElementException("Discount Not Found for id: " + itemId));
    }

    public Discount create(String itemId, Discount discount){
        Item item = itemRepository.findByIdAndAssignedTo(itemId,
                new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Item Not Found by userId"));

        Discount newDiscount = new Discount();

        Discount checkDiscount = discountRepository.findByItemId(new ObjectId(itemId)).orElse(null);

        if(checkDiscount == null){
            newDiscount.setDiscountValue(discount.getDiscountValue());
            newDiscount.setItemCode(item.getItemCode());
            newDiscount.setUserId(new ObjectId(userDetailsService.getUserId()));
            newDiscount.setItemValue(item.getItemValue());
            newDiscount.setItemId(new ObjectId(itemId));
        }else{
            newDiscount = checkDiscount;
            newDiscount.setDiscountValue(discount.getDiscountValue());
        }


        return discountRepository.save(newDiscount);
    }

    public Discount update(Discount discount){
        return discountRepository.save(discount);
    }

    public String delete(String id){
        discountRepository.deleteById(id);
        return "Successfully delete discount with id: " + id;
    }
}
