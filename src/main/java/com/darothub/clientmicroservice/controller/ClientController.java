package com.darothub.clientmicroservice.controller;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.ResponseModel;
import com.darothub.clientmicroservice.entity.SuccessResponse;
import com.darothub.clientmicroservice.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
@Slf4j
public class ClientController {


    private final ClientService clientService;
    private final SuccessResponse successResponse = new SuccessResponse();


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping
    public ResponseEntity<ResponseModel> addClient(@Valid @RequestBody ClientRequest clientRequest) {
        log.info("Inside ClientController add clientRequest"+ clientRequest );
//        Client client = modelMapper.map(clientRequest, Client.class);
        ClientRequest clientCreated = clientService.addClient(clientRequest);
//        log.info("Inside ClientController add clientRequest"+ client );
        return handleSuccessResponseEntity("Client added successfully", HttpStatus.CREATED, clientCreated);

    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getClientById(@PathVariable("id") Long clientId) {

        ClientRequest clientRequest = clientService.getClientById(clientId);
        log.info("Inside ClientController getClientById " + clientRequest);
        return handleSuccessResponseEntity("Client found", HttpStatus.OK, clientRequest);

    }

    @GetMapping
    public ResponseEntity<ResponseModel> getAllClient() {
        log.info("Inside ClientController getAllClient");
        List<ClientRequest> clients = clientService.getAllClient();
        return handleSuccessResponseEntity("Clients", HttpStatus.OK, clients);

    }

    @PostMapping("/greeting")
    public ResponseEntity<ResponseModel> greeting(@Valid @RequestBody ClientRequest clientRequest) {
        ClientRequest res = clientService.greet();
        return handleSuccessResponseEntity("Clients", HttpStatus.OK, res);
    }

    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload){
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return new ResponseEntity<>(successResponse, status);
    }
}
