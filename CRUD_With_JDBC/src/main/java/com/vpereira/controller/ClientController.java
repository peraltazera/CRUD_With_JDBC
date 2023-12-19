package com.vpereira.controller;

import com.vpereira.controller.generic.GenericController;
import com.vpereira.core.domain.Client;
import com.vpereira.repository.ClientRepository;
import com.vpereira.service.ClientService;
import jakarta.ws.rs.Path;
@Path("client")
public class ClientController extends GenericController<Client, Long>{
    public ClientController() {
        super(new ClientService(new ClientRepository()));
    }
}
