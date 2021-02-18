package com.darothub.clientmicroservice.service;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.dto.Artisan;
import com.darothub.clientmicroservice.entity.Client;
import com.darothub.clientmicroservice.entity.ErrorResponse;
import com.darothub.clientmicroservice.entity.SuccessResponse;
import com.darothub.clientmicroservice.exceptions.CustomException;
import com.darothub.clientmicroservice.repository.ClientRepository;
import com.darothub.clientmicroservice.utils.JWTUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
@Slf4j
public class ClientService implements ClientServices, UserDetailsService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpHeaders headers;
    @Autowired
    private JWTUtility jwtUtility;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public <T> ClientRequest addClient(T t, String auth) throws CustomException {
        log.info("Inside ClientService add client");
        Client client = modelMapper.map(t, Client.class);
        log.info("Inside ClientService add client" + client);
        String token = null;

        Client oldUser = clientRepository.findByEmailAddress(client.getEmailAddress());
        if(oldUser != null){
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), "Client already exists");
            throw new CustomException(error);
        }
        if(auth.startsWith("Bearer") && auth.length() > 7){
            token = auth.substring(7);
            log.info("Inside ClientService add client"+ token );
            int id = jwtUtility.getIdFromToken(token);
            log.info("Inside ClientService add client id" + id);
            client.setUserId(Long.valueOf(id));
            Client clientCreated = clientRepository.save(client);

            ClientRequest clientRequest = modelMapper.map(clientCreated, ClientRequest.class);
            return clientRequest;
        }
        else{
            ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(), "You are not authorized");
            throw new CustomException(error);
        }


    }
//    public UserDetails getUserById(Long id, String token){
//
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer "+token);
//
//        HttpEntity entity = new HttpEntity(headers);
//
//        ResponseEntity<SuccessResponse> respEntity = restTemplate.exchange("http://localhost:8082/artisans/"+id, HttpMethod.GET, entity, SuccessResponse.class);
//
//        SuccessResponse resp = respEntity.getBody();
//
//        Object obj = resp.getPayload();
//
//        UserDTO userDTO = modelMapper.map(obj, UserDTO.class);
//
//
//        return new User(userDTO.getEmailAddress(), userDTO.getCountry(), new ArrayList<>());
//    }
//
    public UserDetails getUserById(Long id, String token) throws UsernameNotFoundException, JsonProcessingException {
        log.info("Id {}", id);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+token);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<SuccessResponse> respEntity = restTemplate.exchange("http://localhost:8082/artisans/"+id, HttpMethod.GET, entity, SuccessResponse.class);

        SuccessResponse resp = respEntity.getBody();
//
        log.info("Artisan {}", resp.getPayload());

        final ObjectMapper mapper = new ObjectMapper();
        Artisan artisan = mapper.convertValue(resp.getPayload(), new TypeReference<Artisan>(){});
        return new User(artisan.getEmailAddress(), artisan.getFirstName(), new ArrayList<>());
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Optional<Client> client = Optional.of(clientRepository.findByEmailAddress(email));
            Client getClient = client.get();
            return new User(getClient.getUserId().toString(), getClient.getEmailAddress(), new ArrayList<>());
        } catch (Exception exception) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
            throw new CustomException(error);
        }

    }
}
