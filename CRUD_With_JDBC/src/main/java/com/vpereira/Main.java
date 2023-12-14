package com.vpereira;

import com.vpereira.core.domain.Client;
import com.vpereira.core.domain.Product;
import com.vpereira.repository.ClientRepository;
import com.vpereira.repository.ProductRepository;
import com.vpereira.repository.generic.jdbc.TablesFactory;
import com.vpereira.service.ClientService;
import com.vpereira.service.ProductService;

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
        clientUpdate.setId(5L);
        clientUpdate.setFirstName("Victor5666666");
        clientUpdate.setLastName("Pereira5666666");
        clientUpdate.setEmail("Victor@Pereira.com5666666");
        clientUpdate.setGender("Masculino");

        clientService.create(clientCreate);
        clientService.update(clientUpdate);
        clientService.delete(11L);
        clientService.findById(5L);

        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService(productRepository);

        Product productCreate = new Product();
        productCreate.setName("Monitor");
        productCreate.setPrice(500.00F);
        productCreate.setDesc("Description");

        Product productUpdate = new Product();
        productUpdate.setId(5L);
        productUpdate.setName("Teclado");
        productUpdate.setPrice(150.00F);
        productUpdate.setDesc("Description");

        productService.create(productCreate);
        productService.update(productUpdate);
        productService.delete(11L);
        productService.findById(5L);
    }
}