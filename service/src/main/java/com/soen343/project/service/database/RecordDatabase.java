package com.soen343.project.service.database;

import com.soen343.project.repository.dao.catalog.CatalogRepository;
import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.authenticate.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-25
 */
@Service
public class RecordDatabase {

    private final UserRepository userRepository;
    private final CatalogRepository catalogRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public RecordDatabase(UserRepository userRepository, CatalogRepository catalogRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.catalogRepository = catalogRepository;
        this.authenticationService = authenticationService;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> registerUser(Long id, User userToRegister) {
        if (!this.authenticationService.authenticateAdmin(id)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        List<User> registeredUsers = findAllUsers();
        // Checks if user's email and phone is unique
        for (User registeredUser : registeredUsers) {
            if (userToRegister.getEmail().equals(registeredUser.getEmail()) || userToRegister.getPhoneNumber().equals(registeredUser.getPhoneNumber())) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }

        userRepository.save(userToRegister);
        return new ResponseEntity<>(findAllUsers(), HttpStatus.OK);
    }

    public void updateItem(long itemID, ItemSpecification itemSpec){
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

    public Item getItem(long itemID){
        return catalogRepository.findItem(itemID);
    }

    public List findAllItem(){
        return catalogRepository.findAll();
    }
}
