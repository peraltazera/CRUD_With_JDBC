package com.vpereira.controller.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vpereira.core.domain.Client;
import com.vpereira.core.domain.Entity;
import com.vpereira.dto.ClientDTO;
import com.vpereira.service.generic.GenericReflections;
import com.vpereira.service.generic.GenericService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericController<T extends Entity, E extends Serializable> implements IGenericController<T,E> {

    private final GenericService service;

    public GenericController(GenericService service) {
        this.service = service;
    }

    @Override
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        return service.findAll();
    }

    @Override
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") E id) {
        return service.findById(id);
    }

    @Override
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        return service.create(json);
    }

    @Override
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") E id, String json) {
        return service.update(json, id);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") E id) {
        return service.delete(id);
    }
}
