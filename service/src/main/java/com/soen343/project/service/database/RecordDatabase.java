package com.soen343.project.service.database;

import com.soen343.project.repository.dao.catalog.item.ItemGateway;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-25
 */
@Service
public class RecordDatabase {

    private final UserGateway userRepository;
    private final ItemGateway itemRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RecordDatabase(UserGateway userRepository, ItemGateway itemRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> register(User userToRegister) {
        List<User> registeredUsers = userRepository.findAll();

        if(userToRegister.isUniqueFrom(registeredUsers)) {
            String password = userToRegister.getPassword();
            userToRegister.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(userToRegister);
        }

        return userRepository.findAll();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
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
}
