package com.darothub.clientmicroservice.controller;


import com.darothub.clientmicroservice.dto.ClientRequest;
import com.darothub.clientmicroservice.entity.Client;
import com.darothub.clientmicroservice.entity.ErrorResponse;
import com.darothub.clientmicroservice.exceptions.CustomException;
import com.darothub.clientmicroservice.exceptions.CustomRestExceptionHandler;
import com.darothub.clientmicroservice.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;
    private MockMvc mockMvc;

    @InjectMocks
    private ClientController clientController;

    ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();

    String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYXJvdEBnbWFpbC5jb20iLCJpZCI6MSwiZXhwIjowLCJpYXQiOjE2MTM0MDMxNzR9.69uNWQa-MsyA_NmzLyybaMgALoKg37VZX-HAQALrQzqg_GqAsTTpBQSsV35KMU158w9eK982Ag55-MFhVhn6QQ";

    @BeforeEach
    public void setup() {
        // We would need this line if we would not use the MockitoExtension
        // MockitoAnnotations.initMocks(this);
        // Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context

        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();
    }

    @Test
    void whenAddClientWithCompleteConstraintReturnCreated() throws Exception {
        Client clientRequest = getCompleteClient();
        ClientRequest clientDTO = modelMapper.map(clientRequest, ClientRequest.class);
        String json = objectMapper.writeValueAsString(clientRequest);

        when(clientService.addClient(clientRequest, TOKEN)).thenReturn(clientDTO);
        this.mockMvc.perform(post("/clients")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        )
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string(containsString("Client added successfully")));



    }

    @Test
    void whenGetClientsReturnAnEmptyArrayWhenNoClientAdded() throws Exception {
        Client clientRequest = getCompleteClient();
        String json = objectMapper.writeValueAsString(clientRequest);

        this.mockMvc.perform(get("/clients")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").isEmpty())
                .andReturn();
    }

    @Test
    void whenClientAddedSuccessfullyReturnCreatedStatus() throws Exception {
        Client client = getCompleteClient();
        String json = objectMapper.writeValueAsString(client);
        ClientRequest clientDTO = modelMapper.map(client, ClientRequest.class);
        when(clientService.addClient(client, TOKEN)).thenReturn(clientDTO);

        this.mockMvc.perform(post("/clients")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        ).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").value(clientDTO))
                .andReturn();
    }

    @Test
    void whenAddClientReturnPayload() throws Exception {
        Client clientRequest = getCompleteClient();
        String json = objectMapper.writeValueAsString(clientRequest);
        ClientRequest clientDTO = modelMapper.map(clientRequest, ClientRequest.class);
        when(clientService.addClient(clientRequest, TOKEN)).thenReturn(clientDTO);

        mockMvc.perform(post("/clients")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        ).andDo(print()).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists())
                .andReturn();

    }

    @Test
    void whenGetClientByIdReturnsClientIfPresent() throws Exception {
        Client clientRequest = getCompleteClient();
        clientRequest.setId(1L);
        ClientRequest clientDTO = modelMapper.map(clientRequest, ClientRequest.class);

        String json = objectMapper.writeValueAsString(clientRequest);
        when(clientService.getClientById(1L)).thenReturn(clientDTO);
        mockMvc.perform(get("/clients/1")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists())
                .andReturn();

    }

    @Test
    void whenGetClientByIdThrowsErrorIfNotPresent() throws Exception {
        Client clientRequest = getCompleteClient();
        clientRequest.setId(1L);
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), "No resource found");
        when(clientService.getClientById(1L)).thenThrow(new CustomException(error));
        mockMvc.perform(get("/clients/1")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andReturn();

    }

    @Test
    void whenGetClientByIdThrowsErrorNoResourceFoundIfNotPresent() throws Exception {
        Client clientRequest = getCompleteClient();
        clientRequest.setId(1L);
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), "No resource found");
        when(clientService.getClientById(1L)).thenThrow(new CustomException(error));
        mockMvc.perform(get("/clients/1")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(error.getError()))
                .andReturn();

    }

    @Test
    void whenAddingIncompleteClientShouldReturnError() throws Exception {
        Client client = getIncompleteClient();
        String json = objectMapper.writeValueAsString(client);

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "First name must not be blank");
        this.mockMvc.perform(post("/clients")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        ).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andReturn();

    }

    @Test
    void whenAddingClientWithoutFirstNameItShouldReturnError() throws Exception {
        Client client = getIncompleteClient();
        String json = objectMapper.writeValueAsString(client);

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "First name must not be blank");
        this.mockMvc.perform(post("/clients")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
                .content(json)
        ).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").hasJsonPath())
                .andReturn();

    }

    @Test
    void whenGetAllClientsReturnsListOfClients() throws Exception {
        Client clientRequest = getCompleteClient();
        ClientRequest clientDTO = modelMapper.map(clientRequest, ClientRequest.class);
        when(clientService.getAllClient()).thenReturn(Arrays.asList(clientDTO));
        mockMvc.perform(get("/clients")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+TOKEN)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").isArray())
                .andReturn();
//        when(clientService.addClient(clientRequest)).thenReturn(clientRequest);
//
//        this.mockMvc.perform(post("/clients")
//                .characterEncoding("utf-8")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//        ).andDo(print()).andExpect(status().isCreated())
//                .andExpect(content().string(containsString("Client added successfully")));

//        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/clients", clientRequest,
//                ErrorResponse.class)).hasFieldOrProperty("error");
//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/clients",
//                ResponseModel.class)).hasFieldOrProperty("status");
//
//        ResponseEntity<ResponseModel> response = clientController.addClient(clientRequest);
//        assertNotNull(response);
//
//        Mockito.when(fakeClientService.addClient(clientRequest)).thenReturn(clientRequest);
//        String url = "/clients";
//        mockMvc.perform(MockMvcRequestBuilders.post(url)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
    }

    @Test
    public void getAllClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }


    private Client getCompleteClient() {
        Client clientRequest = new Client();

        clientRequest.setFirstName("Peacedude");
        clientRequest.setLastName("Peacedude");
        clientRequest.setGender("Male");
        clientRequest.setEmailAddress("darot@gmail.com");
        clientRequest.setPhoneNumber("08060085192");

        clientRequest.setDeliveryAddress("Delivery");
        return clientRequest;
    }


    private Client getIncompleteClient() {
        Client clientRequest = new Client();
        clientRequest.setLastName("Peacedude");
        clientRequest.setGender("Male");
        clientRequest.setEmailAddress("darot@gmail.com");
        clientRequest.setPhoneNumber("08060085192");
        clientRequest.setDeliveryAddress("Delivery");
        return clientRequest;
    }
}