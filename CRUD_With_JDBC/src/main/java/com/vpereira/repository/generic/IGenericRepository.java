package com.vpereira.repository.generic;

import com.vpereira.core.domain.Entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

public interface IGenericRepository <T extends Entity, E extends Serializable>{

    public String create(T entity);
    public String generateCreateSQL(T entity);
    public T findById(E id);
    public String generateFindByIdSQL(E id);
    public List<T> findAll();
    public String generatefindAllSQL();
    public String update(T entity, E id);
    public String generateUpdateSQL(T entity, E id);
    public String delete(E id);
    public String generateDeleteSQL(E id);
}
