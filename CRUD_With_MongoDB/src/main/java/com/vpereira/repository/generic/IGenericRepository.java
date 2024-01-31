package com.vpereira.repository.generic;

import com.vpereira.core.domain.Entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IGenericRepository <T extends Entity, E extends Serializable>{

    public String create(T entity);
    public T findById(E id);
    public List<T> findAll();
    public String update(T entity, E id);
    public String delete(E id);
}
