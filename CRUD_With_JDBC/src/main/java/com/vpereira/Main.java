package com.vpereira;

import com.vpereira.model.Client;
import com.vpereira.repository.ClientRepository;
import com.vpereira.repository.ProductRepository;
import com.vpereira.repository.generic.jdbc.TablesFactory;
import com.vpereira.service.ClientService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        TablesFactory.createTablesJDBC();

        ClientRepository clientRepository = new ClientRepository();
        ClientService clientService = new ClientService(clientRepository);

        Client clientCreate = new Client();
        clientCreate.setId(1L);
        clientCreate.setFirstName("Victor");
        clientCreate.setLastName("Pereira");
        clientCreate.setEmail("Victor@Pereira.com");
        clientCreate.setGender("Masculino");

        Client clientUpdate = new Client();
        clientUpdate.setId(1L);
        clientUpdate.setFirstName("Victor5");
        clientUpdate.setLastName("Pereira5");
        clientUpdate.setEmail("Victor@Pereira.com");
        clientUpdate.setGender("Masculino");

        clientService.create(clientCreate);
        clientService.update(clientUpdate);
        clientService.delete(12L);
        clientService.findById(2L);
    }
}