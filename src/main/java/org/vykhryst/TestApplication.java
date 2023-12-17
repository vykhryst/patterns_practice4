package org.vykhryst;

import org.vykhryst.dao.DAOFactory;
import org.vykhryst.dao.MySqlDaoFactory;
import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.dao.entityDao.ClientDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlAdvertisingDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlCategoryDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlClientDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlProgramDAO;
import org.vykhryst.entity.*;
import org.vykhryst.observer.*;
import org.vykhryst.observer.listeners.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class TestApplication {

    public static void main(String[] args) throws SQLException {
        // Initialize DAO Factory
        DAOFactory daoFactory = MySqlDaoFactory.getInstance();

        CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
        AdvertisingDAO advertisingDAO = daoFactory.getAdvertisingDAO();
        ClientDAO clientDAO = daoFactory.getClientDAO();
        ProgramDAO programDAO = daoFactory.getProgramDAO();

        testCategoryListener((MySqlCategoryDAO) categoryDAO);

        testAdvertisingListener((MySqlAdvertisingDAO) advertisingDAO);

        testClientListener((MySqlClientDAO) clientDAO);

        testProgramListener((MySqlAdvertisingDAO) advertisingDAO, (MySqlClientDAO) clientDAO, (MySqlProgramDAO) programDAO);

        // FIRST PRACTICE
        /*// Test Category
        testCategory(daoFactory.getCategoryDAO());

        // Test Advertising
        testAdvertising(daoFactory.getAdvertisingDAO());

        // Test Client
        testClient(daoFactory.getClientDAO());

        // Test Program
        testProgram(daoFactory.getProgramDAO());*/
    }

    // SECOND PRACTICE METHODS
    private static void testProgramListener(MySqlAdvertisingDAO advertisingDAO, MySqlClientDAO clientDAO, MySqlProgramDAO programDAO) throws SQLException {
        System.out.println("\n----- Testing Program -----");
        // Program Event Listener
        EntityEventListener<Program> programEventListener = new ProgramEventListener();
        programDAO.subscribe(programEventListener);

        Advertising advertising1 = advertisingDAO.findById(1).orElse(null);
        Advertising advertising2 = advertisingDAO.findById(2).orElse(null);

        Program program = new Program.Builder().client(clientDAO.findById(1).orElse(null))
                .campaignTitle("TEST Campaign")
                .description("TEST Description")
                .createdAt(LocalDateTime.now())
                .addAdvertising(advertising1, 3)
                .addAdvertising(advertising2, 10)
                .build();
        long programId = programDAO.save(program);
        System.out.println();
        Optional<Program> foundProgram = programDAO.findById(programId);

        if (foundProgram.isPresent()) {
            Program foundProgram1 = foundProgram.get();
            foundProgram1.setCampaignTitle("Updated TEST Campaign");
            foundProgram1.setDescription("Updated TEST Description");
            foundProgram1.getAdvertising().entrySet().stream().findFirst().ifPresent(entry -> {
                entry.getKey().setName("Updated TEST Advertising");
                entry.setValue(20);
            });
            programDAO.update(foundProgram1);
            System.out.println();
            programDAO.delete(programId);
        }
    }

    private static void testClientListener(MySqlClientDAO clientDAO) throws SQLException {
        // Client Event Listener
        System.out.println("\n----- Testing Client -----");
        EntityEventListener<Client> clientEventListener = new ClientEventListener();
        clientDAO.subscribe(clientEventListener);
        Client client = new Client.Builder().username("test_user")
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john@example.com")
                .password("test_password")
                .build();

        long clientId = clientDAO.save(client);
        Optional<Client> foundClient = clientDAO.findById(clientId);
        if (foundClient.isPresent()) {
            Client foundClient1 = foundClient.get();
            foundClient1.setFirstname("Updated John");
            clientDAO.update(foundClient1);
            clientDAO.delete(clientId);
        }
    }

    private static void testAdvertisingListener(MySqlAdvertisingDAO advertisingDAO) throws SQLException {
        //         Advertising Event Listener
        System.out.println("\n----- Testing Advertising -----");
        EntityEventListener<Advertising> advertisingEventListener = new AdvertisingEventListener();
        advertisingDAO.subscribe(advertisingEventListener);

        Advertising advertising = new Advertising.Builder().name("TEST Advertising")
                .category(new Category(1))
                .measurement("Seconds")
                .unitPrice(BigDecimal.valueOf(120.00))
                .description("Prime time TV slot")
                .build();

        long advertisingId = advertisingDAO.save(advertising);
        Optional<Advertising> foundAdvertising = advertisingDAO.findById(advertisingId);
        if (foundAdvertising.isPresent()) {
            Advertising foundAd = foundAdvertising.get();
            foundAd.setName("Updated TEST Advertising");
            advertisingDAO.update(foundAd);
            advertisingDAO.delete(advertisingId);
        }
    }

    private static void testCategoryListener(MySqlCategoryDAO categoryDAO) throws SQLException {
        // Category Event Listener
        System.out.println("\n----- Testing Category -----");
        EntityEventListener<Category> listener1 = new CategoryEventListener();
        EntityEventListener<Category> listener2 = new CategoryEventListener();
        EntityEventListener<Category> listener3 = new CategoryEventListener();

        categoryDAO.subscribe(listener1); // Subscribe to all events
        categoryDAO.subscribe(listener2, EventType.ADDED);
        categoryDAO.subscribe(listener3, EventType.DELETED);

        long categoryId = categoryDAO.save(new Category("TEST Category"));
        Optional<Category> foundCategory = categoryDAO.findById(categoryId);
        if (foundCategory.isPresent()) {
            Category category = foundCategory.get();
            category.setName("Updated TEST Category");
            categoryDAO.update(category);
            categoryDAO.delete(categoryId);
        }
        categoryDAO.unsubscribe(listener1);
        long categoryId2 = categoryDAO.save(new Category("TEST 2 Category"));
        categoryDAO.unsubscribe(listener2);
        categoryDAO.delete(categoryId2);
    }

    // FIRST PRACTICE METHODS
    private static void testCategory(CategoryDAO categoryDAO) throws SQLException {
        System.out.println("----- Testing Category -----");

        // Find All Categories
        System.out.println("All Categories:");
        categoryDAO.findAll().forEach(System.out::println);
        System.out.println();
    }

    private static void testAdvertising(AdvertisingDAO advertisingDAO) throws SQLException {
        System.out.println("----- Testing Advertising -----");

        // Add Advertising
        Category category = new Category(1);
        Advertising advertising = new Advertising.Builder(category, "TEST New TV Ad", "Seconds", BigDecimal.valueOf(120.00), "Prime time TV slot").build();
        advertisingDAO.save(advertising);
        System.out.println("Added Advertising: " + advertising);

        // Find Advertising by ID
        Optional<Advertising> foundAdvertising = advertisingDAO.findById(advertising.getId());
        System.out.println("Found Advertising by ID: " + foundAdvertising.orElse(null));

        // Find Advertising by Name
        Optional<Advertising> foundByName = advertisingDAO.findByName("TEST New TV Ad");
        System.out.println("Found Advertising by Name: " + foundByName.orElse(null));

        // Update Advertising
        advertising.setDescription("Updated description");
        advertisingDAO.update(advertising);
        System.out.println("Updated Advertising: " + advertisingDAO.findById(advertising.getId()).orElse(null));

        // Delete Advertising
        advertisingDAO.delete(advertising.getId());
        System.out.println("Deleting Advertising");
        System.out.println("Found Advertising by ID: " + advertisingDAO.findById(advertising.getId()).orElse(null));
    }

    private static void testClient(ClientDAO clientDAO) throws SQLException {
        System.out.println("\n----- Testing Client -----");

        // Add Client
        Client client = new Client.Builder().id(1)
                .username("test_user")
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john.snow@example.com")
                .password("test_password")
                .build();
        clientDAO.save(client);
        System.out.println("Added Client: " + client);

/*        // Find Client by ID
        Optional<Client> foundClient = clientDAO.findById(6);
        System.out.println("Found Client by ID: " + foundClient.orElse(null));

        // Delete Client and Programs
        System.out.println("Deleting Client and Programs: " + clientDAO.deleteClientAndPrograms(6));
        System.out.println("Found Client by ID: " + clientDAO.findById(6).orElse(null));*/

        // Find Client by Username
        Optional<Client> foundByUsername = clientDAO.findByUsername("test_user");
        System.out.println("Found Client by Username: " + foundByUsername.orElse(null));

        // Update Client
        client.setFirstname("Updated John");
        clientDAO.update(client);
        System.out.println("Updated Client: " + clientDAO.findById(client.getId()).orElse(null));

        // Delete Client
        clientDAO.delete(client.getId());
        System.out.println("Deleting Client");
        System.out.println("Found Client by ID: " + clientDAO.findById(client.getId()).orElse(null));
    }

    private static void testProgram(ProgramDAO programDAO) throws SQLException {
        System.out.println("\n----- Testing Program -----");

        // Add Program
        Program program = new Program.Builder().client(new Client.Builder().id(1).build())
                .campaignTitle("TEST Campaign")
                .description("TEST Description")
                .createdAt(LocalDateTime.now())
                .addAdvertising(new Advertising.Builder().id(1).build(), 3)
                .addAdvertising(new Advertising.Builder().id(2).build(), 10)
                .build();
        programDAO.save(program);
        System.out.println("Added Program: " + program);

        // Find Program by ID
        Optional<Program> foundProgram = programDAO.findById(program.getId());
        System.out.println("Found Program by ID: " + foundProgram.orElse(null));

        // Add Advertising to Program
        Advertising programAdvertising = new Advertising.Builder()
                .id(13)
                .build();
        programDAO.saveAdvertisingToProgram(program.getId(), Map.of(programAdvertising, 20));
        System.out.println("Added Advertising to Program (id): " + programAdvertising.getId());

        // Update Program Advertising
        program.addAdvertising(programAdvertising, 30);
        programDAO.update(program);
        System.out.println("Updated Program Advertising (quantity): " + programDAO.findById(program.getId()).orElse(null));

        // Delete Advertising from Program
        programDAO.deleteAdvertisingFromProgram(program.getId(), programAdvertising.getId());
        System.out.println("Deleted Advertising from Program: " + programDAO.findById(program.getId()).orElse(null));

        // Delete Program
        programDAO.delete(program.getId());
        System.out.println("Deleting Program");
        System.out.println("Found Program by ID: " + programDAO.findById(program.getId()).orElse(null));
    }
}
