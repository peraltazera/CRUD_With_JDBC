package com.vpereira.service.generic;

import com.vpereira.model.Entity;

import java.io.Serializable;
import java.sql.SQLException;

public interface IGenericService <T extends Entity, E extends Serializable>{

    public void create(T entity);
    public void findById(E id);
    public void update(T entity);
    public void delete(E id);

}
