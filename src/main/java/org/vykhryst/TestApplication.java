package org.vykhryst;

import org.vykhryst.dao.DAOFactory;
import org.vykhryst.dao.MySqlDaoFactory;
import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.dao.entityDao.UserDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlAdvertisingDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlCategoryDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlUserDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlProgramDAO;
import org.vykhryst.entity.*;
import org.vykhryst.observer.*;
import org.vykhryst.observer.listeners.*;
import org.vykhryst.proxy.ProxyAdvertisingDAO;
import org.vykhryst.proxy.ProxyProgramDAO;
import org.vykhryst.service.AuthenticationService;
import org.vykhryst.proxy.ProxyUserDAO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class TestApplication {

    public static void main(String[] args) {
        // Initialize DAO Factory
        DAOFactory daoFactory = MySqlDaoFactory.getInstance();

        // Initialize DAOs
        CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
        AdvertisingDAO advertisingDAO = daoFactory.getAdvertisingDAO();
        ProgramDAO programDAO = daoFactory.getProgramDAO();
        UserDAO userDAO = daoFactory.getUserDAO();

        // FOURTH PRACTICE
        AuthenticationService authenticationService = new AuthenticationService(userDAO);
        // Uncomment one of the following lines to test authentication
        User authenticatedUser = authenticationService.authenticate("client1", "password1");
//        User authenticatedUser = authenticationService.authenticate("admin2", "password2");
        System.out.println("AUTHENTICATED USER:\n" + authenticatedUser);

        // Initialize Proxy DAOs
        UserDAO proxyUserDao = new ProxyUserDAO(authenticatedUser, userDAO);
        AdvertisingDAO proxyAdvertisingDao = new ProxyAdvertisingDAO(authenticatedUser, advertisingDAO);
        ProgramDAO proxyProgramDao = new ProxyProgramDAO(authenticatedUser, programDAO);


        System.out.println("\n----- Testing UserDAOProxy -----");

        testProxyUserDao(proxyUserDao);

        System.out.println("\n----- Testing AdvertisingDAOProxy -----");

        testProxyAdvertisingDao(proxyAdvertisingDao);

        System.out.println("\n----- Testing ProgramDAOProxy -----");

        testProxyProgramDao(proxyAdvertisingDao, proxyUserDao, proxyProgramDao);


        // THIRD PRACTICE
        /*EntityEventListener<User> clientEventListener = new UserEventListener();
        userDAO.subscribe(clientEventListener);

        User user = new User.Builder().username("test_user")
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john@example.com")
                .password("test_password")
                .build();

        UserCaretaker userCaretaker = new UserCaretaker();
        userCaretaker.save(user, userDAO);

        System.out.println("\nFirst update");
        user.setFirstname("FIRST Updated name");
        userCaretaker.update(user, userDAO);

        System.out.println("\nSecond update");
        user.setFirstname("SECOND Updated name");
        user.setLastname("SECOND Updated lastname");
        userCaretaker.update(user, userDAO);

        System.out.println("\nFirst undo");
        userCaretaker.undo(user, userDAO);

        System.out.println("\nSecond undo");
        userCaretaker.undo(user, userDAO);

        System.out.println();
        userDAO.delete(user.getId());

        System.out.println("\nThird undo");
        userCaretaker.undo(user, userDAO);*/

        // SECOND PRACTICE
        /*testCategoryListener((MySqlCategoryDAO) categoryDAO);

        testAdvertisingListener((MySqlAdvertisingDAO) advertisingDAO);

        testClientListener((MySqlUserDAO) userDAO);

        testProgramListener((MySqlAdvertisingDAO) advertisingDAO, (MySqlUserDAO) userDAO, (MySqlProgramDAO) programDAO);*/

        // FIRST PRACTICE
        /*
        // Test Category
        testCategory(daoFactory.getCategoryDAO());

        // Test Advertising
        testAdvertising(daoFactory.getAdvertisingDAO());

        // Test Client
        testClient(daoFactory.getUserDAO());

        // Test Program
        testProgram(daoFactory.getProgramDAO());*/
    }

    private static void testProxyProgramDao(AdvertisingDAO advertisingDAO, UserDAO userDAO, ProgramDAO proxyProgramDao) {
        System.out.println("Find program by id: " + proxyProgramDao.findById(1).orElse(null));


        Advertising advertising1 = advertisingDAO.findById(1).orElse(null);
        Advertising advertising2 = advertisingDAO.findById(2).orElse(null);

        Program program = new Program.Builder().client(userDAO.findById(1).orElse(null))
                .campaignTitle("TEST Campaign")
                .description("TEST Description")
                .createdAt(LocalDateTime.now())
                .addAdvertising(advertising1, 3)
                .addAdvertising(advertising2, 10)
                .build();

        System.out.println("Saving program: " + proxyProgramDao.save(program));

        program.setCampaignTitle("Updated TEST Campaign");
        program.setDescription("Updated TEST Description");
        program.getAdvertising().entrySet().stream().findFirst().ifPresent(entry -> {
            entry.getKey().setName("Updated TEST Advertising");
            entry.setValue(20);
        });
        System.out.println("Updating program: " + proxyProgramDao.update(program));

        System.out.println("Deleting program: " + proxyProgramDao.delete(program.getId()));
    }

    private static void testProxyAdvertisingDao(AdvertisingDAO proxyAdvertisingDao) {
        System.out.println("Find advertising by id: " + proxyAdvertisingDao.findById(1).orElse(null));

        Advertising advertisingToAdd = new Advertising.Builder().name("TEST Advertising")
                .category(new Category(1))
                .measurement("Seconds")
                .unitPrice(BigDecimal.valueOf(120.00))
                .description("Prime time TV slot")
                .build();
        System.out.println("Saving advertising: " + proxyAdvertisingDao.save(advertisingToAdd));

        advertisingToAdd.setName("Updated TEST Advertising");
        System.out.println("Updating advertising: " + proxyAdvertisingDao.update(advertisingToAdd));

        System.out.println("Deleting advertising: " + proxyAdvertisingDao.delete(advertisingToAdd.getId()));
    }

    private static void testProxyUserDao(UserDAO proxyUserDao) {
        System.out.println("Find user by id: " + proxyUserDao.findById(1).orElse(null));

        User userToAdd = new User.Builder().username("test_user")
                .role(Role.ADMIN)
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212643790")
                .email("johnnn@example.com")
                .password("test_password")
                .build();
        System.out.println("Saving user: " + proxyUserDao.save(userToAdd));

        userToAdd.setFirstname("Updated John");
        userToAdd.setLastname("Updated Snow");
        System.out.println("Updating user: " + proxyUserDao.update(userToAdd));

        System.out.println("Deleting user: " + proxyUserDao.delete(userToAdd.getId()));
    }

    // SECOND PRACTICE METHODS
    private static void testProgramListener(MySqlAdvertisingDAO advertisingDAO, MySqlUserDAO userDAO, MySqlProgramDAO programDAO) {
        System.out.println("\n----- Testing Program -----");
        // Program Event Listener
        EntityEventListener<Program> programEventListener = new ProgramEventListener();
        programDAO.subscribe(programEventListener);

        Advertising advertising1 = advertisingDAO.findById(1).orElse(null);
        Advertising advertising2 = advertisingDAO.findById(2).orElse(null);

        Program program = new Program.Builder().client(userDAO.findById(1).orElse(null))
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

    private static void testClientListener(MySqlUserDAO userDAO) {
        // Client Event Listener
        System.out.println("\n----- Testing Client -----");
        EntityEventListener<User> clientEventListener = new UserEventListener();
        userDAO.subscribe(clientEventListener);
        User user = new User.Builder().username("test_user")
                .role(Role.USER)
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john@example.com")
                .password("test_password")
                .build();

        long clientId = userDAO.save(user);
        Optional<User> foundClient = userDAO.findById(clientId);
        if (foundClient.isPresent()) {
            User foundUser1 = foundClient.get();
            foundUser1.setFirstname("Updated John");
            userDAO.update(foundUser1);
            userDAO.delete(clientId);
        }
    }

    private static void testAdvertisingListener(MySqlAdvertisingDAO advertisingDAO) {
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

    private static void testCategoryListener(MySqlCategoryDAO categoryDAO) {
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
    private static void testCategory(CategoryDAO categoryDAO) {
        System.out.println("----- Testing Category -----");

        // Find All Categories
        System.out.println("All Categories:");
        categoryDAO.findAll().forEach(System.out::println);
        System.out.println();
    }

    private static void testAdvertising(AdvertisingDAO advertisingDAO) {
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

    private static void testClient(UserDAO userDAO) {
        System.out.println("\n----- Testing User -----");

        // Add User
        User user = new User.Builder().id(1)
                .role(Role.USER)
                .username("test_user")
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john.snow@example.com")
                .password("test_password")
                .build();
        userDAO.save(user);
        System.out.println("Added Client: " + user);

/*        // Find Client by ID
        Optional<Client> foundClient = userDAO.findById(6);
        System.out.println("Found Client by ID: " + foundClient.orElse(null));

        // Delete Client and Programs
        System.out.println("Deleting Client and Programs: " + userDAO.deleteClientAndPrograms(6));
        System.out.println("Found Client by ID: " + userDAO.findById(6).orElse(null));*/

        // Find Client by Username
        Optional<User> foundByUsername = userDAO.findByUsername("test_user");
        System.out.println("Found Client by Username: " + foundByUsername.orElse(null));

        // Update Client
        user.setFirstname("Updated John");
        userDAO.update(user);
        System.out.println("Updated Client: " + userDAO.findById(user.getId()).orElse(null));

        // Delete Client
        userDAO.delete(user.getId());
        System.out.println("Deleting Client");
        System.out.println("Found Client by ID: " + userDAO.findById(user.getId()).orElse(null));
    }

    private static void testProgram(ProgramDAO programDAO) {
        System.out.println("\n----- Testing Program -----");

        // Add Program
        Program program = new Program.Builder().client(new User.Builder().id(1).build())
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
