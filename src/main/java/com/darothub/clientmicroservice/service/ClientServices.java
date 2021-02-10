package com.darothub.clientmicroservice.service;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.Client;
import org.modelmapper.ModelMapper;

import java.util.List;

public interface ClientServices {
    ModelMapper modelMapper = new ModelMapper();

    default ClientRequest convertEntityToDto(Client client) {
        return modelMapper.map(client, ClientRequest.class);
    }

    default Client convertDtoToEntity(ClientRequest clientRequest) {
        return modelMapper.map(clientRequest, Client.class);
    }

    <T> ClientRequest addClient(T t, String token) throws Exception;

    ClientRequest getClientById(Long clientId);

    List<ClientRequest> getAllClient();
}
