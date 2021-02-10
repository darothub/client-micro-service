package com.darothub.clientmicroservice;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.Client;
import com.darothub.clientmicroservice.repository.ClientRepository;
import com.darothub.clientmicroservice.service.ClientService;
import com.darothub.clientmicroservice.service.ClientServices;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;


public class EntityToDtoUnitTests {
    private ClientRepository fakeClientRepository;
    private ClientServices clientServices = new ClientService(fakeClientRepository);

    @Test
    public void whenConvertClientEntityToClientRequest_thenCorrect() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Darot");
        client.setLastName("Peacedude");
        client.setGender("Male");
        client.setEmailAddress("darot@gmail.com");
        client.setPhoneNumber("08060085192");
        client.setThumbnail("darot.jpg");

        ClientRequest clientRequest = clientServices.convertEntityToDto(client);
        assertEquals(client.getId(), clientRequest.getId());
        assertEquals(client.getFirstName(), clientRequest.getFirstName());

    }

    @Test
    public void whenConvertClientRequestToClientEntity_thenCorrect() {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setId(1L);
        clientRequest.setFirstName("Darot");
        clientRequest.setLastName("Peacedude");
        clientRequest.setGender("Male");
        clientRequest.setEmailAddress("darot@gmail.com");
        clientRequest.setPhoneNumber("08060085192");
        clientRequest.setThumbnail("darot.jpg");

        Client client = clientServices.convertDtoToEntity(clientRequest);

        assertEquals(clientRequest.getId(), client.getId());

    }
}
