package com.vpereira;

import com.vpereira.core.domain.Client;
import com.vpereira.core.domain.Default;
import com.vpereira.core.domain.Product;
import com.vpereira.repository.ClientRepository;
import com.vpereira.repository.DefaultRepository;
import com.vpereira.repository.ProductRepository;
import com.vpereira.repository.generic.jdbc.TablesFactory;
import com.vpereira.service.ClientService;
import com.vpereira.service.DefaultService;
import com.vpereira.service.ProductService;

public class Main {
    public static void main(String[] args) {
//        TablesFactory.createTablesJDBC();
//
//        ClientRepository clientRepository = new ClientRepository();
//        ClientService clientService = new ClientService(clientRepository);
//
//        Client clientCreate = new Client();
//        clientCreate.setId(1L);
//        clientCreate.setFirstName("Victor");
//        clientCreate.setLastName("Pereira");
//        clientCreate.setEmail("Victor@Pereira.com");
//        clientCreate.setGender("Masculino");
//
//        Client clientUpdate = new Client();
//        clientUpdate.setId(5L);
//        clientUpdate.setFirstName("Victor5666666");
//        clientUpdate.setLastName("Pereira5666666");
//        clientUpdate.setEmail("Victor@Pereira.com5666666");
//        clientUpdate.setGender("Masculino");
//
//        clientService.create(clientCreate);
//        clientService.update(clientUpdate);
//        clientService.delete(11L);
//        clientService.findById(5L);
//        clientService.findAll();
//
//        ProductRepository productRepository = new ProductRepository();
//        ProductService productService = new ProductService(productRepository);
//
//        Product productCreate = new Product();
//        productCreate.setName("Monitor");
//        productCreate.setPrice(500.00F);
//        productCreate.setDesc("Description");
//
//        Product productUpdate = new Product();
//        productUpdate.setId(5L);
//        productUpdate.setName("Teclado");
//        productUpdate.setPrice(150.00F);
//        productUpdate.setDesc("Description");
//
//        productService.create(productCreate);
//        productService.update(productUpdate);
//        productService.delete(11L);
//        productService.findById(5L);
//        productService.findAll();
//
//        DefaultRepository defaultRepository = new DefaultRepository();
//        DefaultService defaultService = new DefaultService(defaultRepository);
//        Default defaultCreate = new Default();
//        defaultCreate.setVarBoolean(true);
//        defaultCreate.varCharacterSet('d');
//        defaultCreate.varDoubleSet(2.4D);
//        defaultCreate.setVarFloat(2.6F);
//        defaultCreate.setVarInteger(10);
//        defaultCreate.setVarString("String");
//        defaultCreate.setVarStringLength("String Length");
//
//        Default defaultUpdate = new Default();
//        defaultUpdate.setId(1L);
//        defaultUpdate.setVarBoolean(false);
//        defaultUpdate.varCharacterSet('g');
//        defaultUpdate.varDoubleSet(5.4D);
//        defaultUpdate.setVarFloat(5.6F);
//        defaultUpdate.setVarInteger(20);
//        defaultUpdate.setVarString("String Update");
//        defaultUpdate.setVarStringLength("String Length Update");
//
//        defaultService.create(defaultCreate);
//        defaultService.update(defaultUpdate);
//        defaultService.delete(9L);
//        defaultService.findById(8L);
//        defaultService.findAll();
    }
}