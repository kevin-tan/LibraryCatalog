package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.soen343.project.database.connection.DatabaseConnector;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.dao.catalog.item.ItemGateway;
import com.soen343.project.repository.dao.catalog.item.LoanableItemGateway;
import com.soen343.project.repository.dao.catalog.itemspec.BookGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MagazineGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MovieGateway;
import com.soen343.project.repository.dao.catalog.itemspec.MusicGateway;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.dao.transaction.LoanTransactionGateway;
import com.soen343.project.repository.dao.transaction.ReturnTransactionGateway;
import com.soen343.project.repository.dao.user.UserGateway;
import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.repository.entity.catalog.item.LoanableItem;
import com.soen343.project.repository.entity.catalog.itemspec.ItemSpecification;
import com.soen343.project.repository.entity.catalog.itemspec.media.Movie;
import com.soen343.project.repository.entity.catalog.itemspec.media.Music;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Book;
import com.soen343.project.repository.entity.catalog.itemspec.printed.Magazine;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import com.soen343.project.service.catalog.CatalogSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class ExampleController {

    private final UserGateway userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CatalogSearch catalogSearch;

    private final ItemGateway itemRepository;
    private final MovieGateway movieRepository;
    private final BookGateway bookRepository;
    private final MagazineGateway magazineRepository;
    private final MusicGateway musicRepository;
    private final LoanableItemGateway loanableItemGateway;
    private final ReturnTransactionGateway returnTransactionGateway;
    private final LoanTransactionGateway loanTransactionGateway;

    @Autowired
    public ExampleController(UserGateway userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CatalogSearch catalogSearch,
                             MovieGateway movieRepository, ItemGateway itemRepository, BookGateway bookRepository,
                             MagazineGateway magazineRepository, MusicGateway musicRepository, LoanableItemGateway loanableItemGateway,
                             ReturnTransactionGateway returnTransactionGateway, LoanTransactionGateway loanTransactionGateway) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.catalogSearch = catalogSearch;
        this.movieRepository = movieRepository;
        this.itemRepository = itemRepository;
        this.bookRepository = bookRepository;
        this.magazineRepository = magazineRepository;
        this.musicRepository = musicRepository;

        this.loanableItemGateway = loanableItemGateway;
        this.returnTransactionGateway = returnTransactionGateway;
        this.loanTransactionGateway = loanTransactionGateway;
    }

    @PostMapping("/text/loanableItem")
    public ResponseEntity<?> loanableitemTest(@RequestBody Item item) {
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/test/findAll")
    public ResponseEntity<?> findAllTest() {
        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/findAllLoanableItems")
    public ResponseEntity<?> findAllLoanableItemTest() {
        return new ResponseEntity<>(loanableItemGateway.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/saveLoanableItems")
    public ResponseEntity<?> findSaveLoanableItemTest() {
        LoanableItem l1 = new LoanableItem(0L, movieRepository.findById(1L), true,null);
        loanableItemGateway.save(l1);
        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }

    @GetMapping("/test/saveAllLoanableItems")
    public ResponseEntity<?> findSaveAllLoanableItemTest() {
        LoanableItem l1 = new LoanableItem(0L, movieRepository.findById(1L), true,null);
        LoanableItem l2 = new LoanableItem(0L, bookRepository.findById(1L), true,null);
        loanableItemGateway.saveAll(l1, l2);
        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }

    @GetMapping("/test/findAllReturnTransactions")
    public ResponseEntity<?> findAllReturnTransactionsTest() {
        return new ResponseEntity<>(returnTransactionGateway.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/findAllLoanTransactions")
    public ResponseEntity<?> findAllLoanTransactionsTest() {
        return new ResponseEntity<>(loanTransactionGateway.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/concurrency")
    public ResponseEntity<?> testConcurrency() {
        Movie movie = new Movie(0L, "Test", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Movie movie2 = new Movie(0L, "Test", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Item item = new Item(movie);
        Item item11 = new Item(movie2);

        movieRepository.save(movie);
        movieRepository.save(movie2);
        movieRepository.saveAll(movie, movie2);
        return new ResponseEntity<>(movieRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/concurrency2")
    public ResponseEntity<?> testConcurrency2() {
        musicRepository.findById(0L);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    @GetMapping("/test/itemrepo")
    public ResponseEntity<?> testItemRepo() {
        Movie movie = new Movie(0L, "Test", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Movie movie2 = new Movie(0L, "Test", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Item item = new Item(movie);
        Item item11 = new Item(movie2);
        movieRepository.save(movie);
        movieRepository.save(movie2);
        movieRepository.saveAll(movie, movie2);
        itemRepository.save(item);
        itemRepository.save(item11);

        Book book =
                new Book(0L, "Book", "Publisher", "Pub date", "Language", "ISBN-10-1", "ISBN-13-1", "Author", Book.Format.HARDCOVER, 10);
        Book book2 =
                new Book(0L, "Book", "Publisher", "Pub date", "Language", "ISBN-10-1", "ISBN-13-1", "Author", Book.Format.HARDCOVER, 10);
        Item item2 = new Item(book);
        Item item22 = new Item(book2);
        bookRepository.save(book);
        bookRepository.save(book2);
        bookRepository.saveAll(book, book2);
        itemRepository.save(item2);
        itemRepository.save(item22);

        Magazine magazine = new Magazine(0L, "Title", "Pub", "Pub Date", "Language", "ISBN-10-1", "ISBN=13-1");
        Magazine magazine2 = new Magazine(0L, "Title", "Pub", "Pub Date", "Language", "ISBN-10-1", "ISBN=13-1");
        Item item3 = new Item(magazine);
        Item item33 = new Item(magazine2);
        magazineRepository.save(magazine);
        magazineRepository.save(magazine2);
        magazineRepository.saveAll(magazine, magazine2);
        itemRepository.save(item3);
        itemRepository.save(item33);

        Music music = new Music(0L, "Title", "Date", "Type", "Artist", "Label", "Asin");
        Music music2 = new Music(0L, "Title", "Date", "Type", "Artist", "Label", "Asin");
        Item item4 = new Item(music);
        Item item44 = new Item(music2);
        musicRepository.save(music);
        musicRepository.save(music2);
        musicRepository.saveAll(music, music2);
        itemRepository.save(item4);
        itemRepository.save(item44);


        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test/movie")
    public ResponseEntity<?> testMovie() {
        UnitOfWork unitOfWork = new UnitOfWork();
        Movie movie = new Movie(0L, "Test", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Movie movie2 = new Movie(0L, "Test2", "Date2", "Director2", Lists.newArrayList("Producer2"), Lists.newArrayList("Actor2"),
                Lists.newArrayList("Dubbed2"), "Lang2", "Sub2", 50);
        unitOfWork.registerOperation(ItemSpecificationOperation.movieSaveOperation(movie));
        unitOfWork.registerOperation(ItemSpecificationOperation.movieSaveOperation(movie2));
        unitOfWork.commit();
        return new ResponseEntity<>("Movie success", HttpStatus.OK);
    }

    @GetMapping("/test/db")
    public ResponseEntity<?> testDb() {
        DatabaseConnector.executeBatchUpdate((statement -> {
            statement.executeUpdate(
                    "INSERT INTO Movie (title, releaseDate, director, language, subtitles, runTime) VALUES ('Test', 'Test', 'Test', 'Test', 'Test', '50');");
            long id = statement.executeQuery("SELECT LAST_INSERT_ROWID();").getInt(1);
            statement.executeUpdate("INSERT INTO Producers (movieId, producer) VALUES (" + id + " , 'Test');");
            statement.executeUpdate("INSERT INTO Actors (movieId, actor) VALUES (" + id + ", 'Test');");
            statement.executeUpdate("INSERT INTO Dubbed (movieId, dub) VALUES (" + id + ", 'Test');");
        }));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/test/create")
    public ResponseEntity<?> create(@RequestBody User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/test/getUser")
    public ResponseEntity<?> getUser() {
        return new ResponseEntity<>(userRepository.findById(1L), HttpStatus.OK);
    }

    @GetMapping("/test/getLoanableItem")
    public ResponseEntity<?> getLoanableItem() {
        return new ResponseEntity<>(loanableItemGateway.findById(1L), HttpStatus.OK);
    }

    @GetMapping("/test/getReturnTransaction")
    public ResponseEntity<?> getReturnTransaction() {
        return new ResponseEntity<>(returnTransactionGateway.findById(1L), HttpStatus.OK);
    }

    @GetMapping("/test/getLoanTransaction")
    public ResponseEntity<?> getLoanTransaction() {
        return new ResponseEntity<>(loanTransactionGateway.findById(2L), HttpStatus.OK);
    }

    @GetMapping("/test/addLoanableItem")
    public ResponseEntity<?> addLoanableItem() {
        ItemSpecification itemSpecification = new Music(1L, "", "", "", "", "", "");
        LoanableItem loanableItem = new LoanableItem(1L, itemSpecification, true, null);
        loanableItemGateway.save(loanableItem);
        return new ResponseEntity<>(loanableItemGateway.findById(21L), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/addNewUser")
    public ResponseEntity<?> addNewUser() {
        Admin admin = Admin.builder().firstName("Test First").lastName("Test Last").email("Test@hotmail.com").phoneNumber("514-Test")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        userRepository.save(admin);
        return new ResponseEntity<>(userRepository.findById(2L), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     * For testing so more users can be added if needed
     */
    @GetMapping("/test/populateUsers")
    public ResponseEntity<?> populateUsers() {
        Admin admin = Admin.builder().firstName("Test First").lastName("Test Last").email("Test@hotmail.com").phoneNumber("514-Test")
                .physicalAddress("888 Test").password("bigboss").build();
        Client client = Client.builder().firstName("John").lastName("Wick").email("john@wick.com").phoneNumber("1234567")
                .physicalAddress("John Wicks House").password("johnwicksdog").build();
        userRepository.saveAll(admin, client);
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id) {
        User oldAdmin = userRepository.findById(id);
        Admin admin =
                Admin.builder().firstName("Test First2").lastName("Test Last2").email("Tes2t@hotmail.com32").phoneNumber("51324-Tes32t")
                        .physicalAddress("88832 Test32").password("testtest").id(oldAdmin.getId()).build();
        userRepository.update(admin);
        return new ResponseEntity<>(userRepository.findById(oldAdmin.getId()), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User admin = userRepository.findById(id);
        userRepository.delete(admin);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }


    @GetMapping("/test/batch")
    public ResponseEntity<?> batch() {
        UnitOfWork uow = new UnitOfWork();
        Admin admin = Admin.builder().firstName("Test First").lastName("Test Last").email("Test@hotmail.com").phoneNumber("514-Test")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        Admin admin1 = Admin.builder().firstName("Test First").lastName("Test Last").email("Test1@hotmail.com").phoneNumber("514-Test1")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        uow.registerOperation(statement -> executeUpdate((QueryBuilder.createSaveQuery(admin.getTableWithColumns(), admin.toSQLValue()))));
        uow.registerOperation(statement -> executeUpdate(QueryBuilder.createSaveQuery(admin1.getTableWithColumns(), admin1.toSQLValue())));
        User user = userRepository.findAll().get(0);
        uow.registerOperation(statement -> executeUpdate(QueryBuilder.createDeleteQuery(user.getTable(), user.getId())));
        uow.commit();
        return new ResponseEntity<>("It worked.", HttpStatus.OK);
    }

    @GetMapping("/test/saveAll")
    public ResponseEntity<?> saveAll() {
        Admin admin = Admin.builder().firstName("Test First").lastName("Test Last").email("Test@hotmail.com").phoneNumber("514-Test")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        Admin admin1 = Admin.builder().firstName("Test First").lastName("Test Last").email("Test1@hotmail.com").phoneNumber("514-Test1")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        Admin admin2 = Admin.builder().firstName("Test First").lastName("Test Last").email("Test2@hotmail.com").phoneNumber("514-Test2")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        Admin admin3 = Admin.builder().firstName("Test First").lastName("Test Last").email("Test3@hotmail.com").phoneNumber("514-Test3")
                .physicalAddress("888 Test").password(bCryptPasswordEncoder.encode("bigboss")).build();
        userRepository.saveAll(admin, admin1, admin2, admin3);
        return new ResponseEntity<>("It worked.", HttpStatus.OK);
    }

    @GetMapping("/test/titleSearch/{titleValue}")
    public ResponseEntity<?> keyword(@PathVariable String titleValue) {
        Movie movie = new Movie(0L, "Test1", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Movie movie2 = new Movie(0L, "Test2", "Date", "Director", Lists.newArrayList("Producer"), Lists.newArrayList("Actor"),
                Lists.newArrayList("Dubbed"), "Lang", "Sub", 50);
        Item item = new Item(movie);
        Item item11 = new Item(movie2);
        movieRepository.save(movie);
        movieRepository.save(movie2);
        movieRepository.saveAll(movie, movie2);
        itemRepository.save(item);
        itemRepository.save(item11);

        Book book =
                new Book(0L, "BoTestok", "Publisher", "Pub date", "Language", "ISBN-10-13", "ISBN-13-12", "Bob", Book.Format.HARDCOVER, 10);
        Book book2 =
                new Book(0L, "BookTest", "Publisher", "Pub date", "Language", "ISBN-10-14", "ISBN-13-13", "Jim", Book.Format.HARDCOVER, 10);
        Item item2 = new Item(book);
        Item item22 = new Item(book2);
        bookRepository.save(book);
        bookRepository.save(book2);
        bookRepository.saveAll(book, book2);
        itemRepository.save(item2);
        itemRepository.save(item22);

        Magazine magazine = new Magazine(0L, "TitleTest", "Pub", "Pub Date", "Language", "ISBN-10-15", "ISBN=13-17");
        Magazine magazine2 = new Magazine(0L, "TTestitle", "Pub", "Pub Date", "Language", "ISBN-10-16", "ISBN=13-18");
        Item item3 = new Item(magazine);
        Item item33 = new Item(magazine2);
        magazineRepository.save(magazine);
        magazineRepository.save(magazine2);
        magazineRepository.saveAll(magazine, magazine2);
        itemRepository.save(item3);
        itemRepository.save(item33);

        Music music = new Music(0L, "TestTitle", "Date", "Type", "Artist", "Label", "Asin9");
        Music music2 = new Music(0L, "TitlTeste", "Date", "Type", "Artist", "Label", "Asin0");
        Item item4 = new Item(music);
        Item item44 = new Item(music2);
        musicRepository.save(music);
        musicRepository.save(music2);
        musicRepository.saveAll(music, music2);
        itemRepository.save(item4);
        itemRepository.save(item44);

        return new ResponseEntity<>(catalogSearch.searchAllByTitle(titleValue), HttpStatus.OK);
    }

    @PostMapping("/testmulti/{itemType}")
    public ResponseEntity<?> testmulti(@PathVariable String itemType, @RequestBody ObjectNode objectNode) {
        Map<String, String> attributeValue = new HashMap<>();
        objectNode.fieldNames().forEachRemaining(key -> attributeValue.put(key, objectNode.get(key).asText()));

        return new ResponseEntity<>(catalogSearch.searchCatalogByAttribute(itemType, attributeValue), HttpStatus.OK);
    }

    @GetMapping("/testgetall")
    public ResponseEntity<?> testgetall() {
        return new ResponseEntity<>(catalogSearch.getAllItemSpecs(), HttpStatus.OK);
    }
}
