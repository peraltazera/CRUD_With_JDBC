package com.vpereira.service.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vpereira.core.domain.Entity;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IGenericService <T extends Entity, E extends Serializable>{

    public Response create(String json) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, JsonProcessingException;
    public Response findById(E id);
    public Response findAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response update(String json, E id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    public Response delete(E id);

}
