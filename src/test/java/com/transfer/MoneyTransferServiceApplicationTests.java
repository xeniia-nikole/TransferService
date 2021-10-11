package com.transfer;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyTransferServiceApplicationTests {


    private final String HOST = "http://localhost:";


    @Autowired
    TestRestTemplate restTemplate;

    @Container
    public static GenericContainer<?> back_app = new GenericContainer<>("back_transfer:3.0")
            .withExposedPorts(5500);

    @Container
    public static GenericContainer<?> front_app = new GenericContainer<>("front_transfer:3.0")
            .withExposedPorts(3000
            );

    @BeforeAll
    public static void setUp() {
        back_app.start();
        front_app.start();
    }


    @Test
    void context_back() {

        var port_back = back_app.getMappedPort(5500);
        ResponseEntity<String> forEntity_back_app = restTemplate.getForEntity(HOST + port_back + "/profile", String.class);

        System.out.println("\nPort MoneyTransferService: " + port_back);
        System.out.println(forEntity_back_app.getBody());
        Assertions.assertTrue(back_app.isRunning());
    }

    @Test
    void context_front() {

        var port_front = back_app.getMappedPort(5500);
        ResponseEntity<String> forEntity_front_app = restTemplate.getForEntity(HOST + port_front + "/profile", String.class);

        System.out.println("\nPort MoneyTransferService: " + port_front);
        System.out.println(forEntity_front_app.getBody());
        Assertions.assertTrue(front_app.isRunning());
    }

}
