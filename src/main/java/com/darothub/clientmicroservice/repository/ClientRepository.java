package com.darothub.clientmicroservice.repository;


import com.darothub.clientmicroservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsername(@Size(min = 3, max = 20) String username);
}
