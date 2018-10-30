package com.soen343.project.endpoint.controller;

import com.google.common.collect.Lists;
import com.soen343.project.database.connection.DatabaseConnector;
import com.soen343.project.database.query.QueryBuilder;
import com.soen343.project.repository.dao.catalog.itemspec.MovieRepository;
import com.soen343.project.repository.dao.catalog.itemspec.operation.ItemSpecificationOperation;
import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.catalog.Movie;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.uow.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.soen343.project.database.connection.DatabaseConnector.executeUpdate;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class ExampleController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MovieRepository movieRepository;

    @Autowired
    public ExampleController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.movieRepository = movieRepository;
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
        DatabaseConnector.executeBatchOperation((statement -> {
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
}
