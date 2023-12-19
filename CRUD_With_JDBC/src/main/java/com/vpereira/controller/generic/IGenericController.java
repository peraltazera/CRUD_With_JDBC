package com.vpereira.controller.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vpereira.core.domain.Entity;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface IGenericController<T extends Entity, E extends Serializable> {
    public Response findAll() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response findById(E id) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response create(String entity) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response update(E id, String entity) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response delete(E id);
}
