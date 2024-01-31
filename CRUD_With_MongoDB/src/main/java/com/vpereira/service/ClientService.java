package com.vpereira.service;

import com.vpereira.core.domain.Client;
import com.vpereira.repository.ClientRepository;
import com.vpereira.service.generic.GenericService;

public class ClientService extends GenericService<Client, String> implements IClientService{

    public ClientService(ClientRepository clientRepository) {
        super(clientRepository);
    }
}
