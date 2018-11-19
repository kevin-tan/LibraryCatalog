package com.soen343.project.service.database;

import com.google.common.collect.ImmutableMap;
import com.soen343.project.repository.dao.catalog.item.ItemGateway;
import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.dao.mapper.GatewayMapper;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.soen343.project.repository.entity.EntityConstants.*;

/**
 * Created by Kevin Tan 2018-09-25
 */
@Service
public class Library {

    private final GatewayMapper gatewayMapper;
    private final MovieGateway movieRepository;
    private final BookGateway bookRepository;
    private final MagazineGateway magazineRepository;
    private final MusicGateway musicRepository;
    private final ItemGateway itemGateway;
    private final UserGateway userRepository;
    private final LoanableItemGateway loanableItemGateway;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String BOOK = "book";
    private static final String MAGAZINE = "magazine";
    private static final String MOVIE = "movie";
    private static final String MUSIC = "music";

    @Autowired
    public Library(GatewayMapper gatewayMapper, MovieGateway movieRepository, BookGateway bookRepository,
                   MagazineGateway magazineRepository, MusicGateway musicRepository, ItemGateway itemGateway, UserGateway userRepository,
                   LoanableItemGateway loanableItemGateway, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.gatewayMapper = gatewayMapper;
        this.movieRepository = movieRepository;
        this.bookRepository = bookRepository;
        this.magazineRepository = magazineRepository;
        this.musicRepository = musicRepository;
        this.itemGateway = itemGateway;
        this.userRepository = userRepository;
        this.loanableItemGateway = loanableItemGateway;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> register(User userToRegister) {
        List<User> registeredUsers = userRepository.findAll();

        if (userToRegister.isUniqueFrom(registeredUsers)) {
            String password = userToRegister.getPassword();
            userToRegister.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(userToRegister);
        }

        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByID(Long id) {
        return userRepository.findById(id);
    }

    public Item createItem(ItemSpecification itemSpec) {
        if (itemSpec.getTable().equals(MAGAZINE_TABLE)) {
            return new Item(itemSpec);
        } else {
            return new LoanableItem(0L, itemSpec, true, null);
        }
    }

    public Item getItem(Long itemID) {
        return itemGateway.findById(itemID);
    }

    public List<Item> findAllItems() {
        return itemGateway.findAll();
    }

    public List<?> searchCatalogByAttribute(String itemType, Map<String, String> attributeValue) {
        return gatewayMapper.getGateway(itemType).findByAttribute(attributeValue);
    }

    public Map<String, List<?>> getAllItemSpecs() {
        return ImmutableMap.of(MOVIE, movieRepository.findAll(), BOOK, bookRepository.findAll(), MUSIC, musicRepository.findAll(), MAGAZINE,
                magazineRepository.findAll());
    }

    public Map<String, List<?>> searchAllItemSpecByTitle(String titleValue) {
        return ImmutableMap.of(MOVIE, movieRepository.findByTitle(titleValue), BOOK, bookRepository.findByTitle(titleValue), MUSIC,
                musicRepository.findByTitle(titleValue), MAGAZINE, magazineRepository.findByTitle(titleValue));
    }

    public List<?> getAllItemSpecOfType(String itemType) {
        return gatewayMapper.getGateway(itemType).findAll();
    }

    public List<?> getAllItemSpecOfSameType(String itemType, Long itemSpecId) {
        if (itemType.equals(MAGAZINE_TABLE)) {
            return itemGateway.findByItemSpecId(itemType, itemSpecId);
        } else {
            return loanableItemGateway.findByItemSpecId(itemType, itemSpecId);
        }
    }

    public Map<String, ?> getAllItemSpecQuantities() {
        Map<String, Map<Long, Integer>> itemTypeMapping = new HashMap<>();

        // Map
        fillMapForMagazine((List<Item>) itemGateway.findByItemSpec(MAGAZINE_TABLE), itemTypeMapping);
        fillMapForLoanable(loanableItemGateway.findAll(), itemTypeMapping);

        return itemTypeMapping;
    }


    private void fillMapForLoanable(List<LoanableItem> items, Map<String, Map<Long, Integer>> map) {
        items.forEach(item -> {
            if (item.getAvailable()) {
                String itemType = item.getType();
                fillMap(map, itemType, item);
            }
        });
    }

    private void fillMapForMagazine(List<? extends Item> items, Map<String, Map<Long, Integer>> map) {
        items.forEach(item -> fillMap(map, MAGAZINE_TABLE, item));
    }

    private void fillMap(Map<String, Map<Long, Integer>> map, String itemType, Item item) {
        if (map.get(itemType) == null) {
            Map<Long, Integer> temp = new HashMap<>();
            temp.put(item.getSpec().getId(), 1);
            map.put(itemType, temp);
        } else if (map.get(itemType).get(item.getSpec().getId()) == null) {
            map.get(itemType).put(item.getSpec().getId(), 1);
        } else {
            map.get(itemType).put(item.getSpec().getId(), map.get(itemType).get(item.getSpec().getId()) + 1);
        }
    }

}
