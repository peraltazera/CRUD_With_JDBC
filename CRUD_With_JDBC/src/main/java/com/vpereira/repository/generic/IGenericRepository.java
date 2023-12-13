package com.vpereira.repository.generic;

import com.vpereira.model.Entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IGenericRepository <T extends Entity, E extends Serializable>{

    public void create(T entity);
    public String generateCreateSQL(T entity);
    public void findById(Class<T> entityClass, E id);
    public String generateFindByIdSQL(Class<T> entityClass, E id);
    public void update(T entity);
    public String generateUpdateSQL(T entity);
    public void delete(Class<T> entityClass, E id);
    public String generateDeleteSQL(Class<T> entityClass, E id);
    public void executeSQL(String sql);
    public ResultSet querySQL(String sql);
}
