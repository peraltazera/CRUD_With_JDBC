package com.vpereira.service.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpereira.core.domain.Entity;
import com.vpereira.repository.generic.IGenericRepository;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericService <T extends Entity, E extends Serializable> implements IGenericService<T,E>{

    protected IGenericRepository<T,E> repository;
    private Class<T> classEntyti;

    public GenericService(IGenericRepository<T, E> repository){
        this.repository = repository;
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        this.classEntyti = (Class<T>) paramType.getActualTypeArguments()[0];
    }

    @Override
    public Response create(String json) {
        T entity = GenericReflections.converterJsonRequestToEntity(json,classEntyti);
        String response = repository.create(entity);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response findById(E id) {
        T entity = repository.findById(id);
        String response = "Nenhum resultado encontrado";
        if(entity != null) {
            response = GenericReflections.converterEntityToJson(entity);
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response findAll() {
        List<T> listEntity = repository.findAll();
        String response = "Nenhum resultado encontrado";
        if(listEntity != null) {
            response = GenericReflections.converterEntitysToJson(listEntity);
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response update(String json, E id) {
        T entity = GenericReflections.converterJsonRequestToEntity(json,classEntyti);
        String response = repository.update(entity, id);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response delete(E id) {
        String response = repository.delete(id);
        return Response.status(Response.Status.NO_CONTENT).entity(response).build();
    }
}
