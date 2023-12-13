package com.vpereira.service.generic;

import com.vpereira.model.Entity;
import com.vpereira.repository.generic.IGenericRepository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;

public class GenericService <T extends Entity, E extends Serializable> implements IGenericService<T,E>{

    protected IGenericRepository<T,E> repository;
    private Class<T> classEntyti;

    @SuppressWarnings("unchecked")
    public GenericService(IGenericRepository<T, E> repository){
        this.repository = repository;
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        this.classEntyti = (Class<T>) paramType.getActualTypeArguments()[0];
        System.out.println(classEntyti);
    }

    @Override
    public void create(T entity) {
        repository.create(entity);
    }

    @Override
    public void findById(E id) {
        repository.findById(classEntyti, id);
    }

    @Override
    public void update(T entity) {
        repository.update(entity);
    }

    @Override
    public void delete(E id) {
        repository.delete(classEntyti, id);
    }
}
