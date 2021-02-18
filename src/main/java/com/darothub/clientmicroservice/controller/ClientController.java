package com.darothub.clientmicroservice.controller;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.*;
import com.darothub.clientmicroservice.exceptions.CustomException;
import com.darothub.clientmicroservice.filter.JwtFilter;
import com.darothub.clientmicroservice.service.ClientService;
//import com.darothub.clientmicroservice.utils.JWTUtility;
import com.darothub.clientmicroservice.utils.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class ClientController {


    private final ClientService clientService;

    private SuccessResponse successResponse = new SuccessResponse();
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private JwtFilter jwtFilter;
    //
    @Autowired
    private AuthenticationManager authenticationManager;

    ModelMapper modelMapper = new ModelMapper();

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ResponseEntity<ResponseModel> addClient(@Valid @RequestBody Client client, @RequestHeader("Authorization") String auth) {
        ClientRequest clientCreated = clientService.addClient(client, auth);
        return handleSuccessResponseEntity("Client added successfully", HttpStatus.CREATED, clientCreated);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<ResponseModel> removeClient(@PathVariable("id") Long clientId, @RequestHeader("Authorization") String auth) {
        ClientRequest clientCreated = clientService.removeClient(clientId);
        return handleSuccessResponseEntity("Client deleted successfully", HttpStatus.OK, clientCreated);
    }

    //
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseModel> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmailAddress(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "Invalid username/password");
            throw new CustomException(error);
        }

        final UserDetails userDetails = clientService.loadUserByUsername(
                jwtRequest.getEmailAddress());

        if (userDetails.getPassword().equals(jwtRequest.getPassword())) {
            final String token = jwtUtility.generateToken(userDetails);
            return handleSuccessResponseEntity("Client found", HttpStatus.OK, new JwtResponse(token));
        } else {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "Invalid username/password");
            throw new CustomException(error);
        }

    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ResponseModel> getClientById(@PathVariable("id") Long clientId) {

        ClientRequest clientRequest = clientService.getClientById(clientId);
        log.info("Inside ClientController getClientById " + clientRequest);
        return handleSuccessResponseEntity("Client found", HttpStatus.OK, clientRequest);

    }

    @GetMapping("/clients")
    public ResponseEntity<ResponseModel> getAllClient() {
        log.info("Inside ClientController getAllClient");
        List<ClientRequest> clients = clientService.getAllClient();
        return handleSuccessResponseEntity("Clients", HttpStatus.OK, clients);

    }


    @GetMapping("/")
    public String home() {

        return "Welcome";

    }


    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return new ResponseEntity<>(successResponse, status);
    }
}
