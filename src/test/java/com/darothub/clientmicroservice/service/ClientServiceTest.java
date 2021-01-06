package com.darothub.clientmicroservice.service;

import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.Client;
import com.darothub.clientmicroservice.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
class ClientServiceTest {
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private ClientRepository fakeClientRepository;

    @Mock
    private ClientServices clientServices;



    @Test
    void addClient() {

        ClientRequest clientRequest = getClient();
        Client client = modelMapper.map(clientRequest, Client.class);
        Client clientCreated = fakeClientRepository.save(client);

        Assertions.assertEquals(clientCreated, client);
    }

    @Test
    void getAllClients(){
        addClient();
        List<Client> clientList = fakeClientRepository.findAll();
        assertNotNull(clientList);
    }

    @Test
    void confirmSomeValuesOfEachItem(){
        addClient();
        List<Client> clientList = fakeClientRepository.findAll();
        Client client0 = clientList.get(0);
        Assertions.assertEquals("Darot", client0.getFirstName());
    }

    private ClientRequest getClient(){
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setFirstName("Darot");
        clientRequest.setLastName("Peacedude");
        clientRequest.setGender("Male");
        clientRequest.setEmailAddress("darot@gmail.com");
        clientRequest.setPhoneNumber("08060085192");
        clientRequest.setThumbnail("darot.jpg");
        return clientRequest;
    }


}