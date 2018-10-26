package com.soen343.project.service.database;

import com.soen343.project.repository.dao.catalog.CatalogRepository;
import com.soen343.project.repository.dao.catalog.ItemRepository;
import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-25
 */
@Service
public class RecordDatabase {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CatalogRepository catalogRepository;

    @Autowired
    public RecordDatabase(UserRepository userRepository, CatalogRepository catalogRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.catalogRepository = catalogRepository;
        this.itemRepository = itemRepository;
    }

    public List<User> register(User userToRegister) {
        List<User> registeredUsers = userRepository.findAll();

        if(userToRegister.isUniqueFrom(registeredUsers)) {
            userRepository.save(userToRegister);
            return userRepository.findAll();
        } else {
            return null;
        }

    }

    public Item createItem(ItemSpecification itemSpec) {
        return new Item(itemSpec);
    }

    public Item getItem(Long itemID) {
        return itemRepository.findById(itemID);
    }

    public List<Item> findAllItems(){
        return itemRepository.findAll();
    }

/*    public void updateItem(long itemID, ItemSpecification itemSpec){
        Item item = catalogRepository.findItem(itemID);
        ItemSpecification spec = catalogRepository.findItemSpec(itemSpec);

        if(item != null){
            //Creates and adds a new itemSpec
            if(spec == null){
                catalogRepository.addItemSpec(itemSpec);
            }
            catalogRepository.update(item, itemSpec);
        }
    }

    public void removeItem(long itemID){
        Item item = catalogRepository.findItem(itemID);
        if(item != null){
            catalogRepository.remove(item);
        }
    }

    public void insertCatalogItem(ItemSpecification itemSpec){
            if(catalogRepository.findItemSpec(itemSpec) == null){
                catalogRepository.addItemSpec(itemSpec);
            }
            catalogRepository.createItem(itemSpec);
    }


    */
}
