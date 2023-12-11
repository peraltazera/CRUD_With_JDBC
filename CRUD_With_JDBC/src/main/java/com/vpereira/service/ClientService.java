package com.vpereira.service;

import com.vpereira.repository.ClientRepository;

public class ClientService {

    ClientRepository clientRepository;

    ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
}
