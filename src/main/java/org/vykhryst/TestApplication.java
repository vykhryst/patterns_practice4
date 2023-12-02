package org.vykhryst;

import org.vykhryst.dao.DAOFactory;
import org.vykhryst.dao.MySqlDaoFactory;
import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.ClientDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.entity.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TestApplication {

    public static void main(String[] args) throws SQLException {
        // Initialize DAO Factory
        DAOFactory daoFactory = MySqlDaoFactory.getInstance();

        // Test Advertising
        testAdvertising(daoFactory.getAdvertisingDAO());

        // Test Client
        testClient(daoFactory.getClientDAO());

        // Test Program
        testProgram(daoFactory.getProgramDAO());
    }

    private static void testAdvertising(AdvertisingDAO advertisingDAO) throws SQLException {
        System.out.println("----- Testing Advertising -----");

        // Find All Categories
        List<Category> categories = advertisingDAO.findAllCategories();
        System.out.println("All Categories:");
        categories.forEach(System.out::println);

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
        Client client = new Client.Builder(1)
                .username("test_user")
                .firstname("John")
                .lastname("Snow")
                .phoneNumber("1212343490")
                .email("john.snow@example.com")
                .password("test_password")
                .build();
        clientDAO.save(client);
        System.out.println("Added Client: " + client);

        // Find Client by ID
        Optional<Client> foundClient = clientDAO.findById(client.getId());
        System.out.println("Found Client by ID: " + foundClient.orElse(null));

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
        Program program = new Program.Builder().clientId(1)
                .addAdvertising(new ProgramAdvertising.Builder().advertisingId(1).quantity(3).build())
                .addAdvertising(new ProgramAdvertising.Builder().advertisingId(2).quantity(5).build())
                .build();
        programDAO.save(program);
        System.out.println("Added Program: " + program);

        // Find Program by ID
        Optional<Program> foundProgram = programDAO.findById(program.getId());
        System.out.println("Found Program by ID: " + foundProgram.orElse(null));

        // Add Advertising to Program
        ProgramAdvertising programAdvertising = new ProgramAdvertising.Builder()
                .advertisingId(13)
                .quantity(10)
                .build();
        programDAO.saveAdvertisingToProgram(program.getId(), programAdvertising);
        System.out.println("Added Advertising to Program: " + programAdvertising);

        // Update Program Advertising
        programAdvertising.setQuantity(20);
        program.addAdvertising(programAdvertising);
        programDAO.update(program);
        System.out.println("Updated Program Advertising (quantity): " + programDAO.findById(program.getId()).orElse(null));

        // Delete Advertising from Program
        programDAO.deleteAdvertisingFromProgram(program.getId(), programAdvertising.getAdvertisingId());
        System.out.println("Deleted Advertising from Program: " + programDAO.findById(program.getId()).orElse(null));

        // Delete Program
        programDAO.delete(program.getId());
        System.out.println("Deleting Program");
        System.out.println("Found Program by ID: " + programDAO.findById(program.getId()).orElse(null));
    }
}
