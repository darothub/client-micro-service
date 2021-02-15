package com.darothub.clientmicroservice.repository;


import com.darothub.clientmicroservice.entity.Client;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testSaveClient() {
        Client client = getClient();
        Client saved = entityManager.merge(client);

        Client getFromRepo = clientRepository.findById(saved.getId()).get();

        assertEquals(getFromRepo, saved);
    }

    private Client getClient() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Darot");
        client.setLastName("Peacedude");
        client.setGender("Male");
        client.setEmailAddress("darot@gmail.com");
        client.setPhoneNumber("08060085192");
        return client;
    }

}