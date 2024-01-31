package com.vpereira.controller;

import com.vpereira.controller.generic.GenericController;
import com.vpereira.core.domain.Default;
import com.vpereira.repository.DefaultRepository;
import com.vpereira.service.DefaultService;
import jakarta.ws.rs.Path;

@Path("default")
public class DefaultController extends GenericController<Default, String> {
    public DefaultController() {
        super(new DefaultService(new DefaultRepository()));
    }
}
