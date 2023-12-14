package com.vpereira.service.generic;

import com.vpereira.core.domain.Entity;

import java.io.Serializable;

public interface IGenericService <T extends Entity, E extends Serializable>{

    public void create(T entity);
    public void findById(E id);
    public void update(T entity);
    public void delete(E id);

}
