package com.darothub.clientmicroservice.service;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.Client;
import com.darothub.clientmicroservice.entity.ErrorResponse;
import com.darothub.clientmicroservice.exceptions.CustomException;
import com.darothub.clientmicroservice.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
@Slf4j
public class ClientService implements ClientServices, UserDetailsService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public <T> ClientRequest addClient(T t)  {
        log.info("Inside ClientService add client");
        Client client = modelMapper.map(t, Client.class);
        log.info("Inside ClientService add client" + client);
        Client clientCreated = clientRepository.save(client);

        ClientRequest clientRequest = modelMapper.map(clientCreated, ClientRequest.class);
        return clientRequest;
    }

    @Override
    public ClientRequest getClientById(Long clientId) {

        try {
            Optional<Client> client = Optional.of(clientRepository.findById(clientId).get());
            return convertEntityToDto(client.get());

        } catch (Exception exception) {
            ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), "No resource found");
            throw new CustomException(error);
        }

    }

    public List<ClientRequest> getAllClient() {
        List<Client> clients = clientRepository.findAll();
        log.info("Inside ClientService getAllClient" + clients);
        return clients.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<Client> client = Optional.of(clientRepository.findByUsername(username));
            Client getClient = client.get();
            return new User(getClient.getUsername(), getClient.getPassword(), new ArrayList<>());
        } catch (Exception exception) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "Invalid username/password");
            throw new CustomException(error);
        }

    }
}
